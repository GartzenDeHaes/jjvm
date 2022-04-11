/*
 *  This software is Public Domain.
 */
package maximum.jvm.loader;

import java.lang.ClassCircularityError;
import java.io.DataInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Hashtable;

import maximum.utility.ASSERT;
import maximum.utility.FastVector;
import maximum.utility.Integers;

import maximum.jvm.ClassFileDb;
import maximum.jvm.ClassStreamFactory;
import maximum.jvm.ClassDef;
import maximum.jvm.Frame;
import maximum.jvm.HeapManager;
import maximum.jvm.ProgramException;
import maximum.jvm.ReferenceType;
import maximum.jvm.Type;

import maximum.jvm.builtintypes.ClassInstance;


/**
 *  Interface for loading class files from some place.
 *  Places are storage areas such as a filesystem,
 *  database, or URL.  A loader does not have the 
 *  capability to check if the class has already been
 *  loaded.
 */
public class ClassFile implements ClassDef
{
	public static final int MAJIC = 0xCafeBabe;
	
	public static final int CONSTANT_Class = 7;
	public static final int CONSTANT_Fieldref = 9;
	public static final int CONSTANT_Methodref = 10;
	public static final int CONSTANT_InterfaceMethodref = 11;
	public static final int CONSTANT_String = 8;
	public static final int CONSTANT_Integer = 3;
	public static final int CONSTANT_Float = 4;
	public static final int CONSTANT_Long = 5;
	public static final int CONSTANT_Double = 6;
	public static final int CONSTANT_NameAndType = 12;
	public static final int CONSTANT_Utf8 = 1;
	
	private static final int ACC_PUBLIC = 0x0001;//  Declared public; may be accessed from outside its package.  
	private static final int ACC_FINAL = 0x0010;//  Declared final; no subclasses allowed.  
	private static final int ACC_SUPER = 0x0020;//  Treat superclass methods specially when invoked by the invokespecial instruction.  
	private static final int ACC_INTERFACE = 0x0200;//  Is an interface, not a class.  
	private static final int ACC_ABSTRACT = 0x0400;//  Declared abstract; may not be instantiated.  
	
	
	private ClassFileDb classes;

	private String qname;
	private ClassDef mySuperClass;
	private int access_flags;
	
	FastVector constpool;
	private FastVector interfaces;
	private FastVector fields;
	private FastVector methods;
	private FastVector attributes = new FastVector ();
	private Hashtable methodsBySig = new Hashtable();
	private Hashtable fieldIndexByName = new Hashtable();

	/**
	 *  Load and link a class definition.  This may cause other
	 *  classes to be recusively loaded.  This class must not
	 *  be already loaded
	 */
	public ClassFile ( String qname, ClassFileDb cdb, DataInputStream dis ) throws FileNotFoundException, IOException, ClassCircularityError
	{
		classes = cdb;
		this.qname = qname;
		//if ( cdb.get ( qname ) != null )
		//{
		//	throw new ClassCircularityError ( qname + " already defined" );
		//}		
		reload( dis );
	}	
	
	public void reload ( DataInputStream dis ) throws IOException
	{
		constpool = new FastVector ();
		interfaces = new FastVector ();
		methods = new FastVector();
		fields = new FastVector ();
		
		int tmp;

    	//u4 magic;
		tmp = dis.readInt ();
		ASSERT.fatal ( tmp == MAJIC, "Loader", 28, "Bad magic" );
		
    	//u2 minor_version;
		tmp = dis.readShort ();
		
    	//u2 major_version;
		tmp = dis.readShort ();
		
    	//u2 constant_pool_count;
    	//cp_info constant_pool[constant_pool_count-1];
		loadConstPool ( dis, constpool );
		
    	//u2 access_flags;
		access_flags = dis.readShort ();
		
    	//u2 this_class;
		qname = ((Class_Info)constpool.elementAt(dis.readShort ())).name;
		
    	//u2 super_class;
		try
		{
			mySuperClass = classes.get( null, ((Class_Info)constpool.elementAt( dis.readShort () )).name, 5000 );
		}
		catch ( Exception te )
		{
			te.printStackTrace();
			ASSERT.fatal ( false, "ClassFile", 114, "Internal Error" );
		}
    	//u2 interfaces_count;
    	//u2 interfaces[interfaces_count];
		loadInterfaces ( dis, constpool );
		
    	//u2 fields_count;
    	//field_info fields[fields_count];
		loadFields ( dis, constpool );
		
    	//u2 methods_count;
    	//method_info methods[methods_count];
		loadMethods ( dis, constpool );
		
    	//u2 attributes_count;
    	//attribute_info attributes[attributes_count];	
		loadAttribs ( dis, constpool );
	}
	
	public String getQName ()
	{
		return qname;
	}
	
	public ClassDef getSuperClass ()
	{
		return mySuperClass;
	}
	
	public String getRefClassName ( int index )
	{
		return ((Class_Info)constpool.elementAt ( index )).name;
	}
	
	public long getConstant ( int index, HeapManager heap )
	{
		ConstantInfo ci = (ConstantInfo)constpool.elementAt ( index );
		if ( ci.supportsGetData () )
		{
			return ci.getData();
		}
		if ( ci instanceof String_Info )
		{
			return heap.getRefToConstString ( ((String_Info)ci).str ); 
		}
		if ( ci instanceof Utf8_Info )
		{
			return heap.getRefToConstString (((Utf8_Info)ci).str );
		}
		return 0;
	}
		
	/** get a reference to a field in another class */
	Fieldref_Info getFieldRef ( int index )
	{
		return (Fieldref_Info)constpool.elementAt ( index );
	}
	
	/** get a reference to a field in another class */
	Methodref_Info getMethodRef ( int index )
	{
		return (Methodref_Info)constpool.elementAt ( index );
	}

	/** call static method */
	public Frame callMethod ( HeapManager heap, String desc, int[] args, int pos, int len, ClassInstance ci )
	{
		// ClassInstance will be creating the static methods
		return new DynaFrame (this, null, heap, (Method_Info)methodsBySig.get( desc ), args, pos, len );
	}
	
	public int getFieldIndex ( String name )
	{
		return ((Integer)fieldIndexByName.get ( name )).intValue();
	}
	
	public int getStaticDataSize ( )
	{
		return fields.size();
	}
	
	public ClassInstance createClassInstance ( int refId, HeapManager heap )
	{
		try
		{
			return new ClassInstance ( heap, refId, this );
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public ReferenceType createInstance ( int refId, HeapManager heap, ClassInstance ci ) throws IOException, ProgramException
	{
		return new DynaClassInstance ( this, refId, heap, ci, methodsBySig, fields );
	}
	
	public boolean implementsInterface( String clsName )
	{
		for ( int x = 0; x < interfaces.size(); x++ )
		{
			if ( ((Class_Info)interfaces.elementAt ( x )).name.equals ( clsName ) )
			{
				return true;
			}
		}
		return false;
	}
	
	public boolean isAccessSuperSet()
	{
		return (access_flags & ACC_SUPER) != 0;
	}
	
	//cp_info constant_pool[constant_pool_count-1];
	private void loadConstPool ( DataInputStream dis, FastVector cp ) throws IOException
	{
		int type = 0;
		
		int count = dis.readShort ();
		cp.addElement ( null );
		
		for ( int x = 0; x < count-1; x++ )
		{
			int last = type;
			type = dis.readUnsignedByte ();
			switch ( type )
			{
				case CONSTANT_Class:
					cp.addElement ( new Class_Info ( dis, cp ) );
					break;
				case CONSTANT_Fieldref:
					cp.addElement ( new Fieldref_Info ( classes, dis, cp ) );					
					break;
				case CONSTANT_Methodref:
					cp.addElement ( new Methodref_Info ( dis, cp ) );					
					break;
				case CONSTANT_InterfaceMethodref:
					cp.addElement ( new InterfaceMethodref_Info ( dis, cp ) );					
					break;
				case CONSTANT_String:
					cp.addElement ( new String_Info ( dis, cp ) );
					break;
				case CONSTANT_Integer:
					cp.addElement ( new Integer_Info ( dis ) );					
					break;
				case CONSTANT_Float:
					cp.addElement ( new Float_Info ( dis ) );					
					break;
				case CONSTANT_Long:
					cp.addElement ( new Long_Info ( dis ) );					
					break;
				case CONSTANT_Double:
					cp.addElement ( new Double_Info ( dis ) );					
					break;
				case CONSTANT_NameAndType:
					cp.addElement ( new NameAndType_Info ( dis ) );
					break;
				case CONSTANT_Utf8:
					cp.addElement ( new Utf8_Info ( dis ) );
					break;
				default:
					throw new Error ( "Unknown constant type of " + type );
			}
		}
	}
	
    //u2 interfaces[interfaces_count];
	private void loadInterfaces ( DataInputStream dis, FastVector cp ) throws IOException
	{
		int count = dis.readShort ();
		for ( int x = 0; x < count; x++ )
		{
			interfaces.addElement ( (Class_Info)cp.elementAt ( dis.readShort() ) );
		}
	}
		
   	//field_info fields[fields_count];
	private void loadFields ( DataInputStream dis, FastVector cp ) throws IOException
	{
		int count = dis.readShort ();
		Field_Info fi;
		
		for ( int x = 0; x < count; x++ )
		{
			fi = new Field_Info ( dis, cp );
			fields.addElement ( fi );
			fieldIndexByName.put ( fi.fieldName, Integers.getInt ( x ) );
		}
	}
		
   	//method_info methods[methods_count];
	private void loadMethods ( DataInputStream dis, FastVector cp ) throws IOException
	{
		int count = dis.readShort ();

		for ( int x = 0; x < count; x++ )
		{
			Method_Info mi = new Method_Info ( dis, cp );
			methods.addElement ( mi );
			methodsBySig.put ( mi.descriptor + mi.methodName, mi );
		}
	}
		
   	//attribute_info attributes[attributes_count];	
	private void loadAttribs ( DataInputStream dis, FastVector cp ) throws IOException
	{
		int count = dis.readShort ();
		
		for ( int x = 0; x < count; x++ )
		{
			attributes.addElement ( new Attribute_Info ( dis, cp ) );
		}
	}	
}

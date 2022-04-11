/*
 *  This software is Public Domain.
 */
package maximum.jvm;

import java.util.Hashtable;
import java.util.Enumeration;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.FileNotFoundException;

import maximum.utility.FastVector;
import maximum.jvm.loader.ClassFile;
import maximum.jvm.builtintypes.*;

/**
 *  This is a database of loader.constant.ClassFile
 *  structures.
 */
public class ClassFileDb
{
	Hashtable loadedClasses = new Hashtable ();
	ClassStreamFactory inputFactory;
	
	
	/**
	 *  Set the input stream and construct the builtins
	 */
	ClassFileDb ( ClassStreamFactory csf )
	{
		inputFactory = csf;
		loadBuiltIns( );
	}
	
	/**
	 *  Force load the specified fully qualified class.
	 */
	public void load ( String document, String qname, int timeout ) throws FileNotFoundException, IOException
	{
		DataInputStream dis = inputFactory.get ( document, qname, timeout );
		loadedClasses.put ( qname, new ClassFile ( qname, this, dis ) );
		while ( dis.available() != 0 )
		{
			loadedClasses.put ( qname, new ClassFile ( qname, this, dis ) );
		}
	}
	
	/**
	 *  Get a class file.  It will be loaded if required.
	 */
	public ClassDef get ( String document, String qname, int timeout ) throws FileNotFoundException, IOException
	{
		ClassDef cf = (ClassDef)loadedClasses.get ( qname );
		if ( cf == null )
		{
			if ( qname.startsWith ( "[" ) )
			{
				// array
				cf = new ArrayClass ( (ObjectClass)get(document, "java/lang/Object", timeout), qname );
				loadedClasses.put ( qname, cf );
				return cf;
			}
			else
			{
				load ( document, qname, timeout );
				cf = (ClassDef)loadedClasses.get ( qname );
			}
		}
		return cf;
	}
	
	/**
	 *  Load instances of class Class into a heap.
	 */
	void loadClassInstances ( HeapManager heap )
	{
		ClassDef cd;
		Enumeration e = loadedClasses.elements();
		
		cd = (ClassDef)loadedClasses.get ( "java/lang/Object" );
		heap.addClassInstance ( cd );
		
		while ( e.hasMoreElements() )
		{
			cd = (ClassDef)e.nextElement();
			if ( ! cd.getQName().equals ("java/lang/Object") )
			{
				heap.addClassInstance ( cd );
			}
		}
	}
	
	private void loadBuiltIns( )
	{
		ClassDef cd;
		ObjectClass obj;
		
		obj = new ObjectClass ();
		loadedClasses.put ( obj.getQName(), obj );

		cd = new NullClass ( );
		loadedClasses.put ( cd.getQName(), cd );
		
		cd = new ClassClass ( obj );
		loadedClasses.put ( cd.getQName(), cd );

		cd = new StringClass ( obj );
		loadedClasses.put ( cd.getQName(), cd );
		
		cd = new SystemClass ( obj );
		loadedClasses.put ( cd.getQName(), cd );
		
		cd = new ThreadClass ( obj );
		loadedClasses.put ( cd.getQName(), cd );
		
		cd = new StringBufferClass ( obj );
		loadedClasses.put ( cd.getQName(), cd );
				
		cd = new ThrowableClass ( obj );
		loadedClasses.put ( cd.getQName(), cd );
		
		cd = new PrintWriterClass ( obj );
		loadedClasses.put ( cd.getQName(), cd );
	}
}

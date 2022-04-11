/*
 *  This software is Public Domain.
 */
package maximum.jvm.loader;

import java.io.DataInputStream;
import java.io.IOException;

import maximum.utility.FastVector;
import maximum.jvm.Type;
import maximum.jvm.ClassFileDb;
import maximum.jvm.ClassDef;


/**
 *  Info for calling a field in another class.
 */
public class Fieldref_Info
{	
	int classNameIndex;
	int nameAndTypeIndex;

	String className;
	String fieldName;
	Type type;
	
	
	public Fieldref_Info ( ClassFileDb db, DataInputStream dis, FastVector constPool ) throws IOException
	{
		Class_Info cls;
		
		classNameIndex = dis.readShort ();
		nameAndTypeIndex = dis.readShort ();

		cls = (Class_Info)constPool.elementAt ( classNameIndex );
		className = cls.name;
		NameAndType_Info nt = (NameAndType_Info)constPool.elementAt ( nameAndTypeIndex );
		fieldName = ((Utf8_Info)constPool.elementAt ( nt.nameIndex )).str;
		type = DescriptorParser.parse1 ( ((Utf8_Info)constPool.elementAt ( nt.descriptorIndex )).str );		
	}

	public Fieldref_Info ( int cindex, int ntindex )
	{
		classNameIndex = cindex;
		nameAndTypeIndex = ntindex;
	}
}

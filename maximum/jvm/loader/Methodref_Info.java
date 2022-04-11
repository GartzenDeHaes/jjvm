/*
 *  This software is Public Domain.
 */
package maximum.jvm.loader;

import java.io.IOException;
import java.io.DataInputStream;

import maximum.utility.FastVector;
import maximum.jvm.Type;

/**
 *  Info about calling a method in a class
 */
public class Methodref_Info
{	
	int classNameIndex;
	int nameAndTypeIndex;

	Class_Info cls;	
	String methodName;
	String descriptor;

	FastVector args = new FastVector();;
	Type retType;
	
	Methodref_Info ( DataInputStream dis, FastVector constPool ) throws IOException
	{	
		classNameIndex = dis.readShort();
		nameAndTypeIndex = dis.readShort();

		cls = (Class_Info)constPool.elementAt ( classNameIndex );
		NameAndType_Info nt = (NameAndType_Info)constPool.elementAt ( nameAndTypeIndex );
		methodName = ((Utf8_Info)constPool.elementAt ( nt.nameIndex )).str;
		descriptor = ((Utf8_Info)constPool.elementAt ( nt.descriptorIndex )).str;
		
		DescriptorParser.parse( args, descriptor );
		retType = (Type)args.pop();
	}
	
	public Methodref_Info ( int cindex, int ntindex )
	{
		classNameIndex = cindex;
		nameAndTypeIndex = ntindex;
	}
}

/*
 *  This software is Public Domain.
 */
package maximum.jvm.loader;

import java.io.IOException;
import java.io.DataInputStream;
import maximum.utility.FastVector;


public class InterfaceMethodref_Info
{	
	Class_Info cls;	
	String fieldName;
	String descriptor;

	
	public InterfaceMethodref_Info ( DataInputStream dis, FastVector constPool ) throws IOException
	{
		int classNameIndex;
		int nameAndTypeIndex;
		
		classNameIndex = dis.readShort();
		nameAndTypeIndex = dis.readShort();

		cls = (Class_Info)constPool.elementAt ( classNameIndex );
		NameAndType_Info nt = (NameAndType_Info)constPool.elementAt ( nameAndTypeIndex );
		fieldName = ((Utf8_Info)constPool.elementAt ( nt.nameIndex )).str;
		descriptor = ((Utf8_Info)constPool.elementAt ( nt.descriptorIndex )).str;
	}
}

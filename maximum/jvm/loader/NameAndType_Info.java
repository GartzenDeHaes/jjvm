/*
 *  This software is Public Domain.
 */
package maximum.jvm.loader;

import java.io.IOException;
import java.io.DataInputStream;
import maximum.utility.FastVector;


public class NameAndType_Info
{
	public int nameIndex;
	public int descriptorIndex;
	
	NameAndType_Info ( DataInputStream dis ) throws IOException
	{
		nameIndex = dis.readShort();
		descriptorIndex = dis.readShort();
	}
	
	public NameAndType_Info ( int nidx, int descidx )
	{
		nameIndex = nidx;
		descriptorIndex = descidx;
	}
}

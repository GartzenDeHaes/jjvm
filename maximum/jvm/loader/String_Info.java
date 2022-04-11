/*
 *  This software is Public Domain.
 */
package maximum.jvm.loader;

import java.io.IOException;
import java.io.DataInputStream;
import maximum.utility.FastVector;


public class String_Info implements ConstantInfo
{
	String str;
	
	public String_Info ( DataInputStream dis, FastVector constPool ) throws IOException
	{
		int nameIndex;
		
		nameIndex = dis.readShort();
		str = ((Utf8_Info)constPool.elementAt ( nameIndex )).str;
	}

	public boolean supportsGetData ()
	{
		return false;
	}
	
	public long getData ()
	{
		return 0;
	}
}

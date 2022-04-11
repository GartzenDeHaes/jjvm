/*
 *  This software is Public Domain.
 */
package maximum.jvm.loader;

import java.io.IOException;
import java.io.DataInputStream;
import maximum.utility.FastVector;


public class Integer_Info implements ConstantInfo
{
	public int value;
	
	public Integer_Info ( DataInputStream dis ) throws IOException
	{
		value = dis.readInt ();
	}

	public boolean supportsGetData ()
	{
		return true;
	}
	
	public long getData ()
	{
		return value;
	}
}

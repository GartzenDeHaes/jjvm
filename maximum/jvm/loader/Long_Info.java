/*
 *  This software is Public Domain.
 */
package maximum.jvm.loader;

import java.io.IOException;
import java.io.DataInputStream;
import maximum.utility.FastVector;


public class Long_Info implements ConstantInfo
{
	public long value;
	
	public Long_Info ( DataInputStream dis ) throws IOException
	{
		value = dis.readLong();
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

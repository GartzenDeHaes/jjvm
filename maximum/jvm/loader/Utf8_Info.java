/*
 *  This software is Public Domain.
 */
package maximum.jvm.loader;

import java.io.DataInputStream;
import java.io.IOException;

import maximum.utility.FastVector;


public class Utf8_Info implements ConstantInfo
{
	String str;
	
	Utf8_Info ( DataInputStream dis ) throws IOException
	{
		str = dis.readUTF();
	}
	
	public boolean supportsGetData ()
	{
		return false;
	}
	
	public long getData ()
	{
		return 0;
	}
	
	//****************** WRITE 
	
	public Utf8_Info ( String s )
	{
		str = s;
	}
}

/*
 *  This software is Public Domain.
 */
package maximum.jvm.loader;

import java.io.DataInputStream;
import java.io.IOException;
import maximum.utility.FastVector;


public class Double_Info implements ConstantInfo
{
	double value;
	
	public Double_Info ( DataInputStream dis ) throws IOException
	{
		value = Double.longBitsToDouble ( dis.readLong() );
	}
	
	public boolean supportsGetData ()
	{
		return true;
	}
	
	public long getData ()
	{
		return Double.doubleToLongBits( value );
	}
}

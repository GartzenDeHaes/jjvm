/*
 *  This software is Public Domain.
 */
package maximum.jvm.loader;

import java.io.IOException;
import java.io.DataInputStream;
import maximum.utility.FastVector;


public class Float_Info implements ConstantInfo
{
	float value;
	
	public Float_Info ( DataInputStream dis ) throws IOException
	{
		value = Float.intBitsToFloat ( dis.readInt() );
	}
	
	public boolean supportsGetData ()
	{
		return true;
	}
	
	public long getData ()
	{
		return Float.floatToIntBits( value );
	}
}

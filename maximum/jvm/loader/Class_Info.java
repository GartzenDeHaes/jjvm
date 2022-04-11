/*
 *  This software is Public Domain.
 */
package maximum.jvm.loader;

import java.io.DataInputStream;
import java.io.IOException;

import maximum.utility.FastVector;


public class Class_Info
{
	String name;
	
	Class_Info ( DataInputStream dis, FastVector cp ) throws IOException
	{
		name = ((Utf8_Info)cp.elementAt ( dis.readShort () )).str;
	}
	
	//***************************
	// WRITE
	//***************************
	
	int nameIdx;
	
	public Class_Info ( int nidx )
	{
		nameIdx = nidx;
	}
}

/*
 *  This software is Public Domain.
 */
package maximum.jvm.loader;

import java.io.DataInputStream;
import java.io.IOException;

import maximum.utility.FastVector;


public class ExceptionAttribute
{
	//u2 number_of_exceptions;
	//u2 exception_index_table[number_of_exceptions];
	
	private short[] etableIndexes;
	
	ExceptionAttribute ( DataInputStream dis ) throws IOException
	{
		int count = dis.readShort();
		etableIndexes = new short[count];
				
		for ( int x = 0; x < count; x++ )
		{
			etableIndexes[x] = dis.readShort();
		}
	}
	
	//*******************************
	// WRITE
	//*******************************
	int pos;
	
	ExceptionAttribute ()
	{
		etableIndexes = new short[4];
	}
	
	void addException ( int index )
	{
		etableIndexes[pos++] = (short)index;
	}
}

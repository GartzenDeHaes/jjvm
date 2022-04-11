/*
 *  This software is Public Domain.
 */
package maximum.jvm.loader;

import java.io.DataInputStream;
import java.io.IOException;

import maximum.utility.FastVector;
import maximum.jvm.loader.CodeAttribute;
import maximum.jvm.loader.ExceptionAttribute;


public class Attribute_Info
{	
	String name;
	Object data;
	
	public Attribute_Info ( DataInputStream dis, FastVector cp ) throws IOException
	{
		name = ((Utf8_Info)cp.elementAt ( dis.readShort () )).str;		
		int size = dis.readInt ();
		
		if ( name.equals ( "Code" ) )
		{
			data = new CodeAttribute ( dis, cp );
		}
		else if ( name.equals ( "Exceptions" ) )
		{
			data = new ExceptionAttribute ( dis );
		}
		else if ( name.equals ( "ConstantValue" ) )
		{
			data = cp.elementAt ( dis.readShort() );
		}
		else
		{
			data = new byte[size];
			dis.read ( (byte[])data );
		}
	}
}

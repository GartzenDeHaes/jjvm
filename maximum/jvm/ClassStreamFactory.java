/*
 *  This software is Public Domain.
 */
package maximum.jvm;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.FileNotFoundException;

import maximum.utility.ASSERT;


/**
 *  Get a DataInputStream to either the file system
 *  or the database for a .class file.
 */
public class ClassStreamFactory
{
	
	ClassStreamFactory (  )
	{
	}
	
	public DataInputStream get ( String documentName, String qname, int timeout ) throws FileNotFoundException, IOException
	{
		if ( false )
		{
			return null;
		}
		else
		{
			return new DataInputStream( new FileInputStream ( qname + ".class" ) );
		}
	}
}

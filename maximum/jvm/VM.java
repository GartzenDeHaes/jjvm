/*
 *  This software is Public Domain.
 */
package maximum.jvm;

import java.io.IOException;
import java.io.FileNotFoundException;

import maximum.utility.ASSERT;
import maximum.utility.FastVector;
import maximum.jvm.loader.ClassFile;

/**
 *  The interpeter core.  Connections create new processes
 *  in the VM to execute queries.
 */
public class VM
{
	//private FastVector processes = new FastVector();
	private ClassFileDb classes;
	
	public VM ( ) throws IOException, FileNotFoundException
	{
		classes = new ClassFileDb ( new ClassStreamFactory(  ) );
	}
	
	/**
	 *  Execute a stored procedure
	 */
	public void exec ( int procId, String qname, ProcessListener pl ) throws ClassNotFoundException, IOException, FileNotFoundException
	{
		qname = qname.replace ( '.', '/' );
		VProcess p = new VProcess ( procId, qname, classes, pl );
		//processes.addElement ( p );
	}	
}

/*
 *  This software is Public Domain.
 */
package maximum.jvm;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Hashtable;

import maximum.utility.ASSERT;
import maximum.utility.FastVector;
import maximum.jvm.builtintypes.ClassInstance;
import maximum.jvm.builtintypes.PrintWriterInstance;
import maximum.jvm.builtintypes.SystemClass;
import maximum.jvm.builtintypes.ThreadInstance;

/**
 *  A process is a group of threads that share a name space
 *  (i.e., classpath).  Different processes may link classes 
 *  with the same qualified name differently. 
 */
public class VProcess
{
	private HeapManager heap;
	private String startClass;
	private ThreadInstance mainThread;
	private int processId;
	private ProcessListener listener;

	VProcess ( int procId, String qname, ClassFileDb db, ProcessListener pl ) throws ClassNotFoundException, IOException
	{
		processId = procId;
		int[] argStk = new int[1];
		ClassInstance mainClass = null;
		startClass = qname;
		listener = pl;
		try
		{
			heap = new HeapManager ( db, this );
			mainClass = heap.forName ( qname );
			argStk[0] = heap.newArray ( "[Ljava/lang/String;", 1, Type.TYPE_REF, 0, 0, 0 );
			mainThread = (ThreadInstance)heap.get(heap.newInstance ( "java/lang/Thread" ));
		}
		catch (FileNotFoundException fnfe)
		{
			throw new ClassNotFoundException ( );
		}
		catch ( ProgramException pe )
		{
			throw new IOException ( pe.toString() );
		}
		
		mainThread.args = argStk;
		mainThread.runName = "([Ljava/lang/String;)Vmain";
		mainThread.runnable = mainClass;
		mainThread.thread.start();
	}

	public void completed ( int id )
	{
		if ( id == mainThread.getRefId() )
		{
			ClassInstance sys = null;
			try
			{
				sys = heap.forName ( "java/lang/System" );
			}
			catch ( ProgramException pe )
			{
				pe.printStackTrace();
				ASSERT.fatal ( false, "VProcess", 73, "Internal Error" );
			}
			catch ( Exception te )
			{
				te.printStackTrace();
				ASSERT.fatal ( false, "VProcess", 95, "Internal Error" );
			}
			PrintWriterInstance out = (PrintWriterInstance)heap.get ( (int)sys.getField ( "out" ) );
			listener.processComplete ( processId, out.getOutput() );
		}
	}
	
	public void completedWithError ( int id, String str, boolean fatal )
	{
		if ( id == mainThread.getRefId() )
		{
			ClassInstance sys = null;
			try
			{
				sys = heap.forName ( "java/lang/System" );
			}
			catch ( ProgramException pe )
			{
				pe.printStackTrace();
				ASSERT.fatal ( false, "VProcess", 73, "Internal Error" );
			}
			catch ( Exception te )
			{
				te.printStackTrace();
				ASSERT.fatal ( false, "VProcess", 95, "Internal Error" );
			}
			PrintWriterInstance err = (PrintWriterInstance)heap.get ( (int)sys.getField ( "err" ) );
			listener.processError ( processId, err.getOutput() );
		}
	}
}

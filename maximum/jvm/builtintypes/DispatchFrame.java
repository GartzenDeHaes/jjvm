/*
 *  This software is Public Domain.
 */
package maximum.jvm.builtintypes;

import java.io.IOException;
import maximum.jvm.Frame;
import maximum.jvm.HeapManager;
import maximum.jvm.ProgramException;


public class DispatchFrame implements Frame
{
	int methodId;
	Dispatch callback;
	long ret;
	int[] args;
	int argcount;
	HeapManager heap;
	boolean inExcept;

	ClassInstance cls;
	
	DispatchFrame ( HeapManager hm, int methodId, Dispatch callback, int[] argStk, int pos, int len, ClassInstance ci  )
	{
		this.methodId = methodId;
		this.callback = callback;
		heap = hm;
		
		argcount = len;
		args = new int[len];
		for ( int x = 0; x < len; x++ )
		{
			args[x] = argStk[x+pos];
		}
		cls = ci;
	}
	
	public boolean unhandledException ()
	{
		return inExcept;
	}

	public long getRet ( )
	{
		return ret;
	}
	
	public void call () throws IOException
	{
		try
		{
			ret = callback.call ( heap, methodId, args, cls );
		}
		catch ( ProgramException pe )
		{
			inExcept = true;
			ret = pe.excptRefId;
		}
	}
}

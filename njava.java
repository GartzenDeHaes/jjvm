/*
 *  This software is Public Domain.
 */

import maximum.jvm.VM;
import maximum.jvm.ProcessListener;

/**
 *  Command line harness to run jjvm like java.exe.  
 * 
 *  Remember, System.out writes to a buffer which is 
 *  passed to processComplete. System.err is passed to 
 *  processError.
 * 
 *  The process ID passed to exec is used to identify
 *  the terminating thread in the process* callbacks.
 */
public class njava implements ProcessListener
{
	public static void main(String[] args)
	{
		if ( args.length == 0 )
		{
			System.out.println("USAGE: java [-test | my.package.className]");
			return;
		}
		if (args[0].equals("-test"))
		{
			args[0] = "maximum.test.Test";
		}
		new njava(args[0]);
	}
	
	public njava(String cls)
	{
		try
		{
			VM vm = new VM();
			vm.exec ( 0, cls, this );
		}
		catch ( Exception e )
		{
			e.printStackTrace();
		}
	}
	
	public void processComplete ( int processId, String output )
	{
		System.out.println(output);
	}
	
	public void processError ( int processId, String errorOutput )
	{
		System.out.println(errorOutput);
	}
}

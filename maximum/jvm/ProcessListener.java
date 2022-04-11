package maximum.jvm;

/**
 *  Callback interface for process termination.
 */
public interface ProcessListener
{
	/**
	 *  This is called when all threads in a process have
	 *  terminated.  After this call, the process is dead.
	 */
	void processComplete ( int processId, String output );
	
	/**
	 *  This is called if the process encounters an unhandled
	 *  exception.  The process is dead after this call.
	 */
	void processError ( int processId, String errorOutput );
}

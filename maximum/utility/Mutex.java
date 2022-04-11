/*
 *  This software is Public Domain.
 */
package maximum.utility;

/**
 *  Provide mutually exclusive access to a thread.
 */
public class Mutex
{
	private Thread owner = null;
	private int wait_count = 0;
	private int lockCount = 0;
	
	public synchronized void lock(int millis) throws Exception
	{
		if (owner == Thread.currentThread())
		{
			ASSERT.fatal (lockCount > 0, "Mutex", 14, "Illegal mutex state");
			lockCount++;
			return;
		}
		while(owner != null)
		{
			try
			{
				wait_count++;
				wait(millis);
			}
			catch (InterruptedException ie)
			{
				throw new Exception();
			}
			finally
			{
				wait_count--;
			}
			if(millis != 0 && owner != null)
			{
				throw new Exception();
			}
		}
		owner = Thread.currentThread();
		lockCount++;
	}
	
	public final synchronized void lock() throws Exception
	{
		lock(0);
	}
	
	public synchronized void unlock()
	{
		ASSERT.fatal ( lockCount > 0, "Mutex", 53, "Illegal Lock Count" );
		if (owner != Thread.currentThread())
		{
			throw new RuntimeException("Cannot unlock: Thread is not Mutex owner");
		}
		alienUnlock ();
	}
	
	public final synchronized void alienUnlock ()
	{
		ASSERT.fatal ( lockCount > 0, "Mutex", 63, "Illegal Lock Count" );	
		
		if (--lockCount == 0)
		{
			owner = null;
			if (wait_count > 0)
			{
				notify();
			}
		}
		//ASSERT.fatal(lockCount >= 0, "Mutex", 60, "Illegal state");
	}
	
	public final synchronized void deref()
	{
		if (owner != Thread.currentThread())
		{
			throw new RuntimeException("Cannot unlock: Thread is not Mutex owner");
		}
		lockCount = 0;
		owner = null;
		if (wait_count > 0)
		{
			notify();
		}
	}
	
	public final synchronized boolean isLocked()
	{
		return lockCount > 0;
	}
}

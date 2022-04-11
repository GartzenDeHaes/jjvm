/*
 *  This software is Public Domain.
 */
package maximum.jvm.builtintypes;

import java.util.Hashtable;
import java.io.IOException;

import maximum.utility.ASSERT;
import maximum.utility.Integers;
import maximum.utility.Mutex;
import maximum.jvm.ReferenceType;
import maximum.jvm.ClassDef;
import maximum.jvm.Frame;
import maximum.jvm.HeapManager;
import maximum.jvm.VProcess;


public class ThreadInstance extends Mutex implements ReferenceType, Dispatch, Runnable
{
	private static final int M_INIT = 0;
	private static final int M_INIT_R = 1;
	private static final int M_INIT_RS = 2;
	private static final int M_INIT_S = 3;
	private static final int M_CHECKACCESS = 4;
	private static final int M_COUNTSTACKFRAMES = 5;
	private static final int M_DESTROY = 6;
	private static final int M_GETNAME = 8;
	private static final int M_GETPRI = 9;
	private static final int M_INTERRUPT = 10;
	private static final int M_ISALIVE = 11;
	private static final int M_ISDAEMON = 12;
	private static final int M_ISINTERRUPTED = 13;
	private static final int M_JOIN = 14;
	private static final int M_JOIN_L = 15;
	private static final int M_JOIN_LI = 16;
	private static final int M_RESUME = 17;
	private static final int M_RUN = 18;
	private static final int M_SETDAEMON = 19;
	private static final int M_SETNAME = 20;
	private static final int M_SETPRIORITY = 21;
	private static final int M_SLEEP_L = 22;
	private static final int M_SLEEP_LI = 23;
	private static final int M_START = 24;
	private static final int M_STOP = 25;
	private static final int M_STOP_T = 26;
	private static final int M_SUSPEND = 27;
	private static final int M_TOSTRING = 28;
	private static final int M_YIELD = 29;
	
	private static Hashtable methodMap;
	
	static
	{
		methodMap = new Hashtable();
		methodMap.put ( "()V<init>", Integers.getInt ( M_INIT ) );
		methodMap.put ( "(Ljava/lang/Runnable;)V<init>", Integers.getInt ( M_INIT_R ) );
		methodMap.put ( "(Ljava/lang/Runnable;Ljava/lang/String;)V<init>", Integers.getInt ( M_INIT_RS ) );
		methodMap.put ( "(Ljava/lang/String;)V<init>", Integers.getInt ( M_INIT_S ) );
		methodMap.put ( "()ZcheckAccess", Integers.getInt ( M_CHECKACCESS ) );
		methodMap.put ( "()IcountStackFrames", Integers.getInt ( M_COUNTSTACKFRAMES ) );
		methodMap.put ( "()Vdestroy", Integers.getInt ( M_DESTROY ) );
		methodMap.put ( "()Ljava/lang/String;getName", Integers.getInt ( M_GETNAME ) );
		methodMap.put ( "()Vinterrupt", Integers.getInt ( M_INTERRUPT ) );
		methodMap.put ( "()ZisAlive", Integers.getInt ( M_ISALIVE ) );
		methodMap.put ( "()ZisDaemon", Integers.getInt ( M_ISDAEMON ) );
		methodMap.put ( "()ZisInterrupted", Integers.getInt ( M_ISINTERRUPTED ) );
		methodMap.put ( "()Vjoin", Integers.getInt ( M_JOIN ) );
		methodMap.put ( "(J)Vjoin", Integers.getInt ( M_JOIN_L ) );
		methodMap.put ( "(JI)Vjoin", Integers.getInt ( M_JOIN_LI ) );
		methodMap.put ( "()Vresume", Integers.getInt ( M_RESUME ) );
		methodMap.put ( "()Vrun", Integers.getInt ( M_RUN ) );
		methodMap.put ( "(Z)VsetDaemon", Integers.getInt ( M_SETDAEMON ) );
		methodMap.put ( "(Ljava/lang/String;)VsetName", Integers.getInt ( M_SETNAME ) );
		methodMap.put ( "(I)VsetPriority", Integers.getInt ( M_SETPRIORITY ) );
		methodMap.put ( "(J)Vsleep", Integers.getInt ( M_SLEEP_L ) );
		methodMap.put ( "(JI)Vsleep", Integers.getInt ( M_SLEEP_LI ) );
		methodMap.put ( "()Vstart", Integers.getInt ( M_START ) );
		methodMap.put ( "()Vstop", Integers.getInt ( M_STOP ) );
		methodMap.put ( "(Ljava/lang/Throwable)Vstop", Integers.getInt ( M_STOP_T ) );
		methodMap.put ( "()Vsuspend", Integers.getInt ( M_SUSPEND ) );
		methodMap.put ( "()Ljava/lang/String;toString", Integers.getInt ( M_TOSTRING ) );
		methodMap.put ( "()Vyield", Integers.getInt ( M_YIELD ) );
	}
	
	int refCount;
	int id;
	ClassInstance myClass;
	ReferenceType mySuper;
	HeapManager heap;
	VProcess process;
	
	ThreadInstance ( HeapManager heap, int refId, ClassInstance cls )
	{
		process = heap.getProcess();
		id = refId;
		refCount = 0;
		myClass = cls;
		this.heap = heap;
		try
		{
			mySuper = heap.get ( heap.newInstance("java/lang/Object") );
		}
		catch ( Exception cnfe )
		{
			cnfe.printStackTrace();
			ASSERT.fatal ( false, "ThreadInstance", 99, "Can't happen" );
		}
	}
	
	public ReferenceType getSuper()
	{
		return mySuper;
	}
		
	public void addRef ()
	{
		refCount++;
	}
	
	public void release ()
	{
		refCount--;
	}

	public int getRefCount ()
	{
		return refCount;
	}

	public int getRefId ()
	{
		return id;
	}
	
	public String getQName ()
	{
		return "java/lang/Thread";
	}
	
	public ClassInstance getClassDef ()
	{
		return myClass;
	}
	
	public int instanceOf ( String className )
	{
		if ( className.equals ( "java/lang/Thread" ) || className.equals ( "java/lang/Object") )
		{
			return 1;
		}
		return 0;
	}
	
	public long getField ( String name )
	{
		return 0;
	}
	
	public void setField ( String name, long value )
	{
	}
	
	public Frame callMethod ( HeapManager heap, String desc, int[] args, int pos, int len )
	{
		DispatchFrame dm = new DispatchFrame ( heap, ((Integer)methodMap.get ( desc )).intValue(), this, args, pos, len, myClass );
		return dm;
	}

	public ReferenceType runnable;
	int threadName;
	public Thread thread = new Thread(this);
	public String runName = "()Vrun";
	public int[] args = new int[0];
	
	public long call ( HeapManager heap, int method, int[] args, ClassInstance ci )
	{
		try
		{
			switch ( method )
			{
				case M_INIT:
					break;
				case M_INIT_RS:
					threadName = (int)args[1];
				case M_INIT_R:
					runnable = (ReferenceType)heap.get ( (int)args[0] );
					break;
				case M_INIT_S:
					threadName = (int)args[1];
					break;
				case M_CHECKACCESS:
					return 1;
				case M_COUNTSTACKFRAMES:
					return 0;
				case M_DESTROY:
					thread.destroy();
					break;
				case M_GETNAME:
					return threadName;
				case M_GETPRI:
					return thread.getPriority();
				case M_INTERRUPT:
					thread.interrupt();
					break;
				case M_ISALIVE:
					if ( thread.isAlive() )
					{
						return 1;
					}
					return 0;
				case M_ISDAEMON:
					if ( thread.isDaemon() )
					{
						return 1;
					}
					return 0;
				case M_ISINTERRUPTED:
					if ( thread.isInterrupted() )
					{
						return 1;
					}
					return 0;
				case M_JOIN:
					thread.join();
					break;
				case M_JOIN_L:
					thread.join( args[0] );
					break;
				case M_JOIN_LI:
					thread.join ( args[0], (int)args[1] );
					break;
				case M_RESUME:
					thread.resume();
					break;
				case M_RUN:
					break;
				case M_SETDAEMON:
					thread.setDaemon ( args[0] != 0 );
					break;
				case M_SETNAME:
					threadName = (int)args[0];
					break;
				case M_SETPRIORITY:
					thread.setPriority ( (int)args[0] );
					break;
				case M_SLEEP_L:
					thread.sleep( args[0] );
					break;
				case M_SLEEP_LI:
					thread.sleep ( args[0], (int)args[1] );
					break;
				case M_START:
					thread.start();
					break;
				case M_STOP:
					thread.stop();
					break;
				case M_STOP_T:
					// ???
					break;
				case M_SUSPEND:
					thread.suspend();
					break;
				case M_TOSTRING:
					return heap.getRefToConstString ( thread.toString() );
				case M_YIELD:
					thread.yield();
					break;
			}
		}
		catch ( InterruptedException ie )
		{
			// don't know how to handle exceptions yet!
		}
		return 0;
	}
	
	public void run ()
	{
		Frame frame = runnable.callMethod( heap, runName, args, 0, args.length );
		try
		{
			frame.call ();
		}
		catch ( IOException ioe )
		{
			ioe.printStackTrace();
			process.completedWithError ( id, ioe.toString(), true );
		}
		process.completed ( id );
	}
}

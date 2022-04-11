/*
 *  This software is Public Domain.
 */
package maximum.jvm;

import java.io.IOException;

/**
 *  Runtime stack frame.
 */
public interface Frame
{
	boolean unhandledException ();
	long getRet ( );
	void call () throws IOException;
}

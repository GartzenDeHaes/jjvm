/*
 *  This software is Public Domain.
 */
package maximum.utility;

import maximum.utility.AssertionFailure;

public class ASSERT
{
	public static final void fatal(boolean cond, String file, int line, String msg)
	{
		if (! cond) 
		{
			//Logger.log(msg);
			System.out.println(msg);
			throw new AssertionFailure(file, line, msg);
		}
	}
}

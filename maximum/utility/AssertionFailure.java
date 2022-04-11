/*
 *  This software is Public Domain.
 */
package maximum.utility;

public class AssertionFailure extends Error
{
	public AssertionFailure(String file, int line, String msg)
	{
		super("ASSERTion failure!\nFile:" + file + "\nLine: " + Integer.toString(line) + "\n" + msg);
	}
}

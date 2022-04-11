/*
 *  This software is Public Domain.
 */
package maximum.jvm;

/**
 *  This exception is used to signal an
 *  exception to be passed to the interpreted
 *  program.
 */
public class ProgramException extends Exception
{
	public int excptRefId;
	
	public ProgramException ( int refId )
	{
		excptRefId = refId;
	}
}

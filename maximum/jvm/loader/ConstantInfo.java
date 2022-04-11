/*
 *  This software is Public Domain.
 */
package maximum.jvm.loader;

/** marker interface */
public interface ConstantInfo
{
	boolean supportsGetData ();
	long getData();
}

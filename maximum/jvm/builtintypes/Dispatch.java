/*
 *  This software is Public Domain.
 */
package maximum.jvm.builtintypes;

import java.io.IOException;

import maximum.jvm.HeapManager;
import maximum.jvm.ProgramException;


interface Dispatch
{
	long call ( HeapManager heap, int method, int[] args, ClassInstance ci ) throws ProgramException, IOException;
}

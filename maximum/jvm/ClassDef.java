/*
 *  This software is Public Domain.
 */
package maximum.jvm;

import java.io.DataInputStream;
import java.io.IOException;
import maximum.utility.FastVector;
import maximum.jvm.builtintypes.ClassInstance;

/**
 *  Interface to allow interoperability of built-in
 *  and application defined classes.
 */
public interface ClassDef
{	
	/**  Reload the class from it's 'class' file.  This
	 *   can be used to update a JSP complied cache, FE.
	 */
	void reload ( DataInputStream dis ) throws IOException;
	
	/**
	 *  Get the definition for this classes super class
	 */
	ClassDef getSuperClass ();
	boolean implementsInterface( String clsName );
	
	/** Get the fully qualified name */
	String getQName ();
	
	long getConstant ( int index, HeapManager heap );
		
	// for class class
	int getFieldIndex ( String name );
	int getStaticDataSize ( );
	ClassInstance createClassInstance ( int refId, HeapManager heap );
	
	ReferenceType createInstance ( int refId, HeapManager heap, ClassInstance ci ) throws IOException, ProgramException;

	Frame callMethod ( HeapManager heap, String desc, int[] args, int pos, int len, ClassInstance ci );
}

/*
 *  This software is Public Domain.
 */
package maximum.jvm;

import maximum.jvm.builtintypes.ClassInstance;

/**
 *  Interface for objects stored in the heap.  This
 *  allows native and application defined instances
 *  to live together.
 */
public interface ReferenceType
{
	/** When the ref count is zero, the instance can
	 *  be unloaded. Creating the instance sets the
	 *  refcount to 1 */
	void addRef ();
	void release ();
	int getRefCount();

	int getRefId ();
	String getQName();

	ClassInstance getClassDef ();
	ReferenceType getSuper();
	
	int instanceOf ( String className );
	
	long getField ( String name );
	void setField ( String name, long value );
	
	Frame callMethod ( HeapManager heap, String desc, int[] args, int pos, int len );
	
	void lock() throws Exception;
	void unlock();
}

/*
 *  This software is Public Domain.
 */
package maximum.utility;

public class ByteVector
{
	byte[] array;
	final int initial = 16;
	int used;

	
	public ByteVector(int size) 
	{
		array = new byte[size];
		used = 0;
	}

	public ByteVector() 
	{
		array = new byte[initial];
		used = 0;
	}

	public final byte pop()
	{
		if (used > 0) 
		{
		    return array[--used];
		}
		else
		{
		    return -1;
		}		
	}
	
	public int capacity()
	{
		return array.length;
	}
	
	public int addElement(byte o) 
	{
		extend();
		array[used] = o;
		return used++;
	}
	
	public final void extend() 
	{
		if (used >= array.length) 
		{
			int space = array.length;
			while (used >= space) 
			{
				space <<= 1;
			}
			byte array2[] = new byte[space];
			int i;
			for (i = 0; (i < used); i++) 
			{
				array2[i] = array[i];
			}
			array = array2;
		}
	}
	
	public final int size() 
	{
		return used;
	}
	
	public final void setSize(int s) 
	{
		if (array.length < s) 
		{
			array = new byte[s];
		}
		used = s;
	}
	
	public final void clear()
	{
		used = 0;
	}
	
	public final byte elementAt(int at) 
	{
		return array[at];
	}

	public final void setElementAt(int at, byte o) 
	{
		array[at] = o;
	}

	public final byte firstElement() 
	{
		if (used > 0) 
		{
			return array[0];
		} else 
		{
			return -1;
		}
	}
	
	public final byte lastElement()
	{
		if (used > 0) 
		{
		    return array[used-1];
		}
		else
		{
		    return -1;
		}
	}
	
	public final void removeElement() 
	{
		removeElementAt(0);
	}
		
	public final int indexOf(byte o) 
	{
		int i;
		for (i = 0; (i < used); i++) 
		{
			if (array[i] == o) 
			{
				return i;
			}
		}
		return -1;
	}

	public final void removeElementAt(int at) 
	{
		int i;
		for (i = at; (i < (used - 1)); i++) 
		{
			array[i] = array[i + 1];
		}
		used--;
	}
	
	public final void insertElementAt(byte o, int at) 
	{
		int i;
		extend();
		for (i = used; (i > at); i--) 
		{
			array[i] = array[i - 1];
		}
		array[at] = o;
		used++;
	}
}
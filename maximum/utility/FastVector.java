/*
 *  This software is Public Domain.
 */
package maximum.utility;

/**
 *  FastVector is a replacement for java.util.Vector.
 */
public class FastVector implements java.io.Serializable
{
	public Object array[];
	final int initial = 16;
	public int used;

	public FastVector(int size) 
	{
		array = new Object[size];
		used = 0;
	}

	public FastVector() 
	{
		array = new Object[initial];
		used = 0;
	}

	public final Object pop()
	{
		if (used > 0) 
		{
		    return array[--used];
		}
		else
		{
		    return null;
		}		
	}
	
	public int capacity()
	{
		return array.length;
	}
	
	public int addElement(Object o) 
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
			Object array2[] = new Object[space];
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
			array = new Object[s];
		}
		used = s;
	}
	
	public final void clear()
	{
		used = 0;
	}
	
	public final Object elementAt(int at) 
	{
		return array[at];
	}

	public final void setElementAt(int at, Object o) 
	{
		array[at] = o;
	}

	public final Object firstElement() 
	{
		if (used > 0) 
		{
			return array[0];
		} else 
		{
			return null;
		}
	}
	
	public final Object lastElement()
	{
		if (used > 0) 
		{
		    return array[used-1];
		}
		else
		{
		    return null;
		}
	}
	
	public final void removeElement() 
	{
		removeElementAt(0);
	}
	
	public final void removeElement(Object o) 
	{
		int i;
		for (i = 0; (i < used); i++) 
		{
			if (array[i] == o) 
			{
				removeElementAt(i);
				return;
			}
		}
	}
	
	public final int indexOf(Object o) 
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
	
	public final void insertElementAt(Object o, int at) 
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


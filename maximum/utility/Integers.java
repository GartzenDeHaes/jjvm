/*
 *  This software is Public Domain.
 */
package maximum.utility;

public class Integers
{
	static FastVector ints = new FastVector ( 1000 );
	
	static 
	{
		for ( int x = 0; x < 1000; x++ )
		{
			ints.addElement ( new Integer (x) );
		}
	}

	public static Integer getInt ( int i )
	{
		Integer ii = (Integer) ints.elementAt ( i );
		if ( ii == null )
		{
			ii = new Integer ( i );
			ints.setElementAt ( i, ii );
		}
		return ii;
	}
}

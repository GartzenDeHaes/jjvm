/*
 *  This software is Public Domain.
 */
package maximum.jvm.loader;

import maximum.utility.FastVector;
import maximum.jvm.Types;
import maximum.jvm.Type;


public class DescriptorParser implements Types
{
	/**
	 *  Returns the return type
	 */
	public static void parse ( FastVector results, String desc )
	{
		String s;
		Type t;
		int x = 0;
		
		while ( x < desc.length() )
		{
			char c = desc.charAt ( x++ );
			t = null;
			s = "";
			
			if ( c == 'L' )
			{
				s = desc.substring ( x, desc.indexOf ( ";", x )+1);
				x += s.length();
				t = parse1 ( "L" + s );
			}
			else if ( Character.isLetter( c ) )
			{
				t = parse1 ( "" + c );
			}
			else if ( c == ']' )
			{
				s = "" + c;
				while ( c == ']' )
				{
					c = desc.charAt ( x++ );
					s += "" + c;
				}
				t = parse1 ( s );
			}
			if ( t != null )
				results.addElement ( t );
		}
	}
	
	public static Type parse1 ( String desc )
	{
		String s;
		int pos;
		char c = desc.charAt ( 0 );
		switch ( c )
		{
			case 'B': //  byte  signed byte  
				return new Type (TYPE_BYTE, 1);
			case 'C':  //char  Unicode character  
				new Type (TYPE_CHAR, 1);
		 	case 'D':   //double  double-precision floating-point value  
				return new Type (TYPE_DOUBLE, 2);
			case 'F':  //float  single-precision floating-point value  
				return new Type (TYPE_FLOAT, 1);
			case 'I':  //int  integer  
				return new Type (TYPE_INT, 1);
			case 'J':  //long  long integer  
				return new Type (TYPE_LONG, 2);
			case 'L':  //<classname>;  reference  an instance of class <classname>  
				pos = desc.indexOf ( ";" );
				s = desc.substring ( 1, pos );
				return new Type (TYPE_REF, 1);
			case 'S':  //short  signed short  
				return new Type (TYPE_SHORT, 1);
			case 'Z':  //boolean  true or false  
				return new Type (TYPE_BOOL, 1);
			case '[':  //reference  one array dimension 
				pos = 1;
				while ( (c = desc.charAt ( pos )) == '[' )
				{
					pos++;
				}
				return new Type (pos, c);
			case ')':
			case '(':
				break;
			case ';':
				break;
			case 'V':
				return new Type ( TYPE_VOID, 0 );
			default:
				throw new Error ( "Unknown descriptor type " + c );
		}
		return null;
	}
}

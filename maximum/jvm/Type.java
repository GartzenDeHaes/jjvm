/*
 *  This software is Public Domain.
 */
package maximum.jvm;

public class Type implements Types
{
	public int intsize;
	public int type;
	
	public Type ( int type, int intsize )
	{
		this.type = type;
		this.intsize = intsize;
	}
	
	public int arraydims;
	
	public Type ( int dims, char basetype )
	{
		intsize = 1;
		arraydims = dims;
		
		switch ( basetype )
		{
			case 'B': //  byte  signed byte  
				type = TYPE_BYTE;
				break;
			case 'C':  //char  Unicode character  
				type = TYPE_CHAR;
				break;
			case 'D':   //double  double-precision floating-point value  
				type = TYPE_DOUBLE;
				break;
			case 'F':  //float  single-precision floating-point value  
				type = TYPE_FLOAT;
				break;
			case 'I':  //int  integer  
				type = TYPE_INT;
				break;
			case 'J':  //long  long integer  
				type = TYPE_LONG;
				break;
			case 'L':  //<classname>;  reference  an instance of class <classname>  
				type = TYPE_REF;
				break;
			case 'S':  //short  signed short  
				type = TYPE_SHORT;
				break;
			case 'Z':  //boolean  true or false  
				type = TYPE_BOOL;
				break;
			case '[':  //reference  one array dimension 
				throw new Error ( " type of array can't be array " );
			case ')':
				throw new Error ( " ) not valid" );
			default:
				throw new Error ( "Unknown descriptor type " );
		}
	}
	
	public static final String typeToChar ( int type )
	{
		switch ( type )
		{
			case TYPE_VOID:
				return "V";
			case TYPE_REF:
				return "L";
			case TYPE_BOOL:
				return "Z";
			case TYPE_CHAR:
				return "C";
			case TYPE_FLOAT:
				return "F";
			case TYPE_DOUBLE:
				return "D";
			case TYPE_BYTE:
				return "B";
			case TYPE_SHORT:
				return "S";
			case TYPE_INT:
				return "I";
			case TYPE_LONG:
				return "J";
		}
		return ";";
	}
}

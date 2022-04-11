/*
 *  This software is Public Domain.
 */
package maximum.jvm.loader;

import java.io.DataInputStream;
import java.io.IOException;

import maximum.utility.FastVector;
import maximum.jvm.Type;


/**
 *  Definition of a field in this class.
 */
public class Field_Info
{
	int nameIndex;
	int descriptorIndex;

	int accessFlags;
	FastVector attributes;
	
	String fieldName;
	Type type;
	boolean isConst;
		
	/**
	 *  Dynamically loaded field
	 */
	Field_Info ( DataInputStream dis, FastVector constPool ) throws IOException
	{
		accessFlags = dis.readShort ();
		nameIndex = dis.readShort ();
		descriptorIndex = dis.readShort ();
		
		int count = dis.readShort ();
		attributes = new FastVector ( count );
		
		for ( int x = 0; x < count; x++ )
		{
			attributes.addElement ( new Attribute_Info ( dis, constPool ) );
		}
		fieldName = ((Utf8_Info)constPool.elementAt ( nameIndex )).str;
		type = DescriptorParser.parse1 ( ((Utf8_Info)constPool.elementAt ( descriptorIndex )).str );

		for ( int x = 0; x < attributes.size(); x++ )
		{
			Attribute_Info ai = (Attribute_Info)attributes.elementAt ( x );
			if ( ai.name.equals ( "ConstantValue" ) )
			{
				isConst = true;
			}
		}
	}
	
	//*************************
	// WRITE
	//*************************
	
	public Field_Info ( int nindex, int dindex )
	{
		nameIndex = nindex;
		descriptorIndex = dindex;
		attributes = new FastVector ( );	
	}
}

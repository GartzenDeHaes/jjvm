/*
 *  This software is Public Domain.
 */
package maximum.jvm.loader;

import maximum.jvm.Type;


class Field
{
	Type type;
	long data;
	
	Field ( Type t )
	{
		type = t;
	}
}

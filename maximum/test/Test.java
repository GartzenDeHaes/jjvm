package maximum.test;

/*
 *  This software is Public Domain.
 */

public class Test
{
	public static void main (String[] args)
	{
		MathOperatorTest mot = new MathOperatorTest();
		if (! mot.test())
		{
			System.out.println("Aborting tests");
			return;
		}
		System.out.println("Testing complete");
	}
}

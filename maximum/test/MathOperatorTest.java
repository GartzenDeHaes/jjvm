package maximum.test;

public class MathOperatorTest
{
	static int START_NUM;
	
	byte mybyte;
	int myint;
	float myfloat;
	double mydouble;
	
	MathOperatorTest()
	{
		START_NUM = 128;
		myint = START_NUM;
		mybyte = (byte)myint;
		myfloat = myint;
		mydouble = myint;
	}
	
	boolean test()
	{
		if ( myint != START_NUM )
		{
			System.out.println("MathOperatorTest: Int assignment failed");
		}
		if ( mybyte != START_NUM )
		{
			System.out.println("MathOperatorTest: Byte assignment failed");
		}
		if ( myfloat != START_NUM )
		{
			System.out.println("MathOperatorTest: Float assignment failed");
		}
		if ( mydouble != START_NUM )
		{
			System.out.println("MathOperatorTest: Double assignment failed");
		}
		myint += START_NUM*3;
		myint /= 2;
		if ( myint != START_NUM*2 )
		{
			System.out.println("MathOperatorTest: Integer division failed");
		}
		myint /= 2;
		if ( myint != START_NUM )
		{
			System.out.println("MathOperatorTest: Integer division2 failed");
		}
		myint /= 2;
		
		return true;
	}
}

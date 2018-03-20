package com.tentone.math24;

import java.util.Random;

public class RationalNumber
{
	public float numerator, denominator;
	
	public RationalNumber(float numerator,float denominator)
	{
		this.numerator=numerator;
		this.denominator=denominator;
		reduce();
	}
	
	public RationalNumber(float numerator)
	{
		this.numerator=numerator;
		this.denominator=1;
	}
	
	public void set(RationalNumber num)
	{
		this.numerator=num.numerator;
		this.denominator=num.denominator;
	}
	
	public void set(float numerator)
	{
		this.numerator=numerator;
		this.denominator=1;
	}
	
	public void set(float numerator,float denominator)
	{
		this.numerator=numerator;
		this.denominator=denominator;
		reduce();
	}
	
	public void reduce()
	{
		if(numerator==0)
		{
			denominator=1;
		}
		
		int i=2;
		while(i<=numerator)
		{
			if(numerator%i==0 && denominator%i==0)
			{
				numerator=numerator/i;
				denominator=denominator/i;
				i=1;
			}
			i++;
		}
	}
	
	public void addSelf(RationalNumber num)
	{
		this.numerator=numerator*num.denominator+num.numerator*denominator;
		this.denominator=denominator*num.denominator;
		reduce();
	}

	public void subSelf(RationalNumber num)
	{
		this.numerator=numerator*num.denominator-num.numerator*denominator;
		this.denominator=denominator*num.denominator;
		reduce();
	}

	public void mulSelf(RationalNumber num)
	{
		this.numerator=numerator*num.numerator;
		this.denominator=denominator*num.denominator;
		reduce();
	}

	public void divSelf(RationalNumber num)
	{
		this.numerator=numerator*num.denominator;
		this.denominator=denominator*num.numerator;
		reduce();
	}
	
	public RationalNumber add(RationalNumber num)
	{
		return new RationalNumber(numerator*num.denominator+num.numerator*denominator,denominator*num.denominator);
	}

	public RationalNumber sub(RationalNumber num)
	{
		return new RationalNumber(numerator*num.denominator-num.numerator*denominator,denominator*num.denominator);
	}

	public RationalNumber mul(RationalNumber num)
	{
		return new RationalNumber(numerator*num.numerator,denominator*num.denominator);
	}

	public RationalNumber div(RationalNumber num)
	{
		return new RationalNumber(numerator*num.denominator,denominator*num.numerator);
	}
	
	public float realValue()
	{
		return numerator/denominator;
	}
	
	public boolean isIntegerDivision(RationalNumber num)
	{
		return ((numerator*num.denominator)/(denominator*num.numerator))%1==0;
	}
	
	public boolean isInteger()
	{
		return ((numerator/denominator)%1)==0;
	}
	
	public static RationalNumber random(int max)
	{
		Random random = new Random();
		return new RationalNumber(random.nextInt(max-1)+1,random.nextInt(max-1)+1);
	}
	
	public boolean equals(float num)
	{
		return (numerator/denominator)==num;
	}
	
	public boolean equals(RationalNumber num)
	{
		return (numerator/denominator)==(num.numerator/num.denominator);
	}
	
	@Override
	public RationalNumber clone()
	{
		return new RationalNumber(this.numerator,this.denominator);
	}
	
	@Override
	public String toString()
	{
		if(denominator==1)
		{
			return (int)numerator+"";
		}
		else if(denominator==0)
		{
			return "";
		}
		return (int)numerator+"/"+(int)denominator;
	}
}

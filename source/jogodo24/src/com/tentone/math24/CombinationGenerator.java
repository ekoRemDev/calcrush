package com.tentone.math24;

import java.util.Random;

public class CombinationGenerator
{	
	public static RationalNumber[] generateFractionalNumbers()
	{
		Random random = new Random();
		RationalNumber[] num = new RationalNumber[4];
		RationalNumber a = new RationalNumber(24,1);
		RationalNumber temp = new RationalNumber(0,1);
		RationalNumber numero = new RationalNumber(0,1);
		int operacao=0, i=0;

		while(a.realValue()!=0)
		{
			a.set(24);
			i=0;
			while(i<num.length)
			{
				numero=RationalNumber.random(10);
				operacao=random.nextInt(4);

				temp=a.clone();

				if(operacao==0)//Division
				{
					temp.divSelf(numero);
				}
				else if(operacao==1)//Sub
				{
					temp.subSelf(numero);
				}
				else if(operacao==2)//Plus
				{
					temp.addSelf(numero);
				}
				else if(operacao==3)//Mult
				{
					temp.mulSelf(numero);
				}

				//if(!temp.equals(0)||temp.equals(0) && i==3) HARDMODE
				if(temp.isInteger() && (!temp.equals(0)||temp.equals(0) && i==3))
				{
					a=temp;
					num[i]=numero;
					i++;
				}
			}
		}
		
		return num;
	}
	
	
	public static RationalNumber[] generateIntegerNumbers()
	{
		Random random = new Random();
		int[] num = new int[4];
		float a=24,temp=0;
		int numero=0,operacao=0, i=0;

		while(a!=0)
		{
			a=24;
			i=0;
			while(i<num.length)
			{
				numero=random.nextInt(10);
				while(numero==0)
				{
					numero=random.nextInt(10);
				}
				operacao=random.nextInt(4);

				temp=a;

				if(operacao==0)
				{
					temp/=numero;
				}
				else if(operacao==1)
				{
					temp-=numero;
				}
				else if(operacao==2)
				{
					temp+=numero;
				}
				else if(operacao==3)
				{
					temp*=numero;
				}

				if(temp%1==0 && (temp!=0||temp==0 && i==3))
				{
					a=temp;
					num[i]=numero;
					i++;
				}
			}
		}
		
		RationalNumber[] n = new RationalNumber[4];
		i=0;
		while(i<n.length)
		{
			n[i] = new RationalNumber(num[i],1);
			i++;
		}

		return n;
	}
}

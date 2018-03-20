package com.tentone.math24.ui;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.tentone.math24.GameElement;
import com.tentone.math24.Global;

public class Text extends GameElement
{
	private int font;
	public String text;
	public Aligment aligment;
	
	public Text(int fonti,String texto,float pos_x,float pos_y,Aligment aligment, boolean visible)
	{
		super(pos_x,pos_y,0f,0f,visible,false);
		this.aligment=aligment;
		text=texto;
		font=fonti;
	}
	
	public void draw()
	{
		if(visible)
		{
			if(aligment==Aligment.Center)
			{
				Global.font[font].drawMultiLine(Global.batch,text, pos.x, pos.y, 0, BitmapFont.HAlignment.CENTER);
			}
			else if(aligment==Aligment.Right)
			{
				Global.font[font].drawMultiLine(Global.batch,text, pos.x, pos.y, 0, BitmapFont.HAlignment.RIGHT);
			}
			else if(aligment==Aligment.Left)
			{
				Global.font[font].drawMultiLine(Global.batch,text, pos.x, pos.y, 0, BitmapFont.HAlignment.LEFT);
			}
		}
	}

}

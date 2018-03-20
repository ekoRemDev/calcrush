package com.tentone.math24.ui;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.tentone.math24.GameElement;
import com.tentone.math24.Global;

public class TextButton extends GameElement
{
	private Sprite sprite;
	private int font;
	
	public int[] textures;
	public Vector2 pos_text;
	public String text;
	
	public TextButton(int texture, int texture_pressed,float pos_x,float pos_y, float size_x, float size_y, boolean keep_aspect, boolean visible, int fonti, String texto,float y_alig)
	{
		super(pos_x,pos_y,size_x,size_y,visible,false);
		
		if(keep_aspect)
		{
			size.y=size.x/((float)Global.texture[texture].getWidth()/(float)Global.texture[texture].getHeight());
		}
		
		pos_text=new Vector2(pos.x+size.x/2f,pos.y+size.y*y_alig);
		
		font=fonti;
		text=texto;
		
		textures= new int[2];
		textures[0]=texture;
		textures[1]=texture_pressed;
		
		sprite = new Sprite(Global.texture[texture]);
		sprite.setPosition(pos.x,pos.y);
		sprite.setSize(size.x,size.y);
		sprite.setOrigin(0,0);
	}
	
	
	public boolean isOver()
	{
		return Global.is_touched && (Global.touch_x>pos.x && Global.touch_x<(pos.x+size.x) && Global.touch_y>pos.y && Global.touch_y<(pos.y+size.y));
	}
	
	public boolean isPressed()
	{
		return Global.released_touch && (Global.touch_x>pos.x && Global.touch_x<(pos.x+size.x) && Global.touch_y>pos.y && Global.touch_y<(pos.y+size.y));
	}
	
	public void draw()
	{
		if(visible)
		{
			if(isOver())
			{
				sprite.setTexture(Global.texture[textures[1]]);
			}
			else
			{
				sprite.setTexture(Global.texture[textures[0]]);
			}
			sprite.draw(Global.batch);
			Global.font[font].drawMultiLine(Global.batch,text,pos_text.x, pos_text.y, 0, BitmapFont.HAlignment.CENTER);
		}
	}
}

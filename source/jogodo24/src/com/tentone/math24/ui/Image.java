package com.tentone.math24.ui;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.tentone.math24.GameElement;
import com.tentone.math24.Global;

public class Image extends GameElement
{
	public Sprite sprite;
	public float angular_speed;
	
	public Image(int text,float pos_x,float pos_y, float size_x,float size_y,boolean keep_aspect_ratio, boolean center,boolean visible,boolean enable)
	{
		super(pos_x,pos_y,size_x,size_y,visible,enable);
		angular_speed=0;
		
		if(keep_aspect_ratio)
		{
			size.y=size.x/((float)Global.texture[text].getWidth()/(float)Global.texture[text].getHeight());
		}
		
		sprite = new Sprite(Global.texture[text]);
		
		if(center)
		{
			sprite.setOrigin(size.x/2,size.y/2);
			pos.x=pos.x-size.x/2;
			pos.y=pos.y-size.y/2;
		}
		
		sprite.setPosition(pos.x,pos.y);
		sprite.setSize(size.x,size.y);
	}
	
	public Image(int text,float pos_x,float pos_y, float size_x, float size_y, float ang_speed, boolean center,boolean visible)
	{
		super(pos_x,pos_y,size_x,size_y,visible,false);
		angular_speed=ang_speed;
		
		sprite = new Sprite(Global.texture[text]);
		sprite.setPosition(pos.x,pos.y);
		sprite.setSize(size.x,size.y);
		
		if(center)
		{
			sprite.setOrigin(size_x/2,size_y/2);
			sprite.setPosition(pos_x-size_x/2,pos_y-size_y/2);
		}
	}
	
	public Image(int text,float pos_x,float pos_y, float size_x, float size_y, float ang_speed, float center_x, float center_y,boolean visible)
	{
		super(pos_x,pos_y,size_x,size_y,visible,false);
		angular_speed=ang_speed;
		
		sprite = new Sprite(Global.texture[text]);
		sprite.setPosition(pos.x-center_x,pos.y-center_y);
		sprite.setSize(size.x,size.y);
		sprite.setOrigin(center_x,center_y);
	}
	
	public void setTexture(int i)
	{
		sprite.setTexture(Global.texture[i]);
	}
	
	public void draw()
	{
		if(visible)
		{
			if(enabled)
			{
				sprite.rotate(angular_speed);
			}
			sprite.draw(Global.batch);
		}
	}
}

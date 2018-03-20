package com.tentone.math24;

import com.badlogic.gdx.math.Vector2;

public class GameElement
{
	public boolean visible,enabled;
	public Vector2 pos,size;
	
	public GameElement(float pos_x,float pos_y,float size_x,float size_y,boolean visible,boolean enabled)
	{
		this.pos=new Vector2(pos_x,pos_y);
		this.size=new Vector2(size_x,size_y);
		this.enabled=enabled;
		this.visible=visible;
	}
}

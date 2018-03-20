package com.tentone.math24;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.MathUtils;

public class Particle
{ 
	//Particle Emiter Variables
	public int texture;
	
	//Emiter Variables
	public Vector2 particle_size,pos;
	public float particle_life_time,particle_life_time_variation;
	public float particle_speed, particle_speed_variation;
	public float particle_rotation_speed; //Texture Angular Speed
	public float particle_direction, particle_direction_var;
	public int particle_count;//Particle Count
	
	//Control Variables
	public boolean one_burst_only,aditive_mode;

	//Particles Array
	private Vector2[] speed;
	private float[] direction,time;
	private Sprite[] sprite;
	private boolean[] isactive;

	//Auxiliar Variables
	private float x,y,temp;

	public Particle(int text,float pos_x,float pos_y,float box_x,float box_y,float size_x, float size_y, float dir_a,float dir_b,int part_count,float life_time,float life_time_var ,float speeed, float speed_var,float angular_speed,int type,boolean one_burst,boolean aditive_draw)
	{
		pos = new Vector2(pos_x,pos_y);
		
		//Emiter Propieties
		particle_size= new Vector2(size_x,size_y);
		texture=text;
		particle_count=part_count;
		particle_life_time=life_time;
		particle_life_time_variation=life_time_var;
		particle_speed=speeed;
		particle_speed_variation=speed_var;
		particle_rotation_speed=angular_speed;
		one_burst_only=one_burst;
		particle_direction=dir_a;
		particle_direction_var=dir_b;
		aditive_mode=aditive_draw;

		//Ini Particle Data Arrays
		speed= new Vector2[part_count];
		time = new float[part_count];
		direction = new float[part_count];
		sprite = new Sprite[part_count];
		isactive = new boolean[part_count];

		//Calculate Emiter Center
		x=box_x/2f;
		y=box_y/2f;
		
		//Auxiliar Counter Var
		int i;
		
		i=0;
		while(i<part_count)
		{
			temp=MathUtils.random(); //Generate Random Number to Calculate Position for Particle
			x=particle_speed+(MathUtils.random()*particle_speed_variation);
			direction[i]=particle_direction+(MathUtils.random()*particle_direction_var);
			speed[i]= new Vector2(x*MathUtils.cos(direction[i] * 0.017453f),x*MathUtils.sin(direction[i] * 0.017453f));
			time[i]=(particle_life_time+(((MathUtils.random()*2f)-1f)*particle_life_time_variation))*(temp);
			isactive[i]=!one_burst;
			sprite[i]=new Sprite(Global.texture[texture]);
			sprite[i].setRotation(0f);
			sprite[i].setPosition(pos.x+particle_life_time*speed[i].x*temp,pos.y+particle_life_time*speed[i].y*temp);
			sprite[i].setSize(size_x,size_y);
			sprite[i].setOrigin(x,y);
			i++;
		}
	}
	
	public boolean isActive()
	{
		return false;
	}
	
	//Updates Particles Position
	public void update()
	{
		int i=0;
		
		while(i<particle_count)
		{
			if(isactive[i])//If particle is active
			{
				sprite[i].setRotation(sprite[i].getRotation()+particle_rotation_speed);
				sprite[i].setPosition(sprite[i].getX()+speed[i].x,sprite[i].getY()+speed[i].y);
				time[i]--;
				if(time[i]<0)
				{
					if(!one_burst_only)//Restart Particle with new settings
					{
						x=particle_speed+(MathUtils.random()*particle_speed_variation);
						direction[i]=particle_direction+(MathUtils.random()*particle_direction_var);
						speed[i]= new Vector2(x*MathUtils.cos(direction[i] * 0.017453f),x*MathUtils.sin(direction[i] * 0.017453f));
						time[i]=particle_life_time+(((MathUtils.random()*2f)-1f)*particle_life_time_variation);
						sprite[i].setRotation(0f);
						sprite[i].setPosition(pos.x,pos.y);
					}
					else//If on only_one_burst mode deactivate particle
					{
						isactive[i]=false;
					}
				}
			}
			i++;
		}
	}

	//Draw Particle into Global.camera.batch
	public void draw()
	{
		int i=0;
		
		if(aditive_mode)
		{
			//Set Aditive blend
			Global.batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
			
			i=0;
			while(i<particle_count)
			{
				if(isactive[i])
				{
					sprite[i].draw(Global.batch);
				}
				i++;
			}
			
			//Restore Blend Mode
			Global.batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		}
		else
		{
			i=0;
			while(i<particle_count)
			{
				if(isactive[i])
				{
					sprite[i].draw(Global.batch);
				}
				i++;
			}
		}
	}

	//Start new particle burst
	public void burst()
	{
		int i=0;
		while(i<particle_count)
		{
			//Ini Particle
			x=particle_speed+(MathUtils.random()*particle_speed_variation);
			direction[i]=particle_direction+(MathUtils.random()*particle_direction_var);
			speed[i]= new Vector2(x*MathUtils.cos(direction[i] * 0.017453f),x*MathUtils.sin(direction[i] * 0.017453f));
			time[i]=particle_life_time+(((MathUtils.random()*2f)-1f)*particle_life_time_variation);
			sprite[i].setRotation(0f);
			isactive[i]=true;
			
			//Set Particles Position
			sprite[i].setPosition(pos.x,pos.y);

			
			i++;
		}
	}
}

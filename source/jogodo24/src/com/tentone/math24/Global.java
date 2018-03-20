package com.tentone.math24;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Global
{
	//Game Version
	public static String version="1.2.0";
	public static String year="2015";
	
	//Game Data
	public static Preferences shared_prefs;
    public static int def_highscore,def_highscore_rational,def_language,temp_game_count,temp_game_solved_count;
    public static boolean sound_enabled,def_autofinish,def_backanim,debug_mode;
    
	//Touch-Vars
	public static boolean is_touched, just_touched, released_touch;
	public static float touch_x, touch_y;
	
	//Data Variables
	public static Texture[] texture;
	public static BitmapFont[] font;
	public static Sound[] audio;
	
	//Camera Variables
	public static OrthographicCamera camera;
	public static SpriteBatch batch;
	public static float screen_size_x,screen_size_y;
	public static float camera_size_x,camera_size_y;
	public static float aspect_ratio;
    public static float camera_ratio;
    
    //Auxiliary Variables
    static int i=0;
    
	public static void ini()
	{
		//Ini Camera
		screen_size_y=Gdx.graphics.getHeight();
		screen_size_x=Gdx.graphics.getWidth();
		aspect_ratio=screen_size_x/screen_size_y;
		camera_size_x=600;
		camera_size_y=camera_size_x/aspect_ratio;
		camera_ratio=camera_size_y/screen_size_y;
		
		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		camera.setToOrtho(false,camera_size_x,camera_size_y);
		camera.update();
		
		//Ini SharedPrefs
		shared_prefs= Gdx.app.getPreferences("math24prefs");
		
		//Ini Touch Control Vars
		just_touched=false;
		is_touched=false;
		released_touch=false;
		touch_x=0;
		touch_y=0;
	}

	public static void updateTouch()
	{
		if(Gdx.input.justTouched())
		{
			released_touch=false;
			just_touched=true;
			is_touched=true;
			touch_x=Gdx.input.getX()*Global.camera_ratio;
			touch_y=Global.camera_size_y-(Gdx.input.getY()*Global.camera_ratio);
		}
		else if(Gdx.input.isTouched())
		{
			released_touch=false;
			just_touched=false;
			is_touched=true;
			touch_x=Gdx.input.getX()*Global.camera_ratio;
			touch_y=Global.camera_size_y-(Gdx.input.getY()*Global.camera_ratio);
		}
		else
		{
			if(is_touched)
			{
				released_touch=true;
				just_touched=false;
				is_touched=false;
			}
			else
			{
				released_touch=false;
				just_touched=false;
				is_touched=false;
			}
		}
	}
	
	public static void saveData()
	{
		shared_prefs.putInteger("def_language",def_language); //0 -> EN | 1 -> PT
    	shared_prefs.putInteger("def_highscore",def_highscore);
    	shared_prefs.putInteger("def_highscore_rational",def_highscore_rational);
    	shared_prefs.putInteger("temp_game_count",temp_game_count);
    	shared_prefs.putInteger("temp_game_solved_count",temp_game_solved_count);
    	shared_prefs.putBoolean("sound_enabled",sound_enabled);
    	shared_prefs.putBoolean("def_autofinish",def_autofinish);
    	shared_prefs.putBoolean("debug_mode",debug_mode);
    	shared_prefs.putBoolean("def_backanim",def_backanim);
		shared_prefs.flush();
	}
	
	public static void loadData()
	{
		//Load Preferences
		def_language=shared_prefs.getInteger("def_language",0);
		def_highscore=shared_prefs.getInteger("def_highscore",0);
		def_highscore_rational=shared_prefs.getInteger("def_highscore_rational",0);
		temp_game_count=shared_prefs.getInteger("temp_game_count",0);
		temp_game_solved_count=shared_prefs.getInteger("temp_game_solved_count",0);
		sound_enabled=shared_prefs.getBoolean("sound_enabled",true);
		def_autofinish=shared_prefs.getBoolean("def_autofinish",false);
		debug_mode=shared_prefs.getBoolean("debug_mode",false);
		def_backanim=shared_prefs.getBoolean("def_backanim",true);
				
		//Load Texture Pool from TextureList List
		texture=new Texture[TextureList.list.length];
		if(screen_size_x>320)
		{
			try
			{
				i=0;
				while(i<texture.length)
				{
					try
					{
						texture[i] = new Texture("data/texture/"+TextureList.list[i]);
					}
					catch(Exception e){}
					i++;
				}
			}
			catch(Exception e)
			{
				i=0;
				while(i<texture.length)
				{
					try
					{
						texture[i].dispose();
						texture[i] = new Texture("data/texture/lowres/"+TextureList.list[i]);
					}
					catch(Exception ex){}
				}
				
			}
		}
		else
		{
			i=0;
			while(i<texture.length)
			{
				try
				{
					texture[i] = new Texture("data/texture/lowres/"+TextureList.list[i]);
				}
				catch(Exception e){}
				i++;
			}
		}
		
		//Apply filter to all textures
		i=0;
		while(i<texture.length)
		{
			if(texture[i]!=null)
			{
				texture[i].setFilter(TextureFilter.Linear, TextureFilter.Linear);
			}
			i++;
		}
		
		//Load Fonts
		font=new BitmapFont[10];
		font[0] = new BitmapFont(Gdx.files.internal("data/font/bronic.fnt")); //bronic scale 0.35
		font[0].setScale(0.35f);
		font[1] = new BitmapFont(Gdx.files.internal("data/font/arial.fnt")); //arial scale 0.8
		font[1].setScale(0.8f);
		font[2] = new BitmapFont(Gdx.files.internal("data/font/bronic_w.fnt")); //bronic scale 0.5
		font[2].setScale(0.5f);
		font[3] = new BitmapFont(Gdx.files.internal("data/font/bronic_out.fnt")); //bronic_out scale 0.5
		font[3].setScale(0.5f);
		font[4] = new BitmapFont(Gdx.files.internal("data/font/bronic_out.fnt")); //bronic_out scale 1
		font[5] = new BitmapFont(Gdx.files.internal("data/font/bronic_out.fnt")); //bronic_out scale 0.5
		font[5].setScale(0.4f);
		font[6] = new BitmapFont(Gdx.files.internal("data/font/bronic_w.fnt")); //bronic white scale 0.3
		font[6].setScale(0.3f);
		font[7] = new BitmapFont(Gdx.files.internal("data/font/bronic_w.fnt")); //bronic scale 0.65 (Used to draw numbers)
		font[7].setScale(0.65f);
		font[8] = new BitmapFont(Gdx.files.internal("data/font/bronic_w.fnt")); //bronic scale 0.55 (Used to draw rat_numbers)
		font[8].setScale(0.55f);
		font[9] = new BitmapFont(Gdx.files.internal("data/font/bronic_out.fnt")); //bronic_out scale 0.3
		font[9].setScale(0.3f);
		
		i=0;
		while(i<font.length)
		{
		    font[i].getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
			i++;
		}
		
		//Load Audio
		audio= new Sound[3];
		audio[0]= Gdx.audio.newSound(Gdx.files.internal("data/audio/gameclick.ogg"));
		audio[1]= Gdx.audio.newSound(Gdx.files.internal("data/audio/correct.ogg"));
		audio[2]= Gdx.audio.newSound(Gdx.files.internal("data/audio/wrong.ogg"));
	}
	
	public static void audioPlay(int a)
	{
		if(sound_enabled)
		{
			audio[a].play();
		}
	}
	
	public static void startFrame()
	{
		Gdx.gl.glClearColor(1f,1,1,1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
	}

	public static void endFrame()
	{
		batch.end();
	}
	
	public static void dispose()
	{
		i=0;
		while(i<font.length)
		{
		    font[i].dispose();
			i++;
		}
		
		i=0;
		while(i<texture.length)
		{
			try
			{
				texture[i].dispose();
		    }catch(Exception e){}
			i++;
		}
		
		i=0;
		while(i<audio.length)
		{
		    audio[i].dispose();
			i++;
		}
		
		batch.dispose();
	}
}

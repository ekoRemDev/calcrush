package com.tentone.math24;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tentone.ganimator.Animation;
import com.tentone.ganimator.TextureManager;
import com.tentone.math24.ui.Aligment;
import com.tentone.math24.ui.Button;
import com.tentone.math24.ui.Image;
import com.tentone.math24.ui.NumberButton;
import com.tentone.math24.ui.Text;
import com.tentone.math24.ui.TextButton;

public class MainGame implements ApplicationListener
{
	enum GameState{Menu,GameNormalArcade,GameNormalFree,GameRationalArcade,GameRationalFree,GameOver};
	enum MenuOption{Arcade,Free,Classic,Rational};
	
	//Game Vars
	static GameState game_state; //Stores Actual State of the game
	static RationalNumber[] num; //Number "Stack"
    static RationalNumber[] b_num; //Original generated number "stack"
    static boolean[] e_num; //Available numbers
    
    static RationalNumber op_1, op_2; //Number Selector
    static int calc_sel; //Operation selector
    static boolean e_op_1,e_op_2;
    
    static int points,num_count; //Time and point counter
    
    //Auxiliar Variables
    static int i;
    static RationalNumber temp;
    
    //Timer Vars
    static Timer timer;
    static int time;
    
    //Debug Tex
    static Text debug;
    
    //BackGround
    static Image back, back_mountain;
    
	//Menu
    static float game_layout_correct,game_card_size,game_layout_number_size;
    static Image about_back, about_logo, about_sep_a, about_sep_b;
    
    static Text about_backanim,about_lang, about_finish, about_version,about_highscore,about_highscore_value;
    static Button about_but_backanim,about_but_lang, about_but_finish;
    static Button menu_game_arcade, menu_game_free, menu_about, menu_exit,menu_sound,menu_classic,menu_rational;
    static Image game_logo, about_highscore_medal;
    
    static Text about_highscore_rational,about_highscore_rational_value;
    static Image about_highscore_rational_medal;
    
    static MenuOption menu_option;
 	
	//Game
    static NumberButton num_1,num_2,num_3,num_4;
    static Image num_1_back,num_2_back,num_3_back,num_4_back; //Can be changed for animations
    static Button op_add,op_sub,op_multi,op_div;
    static TextButton calc_num_1,calc_num_2,calc_op, game_error;
    static Button but_try_again,but_free_exit;
    static TextButton but_skip, but_retry,but_exit;
    static Text text_points,text_time;
    static Image image_time,image_points,image_card;
    static ArrayList<Image> image_timer;
    
    //GameOver
    static Text game_over_yourscore,game_over_highscore;
    static Button game_over_back,game_over_retry;
    static Image game_over_background, game_over_img_score, game_over_img_highscore, game_over_img_medal,game_over_new_highscore;
    
    //Animations
    static Animation cloud;
    static Particle particle;
    
	@Override
	public void create()
	{		
		Global.ini();
		Global.loadData();
		
		//TODO Force Debug Mode Off for release
		Global.debug_mode=false;
		
		//Game Vars
		game_state=GameState.Menu;
		
		//Timer
		time=0;
		timer= new Timer();
		timer.scheduleAtFixedRate(new TimerTask()
		{
			  @Override
			  public void run()
			  {
				  time--;
			  }
		},10,1000);
		
		//Animations
		try
		{
			cloud = new Animation(new TextureManager("data/animation/cloud/"));
			cloud.loadFile("data/animation/cloud.gsa");
		}
		catch (Exception e){}
		
		//Particles
		particle = new Particle(62,0f,0f,0f,0f,32f,32f,0f,360f,60,140,50,3,2,0,0,true,false);
		iniInterface();
	}

	@Override
	public void dispose()
	{
		Global.dispose();
	}

	@Override
	public void render()
	{
		//Update
		Global.updateTouch();
		if(game_state==GameState.Menu)
		{
			menuUpdate();
		}
		else if(game_state==GameState.GameNormalArcade || game_state==GameState.GameNormalFree || game_state==GameState.GameRationalArcade || game_state==GameState.GameRationalFree)
		{
			gameUpdate();
		}
		else if(game_state==GameState.GameOver)
		{
			gameOverUpdate();
		}
		
		//Render
		Global.startFrame();
		backgroundDraw();
		if(game_state==GameState.Menu)
		{
			menuDraw();
		}
		else if(game_state==GameState.GameNormalArcade || game_state==GameState.GameNormalFree || game_state==GameState.GameRationalArcade || game_state==GameState.GameRationalFree)
		{
			gameDraw();
		}
		else if(game_state==GameState.GameOver)
		{
			gameDraw();
			gameOverDraw();
		}
		
		//Debug Overlay
		if(Global.debug_mode)
		{
			debug.text="Calc Rush | V"+Global.version+" | Tentone "+Global.year+"\nFPS:"+Gdx.graphics.getFramesPerSecond()+"\nSound:"+Global.sound_enabled+"\nAuto-finish:"+Global.def_autofinish+"\nLanguage:"+Global.def_language+"\nWindow Size:"+Gdx.graphics.getWidth()+"x"+Gdx.graphics.getHeight()+"\nCamera Size:"+Global.camera_size_x+"x"+Global.camera_size_y;
			debug.draw();
		}
		
		Global.endFrame();
	}
	
	public void iniInterface()
	{
		//Debug
		debug = new Text(1," ",0,Global.camera_size_y,Aligment.Left,true);

		//BackGround
		back= new Image(2,0,0,Global.camera_size_x,Global.camera_size_y,0,false,true);
		back_mountain = new Image(4,0,0,Global.camera_size_x,0,true,false,true,false);

		//Menu
		game_logo = new Image(11,Global.camera_size_x/2f,Global.camera_size_y-175,550,0,true,true,true,true);
		game_logo.angular_speed=0.08f;
		menu_game_arcade=new Button(27,29,Global.camera_size_x/2-250,Global.camera_size_y-480,500,0,true,true);
		menu_game_free=new Button(33,35,Global.camera_size_x/2-250,Global.camera_size_y-610,500,0,true,true);
		menu_classic=new Button(68,70,Global.camera_size_x/2-250,Global.camera_size_y-480,500,0,true,false);
		menu_classic.enabled=false;
		menu_rational=new Button(72,74,Global.camera_size_x/2-250,Global.camera_size_y-610,500,0,true,false);
		menu_rational.enabled=false;
		menu_exit= new Button(31,32,10,10,120,120,false,true);
		menu_sound= new Button(40,41,140,10,120,120,false,true);
		menu_about=  new Button(25,26,Global.camera_size_x-140,10,120,120,false,true);

		//Menu-About/Settings
		about_back = new Image(37,Global.camera_size_x/2f,Global.camera_size_y/2,500,0,true,true,false,false);
		about_logo = new Image(1,Global.camera_size_x/2f,about_back.pos.y+about_back.size.y-60,280,0,true,true,false,false);
		about_sep_a = new Image(0,Global.camera_size_x/2f,about_logo.pos.y-20,500,0,false,true,false,false);

		about_backanim = new Text(5,"Back. Animation",60,about_sep_a.pos.y,Aligment.Left,false);
		about_but_backanim = new Button(63,63,Global.camera_size_x-110,about_sep_a.pos.y-40,50,0,true,false);

		about_finish = new Text(5,"Auto-Finish",60,about_sep_a.pos.y-70,Aligment.Left,false);
		about_but_finish = new Button(63,63,Global.camera_size_x-110,about_sep_a.pos.y-110,50,0,true,false);

		about_lang = new Text(5,"Language",60,about_sep_a.pos.y-140,Aligment.Left,false);
		about_but_lang = new Button(58,59,Global.camera_size_x-160,about_sep_a.pos.y-190,100,0,true,false);

		about_highscore = new Text(5,"HighScore",60,about_sep_a.pos.y-200,Aligment.Left,false);
		about_highscore_value = new Text(5,"0",Global.camera_size_x-90,about_sep_a.pos.y-200,Aligment.Right,false);
		about_highscore_medal  = new Image(51,Global.camera_size_x-90,about_sep_a.pos.y-260,30,0,true,false,false,false);

		about_highscore_rational = new Text(5,"HighScore ",60,about_sep_a.pos.y-260,Aligment.Left,false);
		about_highscore_rational_value = new Text(5,"0",Global.camera_size_x-90,about_sep_a.pos.y-260,Aligment.Right,false);
		about_highscore_rational_medal  = new Image(51,Global.camera_size_x-90,about_sep_a.pos.y-320,30,0,true,false,false,false);
		
		about_sep_b = new Image(0,Global.camera_size_x/2f,about_logo.pos.y-200,500,0,true,true,false,false);
		about_version = new Text(9,"",Global.camera_size_x/2f,about_back.pos.y+90,Aligment.Center,false);

		//Game Layout
		image_timer = new ArrayList<Image>();
		image_points= new Image(48,Global.camera_size_x-50,Global.camera_size_y-50,50,0,true,false,true,false);
		image_time= new Image(49,Global.camera_size_x-50,Global.camera_size_y-100,50,0,true,false,true,false);
		text_points= new Text(0,"points",Global.camera_size_x-60,Global.camera_size_y-10,Aligment.Right,true);
		text_time= new Text(0,"time",Global.camera_size_x-60,Global.camera_size_y-55,Aligment.Right,true);

		calc_num_1 = new TextButton(5,6,10,Global.camera_size_y-190,220,90,false,true,2,"-",0.8f);
		calc_num_2 = new TextButton(5,6,Global.camera_size_x-230,Global.camera_size_y-190,220,90,false,true,2,"-",0.8f);
		calc_op = new TextButton(7,7,Global.camera_size_x/2-45,Global.camera_size_y-190,90,90,false,true,2,"-",0.8f);
		
		game_layout_correct=0;
		game_card_size=500;
		game_layout_number_size=1;
		
		if(Global.camera_size_y>920)
		{
			game_layout_correct=(Global.camera_size_y-920f)/2f;
		}
		
		if(Global.camera_size_y<920)
		{
			game_card_size=500-(920-Global.camera_size_y);
			game_layout_number_size=game_card_size/500;
			Global.font[7].setScale(game_layout_number_size*0.65f);
			Global.font[8].setScale(game_layout_number_size*0.55f);
		}
		
		image_card = new Image(3,Global.camera_size_x/2f,Global.camera_size_y-200-game_card_size/2-game_layout_correct,game_card_size,game_card_size,false,true,true,false);
		
		num_1 = new NumberButton(0,13,Global.camera_size_x/2-90*game_layout_number_size,image_card.pos.y+image_card.size.y-180*game_layout_number_size,180*game_layout_number_size,180*game_layout_number_size,false,true,7,8,new RationalNumber(1),0.7f,true);
		num_1_back = new Image(12,num_1.pos.x,num_1.pos.y,num_1.size.x,num_1.size.y,true,false,true,true);
		num_3 = new NumberButton(0,13,Global.camera_size_x/2-90*game_layout_number_size,image_card.pos.y,180*game_layout_number_size,180*game_layout_number_size,false,true,7,8,new RationalNumber(1),0.7f,true);
		num_3_back = new Image(12,num_3.pos.x,num_3.pos.y,num_3.size.x,num_3.size.y,true,false,true,true);
		num_4 = new NumberButton(0,13,image_card.pos.x,image_card.pos.y+image_card.size.y/2-90*game_layout_number_size,180*game_layout_number_size,180*game_layout_number_size,false,true,7,8,new RationalNumber(1),0.7f,true);
		num_4_back = new Image(12,num_4.pos.x,num_4.pos.y,num_4.size.x,num_4.size.y,true,false,true,true);
		num_2 = new NumberButton(0,13,image_card.pos.x+image_card.size.x-160*game_layout_number_size,image_card.pos.y+image_card.size.y/2-90*game_layout_number_size,180*game_layout_number_size,180*game_layout_number_size,false,true,7,8,new RationalNumber(1),0.7f,true);
		num_2_back = new Image(12,num_2.pos.x,num_2.pos.y,num_2.size.x,num_2.size.y,true,false,true,true);
		
		op_add = new Button(14,15,10,image_card.pos.y-140-game_layout_correct,130,130,false,true);
		op_sub = new Button(20,21,160,image_card.pos.y-140-game_layout_correct,130,130,false,true);
		op_multi = new Button(18,19,310,image_card.pos.y-140-game_layout_correct,130,130,false,true);
		op_div = new Button(16,17,460,image_card.pos.y-140-game_layout_correct,130,130,false,true);

		but_skip = new TextButton(8,22,Global.camera_size_x-200,op_add.pos.y-60,170,0,true,true,6,"Skip",0.9f);
		but_retry = new TextButton(8,22,30,op_add.pos.y-60,170,0,true,true,6,"Retry",0.9f);
		but_exit = new TextButton(8,22,Global.camera_size_x/2-85,op_add.pos.y-60,170,0,true,true,6,"Exit",0.9f);
		
		//Error Box
		game_error=new TextButton(9,9,Global.camera_size_x/2-250,Global.camera_size_y/2-250/textAspectRatio(9),500,500/textAspectRatio(9),false,false,3,"",0.7f);

		//Game over
		game_over_background = new Image(10,Global.camera_size_x/2f,Global.camera_size_y/2,Global.camera_size_x,0,true,true,true,true);
		game_over_back = new Button(42,43,10,game_over_background.pos.y+80,120,120,false,true);
		game_over_retry = new Button(46,47,140,game_over_background.pos.y+80,120,120,false,true);

		game_over_yourscore = new Text(4,"",Global.camera_size_x/2f,game_over_background.pos.y+game_over_background.size.y-250,Aligment.Center,true);
		game_over_highscore =  new Text(4,"",Global.camera_size_x/2f,game_over_background.pos.y+game_over_background.size.y-500,Aligment.Center,true);

		game_over_img_score = new Image(45,Global.camera_size_x/2f,game_over_background.pos.y+game_over_background.size.y-180,250,0,true,true,true,false); //Reini Lang
		game_over_img_highscore = new Image(24,Global.camera_size_x/2f,game_over_background.pos.y+game_over_background.size.y-450,200,0,true,true,true,false); //Reini Lang
		game_over_img_medal = new Image(51,game_over_yourscore.pos.x+70,game_over_yourscore.pos.y-120,50,0,true,true,true,false);
		game_over_new_highscore = new Image(65,game_over_yourscore.pos.x,game_over_yourscore.pos.y-50,220,220,-0.6f,true,true);
		game_over_new_highscore.enabled=true;
		
		cloud.setPosition(0,Global.camera_size_y-500);
		particle.pos.set(300f,Global.camera_size_y/2f);
		
		//Update Interface Elements
		if(Global.sound_enabled)
		{
			menu_sound.textures[0]=40;
			menu_sound.textures[1]=41;
		}
		else
		{
			menu_sound.textures[0]=38;
			menu_sound.textures[1]=39;
		}

		if(Global.def_autofinish)
		{
			about_but_finish.textures[0]=64;
			about_but_finish.textures[1]=64;
		}
		else
		{
			about_but_finish.textures[0]=63;
			about_but_finish.textures[1]=63;
		}

		if(Global.def_backanim)
		{
			about_but_backanim.textures[0]=64;
			about_but_backanim.textures[1]=64;
		}
		else
		{
			about_but_backanim.textures[0]=63;
			about_but_backanim.textures[1]=63;
		}

		updateLanguage();
	}
	
	//BackGround Draw
	public void backgroundDraw()
	{
		back.draw();
		if(Global.def_backanim)
		{
			cloud.update(1f/60f);
			cloud.draw(Global.batch);
		}
		back_mountain.draw();
	}
	
	//Game Render 
	public void gameDraw()
	{
		image_points.draw();
		image_time.draw();
		text_points.draw();
		text_time.draw();
		
		calc_num_1.draw();
		calc_num_2.draw();
		calc_op.draw();
		
		image_card.draw();
		
		num_1_back.draw();
		num_2_back.draw();
		num_3_back.draw();
		num_4_back.draw();
		
		num_1.draw();
		num_2.draw();
		num_3.draw();
		num_4.draw();
		
		op_add.draw();
		op_sub.draw();
		op_div.draw();
		op_multi.draw();
		
		but_skip.draw();
		but_retry.draw();
		but_exit.draw();
		
		if(Global.def_backanim)
		{
			particle.update();
			particle.draw();
		}
		
		//Iterate all imag timer messages
		Iterator<Image> it = image_timer.iterator();
		while(it.hasNext())
		{
			Image temp = it.next();
			if((game_state==GameState.GameNormalArcade||game_state==GameState.GameRationalArcade) && temp.sprite.getColor().a>0.02)
			{
				temp.sprite.translateX(1.5f);
				temp.sprite.setColor(1f, 1f, 1f, temp.sprite.getColor().a-0.01f);
				temp.draw();
			}
			else
			{
				it.remove();
			}
		}
		game_error.draw();
	}
	
	//Game Update
	public void gameUpdate()
	{
		//Button Processing
		if(game_error.visible && game_error.isPressed())
		{
			game_error.visible=false;
		}
		else if(calc_num_1.isPressed())
		{
            return_num_1();
            updateInterfaceElements();
            Global.audioPlay(0);
		}
		else if(calc_num_2.isPressed())
		{
            return_num_2();
            updateInterfaceElements();
            Global.audioPlay(0);
		}
		else if(num_1.isPressed() && !game_error.visible)
		{
            play(0);
            updateGameState();
            updateInterfaceElements();
            Global.audioPlay(0);
		}
		else if(num_2.isPressed() && !game_error.visible)
		{
            play(1);
            updateGameState();
            updateInterfaceElements();
            Global.audioPlay(0);
		}
		else if(num_3.isPressed() && !game_error.visible)
		{
            play(2);
            updateGameState();
            updateInterfaceElements();
            Global.audioPlay(0);
		}
		else if(num_4.isPressed() && !game_error.visible)
		{
            play(3);
            updateGameState();
            updateInterfaceElements();
            Global.audioPlay(0);
		}
		else if(op_add.isPressed())
		{
            calc_sel=1;
            updateGameState();
            updateInterfaceElements();
            Global.audioPlay(0);
		}
		else if(op_sub.isPressed())
		{
            calc_sel=2;
            updateGameState();
            updateInterfaceElements();
            Global.audioPlay(0);
		}
		else if(op_div.isPressed())
		{
            calc_sel=4;
            updateGameState();
            updateInterfaceElements();
            Global.audioPlay(0);
		}
		else if(op_multi.isPressed())
		{
            calc_sel=3;
            updateGameState();
            updateInterfaceElements();
            Global.audioPlay(0);
		}
		else if(but_retry.isPressed())
		{
			restartBoard();
			updateInterfaceElements();
			Global.audioPlay(0);
		}
		else if(but_skip.isPressed())
		{
			time-=5;
			showTimerSms(67);
			nextBoard();
			updateInterfaceElements();
			Global.audioPlay(0);
		}
		else if(but_exit.isPressed() && but_exit.visible)
		{
			game_state=GameState.Menu;
		}
		
		//Change Wood Card status
		if(e_num[0])
		{num_1_back.setTexture(12);}
		else
		{num_1_back.setTexture(50);}
		
		if(e_num[1])
		{num_2_back.setTexture(12);}
		else
		{num_2_back.setTexture(50);}
		
		if(e_num[2])
		{num_3_back.setTexture(12);}
		else
		{num_3_back.setTexture(50);}
		
		if(e_num[3])
		{num_4_back.setTexture(12);}
		else
		{num_4_back.setTexture(50);}
		
		//Time Update Arcade Mode
		if(game_state==GameState.GameNormalArcade || game_state==GameState.GameRationalArcade)
		{
			text_time.text=""+time;
			if(time<1)
			{
				if(game_state==GameState.GameNormalArcade)
				{
					game_over_new_highscore.visible=false;
		            if(points>Global.def_highscore)
		            {
		                Global.def_highscore=points;
		                game_over_new_highscore.visible=true;
		            }
		            
					Global.saveData();
					
					game_over_yourscore.text=points+"";
					game_over_highscore.text=Global.def_highscore+"";
					text_time.text="-";
				}
				else if(game_state==GameState.GameRationalArcade)
				{
					game_over_new_highscore.visible=false;
		            if(points>Global.def_highscore_rational)
		            {
		                Global.def_highscore_rational=points;
		                game_over_new_highscore.visible=true;
		            }
		            
					Global.saveData();
					
					game_over_yourscore.text=points+"";
					game_over_highscore.text=Global.def_highscore_rational+"";
					text_time.text="-";
				}
				
				if(points>=10 && points <20)
				{
					game_over_img_medal.setTexture(51);
				}
				else if(points>=20 && points <30)
				{
					game_over_img_medal.setTexture(52);
				}
				else if(points>=30 && points <40)
				{
					game_over_img_medal.setTexture(53);
				}
				else if(points>=40 && points <50)
				{
					game_over_img_medal.setTexture(54);
				}
				else if(points>=50 && points <70)
				{
					game_over_img_medal.setTexture(55);
				}
				else if(points>=70 && points <90)
				{
					game_over_img_medal.setTexture(56);
				}
				else if(points>=90)
				{
					game_over_img_medal.setTexture(57);
				}
				else
				{
					game_over_img_medal.setTexture(0);
				}
				
				game_state=GameState.GameOver;
			}
		}
	}
	
	//Game Over Render
	public void gameOverDraw()
	{
		game_over_background.draw();
		game_over_back.draw();
		game_over_retry.draw();
		
		game_over_new_highscore.draw();
		game_over_yourscore.draw();
		game_over_highscore.draw();
		
		game_over_img_score.draw();
		game_over_img_highscore.draw();
		game_over_img_medal.draw();
	}
	
	//Game Over Update
	public void gameOverUpdate()
	{
		if(game_over_retry.isPressed())
		{
			if(menu_option==MenuOption.Rational)
			{
				game_state=GameState.GameRationalArcade;
			}
			else
			{
				game_state=GameState.GameNormalArcade;
			}
			new_game();
			Global.audioPlay(0);
		}
		else if(game_over_back.isPressed())
		{
			game_state=GameState.Menu;
			Global.audioPlay(0);
		}
	}
	
	//Menu Render
	public void menuDraw()
	{
		game_logo.draw();
		menu_game_arcade.draw();
		menu_game_free.draw();
		menu_classic.draw();
		menu_rational.draw();
		menu_exit.draw();
		menu_sound.draw();
		menu_about.draw();
		
		about_back.draw();
		about_logo.draw();
		about_lang.draw();
		about_but_lang.draw();
		about_backanim.draw();
		about_but_backanim.draw();
		about_finish.draw();
		about_but_finish.draw();
		about_version.draw();

		about_highscore.draw();
		about_highscore_value.draw();
		about_highscore_medal.draw();
		
		about_highscore_rational.draw();
		about_highscore_rational_value.draw();
		about_highscore_rational_medal.draw();
	}
	
	//Menu Update
	public void menuUpdate()
	{
		if(menu_game_arcade.enabled && menu_game_arcade.isPressed())
		{
			menu_option=MenuOption.Arcade;
			Global.audioPlay(0);
			menu_classic.visible=true;
			menu_classic.enabled=true;
			menu_rational.visible=true;
			menu_rational.enabled=true;
			
			//Disable this button
			menu_game_arcade.enabled=false;
			menu_game_arcade.visible=false;
			menu_game_free.enabled=false;
			menu_game_free.visible=false;
		}
		else if(menu_game_free.enabled && menu_game_free.isPressed())
		{
			menu_option=MenuOption.Free;
			Global.audioPlay(0);
			menu_classic.visible=true;
			menu_classic.enabled=true;
			menu_rational.visible=true;
			menu_rational.enabled=true;
			
			//Disable this button
			menu_game_arcade.enabled=false;
			menu_game_arcade.visible=false;
			menu_game_free.enabled=false;
			menu_game_free.visible=false;
		}
		else if(menu_classic.isPressed() && menu_classic.enabled)
		{
			menu_classic.visible=false;
			menu_classic.enabled=false;
			menu_rational.visible=false;
			menu_rational.enabled=false;
			menu_game_arcade.enabled=true;
			menu_game_arcade.visible=true;
			menu_game_free.enabled=true;
			menu_game_free.visible=true;
			Global.audioPlay(0);
			if(menu_option==MenuOption.Arcade)
			{
				game_state=GameState.GameNormalArcade;
				but_exit.visible=false;
				new_game();
			}
			else if(menu_option==MenuOption.Free)
			{
				game_state=GameState.GameNormalFree;
				text_time.text="-";
				but_exit.visible=true;
				new_game_free();
			}
			menu_option=MenuOption.Classic;
		}
		else if(menu_rational.isPressed() && menu_classic.enabled)
		{
			menu_classic.visible=false;
			menu_classic.enabled=false;
			menu_rational.visible=false;
			menu_rational.enabled=false;
			menu_game_arcade.enabled=true;
			menu_game_arcade.visible=true;
			menu_game_free.enabled=true;
			menu_game_free.visible=true;
			Global.audioPlay(0);
			if(menu_option==MenuOption.Arcade)
			{
				game_state=GameState.GameRationalArcade;
				but_exit.visible=false;
				new_game();
			}
			else if(menu_option==MenuOption.Free)
			{
				game_state=GameState.GameRationalFree;
				text_time.text="-";
				but_exit.visible=true;
				new_game_free();
			}
			menu_option=MenuOption.Rational;
		}
		else if(menu_exit.isPressed())
		{
			if(menu_classic.visible && menu_classic.enabled)
			{
				menu_classic.visible=false;
				menu_classic.enabled=false;
				menu_rational.visible=false;
				menu_rational.enabled=false;
				menu_game_arcade.enabled=true;
				menu_game_arcade.visible=true;
				menu_game_free.enabled=true;
				menu_game_free.visible=true;
				Global.audioPlay(0);
			}
			else
			{
				Gdx.app.exit();
			}
		}
		else if(menu_sound.isPressed())
		{
			if(Global.sound_enabled)
			{
				Global.sound_enabled=false;
				Global.saveData();
				menu_sound.textures[0]=38;
				menu_sound.textures[1]=39;
			}
			else
			{
				Global.sound_enabled=true;
				Global.saveData();
				menu_sound.textures[0]=40;
				menu_sound.textures[1]=41;
			}
			Global.audioPlay(0);
		}
		else if(menu_about.isPressed())
		{
			if(about_back.visible)
			{
				about_backanim.visible=false;
				about_but_backanim.visible=false;
				about_back.visible=false;
				about_logo.visible=false;
				about_sep_a.visible=false;
				about_finish.visible=false;
				about_but_finish.visible=false;
				about_lang.visible=false;
				about_but_lang.visible=false;
				about_sep_b.visible=false;
				about_version.visible=false;
				
				about_highscore.visible=false;
				about_highscore_value.visible=false;
				about_highscore_medal.visible=false;
				
				about_highscore_rational.visible=false;
				about_highscore_rational_value.visible=false;
				about_highscore_rational_medal.visible=false;
				
				menu_game_arcade.enabled=true;
				menu_game_free.enabled=true;
			}
			else
			{
				//Classic mode
				if(Global.def_highscore>=10 && Global.def_highscore <20)
				{
					about_highscore_medal.setTexture(51);
				}
				else if(Global.def_highscore>=20 && Global.def_highscore <30)
				{
					about_highscore_medal.setTexture(52);
				}
				else if(Global.def_highscore>=30 && Global.def_highscore <40)
				{
					about_highscore_medal.setTexture(53);
				}
				else if(Global.def_highscore>=40 && Global.def_highscore <50)
				{
					about_highscore_medal.setTexture(54);
				}
				else if(Global.def_highscore>=50 && Global.def_highscore <70)
				{
					about_highscore_medal.setTexture(55);
				}
				else if(Global.def_highscore>=70 && Global.def_highscore <90)
				{
					about_highscore_medal.setTexture(56);
				}
				else if(Global.def_highscore>=90)
				{
					about_highscore_medal.setTexture(57);
				}
				else
				{
					about_highscore_medal.setTexture(0);
				}
				
				//Rational Mode Medal
				if(Global.def_highscore_rational>=10 && Global.def_highscore_rational <20)
				{
					about_highscore_rational_medal.setTexture(51);
				}
				else if(Global.def_highscore_rational>=20 && Global.def_highscore_rational <30)
				{
					about_highscore_rational_medal.setTexture(52);
				}
				else if(Global.def_highscore_rational>=30 && Global.def_highscore_rational <40)
				{
					about_highscore_rational_medal.setTexture(53);
				}
				else if(Global.def_highscore_rational>=40 && Global.def_highscore_rational <50)
				{
					about_highscore_rational_medal.setTexture(54);
				}
				else if(Global.def_highscore_rational>=50 && Global.def_highscore_rational <70)
				{
					about_highscore_rational_medal.setTexture(55);
				}
				else if(Global.def_highscore_rational>=70 && Global.def_highscore_rational <90)
				{
					about_highscore_rational_medal.setTexture(56);
				}
				else if(Global.def_highscore_rational>=90)
				{
					about_highscore_rational_medal.setTexture(57);
				}
				else
				{
					about_highscore_rational_medal.setTexture(0);
				}
				
				about_backanim.visible=true;
				about_but_backanim.visible=true;
				about_back.visible=true;
				about_logo.visible=true;
				about_sep_a.visible=true;
				about_finish.visible=true;
				about_but_finish.visible=true;
				about_lang.visible=true;
				about_but_lang.visible=true;
				about_sep_b.visible=true;
				about_version.visible=true;
				
				about_highscore.visible=true;
				about_highscore_value.visible=true;
				about_highscore_medal.visible=true;
				
				about_highscore_rational.visible=true;
				about_highscore_rational_value.visible=true;
				about_highscore_rational_medal.visible=true;
				
				menu_game_arcade.enabled=false;
				menu_game_free.enabled=false;
				
				menu_classic.visible=false;
				menu_classic.enabled=false;
				menu_rational.visible=false;
				menu_rational.enabled=false;
				menu_game_arcade.visible=true;
				menu_game_free.visible=true;
			}
			Global.audioPlay(0);
		}
		else if(about_but_lang.isPressed() && about_back.visible)
		{
			if(Global.def_language==0)
			{
				Global.def_language=1;
			}
			else if(Global.def_language==1)
			{
				Global.def_language=0;
			}
			
			updateLanguage();
			Global.saveData();
			Global.audioPlay(0);
		}
		else if(about_but_backanim.isPressed() && about_back.visible)
		{
			Global.def_backanim=!Global.def_backanim;
			
			if(Global.def_backanim)
			{
				about_but_backanim.textures[0]=64;
				about_but_backanim.textures[1]=64;
			}
			else
			{
				about_but_backanim.textures[0]=63;
				about_but_backanim.textures[1]=63;
			}
			
			Global.saveData();
			Global.audioPlay(0);
		}
		else if(about_but_finish.isPressed() && about_back.visible)
		{
			Global.def_autofinish=!Global.def_autofinish;
			
			if(Global.def_autofinish)
			{
				about_but_finish.textures[0]=64;
				about_but_finish.textures[1]=64;
			}
			else
			{
				about_but_finish.textures[0]=63;
				about_but_finish.textures[1]=63;
			}
			
			Global.saveData();
			Global.audioPlay(0);
		}
		
		if(game_logo.sprite.getRotation()< -5 || game_logo.sprite.getRotation()> 5)
		{
			game_logo.angular_speed*=-1;
		}
		
		about_highscore_value.text=Global.def_highscore+"";
		about_highscore_rational_value.text=Global.def_highscore_rational+"";
	}
	
	//Return texture original aspect ratio
	public float textAspectRatio(Texture texture)
	{
		return texture.getWidth()/texture.getHeight();
	}
	
	//Return texture original aspect ratio
	public float textAspectRatio(int texture_index)
	{
		return (float)Global.texture[texture_index].getWidth()/(float)Global.texture[texture_index].getHeight();
	}
	
	public void updateLanguage()
	{
		if(Global.def_language==0)
		{
			about_but_lang.textures[0]=58;
			about_but_lang.textures[1]=59;
			about_lang.text="Language";
			about_finish.text="Auto-Finish";
			about_highscore.text="Best Classic";
			about_highscore_rational.text="Best Rational";
			about_backanim.text="Animations";
			about_version.text="Calc Rush\nV"+Global.version+" "+Global.year;
			game_over_img_score = new Image(45,Global.camera_size_x/2f,game_over_background.pos.y+game_over_background.size.y-180,250,0,true,true,true,false);
			game_over_img_highscore = new Image(24,Global.camera_size_x/2f,game_over_background.pos.y+game_over_background.size.y-450,200,0,true,true,true,true);
			but_exit.text="Exit";
			but_retry.text="Retry";
			but_skip.text="Skip";
			menu_game_arcade.textures[0]=28;
			menu_game_arcade.textures[1]=30;
			menu_game_free.textures[0]=34;
			menu_game_free.textures[1]=36;
			menu_classic.textures[0]=69;
			menu_classic.textures[1]=71;
			menu_rational.textures[0]=73;
			menu_rational.textures[1]=75;
		}
		else
		{
			about_but_lang.textures[0]=60;
			about_but_lang.textures[1]=61;
			about_lang.text="Idioma";
			about_finish.text="Term. Auto";
			about_backanim.text="Animado";
			about_highscore.text="Recorde Classic";
			about_highscore_rational.text="Recorde Frac.";
			about_version.text="Calc Rush\nV"+Global.version+" "+Global.year;
			game_over_img_score = new Image(44,Global.camera_size_x/2f,game_over_background.pos.y+game_over_background.size.y-180,250,0,true,true,true,false);
			game_over_img_highscore = new Image(23,Global.camera_size_x/2f,game_over_background.pos.y+game_over_background.size.y-450,200,0,true,true,true,true);
			but_exit.text="Sair";
			but_retry.text="Reiniciar";
			but_skip.text="Proximo";
			menu_game_arcade.textures[0]=27;
			menu_game_arcade.textures[1]=29;
			menu_game_free.textures[0]=33;
			menu_game_free.textures[1]=35;
			menu_classic.textures[0]=68;
			menu_classic.textures[1]=70;
			menu_rational.textures[0]=72;
			menu_rational.textures[1]=74;
		}
	}
	
	//Show message image timer
	public void showTimerSms(int texture)
	{
		Image temp=new Image(texture,Global.camera_size_x-200,Global.camera_size_y-200,80,80,false,false,true,false);
		temp.sprite.setColor(1f, 1f, 1f, 1f);
		image_timer.add(temp);
	}
	
    //Funcoes do Jogo
    public void play(int i)
    {
        if(e_num[i])
        {
            if(!e_op_1)
            {
                op_1=num[i];
                e_num[i]=false;
                e_op_1=true;
            }
            else if(!e_op_2)
            {
                op_2=num[i];
                e_num[i]=false;
                e_op_2=true;
            }
        }
    }

    public void return_num_1()
    {
        if(e_op_1)
        {
            i=0;
            while (i<num.length)
            {
                if(e_num[i]==false)
                {
                    num[i]=op_1;
                    e_num[i]=true;
                    e_op_1=false;
                    i+=10;
                }
                i++;
            }
        }
    }

    public void return_num_2()
    {
        if(e_op_2)
        {
            i=0;
            while (i<num.length)
            {
                if(e_num[i]==false)
                {
                    num[i]=op_2;
                    e_num[i]=true;
                    e_op_2=false;
                    i+=10;
                }
                i++;
            }
        }
    }

    public void updateGameState()
    {
        if(e_op_1 && e_op_2 && calc_sel!=0)
        {
            if(calc_sel==1)//adicao
            {
                temp=op_1.add(op_2);
            }
            else if(calc_sel==2)//Subtraccao
            {
                temp=op_1.sub(op_2);
            }
            else if(calc_sel==3)//Multiplicacao
            {
                temp=op_1.mul(op_2);
            }
            else if(calc_sel==4)//Divisao
            {
                if(op_2.equals(0))//Divisao por 0 - ERRO
                {
                    return_num_2();
                    return_num_1();
                    calc_sel=0;
                    if(Global.def_language==0)
            		{
                    	game_error.text="Can´t Divide\n by zero!";
            		}
                    else
                    {
                    	game_error.text="Impossivel\ndividir por 0!";
                    }
                    game_error.visible=true;
                    return;
                }
                if(op_1.isIntegerDivision(op_2)|| game_state==GameState.GameRationalArcade || game_state==GameState.GameRationalFree) //Divisao Inteira ou modo rational activo
                {
                    temp=op_1.div(op_2);
                }
                else //Divisao Decimal - ERRO
                {
                	//In fractional mode this erros dosent exist
                    return_num_2();
                    return_num_1();
                    calc_sel=0;
                    if(Global.def_language==0)
            		{
                    	game_error.text="Decimal\n division!";
            		}
                    else
                    {
                    	game_error.text="Divisao\ndecimal!";
                    }
                    
                    game_error.visible=true;
                    return;
                }
            }

            i=0;
            while (i<num.length)
            {
                if(e_num[i]==false)
                {
                    num[i]=temp;
                    e_num[i]=true;
                    i+=10;
                }
                i++;
            }

            num_count--;
            calc_num_1.text="";
            calc_num_2.text="";
            clearCalcZone();

            if(Global.def_autofinish && canAutoFinish()) //Caso de Autofinish=true
            {
                points++;
                time+=3;
                showTimerSms(66);
                particle.burst();
                nextBoard();
                Global.audioPlay(1);
            }
            else if(num_count==1)
            {
                if(num[0].equals(24)) //Puzzle resolvido passar ao proximo
                {
                    points++;
                    time+=3;
                    showTimerSms(66);
                    particle.burst();
                    nextBoard();
                    Global.audioPlay(1);
                }
                else //Reiniciar puzzle actual
                {
                    restartBoard();
                    Global.audioPlay(2);
                }
            }
        }
    }

    public boolean canAutoFinish() //Verifica se so existem 24 , 1 e 0e existir pelo menos uma vez 24
    {
        int k=0;
        boolean found=false;
        
        while(k<e_num.length)
        {
            if(e_num[k]==true)
            {
                if(num[k].realValue()!=0 && num[k].realValue()!=1 && num[k].realValue()!=24)
                {
                    return false;
                }
                if(num[k].realValue()==24)
                {
                	if(found)
                	{
                		return false;
                	}
                	found=true;
                }
            }
            k++;
        }

        return found;
    }

    public void clearCalcZone()
    {
        e_op_1=false;
        e_op_2=false;
        calc_sel=0;
    }

    public void new_game()
    {
    	if(game_state==GameState.GameNormalArcade || game_state==GameState.GameNormalFree)
    	{	
    		num=CombinationGenerator.generateIntegerNumbers();
    	}
    	else
    	{
    		num=CombinationGenerator.generateFractionalNumbers();
    	}
        
        b_num= new RationalNumber[num.length];
        i=0;
        while(i<num.length)
        {
            b_num[i]=num[i].clone();
            i++;
        }

        e_num=new boolean[4];
        e_num[0]=true;
        e_num[1]=true;
        e_num[2]=true;
        e_num[3]=true;

        e_op_1=false;
        e_op_2=false;
        calc_sel=0;
        num_count=4;

        points=0;
        time=90;
        updateInterfaceElements();
    }

    public void new_game_free()
    {
    	if(game_state==GameState.GameNormalArcade || game_state==GameState.GameNormalFree)
    	{	
    		num=CombinationGenerator.generateIntegerNumbers();
    	}
    	else
    	{
    		num=CombinationGenerator.generateFractionalNumbers();
    	}
        
        b_num= new RationalNumber[num.length];
        i=0;
        while(i<num.length)
        {
            b_num[i]=num[i].clone();
            i++;
        }

        e_num=new boolean[4];
        e_num[0]=true;
        e_num[1]=true;
        e_num[2]=true;
        e_num[3]=true;

        e_op_1=false;
        e_op_2=false;
        calc_sel=0;
        num_count=4;

        points=0;

        updateInterfaceElements();
    }

    public void restartBoard()
    {
        num=new RationalNumber[b_num.length];
        i=0;
        while(i<num.length)
        {
            num[i]=b_num[i].clone();
            i++;
        }

        e_num=new boolean[4];
        e_num[0]=true;
        e_num[1]=true;
        e_num[2]=true;
        e_num[3]=true;

        e_op_1=false;
        e_op_2=false;
        calc_sel=0;
        num_count=4;
    }

    public void nextBoard()
    {
    	if(game_state==GameState.GameNormalArcade || game_state==GameState.GameNormalFree)
    	{	
    		num=CombinationGenerator.generateIntegerNumbers();
    	}
    	else
    	{
    		num=CombinationGenerator.generateFractionalNumbers();
    	}
    	
        b_num= new RationalNumber[num.length];
        i=0;
        while(i<num.length)
        {
            b_num[i]=num[i].clone();
            i++;
        }
        
        e_num=new boolean[4];
        e_num[0]=true;
        e_num[1]=true;
        e_num[2]=true;
        e_num[3]=true;

        e_op_1=false;
        e_op_2=false;
        calc_sel=0;
        num_count=4;
    }

    public void updateInterfaceElements()
    {
        if(e_num[0])
        {
            num_1.setNumber(num[0]);
        }
        else
        {
            num_1.empty_mode=true;
        }

        if(e_num[1])
        {
            num_2.setNumber(num[1]);
        }
        else
        {
            num_2.empty_mode=true;
        }

        if(e_num[2])
        {
            num_3.setNumber(num[2]);
        }
        else
        {
            num_3.empty_mode=true;
        }

        if(e_num[3])
        {
            num_4.setNumber(num[3]);
        }
        else
        {
            num_4.empty_mode=true;
        }


        if(calc_sel==0)
        {
            calc_op.text="";
        }
        else if(calc_sel==1)
        {
            calc_op.text="+";
        }
        else if(calc_sel==2)
        {
            calc_op.text="-";
        }
        else if(calc_sel==3)
        {
            calc_op.text="*";
        }
        else if(calc_sel==4)
        {
            calc_op.text="/";
        }

        calc_num_1.text="";
        calc_num_2.text="";
        
        
        if(e_op_1)
        {
            calc_num_1.text=op_1+"";
        }

        if(e_op_2)
        {
            calc_num_2.text=op_2+"";
        }

        text_points.text=""+points;
    }
    
	@Override
	public void resize(int width, int height)
	{
		if(Global.debug_mode)
		{
			Global.screen_size_y=Gdx.graphics.getHeight();
			Global.screen_size_x=Gdx.graphics.getWidth();
			Global.aspect_ratio=Global.screen_size_x/Global.screen_size_y;
			
			Global.camera_size_x=600;
			Global.camera_size_y=Global.camera_size_x/Global.aspect_ratio;
			Global.camera_ratio=Global.camera_size_y/Global.screen_size_y;
			
			Global.batch = new SpriteBatch();
			Global.camera = new OrthographicCamera();
			Global.camera.setToOrtho(false,Global.camera_size_x,Global.camera_size_y);
			Global.camera.update();
			iniInterface();
		}
	}
	
	@Override
	public void pause() {}
	@Override
	public void resume() {}
	
}

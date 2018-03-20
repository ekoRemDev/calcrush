package com.tentone.math24;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main
{
	public static void main(String[] args)
	{
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		
		cfg.title = "Calc Rush V1.2.0";
		cfg.width = 360;
		cfg.height = 640;
		cfg.resizable=true;

		new LwjglApplication(new MainGame(), cfg);
	}
}

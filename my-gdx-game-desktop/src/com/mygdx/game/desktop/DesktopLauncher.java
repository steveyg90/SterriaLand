package com.mygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.Sterria;

public class DesktopLauncher {
	public static void main (String[] arg) {
		
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();

		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Sterria V1.0";
		config.width = 1024;
		config.height = 768;
		config.vSyncEnabled = true;
		config.fullscreen = !true;
		new LwjglApplication(new Sterria(), config);
		

	}
}

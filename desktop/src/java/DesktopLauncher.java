package java;

import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class DesktopLauncher {
	private final String App_Id = "JaMGdx";
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 700;
		config.height =700;
		config.y=40;
		config.fullscreen=false;
	}
}



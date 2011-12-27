package cody.gravityshock;

import com.badlogic.gdx.backends.jogl.JoglApplication;

public class MainDesktop {
	public static void main (String[] argv) {
		new JoglApplication(new Main(), "GravityShock", 640, 400, true);
	}
}

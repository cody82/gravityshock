package cody.gravityshock;

import com.badlogic.gdx.backends.jogl.JoglApplication;

public class MainDesktop {
	public static void main (String[] argv) {
		MainGame main = new MainGame();

		for(int i = 0;i<argv.length;++i) {
			if(argv[i].equals("--numplayers")) {
				main.numplayers = Integer.parseInt(argv[i+1]);
			}
		}
		
		new JoglApplication(main, "GravityShock", 800, 450, true);
	}
}

package cody.gravityshock;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;;

public class MainDesktop {
	public static void main (String[] argv) {
		MainGame main = new MainGame();
		int resx = 800;
		int resy = 450;
		
		for(int i = 0;i<argv.length;++i) {
			if(argv[i].equals("--resolution")) {
				String[] s = argv[i+1].split("x");
				resx = Integer.parseInt(s[0]);
				resy = Integer.parseInt(s[1]);
			}
			else if(argv[i].equals("--level")) {
				main.level = Integer.parseInt(argv[i+1]);
			}
		}
		
		new LwjglApplication(main, "Gravity Shock", resx, resy, true);
	}
}

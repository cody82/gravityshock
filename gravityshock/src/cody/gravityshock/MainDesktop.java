package cody.gravityshock;

import com.badlogic.gdx.backends.jogl.JoglApplication;

public class MainDesktop {
	public static void main (String[] argv) {
		MainGame main = new MainGame(new FileData());
		int resx = 800;
		int resy = 450;
		
		for(int i = 0;i<argv.length;++i) {
			if(argv[i].equals("--resolution")) {
				String[] s = argv[i+1].split("x");
				resx = Integer.parseInt(s[0]);
				resy = Integer.parseInt(s[1]);
			}
		}
		
		new JoglApplication(main, "Gravity Shock", resx, resy, true);
	}
}

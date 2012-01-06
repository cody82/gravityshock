package cody.gravityshock;

import com.badlogic.gdx.Game;

public class MainGame extends Game{
	public int numplayers = 1;
	@Override
	public void create() {
		this.setScreen(new MainMenu(this));
	}

	public void start() {
		Main main = new Main();
		main.numplayers = numplayers;
		this.setScreen(main);
		
	}
}

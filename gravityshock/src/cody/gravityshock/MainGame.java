package cody.gravityshock;

import com.badlogic.gdx.Game;

public class MainGame extends Game{
	public int numplayers = 1;
	public PersistantData data;
	
	public MainGame(PersistantData _data) {
		data = _data;
	}
	@Override
	public void create() {
		this.setScreen(new MainMenu(this));
	}

	public void start() {
		Main main = new Main(this);
		main.numplayers = numplayers;
		this.setScreen(main);
		
	}

	public void startMainMenu() {
		this.setScreen(new MainMenu(this));
		
	}
}

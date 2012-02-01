package cody.gravityshock;

import com.badlogic.gdx.Game;

public class MainGame extends Game{
	public int level = 1;
	public int numplayers = 1;
	public PersistantData data;
	
	public MainGame(PersistantData _data) {
		data = _data;
	}
	@Override
	public void create() {
		this.setScreen(new MainMenu(this));
	}

	@Override
	public void render() {
		
		super.render();
		if(gotoMainMenu) {
			gotoMainMenu = false;
			startMainMenu();
		}
	}
	public void start() {
		Main main = new Main(this);
		main.level = level - 1;
		main.numplayers = numplayers;
		this.setScreen(main);
		
	}

	boolean gotoMainMenu = false;
	public void beginMainMenu() {
		gotoMainMenu = true;
	}
	public void startMainMenu() {
		this.setScreen(new MainMenu(this));
		
	}
}

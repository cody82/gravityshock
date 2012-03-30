package cody.gravityshock;

import bloom.Bloom;

import com.badlogic.gdx.Game;

public class MainGame extends Game{
	public int level = 1;
	public int numplayers = 1;
	public boolean enable_bloom = true;
	Bloom bloom;
	
	public MainGame() {
	}
	@Override
	public void create() {
		if(enable_bloom)
			bloom = new Bloom();
		this.setScreen(new MainMenu(this));
	}

	@Override
	public void render() {
		boolean b = bloom != null;
		if(b)
			bloom.capture();
		super.render();
		if(gotoMainMenu) {
			gotoMainMenu = false;
			startMainMenu();
		}
		if(b)
			bloom.render();
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
	
	@Override
	public void dispose() {
		//System.exit(0);
	}
}

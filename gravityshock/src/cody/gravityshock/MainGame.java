package cody.gravityshock;

import bloom.Bloom;

import com.badlogic.gdx.Game;

public class MainGame extends Game{
	public int level = 1;
	public int numplayers = 1;
	
	public static MainGame Instance;
	
	Bloom bloom;
	
	void setBloom(boolean enable) {
		if(enable == getBloom())
			return;
		
		if(enable)
			createbloom();
		else{
			bloom.dispose();
			bloom = null;
		}
	}
	
	public boolean getBloom() {
		return bloom != null;
	}
	
	public MainGame() {
		Instance = this;
	}
	
	void createbloom() {
		bloom = new Bloom();
		//bloom.setOriginalIntesity(1.2f);
		bloom.setTreshold(0.3f);
		bloom.setBloomIntesity(1.5f);
	}
	@Override
	public void create() {
		setBloom(Util.getBloomOption());
		
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
	
	public void resize(int width, int height) {
		if(bloom != null) {
			bloom.dispose();
			createbloom();
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
	
	@Override
	public void dispose() {
		//System.exit(0);
	}
}

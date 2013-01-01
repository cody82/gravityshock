package cody.gravityshock;



import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox.CheckBoxStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.List.ListStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Slider.SliderStyle;
import com.badlogic.gdx.scenes.scene2d.ui.SplitPane;
import com.badlogic.gdx.scenes.scene2d.ui.SplitPane.SplitPaneStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldListener;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle;



public class MainMenu implements Screen {

    Skin skin;
    Stage ui;
    SpriteBatch batch;
    Actor root;

    MainGame game;
    public MainMenu(MainGame _game) {
    	game = _game;
    }
	@Override
	public void dispose() {
	}

	@Override
	public void hide() {
		Gdx.input.setInputProcessor(null);
	}

	@Override
	public void pause() {
		
	}

	@Override
	public void render(float arg0) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

        Util.drawMenuBackground(arg0);
		
		

        float window_width = Gdx.graphics.getWidth();
        float window_height = Gdx.graphics.getHeight();
        ui.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        ui.draw();
        //Table.drawDebug(ui);
        batch.begin();
        		batch.draw(title, 0, window_height - 128, window_width, 128);
        batch.end();

	}

	@Override
	public void resize(int width, int height) {
		if(ui!=null)
        ui.setViewport(width, height, false);
		ui.dispose();
		show();
    }

	@Override
	public void resume() {
		
	}
	Window window;
	Texture title;
	
	
	@Override
	public void show() {

		//if(batch == null)
			batch = new SpriteBatch();
		if(skin == null)
			skin = Assets.getSkin();
		if(title == null)
			title = Assets.getTexture("data/title.png");
        
        ui = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
        
        Gdx.input.setInputProcessor(ui);

        Assets.playMusic();
        
        window = new Window("Gravity Shock", skin){
        	protected void drawBackground(SpriteBatch batch,
                    float parentAlpha) {
        		
        	}
        };

        window.setBounds(0, 0, ui.getWidth(), ui.getHeight() - 64);
        window.setMovable(false);
        window.setColor(0, 0, 0, 0.8f);
        
        // Group.debug = true;

        final TextButton button = new TextButton("Start Game", skin);
        button.addListener(
            	new InputListener() {
            		@Override
            	public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                		game.numplayers = 1;
                		game.start();
    				return true;
            	}});
        
        final TextButton button2 = new TextButton("Start Two-Player Splitscreen Game", skin);

        button2.addListener(
            	new InputListener() {
            		@Override
            	public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                		game.numplayers = 2;
                		game.start();
    				return true;
            	}});
        
        final TextButton button3 = new TextButton("Quit", skin);

        button3.addListener(
            	new InputListener() {
            		@Override
            	public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                		System.exit(0);
                		//Gdx.app.exit();
    				return true;
            	}});
        
        final TextButton highscorebutton = new TextButton("Highscore", skin);

        highscorebutton.addListener(
            	new InputListener() {
            		@Override
            	public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                		game.setScreen(new HighscoreScreen(game));
    				return true;
            	}});
        
        
        final TextButton levelbutton = new TextButton("Levels", skin);

        levelbutton.addListener(
            	new InputListener() {
            		@Override
            	public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                		game.setScreen(new LevelScreen(game));
    				return true;
            	}});
        
        final TextButton optionsbutton = new TextButton("Options", skin);

        optionsbutton.addListener(
            	new InputListener() {
            		@Override
            	public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                		game.setScreen(new OptionsMenu(game));
    				return true;
            	}});
        
        int width = (int)(window.getWidth() * 0.6f);
        int height = (int)(window.getHeight() * 0.10f);
        //window.debug();
        window.defaults().spaceBottom(10);
        window.row().fill().expandX();
        window.add(button).width(width).height(height);
    	if(Gdx.app.getType() != ApplicationType.Android) {
    		window.row();
    		window.add(button2).width(width).height(height);
    	}
        window.row();
        window.add(highscorebutton).width(width).height(height);
        window.row();
        window.add(levelbutton).width(width).height(height);

        window.row();
        window.add(optionsbutton).width(width).height(height);
        
    		window.row();
    		window.add(button3).width(width).height(height);

        ui.addActor(window);


	

	}

}

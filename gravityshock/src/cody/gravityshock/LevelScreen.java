package cody.gravityshock;

import java.util.ArrayList;
import java.util.Arrays;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.List.ListStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle;


public class LevelScreen implements Screen {

    Skin skin;
    Stage ui;
    Actor root;

    MainGame game;

    public LevelScreen(MainGame _game) {
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
        
        ui.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        ui.draw();
		
	}

	@Override
	public void resize(int width, int height) {
        ui.setViewport(width, height, false);
		
	}

	@Override
	public void resume() {
		
	}

	int LoadMaxLevel() {
		Preferences prefs = Gdx.app.getPreferences("levels");
		return prefs.getInteger("maxfreelevel", 1);
	}
	
	Window window;
	@Override
	public void show() {
        skin = Assets.getSkin();
        ui = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
        Gdx.input.setInputProcessor(ui);

        window = new Window("window", skin){
        	protected void drawBackground(SpriteBatch batch,
                    float parentAlpha) {
        		
        	}
        };
        window.setBounds(0,0,ui.getWidth(),ui.getHeight());
        window.setMovable(false);
        window.setColor(0, 0, 0, 0.8f);


        //window.debug();
        window.defaults().spaceBottom(10);
        window.row().fill().expandX();
        
       int maxfreelevel = LoadMaxLevel();
       ArrayList<String> listentries = new ArrayList<String>();
        for(int i=1;i<=maxfreelevel;++i) {
        	listentries.add("Level " + Integer.toString(i));
        	
        }
        final List list = new List(listentries.toArray(), skin);

        window.add(list);
        
        final TextButton button = new TextButton("Done", skin);

        button.addListener(
        	new InputListener() {
        		@Override
        	public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
            		game.level = Integer.parseInt(list.getSelection().split(" ")[1]);
            		game.startMainMenu();
				return true;
        	}});
        
        window.add(button).fill(0f, 0f);

        ui.addActor(window);
	}

}
package cody.gravityshock;

import java.util.Arrays;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle;

public class HighscoreScreen implements Screen {

    Skin skin;
    Stage ui;
    Actor root;

    MainGame game;

    public HighscoreScreen(MainGame _game) {
    	game = _game;
    }
    
	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
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
		// TODO Auto-generated method stub
		
	}
	
	Window window;
	@Override
	public void show() {
		Assets.playMusic();
        skin = Assets.getSkin();
        ui = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
        Gdx.input.setInputProcessor(ui);

        window = new Window("window", skin){
        	protected void drawBackground(SpriteBatch batch,
                    float parentAlpha) {
        		
        	}
        };
        window.setBounds(0, 0, ui.getWidth(), ui.getHeight());
        window.setMovable(false);
        window.setColor(0,0,0,0.8f);


        //window.debug();
        window.defaults().spaceBottom(10);
        window.row().fill().expandX();
        for(String s : Util.LoadHighscore()) {
        	String[] split = s.split("\t");
            window.add(new Label(split[0], skin)).fill(0f, 0f);
            window.add(new Label(split[1], skin)).fill(0f, 0f);
            window.row();
        }

        final TextButton button = new TextButton("Done", skin);

        button.addListener(
        	new InputListener() {
        		@Override
        	public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
            		game.startMainMenu();
				return true;
        	}});
        
        window.add(button).fill(0f, 0f);

        ui.addActor(window);
	}

}

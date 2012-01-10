package cody.gravityshock;

import java.util.Arrays;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
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

        //((Label)ui.findActor("label")).setText("fps: " + Gdx.graphics.getFramesPerSecond());

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

	String[] LoadHighscore() {
		FileHandle fh = Gdx.files.external(".gravityshock/highscore.txt");
		if(!fh.exists())
			return new String[0];
		
		String data = fh.readString();
		String[] list = data.split("\n");
		Arrays.sort(list, new HighscoreComparator(true));
		return list;
	}
	
	Window window;
	@Override
	public void show() {
        skin = new Skin(Gdx.files.internal("data/uiskin.json"), Gdx.files.internal("data/uiskin.png"));
        ui = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
        Gdx.input.setInputProcessor(ui);

        window = new Window("window", "GravityShock", ui, skin.getStyle(WindowStyle.class), Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        window.x = window.y = 0;


        //window.debug();
        window.defaults().spaceBottom(10);
        window.row().fill().expandX();
        for(String s : LoadHighscore()) {
        	String[] split = s.split("\t");
            window.add(new Label(split[0], skin.getStyle(LabelStyle.class), "label")).fill(0f, 0f);
            window.add(new Label(split[1], skin.getStyle(LabelStyle.class), "label2")).fill(0f, 0f);
            window.row();
        }

        final Button button = new Button("Done", skin.getStyle(ButtonStyle.class), "button-sl") {
        	@Override
        	public boolean touchDown(float x, float y, int pointer) {
        		game.startMainMenu();
				return isChecked;
        	}
        };
        window.add(button).fill(0f, 0f);

        ui.addActor(window);
	}

}

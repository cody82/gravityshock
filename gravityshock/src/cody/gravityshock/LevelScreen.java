package cody.gravityshock;

import java.util.ArrayList;
import java.util.Arrays;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.scenes.scene2d.Actor;
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
		String data = game.data.LoadString("maxfreelevel.txt");
		if(data == null)
			return 1;
		
		return Integer.parseInt(data);
	}
	
	Window window;
	@Override
	public void show() {
        skin = Assets.getSkin();
        ui = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
        Gdx.input.setInputProcessor(ui);

        window = new Window("window", skin.getStyle(WindowStyle.class));
        window.x = window.y = 0;
        window.width = ui.width();
        window.height= ui.height();
        window.setMovable(false);


        //window.debug();
        window.defaults().spaceBottom(10);
        window.row().fill().expandX();
        
       int maxfreelevel = LoadMaxLevel();
       ArrayList<String> listentries = new ArrayList<String>();
        for(int i=1;i<=maxfreelevel;++i) {
        	listentries.add("Level " + Integer.toString(i));
        	
        }
        final List list = new List(listentries.toArray(), skin.getStyle(ListStyle.class), "list");

        window.add(list);
        
        final TextButton button = new TextButton("Done", skin.getStyle(TextButtonStyle.class), "button-sl") {
        	@Override
        	public boolean touchDown(float x, float y, int pointer) {
        		game.level = Integer.parseInt(list.getSelection().split(" ")[1]);
        		game.startMainMenu();
				return isChecked();
        	}
        };
        window.add(button).fill(0f, 0f);

        ui.addActor(window);
	}

}
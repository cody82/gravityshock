package cody.gravityshock;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
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
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle;

class HighscoreComparator implements Comparator<String> {
	boolean reverse;
	public HighscoreComparator(boolean _reverse) {
		reverse = _reverse;
	}
	public HighscoreComparator() {
		reverse = false;
	}
    // Comparator interface requires defining compare method.
    public int compare(String filea, String fileb) {
String[] split1 = filea.split("\t");
String[] split2 = fileb.split("\t");
int score1 = Integer.parseInt(split1[1]);
int score2 = Integer.parseInt(split2[1]);
if(score1 > score2)
	return reverse ? -1 : 1;
else if(score2 > score1)
	return reverse ? 1 : -1;
else
	return 0;
    }
}
public class GameOverScreen implements Screen {

    Skin skin;
    Stage ui;
    Actor root;

    MainGame game;
    int score;
    boolean win;
    
    public GameOverScreen(MainGame _game, int _score, boolean _win) {
    	game = _game;
    	score = _score;
    	win = _win;
    }
    
	@Override
	public void dispose() {
		
	}

	@Override
	public void hide() {
		
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

	
	TextField namefield;
	Window window;
	@Override
	public void show() {
        Assets.playMusic();
        skin = Assets.getSkin();
        ui = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
        Gdx.input.setInputProcessor(ui);

        window = new Window("window", skin.getStyle(WindowStyle.class)){
        	protected void drawBackground(SpriteBatch batch,
                    float parentAlpha) {
        		
        	}
        };
        window.x = window.y = 0;
        window.width = ui.width();
        window.height= ui.height();
        window.setMovable(false);
        window.color.r = 0f;
        window.color.g = 0f;
        window.color.b = 0f;
        window.color.a = 0.8f;

      final Label fpsLabel = new Label(win ? "GAME COMPLETE" : "GAME OVER", skin.getStyle(LabelStyle.class), "label");

        //window.debug();
        window.defaults().spaceBottom(10);
        window.row().fill().expandX();
        window.add(fpsLabel).fill(0f, 0f);
        window.row();
        window.add(new Label("Your score:", skin.getStyle(LabelStyle.class), "label2")).fill(0f, 0f);
        window.add(new Label(Integer.toString(score), skin.getStyle(LabelStyle.class), "label3")).fill(0f, 0f);
        window.row();
        window.add(new Label("Enter your name:", skin.getStyle(LabelStyle.class), "label4")).fill(0f, 0f);
        window.add(namefield = new TextField("", skin.getStyle(TextFieldStyle.class))).fill(0f, 0f);
        window.row();

        final TextButton button = new TextButton("Done", skin.getStyle(TextButtonStyle.class)) {
        	@Override
        	public boolean touchDown(float x, float y, int pointer) {
        		String name = namefield.getText();
        		if(!name.isEmpty())
        			Util.SaveHighscore(namefield.getText(), score);
        		game.startMainMenu();
				return isChecked();
        	}

        };
        window.add(button).fill(0f, 0f);

        ui.addActor(window);
	}

}

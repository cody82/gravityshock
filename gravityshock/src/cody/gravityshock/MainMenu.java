package cody.gravityshock;



import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox.CheckBoxStyle;
import com.badlogic.gdx.scenes.scene2d.ui.FlickScrollPane;
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
import com.badlogic.gdx.scenes.scene2d.ui.Slider.ValueChangedListener;
import com.badlogic.gdx.scenes.scene2d.ui.SplitPane;
import com.badlogic.gdx.scenes.scene2d.ui.SplitPane.SplitPaneStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldListener;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle;
import com.badlogic.gdx.scenes.scene2d.ui.tablelayout.Table;



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

        ui.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        ui.draw();
        //Table.drawDebug(ui);

	}

	@Override
	public void resize(int width, int height) {
		if(ui!=null)
        ui.setViewport(width, height, false);
    }

	@Override
	public void resume() {
		
	}
	Window window;
	@Override
	public void show() {

        batch = new SpriteBatch();
        skin = Assets.getSkin();

        ui = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
        Gdx.input.setInputProcessor(ui);

        window = new Window("window", skin.getStyle(WindowStyle.class));
        window.x = window.y = 0;
        window.width = ui.width();
        window.height= ui.height();
        // Group.debug = true;

        final TextButton button = new TextButton("Start Game", skin.getStyle(TextButtonStyle.class), "button-sl") {
        	@Override
        	public boolean touchDown(float x, float y, int pointer) {
        		game.numplayers = 1;
        		game.start();
				return isChecked();
        	}
        };
        
        final TextButton button2 = new TextButton("Start Two-Player Splitscreen Game", skin.getStyle(TextButtonStyle.class), "button-sl") {
        	@Override
        	public boolean touchDown(float x, float y, int pointer) {
        		game.numplayers = 2;
        		game.start();
				return isChecked();
        	}
        };
        final TextButton button3 = new TextButton("Quit", skin.getStyle(TextButtonStyle.class), "button-sl") {
        	@Override
        	public boolean touchDown(float x, float y, int pointer) {
        		Gdx.app.exit();
				return isChecked();
        	}
        };
        final TextButton highscorebutton = new TextButton("Highscore", skin.getStyle(TextButtonStyle.class), "button-sl") {
        	@Override
        	public boolean touchDown(float x, float y, int pointer) {
        		game.setScreen(new HighscoreScreen(game));
				return isChecked();
        	}
        };
        final TextButton levelbutton = new TextButton("Levels", skin.getStyle(TextButtonStyle.class), "button-sl") {
        	@Override
        	public boolean touchDown(float x, float y, int pointer) {
        		game.setScreen(new LevelScreen(game));
				return isChecked();
        	}
        };

        //window.debug();
        window.defaults().spaceBottom(10);
        window.row().fill().expandX();
        window.add(button).fill(0f, 0f);
    	if(Gdx.app.getType() != ApplicationType.Android) {
    		window.row();
    		window.add(button2).fill(0f, 0f);
    	}
        window.row();
        window.add(highscorebutton).fill(0f, 0f);
        window.row();
        window.add(levelbutton).fill(0f, 0f);
        window.row();
        window.add(button3).fill(0f, 0f);
        /*window.add(buttonMulti);
        window.add(imgButton);
        window.add(imgToggleButton);
        window.row();
        window.add(checkBox);
        window.add(slider).minWidth(100).fillX().colspan(3);
        window.row();
        window.add(combobox);
        window.add(textfield).minWidth(100).expandX().fillX().colspan(3);
        window.row();
        window.add(splitPane).fill().expand().colspan(4).minHeight(200);
        window.row();
        window.add(fpsLabel).colspan(4);
*/

        ui.addActor(window);


	

	}

}

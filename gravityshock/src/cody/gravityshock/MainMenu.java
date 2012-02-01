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

	float x2 = 0;
	@Override
	public void render(float arg0) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

        float window_width = Gdx.graphics.getWidth();
        float window_height = Gdx.graphics.getHeight();
        float y2 = 0;
        x2 += arg0;

        batch.begin();
 		batch.enableBlending();
 		batch.setBlendFunction(GL20.GL_BLEND_SRC_ALPHA, GL20.GL_ONE);
        batch.draw(background, 0, 0, window_width, window_height, x2, y2, x2 + (window_width / 256), y2 +  + (window_height / 256));
        
        batch.draw(background, 0, 0, window_width, window_height, x2 * 2, 0.3f + y2 * 2, x2 * 2 + (window_width / 256), 0.3f + y2 * 2 + (window_height / 256));
		batch.end();
		
		
		
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
    }

	@Override
	public void resume() {
		
	}
	Window window;
	Texture background;
	Texture title;
	
	
	@Override
	public void show() {

        batch = new SpriteBatch();
        skin = Assets.getSkin();
        background = Assets.getTexture("data/space2.png");
        background.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
        title = Assets.getTexture("data/title.png");
        
        ui = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
        Gdx.input.setInputProcessor(ui);

        Util.playMusic();
        
        window = new Window("Gravity Shock", skin.getStyle(WindowStyle.class)){
        	protected void drawBackground(SpriteBatch batch,
                    float parentAlpha) {
        		
        	}
        };
        window.x = 0;
        window.y = 0;
        window.width = ui.width();
        window.height= ui.height() - 64;
        window.setMovable(false);
        window.color.r = 0f;
        window.color.g = 0f;
        window.color.b = 0f;
        window.color.a = 0.8f;
        
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

        int width = (int)(window.width * 0.6f);
        int height = (int)(window.height * 0.10f);
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
        window.add(button3).width(width).height(height);
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

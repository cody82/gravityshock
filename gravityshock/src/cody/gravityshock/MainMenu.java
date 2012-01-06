package cody.gravityshock;



import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox.CheckBoxStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ComboBox;
import com.badlogic.gdx.scenes.scene2d.ui.ComboBox.ComboBoxStyle;
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

    String[] listEntries = {"This is a list entry", "And another one", "The meaning of life", "Is hard to come by",
            "This is a list entry", "And another one", "The meaning of life", "Is hard to come by", "This is a list entry",
            "And another one", "The meaning of life", "Is hard to come by", "This is a list entry", "And another one",
            "The meaning of life", "Is hard to come by", "This is a list entry", "And another one", "The meaning of life",
            "Is hard to come by"};

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
		// TODO Auto-generated method stub
	}

	@Override
	public void hide() {
		Gdx.input.setInputProcessor(null);
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
        //Table.drawDebug(ui);

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

        batch = new SpriteBatch();
        skin = new Skin(Gdx.files.internal("data/uiskin.json"), Gdx.files.internal("data/uiskin.png"));
        ui = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
        Gdx.input.setInputProcessor(ui);

        window = new Window("window", "GravityShock", ui, skin.getStyle(WindowStyle.class), Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        window.x = window.y = 0;

        // Group.debug = true;

        final Button button = new Button("Start Singleplayer Game", skin.getStyle(ButtonStyle.class), "button-sl") {
        	@Override
        	public boolean touchDown(float x, float y, int pointer) {
        		game.numplayers = 1;
        		game.start();
				return isChecked;
        	}
        };
        
        final Button button2 = new Button("Start Two-Player Splitscreen Game", skin.getStyle(ButtonStyle.class), "button-sl") {
        	@Override
        	public boolean touchDown(float x, float y, int pointer) {
        		game.numplayers = 2;
        		game.start();
				return isChecked;
        	}
        };
        final Button button3 = new Button("Quit", skin.getStyle(ButtonStyle.class), "button-sl") {
        	@Override
        	public boolean touchDown(float x, float y, int pointer) {
        		Gdx.app.exit();
				return isChecked;
        	}
        };
        //final Label fpsLabel = new Label("fps:", skin.getStyle(LabelStyle.class), "label");

        //window.debug();
        window.defaults().spaceBottom(10);
        window.row().fill().expandX();
        window.add(button).fill(0f, 0f);
        window.row();
        window.add(button2).fill(0f, 0f);
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

package cody.gravityshock;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


public class Main implements ApplicationListener {
	SpriteBatch spriteBatch;
	Texture texture;
	BitmapFont font;

    World world;

    OrthographicCamera cam;

    Spaceship player;
  
	@Override
	public void create () {
		font = new BitmapFont();
		font.setColor(Color.RED);
		//texture = new Texture(Gdx.files.internal("badlogic.jpg"))
		spriteBatch = new SpriteBatch();
                
    world = new World();
    Map map = new Map();
    map.load(world, "data/level1.svg");
    player = new Spaceship();
    world.add(player);
    player.create();
    
    //var musicfile = Gdx.files.internal("data/2ND_PM.ogg")
    //var music = Gdx.audio.newMusic(musicfile)
    //music.play
    
    cam = new OrthographicCamera(640, 480);
	}

	@Override
	public void render () {
		//var centerX = Gdx.graphics.getWidth() / 2;
		//var centerY = Gdx.graphics.getHeight() / 2;

		Gdx.graphics.getGL10().glClear(GL10.GL_COLOR_BUFFER_BIT);
    cam.update();
    world.render(cam);

    float t = Gdx.graphics.getDeltaTime();
    world.tick(t);
    
    //Vector2 v = player.body.getPosition();
    //cam.position.x = v.x;
    //cam.position.y = v.y;
    
	}

	@Override
	public void resize (int width, int height) {
		spriteBatch.getProjectionMatrix().setToOrtho2D(0, 0, width, height);
	}

	@Override
	public void pause () {

	}

	@Override
	public void resume () {

	}

	@Override
	public void dispose () {

	}

}

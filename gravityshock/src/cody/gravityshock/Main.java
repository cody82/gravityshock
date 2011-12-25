package cody.gravityshock;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;


public class Main implements ApplicationListener {
	SpriteBatch spriteBatch;
	Texture texture;
	BitmapFont font;

    World world;

    OrthographicCamera cam;

    Spaceship player;
  
    int lifes = 5;
    int level = 1;
    
    void createPlayer(){
		player = new Spaceship();
		world.add(player);
		player.create();
    }
    
	@Override
	public void create () {
		font = new BitmapFont();
		font.setColor(Color.WHITE);
		//texture = new Texture(Gdx.files.internal("badlogic.jpg"))
		spriteBatch = new SpriteBatch();
                
		world = new World();
		Map map = new Map();
		map.load(world, "data/level" + level + ".svg");
    
		//var musicfile = Gdx.files.internal("data/2ND_PM.ogg")
		//var music = Gdx.audio.newMusic(musicfile)
		//music.play
    
		cam = new OrthographicCamera(640, 480);
		
		createPlayer();
	}

	@Override
	public void render () {

		float t = Gdx.graphics.getDeltaTime();
		
		Gdx.graphics.getGL10().glClear(GL10.GL_COLOR_BUFFER_BIT);
		Gdx.graphics.getGL10().glEnable(GL10.GL_BLEND);
		cam.update();
		world.render(cam);

		world.tick(t);
    
		if(player.health <= 0) {
			if(lifes <= 1) {
				spriteBatch.begin();
				font.draw(spriteBatch, "GAME OVER", Gdx.graphics.getWidth()/2 - font.getSpaceWidth()*9, Gdx.graphics.getHeight()/2 + font.getLineHeight()/2);
				spriteBatch.end();
			}
			else {
				createPlayer();
				lifes--;
			}
		}
		else {
			Vector2 v = player.body.getPosition();
			cam.position.x = v.x;
			cam.position.y = v.y;
	    
			int fps = (int)(1f/t);
			spriteBatch.begin();
			font.draw(spriteBatch, "fps: " + Integer.toString(fps), 20, 20);
			font.draw(spriteBatch, "score: " + Integer.toString(player.score), 20, 40);
			font.draw(spriteBatch, "speed: " + Integer.toString((int)player.body.getLinearVelocity().len()) + "m/s", 20, 60);
			font.draw(spriteBatch, "health: " + Integer.toString(player.health) + "%", 20, 80);
			font.draw(spriteBatch, "lifes: " + Integer.toString(lifes), 20, 100);
			font.draw(spriteBatch, "level: " + Integer.toString(level), 20, 120);
			spriteBatch.end();
		}
	}

	@Override
	public void resize (int width, int height) {
	    cam.setToOrtho(false, width, height);
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

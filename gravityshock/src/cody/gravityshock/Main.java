package cody.gravityshock;

import java.io.File;
import java.io.IOException;

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

import com.gemserk.util.ScreenshotSaver;

public class Main implements ApplicationListener {
	SpriteBatch spriteBatch;
	Texture texture;
	BitmapFont font;

    World world;

    OrthographicCamera cam;

    Spaceship player;
    Map map;
    
    int lifes = 5;
    int level = 0;
    
    boolean record = false;
    int frame = 1;
    
    void createPlayer(){
    	Spaceship oldplayer = player;
		player = new Spaceship();
		world.add(player);
		player.create();
		if(oldplayer != null)
			player.score = oldplayer.score;
    }
    
    void nextLevel() {
    	level++;
		world = new World();
		map = new Map();
		map.load(world, "data/level" + level + ".svg");
		createPlayer();
		player.score = 0;
    	
    }
	@Override
	public void create () {
		font = new BitmapFont();
		font.setColor(Color.WHITE);
		//texture = new Texture(Gdx.files.internal("badlogic.jpg"))
		spriteBatch = new SpriteBatch();
                
		
		//var musicfile = Gdx.files.internal("data/2ND_PM.ogg")
		//var music = Gdx.audio.newMusic(musicfile)
		//music.play
    
		cam = new OrthographicCamera(640, 480);
		
		nextLevel();
	}

	@Override
	public void render () {

		float t = Gdx.graphics.getDeltaTime();
		if(record) {
			// 30 FPS
			t = 1f/30f;
		}
		
		Gdx.graphics.getGL10().glClear(GL10.GL_COLOR_BUFFER_BIT);
		Gdx.graphics.getGL10().glEnable(GL10.GL_BLEND);
		cam.update();
		world.render(cam);

		
		world.tick(t);
		map.tick(t);
    
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
		else if(player.score >= map.getGoalScore()) {
			nextLevel();
		}
		else {
			Vector2 v = player.body.getPosition();
			cam.position.x = v.x;
			cam.position.y = v.y;
	    
			int fps = (int)(1f/t);
			spriteBatch.begin();
			font.draw(spriteBatch, "fps: " + Integer.toString(fps), 20, 20);
			font.draw(spriteBatch, "score: " + Integer.toString(player.score) + "/" + Integer.toString(map.getGoalScore()), 20, 40);
			font.draw(spriteBatch, "speed: " + Integer.toString((int)player.body.getLinearVelocity().len()) + "m/s", 20, 60);
			font.draw(spriteBatch, "health: " + Integer.toString(player.health) + "%", 20, 80);
			font.draw(spriteBatch, "lifes: " + Integer.toString(lifes), 20, 100);
			font.draw(spriteBatch, "level: " + Integer.toString(level), 20, 120);
			font.draw(spriteBatch, "fuel: " + Integer.toString((int)player.fuel), 20, 140);
			font.draw(spriteBatch, "time: " + Integer.toString((int)map.age), 20, 160);
			spriteBatch.end();
		}

		if(record) {
			
		try {
			File f = new File(String.format("screen%06d.png", frame++));
			ScreenshotSaver.saveScreenshot(f);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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

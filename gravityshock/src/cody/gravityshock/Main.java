package cody.gravityshock;

import java.io.File;
import java.io.IOException;
import java.nio.IntBuffer;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.Pixmap;

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
    
    FrameBuffer framebuffer;
    FrameBuffer framebuffer2;

	GL10 gl10;
	GL20 gl20;
	
	float zoom = 2f;
	
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
    
    void controls() {

	    if(Gdx.input.isKeyPressed(Input.Keys.UP)){
	    	player.control_thrust=1f;
	    }
	    else
	    	player.control_thrust=0f;
	    
	    float omega = 0;
	    if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
	    	omega+=1f;
	    }
	    if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
	    	omega-=1f;
	    }
	    player.control_direction = omega;
	    
	    if(Gdx.input.isKeyPressed(Input.Keys.SPACE)){
	    	player.control_shoot = true;
	    }
	    else {
	    	player.control_shoot = false;
	    }
    }
	@Override
	public void create () {
		font = new BitmapFont();
		font.setColor(Color.WHITE);
		//texture = new Texture(Gdx.files.internal("badlogic.jpg"))
		spriteBatch = new SpriteBatch();

		GL10 gl10 = Gdx.graphics.getGL10();
		GL20 gl20 = Gdx.graphics.getGL20();
		
		if(Gdx.graphics.isGL20Available()) {
			framebuffer = new FrameBuffer(Pixmap.Format.RGB888, 128, 128, false);
			framebuffer2 = new FrameBuffer(Pixmap.Format.RGB888, 512, 512, false);
		}

		//var musicfile = Gdx.files.internal("data/2ND_PM.ogg")
		//var music = Gdx.audio.newMusic(musicfile)
		//music.play
    
		cam = new OrthographicCamera(256, 256);
		
		nextLevel();
	}

	void clear() {
		if(gl10 != null) {
			gl10.glClear(GL10.GL_COLOR_BUFFER_BIT);
			gl10.glEnable(GL10.GL_BLEND);
		}
		else if(gl20 != null){
			gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
			gl20.glEnable(GL20.GL_BLEND);
		}
		
	}
	@Override
	public void render () {

		float t = Gdx.graphics.getDeltaTime();
		if(record) {
			// 30 FPS
			t = 1f/30f;
		}

		gl10 = Gdx.graphics.getGL10();
		gl20 = Gdx.graphics.getGL20();

		cam.update();

		controls();
		
		if(gl20 != null) {
			framebuffer2.begin();
			clear();
			world.render(cam);
			framebuffer2.end();
		}
		else {
			clear();
			world.render(cam);
		}
    

		if(gl20 != null) {
			Texture texture = framebuffer.getColorBufferTexture();
			Texture texture2 = framebuffer2.getColorBufferTexture();

			SpriteBatch spriteBatch2 = new SpriteBatch();
			
			framebuffer.begin();
			spriteBatch2.begin();
			spriteBatch2.setProjectionMatrix(spriteBatch2.getProjectionMatrix().setToOrtho2D(0, 1, 1, -1));
			spriteBatch2.disableBlending();
			spriteBatch2.draw(texture2, 0, 0, 1, 1);
			spriteBatch2.end();

			framebuffer.end();

			spriteBatch2.begin();
			spriteBatch2.setColor(1, 1, 1, 1);
			spriteBatch2.draw(texture2, 0, 0, 1, 1);
			spriteBatch2.setColor(1, 1, 1, 1);
			spriteBatch2.enableBlending();
			spriteBatch2.setBlendFunction(GL20.GL_ONE, GL20.GL_ONE);
			spriteBatch2.draw(texture, 0, 0, 1, 1);
			spriteBatch2.end();
			spriteBatch2.dispose();
		}

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
		
		world.tick(t);
		map.tick(t);
	}

	@Override
	public void resize (int width, int height) {
		//if(gl20 == null) {
			cam.setToOrtho(false, ((float)width / zoom), ((float)height / zoom));
			spriteBatch.getProjectionMatrix().setToOrtho2D(0, 0, width, height);
		//}

			if(Gdx.graphics.isGL20Available()) {
				framebuffer.dispose();
				framebuffer2.dispose();
				framebuffer = new FrameBuffer(Pixmap.Format.RGB888, width / 4, height / 4, false);
				framebuffer2 = new FrameBuffer(Pixmap.Format.RGB888, width, height, false);
			}
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

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

    public int numplayers = 1;
    
    OrthographicCamera[] cams;
    
    Spaceship[] players;
    Map map;
    
    int level = 0;
    
    boolean record = false;
    int frame = 1;
    
    FrameBuffer framebuffer;
    FrameBuffer framebuffer2;

	GL10 gl10;
	GL20 gl20;
	
	float zoom = 1f;
	
    void createPlayer(int index){

    	Spaceship oldplayer = players[index];
    	players[index] = new Spaceship();
		world.add(players[index]);
		players[index].create();
		if(oldplayer != null) {
			players[index].score = oldplayer.score;
			players[index].lifes = oldplayer.lifes;
		}
    }
    
    void nextLevel() {
    	level++;
		world = new World();
		map = new Map();
		map.load(world, "data/level" + level + ".svg");
		for(int i =0;i<numplayers;++i)
		{
			createPlayer(i);
			players[i].score = 0;
		}
    }
    
    void controls() {
    	float ax = Gdx.input.getAccelerometerX();
    	float ay = Gdx.input.getAccelerometerY();
    	float az = Gdx.input.getAccelerometerZ();
    	
    	{
	    if(Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isTouched()){
	    	players[0].control_thrust=1f;
	    }
	    else
	    	players[0].control_thrust=0f;
	    
	    float omega = 0;
	    if(Gdx.input.isKeyPressed(Input.Keys.LEFT) || ax > 0){
	    	omega+=1f;
	    }
	    if(Gdx.input.isKeyPressed(Input.Keys.RIGHT) || ax < 0){
	    	omega-=1f;
	    }
	    players[0].control_direction = omega;
	    
	    if(Gdx.input.isKeyPressed(Input.Keys.SPACE)){
	    	players[0].control_shoot = true;
	    }
	    else {
	    	players[0].control_shoot = false;
	    }
    	}
	    
    	if(numplayers > 1) {
	    if(Gdx.input.isKeyPressed(Input.Keys.W)){
	    	players[1].control_thrust=1f;
	    }
	    else
	    	players[1].control_thrust=0f;
	    
	    float omega = 0;
	    if(Gdx.input.isKeyPressed(Input.Keys.A)){
	    	omega+=1f;
	    }
	    if(Gdx.input.isKeyPressed(Input.Keys.D)){
	    	omega-=1f;
	    }
	    players[1].control_direction = omega;
	    
	    if(Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)){
	    	players[1].control_shoot = true;
	    }
	    else {
	    	players[1].control_shoot = false;
	    }
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
    
		cams = new OrthographicCamera[numplayers];
		players = new Spaceship[numplayers];
		
		for(int i=0;i<numplayers;++i)
			cams[i] = new OrthographicCamera(256, 256);
		
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
	
	int getTotalScore() {
		int score = 0;
		for(Spaceship s : players) {
			score += s.score;
		}
		return score;
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

		cams[0].update();

		controls();
		
		gl10.glViewport(0, 0, window_width / numplayers, window_height);
		
		if(gl20 != null) {
			framebuffer2.begin();
			clear();
			world.render(cams[0]);
			framebuffer2.end();
		}
		else {
			clear();
			world.render(cams[0]);
		}
		
		if(numplayers > 1){
			cams[1].update();
			gl10.glViewport(window_width / 2, 0, window_width / 2, window_height);
			
			if(gl20 != null) {
				framebuffer2.begin();
				world.render(cams[1]);
				framebuffer2.end();
			}
			else {
				world.render(cams[1]);
			}
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

		for(int i=0;i<numplayers;++i) {
		if(players[i].health <= 0) {
			if(players[i].lifes <= 1) {
				spriteBatch.begin();
				gl10.glViewport(i * (window_width / numplayers), 0, (window_width / numplayers), window_height);
				font.draw(spriteBatch, "GAME OVER", (window_width / numplayers) / 2 - font.getSpaceWidth()*9, Gdx.graphics.getHeight()/2 + font.getLineHeight()/2);
				spriteBatch.end();
			}
			else {
				createPlayer(i);
				players[i].lifes--;
			}
		}
		else if(getTotalScore() >= map.getGoalScore()) {
			nextLevel();
			break;
		}
		else {
			Vector2 v = players[i].body.getPosition();
			Vector2 cp = new Vector2(cams[i].position.x, cams[i].position.y);
			Vector2 d = v.sub(cp);
			float l = d.len();
			Vector2 result = cp.add(d.mul(Math.min(t * 2f, 1f)));
			cams[i].position.x = result.x;
			cams[i].position.y = result.y;
			

			gl10.glViewport(i * (window_width / numplayers), 0, (window_width / numplayers), window_height);
			
			int fps = (int)(1f/t);
			spriteBatch.begin();
			font.draw(spriteBatch, "fps: " + Integer.toString(fps), 20, 20);
			font.draw(spriteBatch, "score: " + Integer.toString(players[i].score) + "/" + Integer.toString(map.getGoalScore()), 20, 40);
			font.draw(spriteBatch, "speed: " + Integer.toString((int)players[i].body.getLinearVelocity().len()) + "m/s", 20, 60);
			font.draw(spriteBatch, "health: " + Integer.toString(players[i].health) + "%", 20, 80);
			font.draw(spriteBatch, "lifes: " + Integer.toString(players[i].lifes), 20, 100);
			font.draw(spriteBatch, "level: " + Integer.toString(level), 20, 120);
			font.draw(spriteBatch, "fuel: " + Integer.toString((int)players[i].fuel), 20, 140);
			font.draw(spriteBatch, "time: " + Integer.toString((int)map.age), 20, 160);
			spriteBatch.end();
		}
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

	int window_width = 640;
	int window_height = 400;
	
	@Override
	public void resize (int width, int height) {
		window_width = width;
		window_height = height;
		for(int i=0;i<numplayers;++i) {
			cams[i].setToOrtho(false, ((float)(width/numplayers) / zoom), ((float)height / zoom));
			
			spriteBatch.getProjectionMatrix().setToOrtho2D(0, 0, width/numplayers, height);
		}

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

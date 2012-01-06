package cody.gravityshock;

import java.io.File;
import java.io.IOException;
import java.nio.IntBuffer;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
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

public class Main implements Screen {
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
    	float ax = 0f;//Gdx.input.getAccelerometerX();
    	
    	boolean touch_shoot = false;
    	float touch_direction = 0f;
    	boolean touch_thrust = false;
    	
    	for(int i=0;i<5;++i) {
    		if(Gdx.input.isTouched(i)) {
    			int x = Gdx.input.getX(i);
    			int y = Gdx.input.getY(i);
    			if(x > window_width * 3 / 4) {
    				if(y > window_height / 2)
    					touch_shoot = true;
    				else
        				touch_thrust = true;
    			}
    			else {
    				
    				float vx = cams[0].position.x + x - window_width/2;
    				float vy = cams[0].position.y - y + window_height/2;
    				Vector2 dir = new Vector2(vx, vy).sub(players[0].body.getPosition());
    				float angle1 = dir.angle() -90;
    				float angle2 = players[0].body.getAngle() * 180f / (float)Math.PI;
    				angle2 = (((int)angle2) % 360 + 360) % 360;
    				angle1 = (((int)angle1) % 360 + 360) % 360;
    				//float diff = angle1 - angle2;
    				if(angle2 > 180) {
    					if(angle1 < angle2 && angle1 > angle2 -180) {
    						touch_direction = -1;
    					}
    					else {
    						touch_direction = 1;
    					}
    				}
    				else {
    					if(angle1 > angle2 && angle1 < angle2 + 180) {
    						touch_direction = 1;
    					}
    					else {
    						touch_direction = -1;
    					}
    					
    				}
    				//System.out.println(Float.toString(angle1) + " " + Float.toString(angle2));
    			}
    			/*if(y > window_height * 3 / 4 && x < window_width / 2) {
    				touch_direction = -(float)(x - (window_width / 4)) / (float)(window_width / 2);
    			}*/
    			
    		}
    	}
    	
    	{
	    if(Gdx.input.isKeyPressed(Input.Keys.UP) || touch_thrust){
	    	players[0].control_thrust=1f;
	    }
	    else
	    	players[0].control_thrust=0f;
	    
	    float omega = 0;
	    
	    omega += ax * 0.5f;
	    omega += touch_direction;
	    
	    if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
	    	omega+=1f;
	    }
	    if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
	    	omega-=1f;
	    }
	    omega = Math.max(-1f, Math.min(1f, omega));
	    
	    players[0].control_direction = omega;
	    
	    if(Gdx.input.isKeyPressed(Input.Keys.SPACE) || touch_shoot){
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
	public void show () {
		font = new BitmapFont();
		font.setColor(Color.WHITE);
		//texture = new Texture(Gdx.files.internal("badlogic.jpg"))
		spriteBatch = new SpriteBatch();

		GL10 gl10 = Gdx.graphics.getGL10();
		GL20 gl20 = Gdx.graphics.getGL20();
		
		/*if(Gdx.graphics.isGL20Available()) {
			framebuffer = new FrameBuffer(Pixmap.Format.RGB888, 128, 128, false);
			framebuffer2 = new FrameBuffer(Pixmap.Format.RGB888, 512, 512, false);
		}*/

		//var musicfile = Gdx.files.internal("data/2ND_PM.ogg")
		//var music = Gdx.audio.newMusic(musicfile)
		//music.play
    
		cams = new OrthographicCamera[numplayers];
		players = new Spaceship[numplayers];
		
		for(int i=0;i<numplayers;++i)
			cams[i] = new OrthographicCamera(256, 256);
		
		nextLevel();
		
		//this.setScreen(new MainMenu());
	}

	void clear() {
        Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1);
		if(gl10 != null) {
			gl10.glClear(GL10.GL_COLOR_BUFFER_BIT);
			gl10.glEnable(GL10.GL_BLEND);
		}
		else if(gl20 != null){
			gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
			gl20.glEnable(GL20.GL_BLEND);
		}
		
	}
	 
	void viewport(int x, int y, int width, int height) {
		if(gl10 != null) {
			gl10.glViewport(x, y, width, height);
		}
		else if(gl20 != null){
			gl20.glViewport(x, y, width, height);
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
	public void render (float t2) {
		float t = Gdx.graphics.getDeltaTime();
		if(record) {
			// 30 FPS
			t = 1f/30f;
		}

		gl10 = Gdx.graphics.getGL10();
		gl20 = Gdx.graphics.getGL20();

		cams[0].update();

		controls();
		
		
		/*if(gl20 != null) {
			framebuffer2.begin();
			viewport(0, 0, window_width / numplayers, window_height);
			clear();
			world.render(cams[0]);
			framebuffer2.end();
		}
		else {*/
			viewport(0, 0, window_width / numplayers, window_height);
			clear();
			world.render(cams[0]);
		//}
		
		if(numplayers > 1){
			cams[1].update();
			
			/*if(gl20 != null) {
				framebuffer2.begin();
				viewport(window_width / 2, 0, window_width / numplayers, window_height);
				world.render(cams[1]);
				framebuffer2.end();
			}
			else {*/
				viewport(window_width / 2, 0, window_width / numplayers, window_height);
				world.render(cams[1]);
			//}
		}

		
		/*if(gl20 != null) {
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
		}*/

		for(int i=0;i<numplayers;++i) {
		if(players[i].health <= 0) {
			if(players[i].lifes <= 1) {
				spriteBatch.begin();
				viewport(i * (window_width / numplayers), 0, (window_width / numplayers), window_height);
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
			

			viewport(i * (window_width / numplayers), 0, (window_width / numplayers), window_height);
			
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

			/*if(Gdx.graphics.isGL20Available()) {
				framebuffer.dispose();
				framebuffer2.dispose();
				framebuffer = new FrameBuffer(Pixmap.Format.RGB888, width / 4, height / 4, false);
				framebuffer2 = new FrameBuffer(Pixmap.Format.RGB888, width, height, false);
			}*/
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

	@Override
	public void hide () {

	}
}

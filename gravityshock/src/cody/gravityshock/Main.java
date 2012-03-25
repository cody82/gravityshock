package cody.gravityshock;

import java.io.File;
import java.io.IOException;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.logging.Logger;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
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
    final int maxLevel = 8;
    
    boolean record = false;
    int frame = 1;
    
    FrameBuffer framebuffer;
    FrameBuffer framebuffer2;

	static GL20 gl20;
	
	float zoom = 1f;
	
	float countdown;
	
	MainGame game;
	Texture background;
	
	public Main(MainGame _game) {
		game = _game;
	}
	
    void createPlayer(int index){

    	Spaceship oldplayer = players[index];
    	players[index] = new Spaceship();
		world.add(players[index]);
		players[index].create();
		players[index].body.setTransform((index + 0.5f) * 22f - numplayers * 11f , 0, 0);
		if(oldplayer != null) {
			players[index].pickups = oldplayer.pickups;
			players[index].lifes = oldplayer.lifes;
		}
    }
    
    int score = 0;
    

	int LoadMaxLevel() {
		Preferences prefs = Gdx.app.getPreferences("levels");
		int i = prefs.getInteger("maxfreelevel", 1);
		return i;
	}
	
	void SaveMaxLevel(int level) {
		
		int l = LoadMaxLevel();
		if(level > l || l > maxLevel){
			Preferences prefs = Gdx.app.getPreferences("levels");
			prefs.putInteger("maxfreelevel", level);
			prefs.flush();
		}
	}
	
	int calcScore() {
		int s = score;
		for(Spaceship p : players) {
			s += p.score;
		}
		return s;
	}
    void nextLevel() {
    	if(level > 0 && map != null)
    		score += 1000 + Math.max(0, 1000 - map.age);
    	if(level == maxLevel) {
    		game.setScreen(new GameOverScreen(game, calcScore(), true));
    		return;
    	}
    	level++;
    	countdown = 3;
		if(world != null)
			world.dispose();
    	world = new World();
		
    	SaveMaxLevel(level);
		map = new Map();
		map.load(world, "data/level" + level + ".svg");
		for(int i =0;i<numplayers;++i)
		{
			createPlayer(i);
			players[i].pickups = 0;
		}
    }

	ShapeRenderer r = new ShapeRenderer();
    void drawbuttons() {
		viewport(0, 0, window_width, window_height);

		gl20.glEnable(GL20.GL_BLEND);
    	Matrix4 m = new Matrix4();
    	m.setToOrtho2D(0, window_height, window_width, -window_height);
    	r.setProjectionMatrix(m);
    	
    	
    	r.begin(ShapeType.FilledRectangle);

    	r.setColor(1f, 0, 0, touch_direction > 0 ? 0.7f : 0.5f);
    	r.filledRect(0, window_height - 100, 100, 100);
    	r.setColor(0.0f, 1f, 0, touch_direction < 0 ? 0.7f : 0.5f);
    	r.filledRect(150, window_height - 100, 100, 100);
    	
    	r.setColor(1f, 1f, 0.0f, touch_shoot ? 0.7f : 0.5f);
    	r.filledRect(window_width -100, window_height - 250, 100, 100);
    	r.setColor(0.0f, 0.0f, 1f, touch_thrust ? 0.7f : 0.5f);
    	r.filledRect(window_width -100, window_height - 100, 100, 100);
    	r.end();
    	
    	spriteBatch.begin();
		font.draw(spriteBatch, "left", 20, 60);
		font.draw(spriteBatch, "right", 170, 60);
		font.draw(spriteBatch, "shoot", window_width - 60, 210);
		font.draw(spriteBatch, "thrust", window_width - 60, 60);
		spriteBatch.end();
		
    }
    
    boolean zoom_enable = false;
    float zoom_start;
    
    int[] getTouches() {
    	ArrayList<Integer> l = new ArrayList<Integer>();
    	
    	for(int i=0;i<5;++i) {
    		if(Gdx.input.isTouched(i))
    			l.add(i);
    	}
    	int[] a = new int[l.size()];
    	for(int i=0;i<l.size();++i) {
    		a[i] = l.get(i);
    	}
    	return a;
    }

	boolean touch_shoot = false;
	float touch_direction = 0f;
	boolean touch_thrust = false;
	
    void controls() {
    	float ax = 0f;//Gdx.input.getAccelerometerX();
    	
    	touch_shoot = false;
    	touch_direction = 0f;
    	touch_thrust = false;
    	

	    if(Gdx.input.isKeyPressed(Input.Keys.O)){
	    	zoom += 0.01f;
	    	resize(window_width, window_height);
	    }
	    else if(Gdx.input.isKeyPressed(Input.Keys.I)){
	    	zoom -= 0.01f;
	    	resize(window_width, window_height);
	    }
	    
    	if(Gdx.app.getType() == ApplicationType.Android) {

    		boolean button_pressed = false;
    	for(int i=0;i<5;++i) {
    		if(Gdx.input.isTouched(i)) {
    			int x = Gdx.input.getX(i);
    			int y = Gdx.input.getY(i);
    			if(x > window_width - 100 && y > window_height - 100) {
        			touch_thrust = true;
        			button_pressed = true;
    			}
    			else if(x > window_width - 100 && y > window_height - 250 && y < window_height - 150) {
    				touch_shoot = true;
        			button_pressed = true;
    			}
    			else if(y > window_height - 100 && x < 100) {
					touch_direction = 1;
        			button_pressed = true;
    			}
    			else if(y > window_height - 100 && x < 250 && x > 150) {
					touch_direction = -1;
        			button_pressed = true;
    			}
    			else {
    				float vx = cams[0].position.x + x - window_width/2;
    				float vy = cams[0].position.y - y + window_height/2;
    				Vector2 dir = new Vector2(vx, vy).sub(players[0].body.getPosition());
    				float angle1 = dir.angle() -90;
    				float angle2 = players[0].body.getAngle() * 180f / (float)Math.PI;
    				angle2 = (((int)angle2) % 360 + 360) % 360;
    				angle1 = (((int)angle1) % 360 + 360) % 360;
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
    			}
    			
    		}
    	}
    	
    	if(!button_pressed) {
    		int[] t = getTouches();
    	    Vector2 zoom_pos1;
    	    Vector2 zoom_pos2;
    		boolean z = t.length == 2;

    		if(z) {
    			zoom_pos1 = new Vector2(Gdx.input.getX(t[0]), Gdx.input.getY(t[0]));
    			zoom_pos2 = new Vector2(Gdx.input.getX(t[1]), Gdx.input.getY(t[1]));
    			if(zoom_enable) {
    				zoom = zoom_pos1.dst(zoom_pos2) / zoom_start;
    		    	resize(window_width, window_height);
    			} else {
    				zoom_start = zoom_pos1.dst(zoom_pos2);
    				zoom_enable = true;
    			}
    		} else {
    			zoom_enable = false;
    		}
    	}
    	else {
    		zoom_enable = false;
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
		if(cams != null)
			return;
		
        Util.stopMusic();
		
        font = new BitmapFont(Gdx.files.internal("data/default.fnt"), Gdx.files.internal("data/default.png"),false);
		font.setColor(Color.WHITE);

		spriteBatch = new SpriteBatch();
		
		gl20 = Gdx.graphics.getGL20();
		
		background = Assets.getTexture("data/space.png");
		background.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
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
        if(gl20 != null){
			gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
			gl20.glEnable(GL20.GL_BLEND);
		}
		
	}
	public static void blend(boolean b) {
		if(gl20 != null){
			if(b)
				gl20.glEnable(GL20.GL_BLEND);
			else
				gl20.glDisable(GL20.GL_BLEND);
		}
		
	}
	
	void viewport(int x, int y, int width, int height) {if(gl20 != null){
			gl20.glViewport(x, y, width, height);
		}
	}
	int getTotalScore() {
		int score = 0;
		for(Spaceship s : players) {
			score += s.pickups;
		}
		return score;
	}
	@Override
	public void render (float t2) {
		if(cams == null)
			show();
		
		float t = Gdx.graphics.getDeltaTime();
		if(record) {
			// 30 FPS
			t = 1f/30f;
		}

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
			float x2 = cams[0].position.x * 0.01f;
			float y2 = cams[0].position.y * 0.01f;

			blend(false);
			spriteBatch.begin();
			spriteBatch.draw(background, 0, 0, window_width / numplayers, window_height, x2, y2, x2 + (window_width / numplayers / 64), y2 +  + (window_height / 64));
			spriteBatch.end();
			blend(true);
			
			//clear();
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
			x2 = cams[1].position.x * 0.01f;
			y2 = cams[1].position.y * 0.01f;
				viewport(window_width / 2, 0, window_width / numplayers, window_height);
				gl20.glDisable(GL20.GL_BLEND);
				spriteBatch.begin();
				spriteBatch.draw(background, 0, 0, window_width / numplayers, window_height, x2, y2, x2 + (window_width / numplayers / 64), y2 +  + (window_height / 64));
				spriteBatch.end();
				gl20.glEnable(GL20.GL_BLEND);

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

		boolean game_over = true;
		for(int i=0;i<numplayers;++i) {
		if(players[i].health <= 0) {
			if(players[i].lifes <= 1) {
				spriteBatch.begin();
				viewport(i * (window_width / numplayers), 0, (window_width / numplayers), window_height);
				font.draw(spriteBatch, "GAME OVER", (window_width / numplayers) / 2 - font.getSpaceWidth()*9, Gdx.graphics.getHeight()/2 + font.getLineHeight()/2);
				spriteBatch.end();
			}
			else {
				if(players[i].countdown <= 0){
					createPlayer(i);
					players[i].lifes--;
				}
				game_over = false;
			}
		}
		else if(getTotalScore() >= map.getGoalScore()) {
			countdown -= t;
			game_over = false;
			if(countdown <= 0) {
				nextLevel();
			}
			else {
				viewport(0, 0, window_width, window_height);
				spriteBatch.begin();
				font.draw(spriteBatch, "LEVEL COMPLETE", window_width * 0.5f- font.getSpaceWidth()*14, window_height * 0.5f + font.getLineHeight()/2);
				spriteBatch.end();
			}
			break;
		}
		else {
			game_over = false;
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
			int y = window_height - 280;
			font.draw(spriteBatch, "fps: " + Integer.toString(fps), 20, y);
			font.draw(spriteBatch, "pickups: " + Integer.toString(players[i].pickups) + "/" + Integer.toString(map.getGoalScore()), 20, y+30);
			font.draw(spriteBatch, "speed: " + Integer.toString((int)players[i].body.getLinearVelocity().len()) + "m/s", 20, y+60);
			font.draw(spriteBatch, "health: " + Integer.toString(players[i].health) + "%", 20, y+90);
			font.draw(spriteBatch, "lifes: " + Integer.toString(players[i].lifes), 20, y+120);
			font.draw(spriteBatch, "level: " + Integer.toString(level), 20, y+150);
			font.draw(spriteBatch, "fuel: " + Integer.toString((int)players[i].fuel), 20, y+180);
			font.draw(spriteBatch, "time: " + Integer.toString((int)map.age), 20, y+210);
			font.draw(spriteBatch, "score: " + calcScore(), 20, y+240);
			font.draw(spriteBatch, "box2d: " + world.b2world.getBodyCount() + " " + world.b2world.getJointCount(), 20, y+270);
			spriteBatch.end();
			if(Gdx.app.getType() == ApplicationType.Android)
				drawbuttons();
		}
		}
		
		if(game_over) {
			countdown -= t;
			if(countdown <= 0){
				game.setScreen(new GameOverScreen(game, score, false));
				return;
			}
		}
		/*
		if(record) {
			
		try {
			File f = new File(String.format("screen%06d.png", frame++));
			ScreenshotSaver.saveScreenshot(f);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
		*/
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
			//cams[i].setToOrtho(false, ((float)(width/numplayers) / zoom), ((float)height / zoom));
			cams[i].viewportWidth = ((float)(width/numplayers) / zoom);
			cams[i].viewportHeight = ((float)(height/numplayers) / zoom);
			
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

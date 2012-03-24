package cody.gravityshock;

import cody.svg.Svg;
import cody.svg.SvgImage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;

public class Explosion extends Actor{
	public Explosion(World _world) {
		super(_world);
		create();
	}
	
	public Explosion() {
		create();
	}

	static Svg svg;
	
	public Vector2 position;
	
	float age;
	
	static Sound sound;
	
	public void create() {
		if(svg == null)
			svg = Assets.getSvg("data/explosion1.svg");
		if(mesh == null) {
			mesh = Assets.getMesh("data/explosion1.svg");
		}

		if(sound == null) {
			sound = Assets.getSound("data/explosion1.ogg");
		}
		
		if(sound != null)
			sound.play();
	}
	  
	  void tick(float dtime) {
		  age += dtime;
		  if(age>3f){
			  world.actors.remove(this);
			  dispose();
		  }
	  }

	  static Mesh mesh;

		static SpriteBatch sb = new SpriteBatch();
	  void render(OrthographicCamera cam) {
		    Vector2 pos = position;
		    float scale = (float)Math.sqrt((double)age) * 0.8f;
		    float alpha = (3f - age)/3f;
			  Matrix4 matrix = cam.combined.cpy();
			  matrix.translate(pos.x, pos.y, 0);
			  matrix.scale(scale, scale, 0);

			  sb.setProjectionMatrix(matrix);
			  sb.begin();
			  sb.setColor(1, 1, 1, alpha);
			  for(SvgImage i : svg.images) {
				  sb.draw(i.texture, i.x, i.y, i.width, i.height);
			  }
			  sb.end();
			  Main.blend(true);

			Util.render(mesh, GL10.GL_TRIANGLES, matrix, alpha);
	  }
}

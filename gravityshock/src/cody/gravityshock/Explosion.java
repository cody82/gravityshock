package cody.gravityshock;

import cody.svg.Svg;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.OrthographicCamera;
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
	
	public void create() {
		if(svg == null)
			svg = new Svg("data/explosion1.svg");
		if(mesh == null) {
			mesh = Util.createMesh(svg, 3);
		}
	}
	  
	  void tick(float dtime) {
		  age += dtime;
		  if(age>3f){
			  world.actors.remove(this);
			  dispose();
		  }
	  }

	  static Mesh mesh;
	  
	  void render(OrthographicCamera cam) {
		    Vector2 pos = position;
		    float scale = (float)Math.sqrt((double)age) * 0.8f;

			  Matrix4 matrix = cam.combined.cpy();
			  matrix.translate(pos.x, pos.y, 0);
			  matrix.scale(scale, scale, 0);
	//cam.apply(Gdx.graphics.getGL10());
	//Gdx.graphics.getGL10().glTranslatef(pos.x, pos.y, 0);
	//Gdx.graphics.getGL10().glScalef(scale, scale, 0);

			Util.render(mesh, GL10.GL_TRIANGLES, matrix, (3f - age)/3f);
	  }
}

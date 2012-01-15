package cody.gravityshock;

import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;

public class Home extends Actor{
	Mesh mesh;
	public Home(World _world) {
		super(_world);
	}
	public Home(World _world, Mesh m, Fixture f) {
		super(_world);
		mesh = m;
		if(f != null)
			f.setUserData(this);
	}
	public Home() {
	}
	
	public void create() {
		BodyDef bdef = new BodyDef();
	    bdef.type = BodyDef.BodyType.StaticBody;
	    
	    body = world.b2world.createBody(bdef);
	  }
	  
	  void tick(float dtime) {
	  }
	  
	  void render(OrthographicCamera cam) {
		  if(mesh==null)
			  return;
		  
Vector2 pos = body.getPosition();
	  float rad = body.getAngle();


	  Matrix4 matrix = cam.combined.cpy();
	  matrix.translate(pos.x, pos.y, 0);
	  matrix.rotate(0, 0, 1, rad*180f/(float)Math.PI);

	Util.render(mesh, GL10.GL_TRIANGLES, matrix);
	  }
}

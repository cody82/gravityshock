package cody.gravityshock;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

public class Pickup extends Actor{
	public boolean returned = false;
	
	public Pickup(World _world) {
		super(_world);
	}
	public Pickup() {
	}
	
	public void create() {
		BodyDef bdef = new BodyDef();
	    bdef.type = BodyDef.BodyType.DynamicBody;
	    
	    body = world.b2world.createBody(bdef);
	    //shape = new box2d.CircleShape()
	    //shape.setRadius(10)
	    PolygonShape s = new PolygonShape();
	    shape = s;
	    Vector2[] array = new Vector2[]{new Vector2(-5, 5), new Vector2(-5, -5), new Vector2(5, -5), new Vector2(5, 5)};
	    s.set(array);
	    
	    fixture = body.createFixture(shape, 0.1f);
		fixture.setRestitution(0.3f);
		fixture.setUserData(this);
		
		if(mesh == null) {
			mesh = Util.createMesh(array, new Color(0,0,1,1), 3, true);
			mesh2 = Util.createMesh(array, new Color(0,1,0,1), 3, true);
		}
	  }
	  
	  void tick(float dtime) {
	  }

	  static Mesh mesh;
	  static Mesh mesh2;

	  void render(OrthographicCamera cam) {
		  Vector2 pos = body.getPosition();
		  float rad = body.getAngle();


		  Matrix4 matrix = cam.combined.cpy();
		  matrix.translate(pos.x, pos.y, 0);
		  matrix.rotate(0, 0, 1, rad*180f/(float)Math.PI);
		  
	if(!returned)
		Util.render(mesh, GL20.GL_TRIANGLES, matrix);
	else
		Util.render(mesh2, GL20.GL_TRIANGLES, matrix);
	  }
}

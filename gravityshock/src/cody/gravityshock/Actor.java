package cody.gravityshock;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class Actor {
	  public Body body;
	  Shape shape;
	  Fixture fixture;
	  World world;
	  Mesh mesh;
	  
	  public Actor() {
			  }
	  
	  public Actor(World _world) {
			    world = _world;
			    world.add(this);
			    create();
			  }	  
	  public Actor(World _world, Mesh m, Fixture f, Body b) {
				    world = _world;
				    world.add(this);
				    mesh=m;
				    fixture = f;
				    fixture.setUserData(this);
				    body = b;
				    create();
				  }
			  void create() {
			  }
			  
			  void render(OrthographicCamera cam) {
			    if(mesh != null) {
					  Vector2 pos = body.getPosition();
					  float rad = body.getAngle();

					  Matrix4 matrix = cam.combined.cpy();
					  matrix.translate(pos.x, pos.y, 0);
					  matrix.rotate(0, 0, 1, rad*180f/(float)Math.PI);
					Util.render(mesh, GL10.GL_TRIANGLES, matrix);
			    }
			  }
			  
			  
			  void tick(float dtime) {
			  }

			  public void dispose(){
				  if(body != null) {
					  body.destroyFixture(fixture);
					  world.b2world.destroyBody(body);
					  body = null;
				  }
				  if(shape != null) {
					  shape.dispose();
					  shape = null;
				  }
			  }
}

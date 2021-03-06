package cody.gravityshock;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;

public class Projectile extends Actor{
	public Projectile(World _world) {
		super(_world);
	}
	public Projectile() {
	}
	
	public Actor source;
	
	float age;
	public void create() {
		BodyDef bdef = new BodyDef();
	    bdef.type = BodyDef.BodyType.DynamicBody;
	    bdef.bullet = true;
	    body = world.b2world.createBody(bdef);
	    shape = new CircleShape();
	    shape.setRadius(1);
	    fixture = body.createFixture(shape, 1f);
		fixture.setRestitution(0.5f);
		fixture.setUserData(this);
		
		if(mesh == null)
			mesh = Util.createMesh(new Vector2[]{new Vector2(-1,-1), new Vector2(1,-1), new Vector2(1,1), new Vector2(-1,1)}, new Color(1,1,0,1), 2, true);
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

		  Vector2 pos = body.getPosition();
		  float rad = body.getAngle();


		  Matrix4 matrix = cam.combined.cpy();
		  matrix.translate(pos.x, pos.y, 0);
		  matrix.rotate(0, 0, 1, rad*180f/(float)Math.PI);

		  Util.render(mesh, GL20.GL_TRIANGLES, matrix, (3f - age)/3f);
	  }


}

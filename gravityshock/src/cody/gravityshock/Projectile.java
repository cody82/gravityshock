package cody.gravityshock;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
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
	  }
	  
	  void tick(float dtime) {
		  age += dtime;
		  if(age>3f){
			  world.actors.remove(this);
			  dispose();
		  }
	  }

	  void render(OrthographicCamera cam) {
		  Vector2 pos = body.getPosition();
		  float rad = body.getAngle();
		  
cam.apply(Gdx.graphics.getGL10());
Gdx.graphics.getGL10().glTranslatef(pos.x, pos.y, 0);
Gdx.graphics.getGL10().glRotatef(rad*180f/(float)Math.PI, 0, 0, 1);

	    Vector2[] array = new Vector2[]{new Vector2(-1, 1), new Vector2(-1, -1), new Vector2(1, -1), new Vector2(1, 1)};

	    Color c = new Color(1, 1, 0, (3f-age)/3f);
	    for(int i =0; i < array.length - 1; ++i) {
			  LineDrawer.DrawLine(array[i], array[i+1], 3, c);
	    }
		  LineDrawer.DrawLine(array[array.length-1], array[0], 3, c);
	  }


}

package cody.gravityshock;

import java.util.Iterator;

import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;

public class Gravity extends Actor{
	public Vector2 position;
	public float strength;
	
	public Gravity(World _world) {
		super(_world);
	}
	public Gravity(World _world, Vector2 position, float strength) {
		super(_world);
		this.position = position.cpy();
		this.strength = strength;
	}
	public Gravity() {
		this.position = new Vector2(0,0);
	}

	@Override
	public void create() {
	  }
	  
	@Override
	  void tick(float dtime) {
		super.tick(dtime);
		  Iterator<Body> bodies = world.b2world.getBodies();
		  while(bodies.hasNext()) {
			  Body b = bodies.next();
			  Vector2 d = position.cpy().sub(b.getPosition());
			  float d2 = d.len2();
			  d.nor();
			  d.mul(strength/d2);
			  b.applyForceToCenter(d);
		  }
	  }

	@Override
	  void render(OrthographicCamera cam) {
	  }
}
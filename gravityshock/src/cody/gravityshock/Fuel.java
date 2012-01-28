package cody.gravityshock;

import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;

public class Fuel extends Actor{
	public Fuel(World _world) {
		super(_world);
	}
	public Fuel(World _world, Mesh m, Fixture f) {
		super(_world);
		mesh = m;
		if(f != null)
			f.setUserData(this);
	}
	public Fuel() {
	}
	
	public void create() {
		BodyDef bdef = new BodyDef();
	    bdef.type = BodyDef.BodyType.StaticBody;
	    
	    body = world.b2world.createBody(bdef);
	  }
	  
	  void tick(float dtime) {
	  }
	  
	  void render(OrthographicCamera cam) {
		  super.render(cam);
	  }

}

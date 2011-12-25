package cody.gravityshock;

import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.graphics.OrthographicCamera;

public abstract class Actor {
	  public Body body;
	  Shape shape;
	  Fixture fixture;
	  World world;

	  
	  public Actor() {
			  }
	  
	  public Actor(World _world) {
			    world = _world;
			    world.add(this);
			    create();
			  }

			  void create() {
			  }
			  
			  void render(OrthographicCamera cam) {
			    
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

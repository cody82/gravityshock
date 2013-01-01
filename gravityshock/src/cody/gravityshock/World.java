package cody.gravityshock;

import java.util.ArrayList;

import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;


public class World implements ContactListener {
	com.badlogic.gdx.physics.box2d.World b2world;
	ArrayList<Actor> actors;
	Map map;
	  
	public class CollisionInfo {
		public CollisionInfo(Object _object1, Object _object2, Fixture _fixture1, Fixture _fixture2, float _impulse) {
			impulse = _impulse;
			object1 = _object1;
			object2 = _object2;
			fixture1 = _fixture1;
			fixture2 = _fixture2;
		}
		public Object object1;
		public Object object2;
		
		public Fixture fixture1;
		public Fixture fixture2;
		
		public float impulse;
	}
	ArrayList<CollisionInfo> collisions;
	
	  public World(){
		  b2world = new com.badlogic.gdx.physics.box2d.World(new Vector2(0, -9.81f), true);
		  b2world.setContactListener(this);
		  actors = new ArrayList<Actor>();
		  collisions = new ArrayList<CollisionInfo>();
	  }
	  
	  public void add(Actor actor) {
	    actors.add(actor);
	    actor.world = this;
	  }
	  
	  public void dispose() {
		  System.out.println("world.dispose1");
		  map.dispose();
		  System.out.println("world.dispose2");
		  b2world.dispose();
	  }
	  
	  float last;
	  public void tick(float dtime) {
		  float timestep = 1f/120f;
		  dtime+=last;
		  if(dtime<timestep){
			  last = dtime;
			  return;
		  }

		  float t = 0;
		  while(t + timestep < dtime){
			  b2world.step(timestep, 1, 1);
			  for(Actor a : (ArrayList<Actor>)actors.clone()) {
				  a.tick(timestep);
			  }
			  for(CollisionInfo s : collisions) {
				  OnCollide(s);
			  }
			  collisions.clear();
			  t += timestep;
		  }
		  last = dtime - t;
	  }
	  
	  public void render(OrthographicCamera cam) {
	    map.render(cam);
	    for(Actor a : actors) {
	      a.render(cam);
	    }
	  }

	  void OnCollide(CollisionInfo s) {
		  if(s.object1 instanceof Actor) {
			  ((Actor)s.object1).onCollide(s.object2, s.impulse, s.fixture1, s.fixture2);
		  }
		  if(s.object2 instanceof Actor) {
			  ((Actor)s.object2).onCollide(s.object1, s.impulse, s.fixture2, s.fixture1);
		  }
	  }
	@Override
	public void beginContact(Contact contact) {
	}

	@Override
	public void endContact(Contact arg0) {
		
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse contactimpulse) {
		float[] impulses = contactimpulse.getNormalImpulses();
		float impulse = 0;

		for(float f : impulses)
			impulse += Math.abs(f) < 100000 ? f : 0;

		Fixture f1 = contact.getFixtureA();
		Fixture f2 = contact.getFixtureB();
		
		Object o1 = f1.getUserData();
		Object o2 = f2.getUserData();
		
		if(o1 != null && o2 != null) {
			//System.out.println(o1.getClass().getName() + " | " + o2.getClass().getName() + ": " + impulse);
			collisions.add(new CollisionInfo(o1, o2, f1, f2, impulse));
		}
	}

	@Override
	public void preSolve(Contact arg0, Manifold arg1) {
		
	}

}

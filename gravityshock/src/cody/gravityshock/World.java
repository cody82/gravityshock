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
		public CollisionInfo(Spaceship _ship, Vector2 _pos, float _impulse, boolean _isgear) {
			ship = _ship;
			pos = _pos;
			impulse = _impulse;
			isgear = _isgear;
		}
		public Spaceship ship;
		public Vector2 pos;
		public float impulse;
		public boolean isgear;
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
	  
	  void dispose() {
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
		  s.ship.damage(s);
	  }
	@Override
	public void beginContact(Contact contact) {
	}

	@Override
	public void endContact(Contact arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse contactimpulse) {
		float[] impulses = contactimpulse.getNormalImpulses();
		float impulse = 0;
		for(float f : impulses)
			impulse += f;
				
		Fixture f1 = contact.getFixtureA();
		Fixture f2 = contact.getFixtureB();
		
		Object o1 = f1.getUserData();
		Object o2 = f2.getUserData();
		
		if(o1 != null && o2 != null) {
			//System.out.println(o1.getClass().getName() + " | " + o2.getClass().getName() + ": " + impulse);
			if(o1 instanceof Spaceship) {
				if(o2 instanceof Map || o2 instanceof Projectile)
					collisions.add(new CollisionInfo((Spaceship)o1, ((Spaceship) o1).body.getPosition(), impulse, ((Spaceship)o1).gear1 == f1 || ((Spaceship)o1).gear2 == f1));
			}
			if(o2 instanceof Spaceship) {
				if(o1 instanceof Map || o2 instanceof Projectile)
					collisions.add(new CollisionInfo((Spaceship)o2, ((Spaceship) o2).body.getPosition(), impulse, ((Spaceship)o2).gear1 == f2 || ((Spaceship)o2).gear2 == f2));
			}
		}
	}

	@Override
	public void preSolve(Contact arg0, Manifold arg1) {
		// TODO Auto-generated method stub
		
	}

}

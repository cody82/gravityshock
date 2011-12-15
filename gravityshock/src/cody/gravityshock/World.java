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
	  
	ArrayList<Spaceship> collisions;
	
	  public World(){
		  b2world = new com.badlogic.gdx.physics.box2d.World(new Vector2(0, -9.81f), true);
		  b2world.setContactListener(this);
		  actors = new ArrayList<Actor>();
		  collisions = new ArrayList<Spaceship>();
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
			  for(Spaceship s : collisions) {
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

	  void OnCollide(Spaceship s) {
		  s.damage();
	  }
	@Override
	public void beginContact(Contact contact) {
		Fixture f1 = contact.getFixtureA();
		Fixture f2 = contact.getFixtureB();
		
		Object o1 = f1.getUserData();
		Object o2 = f2.getUserData();
		
		if(o1 != null && o2 != null) {
			System.out.println(o1.getClass().getName() + " | " + o2.getClass().getName());
			if(o1 instanceof Spaceship) {
				if(o2 instanceof Map)
					collisions.add((Spaceship)o1);
			}
			if(o2 instanceof Spaceship) {
				if(o1 instanceof Map)
					collisions.add((Spaceship)o2);
			}
		}
	}

	@Override
	public void endContact(Contact arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postSolve(Contact arg0, ContactImpulse arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void preSolve(Contact arg0, Manifold arg1) {
		// TODO Auto-generated method stub
		
	}

}

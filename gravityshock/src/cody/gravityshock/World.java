package cody.gravityshock;

import java.util.ArrayList;

import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class World {
	com.badlogic.gdx.physics.box2d.World b2world;
	ArrayList<Actor> actors;
	Map map;
	  
	  public World(){
		  b2world = new com.badlogic.gdx.physics.box2d.World(new Vector2(0, -9.81f), true);
		  actors = new ArrayList<Actor>();
	  }
	  
	  public void add(Actor actor) {
	    actors.add(actor);
	    actor.world = this;
	  }
	  
	  void dispose() {
	    b2world.dispose();
	  }
	  
	  public void tick(float dtime) {
	    b2world.step(dtime, 1, 1);
	    for(Actor a : actors) {
	      a.tick(dtime);
	    }
	  }
	  
	  public void render(OrthographicCamera cam) {
	    map.render(cam);
	    for(Actor a : actors) {
	      a.render(cam);
	    }
	  }

}

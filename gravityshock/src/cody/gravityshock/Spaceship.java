package cody.gravityshock;

import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.Input;

public class Spaceship extends Actor {

	public Spaceship(World _world) {
		super(_world);
	}
	public Spaceship() {
	}
	
	public void create() {
		BodyDef bdef = new BodyDef();
	    bdef.type = BodyDef.BodyType.DynamicBody;
	    
	    body = world.b2world.createBody(bdef);
	    //shape = new box2d.CircleShape()
	    //shape.setRadius(10)
	    PolygonShape s = new PolygonShape();
	    shape = s;
	    s.set(new Vector2[]{new Vector2(0, 10), new Vector2(-10, -5), new Vector2(10, -5)});
	    
	    fixture = body.createFixture(shape, 0.1f);
	  }
	  
	  void tick(float dtime) {
	    if(Gdx.input.isKeyPressed(Input.Keys.UP)){
	      body.applyForceToCenter(body.getWorldVector(new Vector2(0,1000)));
	    }
	    if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
	      body.applyTorque(1000);
	    }
	    if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
	      body.applyTorque(-1000);
	    }
	  }
	  
	  void render(OrthographicCamera cam) {
	        
		  ShapeRenderer sr = new ShapeRenderer();
	    sr.setProjectionMatrix(cam.combined);
	    
	    sr.begin(ShapeType.Line);
	    
	    Vector2 pos = body.getPosition();
	    float rad = body.getAngle();
	   
	    sr.translate(pos.x, pos.y, 0);
	    sr.rotate(0, 0, 1, rad*180f/(float)Math.PI);
	    Vector2[] array = new Vector2[]{new Vector2(0, 10), new Vector2(10, -5), new Vector2(-10, -5)};

	    sr.setColor(1, 1, 0, 1);
	    for(int i =0; i < array.length - 1; ++i) {
	      sr.line(array[i].x, array[i].y, array[i+1].x, array[i+1].y);
	    }
	    sr.line(array[array.length-1].x, array[array.length-1].y, array[0].x, array[0].y);
	    sr.end();

	  }
}
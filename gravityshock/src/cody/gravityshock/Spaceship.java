package cody.gravityshock;

import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;
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
	
	public int health = 100;
	
	public void damage() {
		if(health <= 0)
			return;
		health -= 10;
		if(health <= 0) {
			  world.actors.remove(this);
			  dispose();
		}
	}
	public void create() {
		BodyDef bdef = new BodyDef();
	    bdef.type = BodyDef.BodyType.DynamicBody;
	    
	    body = world.b2world.createBody(bdef);
	    //body.setLinearDamping(1);
	    //shape = new box2d.CircleShape()
	    //shape.setRadius(10)
	    PolygonShape s = new PolygonShape();
	    shape = s;
	    s.set(new Vector2[]{new Vector2(0, 10), new Vector2(-10, -5), new Vector2(10, -5)});
	    
	    fixture = body.createFixture(shape, 0.1f);
		fixture.setRestitution(0.5f);
		fixture.setUserData(this);
	  }

	  float shoot_time;
	  boolean thrust;
	  void tick(float dtime) {
		  shoot_time+=dtime;
	    if(Gdx.input.isKeyPressed(Input.Keys.UP)){
	    	thrust=true;
	      body.applyForceToCenter(body.getWorldVector(new Vector2(0,1000)));
	    }
	    else
	    	thrust = false;
	    
	    float omega = 0;
	    if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
	    	omega+=3;
	    }
	    if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
	    	omega-=3;
	    }
	    body.setAngularVelocity(omega);
	    
	    if(Gdx.input.isKeyPressed(Input.Keys.SPACE)){
	    	if(shoot_time > 0.1f){
	    		shoot();
	    		shoot_time = 0;
	    	}
	    }
	    
	    for(Actor a : world.actors) {
		      float dist = body.getPosition().dst(a.body.getPosition());
		      if(!connected && a instanceof Pickup && dist < 25){
		    	  connect((Pickup)a);
		      }
		      else if(connected && a instanceof Home && dist < 25){
		    	  disconnect();
		      }
		    }
	  }
	  
	  boolean connected = false;
	  Joint pickupjoint;
	  Pickup pickup;
	  
	  void connect(Pickup p) {
		  if(p.returned)
			  return;
		  
		  pickup = p;
		  DistanceJointDef def = new DistanceJointDef();
		  def.bodyA = body;
		  def.bodyB = p.body;
		  def.collideConnected = true;
		  def.length = 25;
		  def.type = JointDef.JointType.DistanceJoint;
		  pickupjoint = world.b2world.createJoint(def);
		  connected = true;
	  }
	  int score = 0;
	  
	  void disconnect(){
		  world.b2world.destroyJoint(pickupjoint);
		  pickupjoint = null;
		  connected = false;
		  pickup.returned = true;
		  pickup = null;
		  score++;
	  }
	  
	  void shoot(){
		  Projectile p = new Projectile(world);
		  p.body.setTransform(body.getWorldPoint(new Vector2(0,15)), body.getAngle());
		  p.body.setLinearVelocity(body.getWorldVector(new Vector2(0,1000)));
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

		  sr.setColor(1, 1, 1, 1);
		  for(int i =0; i < array.length - 1; ++i) {
			  sr.line(array[i].x, array[i].y, array[i+1].x, array[i+1].y);
		  }
		  sr.line(array[array.length-1].x, array[array.length-1].y, array[0].x, array[0].y);
		  
		  sr.end();
		  if(thrust){
			  sr.begin(ShapeType.FilledRectangle);
			  sr.setColor(1, 1, 0, 0.5f);
			  sr.filledRect(-5, -10, 10, 5);
			  sr.end();
		  }
		  sr.dispose();
	  }
}

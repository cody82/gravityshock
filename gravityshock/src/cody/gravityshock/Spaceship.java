package cody.gravityshock;

import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;
import com.badlogic.gdx.physics.box2d.joints.RopeJointDef;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.Input;

public class Spaceship extends Actor {

	public Spaceship(World _world) {
		super(_world);
	}
	public Spaceship() {
	}
	
	public int health = 100;
	public float fuel = 100;
    public int lifes = 5;
	
	public void damage(World.CollisionInfo i) {
		if(health <= 0)
			return;
		
		float damage = Math.abs(i.impulse) * 0.06f;
		if(damage < 0.5f)
			return;
		health -= damage;
		
		if(health <= 0) {
			  Explosion x = new Explosion(world);
			  x.position = i.pos.cpy();
			  if(connected) {
				  world.b2world.destroyJoint(pickupjoint);
				  pickupjoint = null;
				  connected = false;
				  pickup = null;
			  }
			  //world.actors.remove(this);
			  //dispose();
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
	  
	  public float control_direction;
	  public float control_thrust;
	  public boolean control_shoot;
	  
	  void tick(float dtime) {
		  shoot_time+=dtime;
		  
		  
	    if(control_thrust > 0 && fuel > 0f && health > 0){
	    	thrust=true;
	      body.applyForceToCenter(body.getWorldVector(new Vector2(0,1000)));
	      fuel -= dtime;
	    }
	    else
	    	thrust = false;
	    
	    float omega = control_direction * 3f;
	    
	    if(fuel > 0 && health > 0) {
	    	float av = body.getAngularVelocity();
	    	body.applyTorque(Math.signum((omega - av)) * 8000);
	    }
	    
	    if(control_shoot && health > 0){
	    	if(shoot_time > 0.1f){
	    		shoot();
	    		shoot_time = 0;
	    	}
	    }
	    
	    for(Actor a : world.actors) {
	    	if(a.body == null)
	    		continue;
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
		  if(p.returned || health <= 0)
			  return;
		  
		  pickup = p;
		  RopeJointDef def = new RopeJointDef();
		  def.bodyA = body;
		  def.bodyB = p.body;
		  def.collideConnected = true;
		  def.maxLength = 25;
		  def.type = JointDef.JointType.RopeJoint;
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

		  Vector2 pos = body.getPosition();
		  float rad = body.getAngle();
		  
cam.apply(Gdx.graphics.getGL10());
Gdx.graphics.getGL10().glTranslatef(pos.x, pos.y, 0);
Gdx.graphics.getGL10().glRotatef(rad*180f/(float)Math.PI, 0, 0, 1);
		  
		  Vector2[] array = new Vector2[]{new Vector2(0, 10), new Vector2(10, -5), new Vector2(-10, -5)};

		  Color c;
		  if(health > 0) {
			  c = new Color(1, 1, 1, 1);
		  }
		  else {
			  c = new Color(0.4f, 0.4f, 0.4f, 1);
		  }
		  for(int i =0; i < array.length - 1; ++i) {
			  //sr.line(array[i].x, array[i].y, array[i+1].x, array[i+1].y);
			  LineDrawer.DrawLine(array[i], array[i+1], 3, c);
		  }
		  //LineDrawer.DrawLine(array[0], array[0+1], 3, c);
		  //LineDrawer.DrawLine(array[1], array[1+1], 3, c);
		  //sr.line(array[array.length-1].x, array[array.length-1].y, array[0].x, array[0].y);
		  LineDrawer.DrawLine(array[array.length-1], array[0], 3, c);
		  
		  
		  
		  if(thrust){
			  c = new Color(1, 1, 0, 1);
			  LineDrawer.DrawLine(new Vector2(-5,-15), new Vector2(-5,-5), 3, c);
			  LineDrawer.DrawLine(new Vector2(5,-15), new Vector2(5,-5), 3, c);
			  LineDrawer.DrawLine(new Vector2(0,-15), new Vector2(0,-5), 3, c);
		  }
	  }
}

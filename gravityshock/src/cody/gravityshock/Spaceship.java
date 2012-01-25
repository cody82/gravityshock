package cody.gravityshock;

import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;
import com.badlogic.gdx.physics.box2d.joints.RopeJointDef;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
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
	public float countdown = 3;
	
	@Override
	public void dispose() {
		super.dispose();
	      if(play_thrust != -1) {
	    	  thrust_sound.stop(play_thrust);
	    	  play_thrust = -1;
	      }
	}
	public void damage(World.CollisionInfo i) {
		if(health <= 0)
			return;
		
		float damage = Math.abs(i.impulse) * 0.06f * (i.isgear ? 0.05f : 1f);
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
	
	static Sound thrust_sound;
	static Sound shoot_sound;
	static Sound return_sound;
	static Sound pickup_sound;
	
	long play_thrust = -1;
	
	static Mesh mesh;
	static Mesh mesh2;
	static Mesh mesh_thrust;
	
	public Fixture gear1, gear2;
	public void create() {
		if(thrust_sound == null) {
			thrust_sound = Gdx.audio.newSound(Gdx.files.internal("data/thrust.ogg"));
		}
		if(shoot_sound == null) {
			shoot_sound = Gdx.audio.newSound(Gdx.files.internal("data/shoot1.ogg"));
		}
		if(return_sound == null) {
			return_sound = Gdx.audio.newSound(Gdx.files.internal("data/return.ogg"));
		}
		if(pickup_sound == null) {
			pickup_sound = Gdx.audio.newSound(Gdx.files.internal("data/sound1.wav"));
		}
		BodyDef bdef = new BodyDef();
	    bdef.type = BodyDef.BodyType.DynamicBody;
	    
	    body = world.b2world.createBody(bdef);
	    //body.setLinearDamping(1);
	    CircleShape landinggear1 = new CircleShape();
	    landinggear1.setRadius(1);
	    landinggear1.setPosition(new Vector2(-9, -6));
	    CircleShape landinggear2 = new CircleShape();
	    landinggear2.setRadius(1);
	    landinggear2.setPosition(new Vector2(9, -6));
	    
	    PolygonShape s = new PolygonShape();
	    shape = s;
	    s.set(new Vector2[]{new Vector2(0, 10), new Vector2(-10, -5), new Vector2(10, -5)});
	    
	    fixture = body.createFixture(shape, 0.1f);
		fixture.setRestitution(0.5f);
		fixture.setUserData(this);

		gear1 = body.createFixture(landinggear1, 0.1f);
		gear1.setRestitution(0.5f);
		gear1.setUserData(this);
		gear2 = body.createFixture(landinggear2, 0.1f);
		gear2.setRestitution(0.5f);
		gear2.setUserData(this);
		
		if(mesh == null) {
			mesh = Util.createMesh(new Vector2[]{new Vector2(0, 10), new Vector2(-10, -5), 
					new Vector2(0, -3), 
					new Vector2(10, -5),new Vector2(0, 10)}, new Color(1,1,1,1), 3);
			mesh2 = Util.createMesh(new Vector2[]{new Vector2(0, 10), new Vector2(-10, -5), new Vector2(10, -5),new Vector2(0, 10)}, new Color(0.4f,0.4f,0.4f,1), 3);
			mesh_thrust = Util.createMesh(new Vector2[]{new Vector2(0, -5), new Vector2(0, -15),new Vector2(-5, -5), new Vector2(-5, -15),new Vector2(5, -5), new Vector2(5, -15)}, new Color[]{new Color(1,1,0,1),new Color(1,1,0,1),new Color(1,1,0,1),new Color(1,1,0,1),new Color(1,1,0,1),new Color(1,1,0,1)}, 3);
		}
			/*mesh = new Mesh(true, 3, 4, 
				new VertexAttribute(Usage.Position, 3, "a_position"), 
				new VertexAttribute(Usage.ColorPacked, 4,"a_color"));
		
		mesh.setVertices(new float[] {0f, 10f, 0, Color.toFloatBits(255, 0, 0, 255), -10, -5, 0,
				Color.toFloatBits(0, 255, 0, 255), 10, -5, 0, Color.toFloatBits(0, 0, 255, 255)});
		mesh.setIndices(new short[] {0, 1, 2, 0});*/
	  }

	  float shoot_time;
	  boolean thrust;
	  
	  public float control_direction;
	  public float control_thrust;
	  public boolean control_shoot;
	  
	  void tick(float dtime) {
		  if(body == null)
			  return;
		  
		  shoot_time+=dtime;
		  if(health <= 0 && countdown > 0) {
			  countdown -= dtime;
		  }
		  
		  
	    if(control_thrust > 0 && fuel > 0f && health > 0){
	    	thrust=true;
	      body.applyForceToCenter(body.getWorldVector(new Vector2(0,1000)));
	      fuel -= dtime;
	      if(play_thrust == -1) {
	    	  play_thrust = thrust_sound.loop();
	      }
	    }
	    else {
	    	thrust = false;

		      if(play_thrust != -1) {
		    	  thrust_sound.stop(play_thrust);
		    	  play_thrust = -1;
		      }
	    }
	    float omega = control_direction * 3f;
	    
	    if(fuel > 0 && health > 0) {
	    	float av = body.getAngularVelocity();
	    	body.applyTorque(Math.signum((omega - av)) * 8000);
	    }
	    
	    if(control_shoot && health > 0){
	    	if(shoot_time > 0.20f){
	    		shoot();
	    		shoot_time = 0;
	    	}
	    }
	    
	    Vector2 p1 = body.getWorldPoint(new Vector2(0, -5.5f)).cpy();
	    Vector2 p2 = body.getWorldPoint(new Vector2(0, -20f)).cpy();
	    
	    Actor a = Util.RayCastNearestActor(world, p1, p2, pickup);
	    if(!connected && a instanceof Pickup) {
	    	  connect((Pickup)a);
	    }
	    else if(connected) {
	    	if(a instanceof Home)
		    	disconnect();
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
		  def.localAnchorA.y = -5.5f;
		  def.collideConnected = true;
		  def.maxLength = 10;
		  def.type = JointDef.JointType.RopeJoint;
		  pickupjoint = world.b2world.createJoint(def);
		  connected = true;
		  pickup_sound.play();
	  }
	  int pickups = 0;
	  
	  void disconnect(){
		  world.b2world.destroyJoint(pickupjoint);
		  pickupjoint = null;
		  connected = false;
		  pickup.returned = true;
		  pickup = null;
		  pickups++;
		  return_sound.play();
	  }
	  
	  void shoot(){
		  Projectile p = new Projectile(world);
		  p.body.setTransform(body.getWorldPoint(new Vector2(0,15)), body.getAngle());
		  p.body.setLinearVelocity(body.getWorldVector(new Vector2(0,1000)));
		  shoot_sound.play();
	  }
	  void render(OrthographicCamera cam) {
		  if(body == null)
			  return;
		  
		  Vector2 pos = body.getPosition();
		  float rad = body.getAngle();

		  Matrix4 matrix = cam.combined.cpy();
		  matrix.translate(pos.x, pos.y, 0);
		  matrix.rotate(0, 0, 1, rad*180f/(float)Math.PI);
		  
	if(health > 0)
		Util.render(mesh, GL10.GL_TRIANGLES, matrix);
	else
		Util.render(mesh2, GL10.GL_TRIANGLES, matrix);
	if(thrust)
		Util.render(mesh_thrust, GL10.GL_TRIANGLES, matrix);
	  }
}

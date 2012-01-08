package cody.gravityshock;

import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;
import com.badlogic.gdx.physics.box2d.joints.RopeJointDef;
import com.badlogic.gdx.Gdx;
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
	
	
	
	static Mesh mesh;
	static Mesh mesh2;
	static Mesh mesh_thrust;
	
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
		
		if(mesh == null) {
			mesh = Util.createMesh(new Vector2[]{new Vector2(0, 10), new Vector2(-10, -5), new Vector2(10, -5),new Vector2(0, 10)}, new Color(1,1,1,1), 3);
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

package cody.gravityshock;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.RayCastCallback;

import cody.svg.Svg;

public class Turret extends Actor implements RayCastCallback {
	static Svg svg;
	
	public Turret(World _world) {
		super(_world);
	}
	public Turret() {
	}
	

	public void create() {
		if(svg == null)
			svg = new Svg("data/turret.svg");
		
		BodyDef bdef = new BodyDef();
	    bdef.type = BodyDef.BodyType.StaticBody;
	    
	    body = world.b2world.createBody(bdef);
	    
	    /*for(int i = 0; i < svg.pathCount();++i) {
	    	PolygonShape s = new PolygonShape();
	    	shape = s;
	    	s.set(svg.getPath(i).points);
	    
	    	fixture = body.createFixture(shape, 0.1f);
	    	fixture.setUserData(this);
	    }*/
	    
	    if(mesh == null) {
	    	mesh = Util.createMesh(svg, 3);
	    }
	  }
	  
	float shoot_time = 1;
	void target(Spaceship s) {
		Vector2 dir = s.body.getPosition().cpy();
		dir.sub(body.getPosition());
		dir.nor();
		dir.mul(20);
		Vector2 pos = body.getPosition().cpy();
		pos.add(dir);

		float angle = dir.cpy().nor().angle();
		System.out.println(angle);
		body.setTransform(body.getPosition(), angle);
		  Projectile p = new Projectile(world);
		  p.body.setTransform(body.getPosition(), angle);
		  p.body.setLinearVelocity(dir.mul(10));
	}
	
	
	Fixture rayfixture;
	Spaceship starget;
	float dist1;
	float dist2;
	public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
		Object obj = fixture.getUserData();
		boolean isthis = false;
		if(obj instanceof Turret) {
			isthis = ((Turret)obj) == this;
		}
		
		if(fixture == rayfixture) {
			dist1 = fraction;
			return fraction;
		}
		else if(isthis) {
			return 1;
		}
		dist2 = fraction;
		return fraction;
	}

	  void tick(float dtime) {
		  
		  shoot_time -=dtime;
		  if(shoot_time <= 0) {
		  for(Actor a : world.actors) {
			  if(a instanceof Spaceship) {
				  Spaceship s = (Spaceship)a;
				  if(s.health > 0) {
					  rayfixture = s.fixture;
					  starget = s;
					  dist1=dist2=0;
					  world.b2world.rayCast(this, body.getPosition(), s.body.getPosition());
					  if((dist1 < dist2 || dist2 == 0) && dist1 != 0) {
						  target(starget);
						  shoot_time = 1;
					  }
					  break;
				  }
			  }
		  }
		  }
	  }
	  
	  static Mesh mesh;

	  void render(OrthographicCamera cam) {
		  Vector2 pos = body.getPosition();
		  float rad = body.getAngle();

		  Matrix4 matrix = cam.combined.cpy();
		  matrix.translate(pos.x, pos.y, 0);
		  matrix.rotate(0, 0, 1, rad*180f/(float)Math.PI);
		  
		  Util.render(mesh, GL10.GL_TRIANGLES, matrix);
	  }
}

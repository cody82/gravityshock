package cody.gravityshock;

import java.util.ArrayList;

import cody.svg.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;

public class Enemy extends Actor{
	public boolean returned = false;
	ArrayList<Vector2> path;
	float path_pos = 0;
	float path_speed = 10;
	Vector2 turret;
	
	static Svg svg;
	
	public Enemy(World _world, ArrayList<Vector2> _path) {
		super(_world);
		path = _path;
	}
	public Enemy() {
	}

	public int health = 1000;
	public void damage(float impulse, boolean isgear) {
		if(health <= 0)
			return;
		
		float damage = Math.abs(impulse) * 0.06f * (isgear ? 0.05f : 1f);
		if(damage < 0.5f)
			return;
		health -= damage;
		
		if(health <= 0) {
			  Explosion x = new Explosion(world);
			  x.position = body.getPosition().cpy();
			  body.setType(BodyType.DynamicBody);
			  body.setAwake(true);
			  //world.actors.remove(this);
			  //dispose();
		}
	}
	@Override
	  public void onCollide(Object other, float impulse, Fixture thisfixture, Fixture otherfixture) {
		  super.onCollide(other, impulse, thisfixture, otherfixture);
		  
		  if(other instanceof Projectile) {
			  if(((Projectile)other).source != this)
			  	damage(impulse, false);
		  }
	  }
	public void create() {
		if(svg == null)
			svg = Assets.getSvg("data/enemy.svg");
	    if(mesh == null)
	    	mesh = Assets.getMesh("data/enemy.svg");
		
		BodyDef bdef = new BodyDef();
	    bdef.type = BodyDef.BodyType.StaticBody;
	    
	    body = world.b2world.createBody(bdef);
	    //shape = new box2d.CircleShape()
	    //shape.setRadius(10)
	    
	    for(int i = 0; i < svg.pathCount();++i) {
	    	PolygonShape s = new PolygonShape();
	    	shape = s;
	    	s.set(svg.getPath(i).points);
	    
	    	fixture = body.createFixture(shape, 0.1f);
	    	fixture.setUserData(this);
	    }
	    
	    
	    if(svg.textCount() > 0) {
	    	SvgText t = svg.getText(0);
	    	if(t.text.equals("turret")) {
		    	turret = t.position;
	    	}
	    }
	  }

	float shoot_time = 3;
	void target(Spaceship s) {
		Vector2 dir = s.body.getPosition().cpy();
		dir.sub(body.getWorldPoint(turret));
		dir.nor();
		dir.mul(20);
		Vector2 pos = body.getPosition().cpy();
		pos.add(dir);

		float angle = dir.cpy().nor().angle() /180f * (float)Math.PI;

		//body.setTransform(body.getPosition(), angle);
		Projectile p = new Projectile(world);
		p.source = this;
		p.body.setTransform(body.getWorldPoint(turret), angle);
		p.body.setLinearVelocity(dir.mul(10));
	}
	
	  void tick(float dtime) {
		  if(health <= 0)
			  return;
		  
		  if(turret != null) {
			  
		  shoot_time -=dtime;
		  if(shoot_time <= 0) {
		  for(Actor a : world.actors) {
			  if(a instanceof Spaceship) {
				  Spaceship s = (Spaceship)a;
				  if(s.health > 0) {
					  Actor s2 = Util.RayCastNearestActor(world, body.getWorldPoint(turret), s.body.getPosition());
					  
					  if(s2 == s) {
						  target(s);
						  shoot_time = 3;
					  }
					  break;
				  }
			  }
		  }
		  }

		  }
		  
		  
		  path_pos += path_speed * dtime;
		  float p = path_pos;
		  for(int i=0;i<path.size()-1;++i){
			  float dist = path.get(i+1).dst(path.get(i));
			  if(dist<p){
				  p-=dist;
			  }
			  else{
				  float a = p/dist;
				  Vector2 pos = path.get(i).lerp(path.get(i+1), a);
				  body.setTransform(pos, 0);
				  return;
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
		  
	Util.render(mesh, GL10.GL_TRIANGLES, matrix, health > 0 ? 1f : 0.6f);
	  }
}

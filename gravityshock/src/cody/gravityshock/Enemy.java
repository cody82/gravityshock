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
import com.badlogic.gdx.physics.box2d.PolygonShape;

public class Enemy extends Actor{
	public boolean returned = false;
	ArrayList<Vector2> path;
	float path_pos = 0;
	float path_speed = 10;
	
	static Svg svg;
	
	public Enemy(World _world, ArrayList<Vector2> _path) {
		super(_world);
		path = _path;
	}
	public Enemy() {
	}
	
	public void create() {
		if(svg == null)
			svg = new Svg("data/enemy.svg");
		
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
	    }
	    
	    if(mesh == null) {
	    	mesh = Util.createMesh(svg, 3);
	    }
	  }
	  
	  void tick(float dtime) {
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

	cam.apply(Gdx.graphics.getGL10());
	Gdx.graphics.getGL10().glTranslatef(pos.x, pos.y, 0);
	Gdx.graphics.getGL10().glRotatef(rad*180f/(float)Math.PI, 0, 0, 1);
		  
		  mesh.render(GL10.GL_TRIANGLES);
	  }
}

package cody.gravityshock;

import java.util.ArrayList;

import cody.svg.*;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

public class Enemy extends Actor{
	public boolean returned = false;
	ArrayList<Vector2> path;
	float path_pos = 0;
	float path_speed = 10;
	
	Svg svg;
	
	public Enemy(World _world, ArrayList<Vector2> _path) {
		super(_world);
		path = _path;
	}
	public Enemy() {
	}
	
	public void create() {
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
	  
	  void render(OrthographicCamera cam) {
	        
		  ShapeRenderer sr = new ShapeRenderer();
	    sr.setProjectionMatrix(cam.combined);
	    
	    sr.begin(ShapeType.Line);
	    
	    Vector2 pos = body.getPosition();
	    float rad = body.getAngle();
	   
	    sr.translate(pos.x, pos.y, 0);
	    sr.rotate(0, 0, 1, rad*180f/(float)Math.PI);
	    for(int j=0;j<svg.pathCount();++j) {
	    	Vector2[] array = svg.getPath(j).points;
	    	Color c = svg.getPath(j).color;
	    	sr.setColor(c.r, c.g, c.b, c.a);
	    	for(int i =0; i < array.length - 1; ++i) {
	    		sr.line(array[i].x, array[i].y, array[i+1].x, array[i+1].y);
	    	}
	    	sr.line(array[array.length-1].x, array[array.length-1].y, array[0].x, array[0].y);
	    }
	    sr.end();
	    sr.dispose();
	  }
}

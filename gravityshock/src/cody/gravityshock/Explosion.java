package cody.gravityshock;

import cody.svg.Svg;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;

public class Explosion extends Actor{
	public Explosion(World _world) {
		super(_world);
		create();
	}
	
	public Explosion() {
		create();
	}

	static Svg svg;
	
	public Vector2 position;
	
	float age;
	
	public void create() {
		if(svg == null)
			svg = new Svg("data/explosion1.svg");
	}
	  
	  void tick(float dtime) {
		  age += dtime;
		  if(age>3f){
			  world.actors.remove(this);
			  dispose();
		  }
	  }
	  
	  void render(OrthographicCamera cam) {
		  ShapeRenderer sr = new ShapeRenderer();
		    sr.setProjectionMatrix(cam.combined);
		    
		    sr.begin(ShapeType.Line);
		    
		    Vector2 pos = position;
		    //float rad = body.getAngle();
		   
		    float scale = (float)Math.sqrt((double)age) * 0.8f;
		    sr.translate(pos.x, pos.y, 0);
		    //sr.rotate(0, 0, 1, rad*180f/(float)Math.PI);
		    sr.scale(scale, scale, 0);
		    
		    for(int j=0;j<svg.pathCount();++j) {
		    	Vector2[] array = svg.getPath(j).points;
		    	Color c = svg.getPath(j).color;
		    	c.a = (3f - age) / 3f;
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

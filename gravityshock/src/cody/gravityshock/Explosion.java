package cody.gravityshock;

import cody.svg.Svg;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Matrix4;
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
		    float scale = (float)Math.sqrt((double)age) * 0.8f;
		  Vector2 pos = this.position;
		  
cam.apply(Gdx.graphics.getGL10());
Gdx.graphics.getGL10().glTranslatef(pos.x, pos.y, 0);
//Gdx.graphics.getGL10().glRotatef(rad*180f/(float)Math.PI, 0, 0, 1);
Gdx.graphics.getGL10().glScalef(scale, scale, 0);

		    
		    for(int j=0;j<svg.pathCount();++j) {
		    	Vector2[] array = svg.getPath(j).points;
		    	Color c = svg.getPath(j).color;
		    	c.a = (3f - age) / 3f;
		    	for(int i =0; i < array.length - 1; ++i) {
					  LineDrawer.DrawLine(array[i], array[i+1], 8, c);
		    	}
				  LineDrawer.DrawLine(array[array.length-1], array[0], 5, c);
		    }
	  }
}

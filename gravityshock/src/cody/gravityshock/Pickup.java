package cody.gravityshock;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

public class Pickup extends Actor{
	public boolean returned = false;
	
	public Pickup(World _world) {
		super(_world);
	}
	public Pickup() {
	}
	
	public void create() {
		BodyDef bdef = new BodyDef();
	    bdef.type = BodyDef.BodyType.DynamicBody;
	    
	    body = world.b2world.createBody(bdef);
	    //shape = new box2d.CircleShape()
	    //shape.setRadius(10)
	    PolygonShape s = new PolygonShape();
	    shape = s;
	    s.set(new Vector2[]{new Vector2(-5, 5), new Vector2(-5, -5), new Vector2(5, -5), new Vector2(5, 5)});
	    
	    fixture = body.createFixture(shape, 0.1f);
		fixture.setRestitution(0.3f);
	  }
	  
	  void tick(float dtime) {
	  }


	  void render(OrthographicCamera cam) {

		  Vector2 pos = body.getPosition();
		  float rad = body.getAngle();
		  
cam.apply(Gdx.graphics.getGL10());
Gdx.graphics.getGL10().glTranslatef(pos.x, pos.y, 0);
Gdx.graphics.getGL10().glRotatef(rad*180f/(float)Math.PI, 0, 0, 1);

	    Vector2[] array = new Vector2[]{new Vector2(-5, 5), new Vector2(-5, -5), new Vector2(5, -5), new Vector2(5, 5)};

	    Color c;
	    if(returned)
	    	c = new Color(0, 1, 0, 1);
	    else
	    	c = new Color(0, 0, 1, 1);
	    	
	    for(int i =0; i < array.length - 1; ++i) {
			  LineDrawer.DrawLine(array[i], array[i+1], 3, c);
	    }
		  LineDrawer.DrawLine(array[array.length-1], array[0], 3, c);
	  }
}

package cody.gravityshock;

import java.io.IOException;
import java.util.ArrayList;

import org.w3c.dom.NodeList;

import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.IndexData;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.VertexData;
import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.graphics.OrthographicCamera;


class Image {
	public float x;
	public float y;
	public Texture texture;
	public float width;
	public float height;
}

public class Map {
	World world;
	ArrayList<ChainShape> shapes;
	ArrayList<Color> colors;
	Body body;
	ArrayList<Fixture> fixtures;
	ArrayList<ArrayList<Vector2>> points;
	ArrayList<Pickup> pickups;
	float age;
	ArrayList<Mesh> meshes;
	
	ArrayList<Image> images;
	
	public void dispose() {
		for(ChainShape shape : shapes) {
			shape.dispose();
		}
		world.b2world.destroyBody(body);
		for(Pickup p : pickups) {
			p.dispose();
		}
		for(Mesh m : meshes) {
			m.dispose();
		}
		for(Image i : images) {
			i.texture.dispose();
		}
	}
	public void load(World _world, String filename) {
			    world = _world;
			    world.map = this;
			    create(filename);
	}
	
	void create(String filename) {
		points = new ArrayList<ArrayList<Vector2>>();
		fixtures = new ArrayList<Fixture>();
		shapes = new ArrayList<ChainShape>();
		colors = new ArrayList<Color>();
		pickups = new ArrayList<Pickup>();
		meshes = new ArrayList<Mesh>();
		images = new ArrayList<Image>();
		
		BodyDef bdef = new BodyDef();
		bdef.type = BodyDef.BodyType.StaticBody;
		body = world.b2world.createBody(bdef);
		
		FileHandle fh = Gdx.files.internal(filename);
		java.io.InputStream stream = fh.read();
		
		XmlReader reader = new XmlReader();
		 
		 Element doc;
		try {
			doc = reader.parse(stream);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		 
		Array<Element> list = doc.getChildrenByNameRecursively("path");
		
		for(int i=0;i<list.size;++i){
			Element n=list.get(i);
			String d = n.getAttribute("d");
			String style = n.getAttribute("style");
			
			String[] split = d.split(" ");
			char cmd = ' ';
			Vector2 last = new Vector2(0,0);
			ArrayList<Vector2> array = new ArrayList<Vector2>();
			Color color = new Color();
			color.a = 1;
			
			String type = "";

			Element type_element = n.getChildByName("title");
			if(type_element != null)
				type = type_element.getText();

			String desc = "";
			Element desc_element = n.getChildByName("desc");
			if(desc_element != null)
				desc = desc_element.getText();
			
			int stroke = style.indexOf("stroke:");
			if(stroke >= 0){
				String c = style.substring(stroke + 8, stroke + 8 + 6);
				int r = Integer.parseInt(c.substring(0, 2), 16);
				int g = Integer.parseInt(c.substring(2, 4), 16);
				int b = Integer.parseInt(c.substring(4, 6), 16);
				color.r = ((float)r)/255f;
				color.g = ((float)g)/255f;
				color.b = ((float)b)/255f;
			}
			
			for(String s : split){
				if(s.length()<=1){
					cmd = s.charAt(0);
				}
				else {
					String[] v = s.split(",");
					Vector2 v2 = new Vector2(Float.parseFloat(v[0]),-Float.parseFloat(v[1]));
					if(Character.isLowerCase(cmd)){
						v2 = v2.add(last);
					}
					last = v2;
					array.add(v2);
				}
			}
			
			if(type.equals("enemy")){
				Enemy enemy = new Enemy(world, array);
				enemy.body.setTransform(array.get(0).x, array.get(0).y, 0);
			}
			else if(type.equals("home")) {
				ChainShape shape = new ChainShape();
				shape.createLoop(array.toArray(new Vector2[0]));
				//shapes.add(shape);
				Fixture fixture = body.createFixture(shape, 1);
				fixture.setRestitution(0.2f);
				Home home = new Home(world,Util.createMesh(array.toArray(new Vector2[]{}), color, 3, true), fixture);
			}
			else if(type.equals("Actor")) {
		    	PolygonShape shape = new PolygonShape();
		    	shape.set(array.toArray(new Vector2[]{}));
				//shapes.add(shape);
				BodyDef def = new BodyDef();
				def.type = BodyDef.BodyType.DynamicBody;
				Body b = world.b2world.createBody(def);
				Fixture fixture = b.createFixture(shape, 1);
				fixture.setRestitution(0.2f);
				Actor actor = new Actor(world,Util.createMesh(array.toArray(new Vector2[]{}), color, 3, true), fixture, b);				
			}
			else if(type.equals("Fuel")) {
				ChainShape shape = new ChainShape();
				shape.createLoop(array.toArray(new Vector2[0]));
				//shapes.add(shape);
				Fixture fixture = body.createFixture(shape, 1);
				fixture.setRestitution(0.2f);
				Fuel fuel = new Fuel(world,Util.createMesh(array.toArray(new Vector2[]{}), color, 3, true), fixture);			
			}
			else {
				colors.add(color);
				points.add(array);
				meshes.add(Util.createMesh(array.toArray(new Vector2[]{}), color, 3, true));
			
				if(!desc.contains("nocollision")) {
					ChainShape shape = new ChainShape();
					shape.createLoop(array.toArray(new Vector2[0]));
					shapes.add(shape);
					Fixture fixture = body.createFixture(shape, 1);
					fixture.setRestitution(0.2f);
					if(!desc.contains("landingzone"))
						fixture.setUserData(this);
					fixtures.add(fixture);
				}
			}
		}
		
		list = doc.getChildrenByNameRecursively("text");
		for(int i=0;i<list.size;++i){
			Element n=list.get(i);
			float x = Float.parseFloat(n.getAttribute("x"));
			float y = Float.parseFloat(n.getAttribute("y"));
			String text = null;
			String desc = "";
			Element desc_element = n.getChildByName("desc");
			if(desc_element != null)
				desc = desc_element.getText();

			Element tspan_element = n.getChildByName("tspan");
			if(tspan_element != null)
				text = tspan_element.getText();
			
			
			if(text.equals("pickup")){
				Pickup pickup = new Pickup(world);
				pickup.body.setTransform(x, -y, 0);
				pickups.add(pickup);
			}
			else if(text.equals("home")){
				Home home = new Home(world);
				home.body.setTransform(x, -y, 0);
			}
			else if(text.equals("turret")){
				Turret t = new Turret(world);
				t.body.setTransform(x, -y, 0);
			}
			else if(text.equals("gravity")){
				String[] properties = desc.split("[ =]");
				float strength = 10000000;
				for(int j=0;j<properties.length; ++j) {
					if(properties[j].equals("strength")) {
						strength = Float.parseFloat(properties[j+1]);
					}
				}
				Gravity g = new Gravity(world, new Vector2(x, -y), strength);
			}
		}

		list = doc.getChildrenByNameRecursively("image");
		for(int i=0;i<list.size;++i){
			Element n=list.get(i);
			float x = Float.parseFloat(n.getAttribute("x"));
			float y = Float.parseFloat(n.getAttribute("y"));
			float width = Float.parseFloat(n.getAttribute("width"));
			float height = Float.parseFloat(n.getAttribute("height"));
			String file = n.getAttribute("xlink:href");
			Image img = new Image();
			img.x = x;
			img.y = -y - height;
			img.width = width;
			img.height = height;
			img.texture = new Texture(Gdx.files.internal("data" + file.substring(file.lastIndexOf("/"))));
			//img.texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
			images.add(img);
		}
		
		list = doc.getChildrenByNameRecursively("dc:description");
		for(int i=0;i<list.size;++i){
			Element n=list.get(i);
			String text = n.getText();
			if(text.contains("gravity=0")){
				world.b2world.setGravity(new Vector2(0,0));
			}
		}
		
	}
	
	public int getGoalScore() {
		return pickups.size();
	}
	
	public void tick(float dtime) {
		age += dtime;
	}

	static SpriteBatch sb = new SpriteBatch();
	public void render(OrthographicCamera cam) {

		  Matrix4 matrix = cam.combined.cpy();

		  sb.setProjectionMatrix(matrix);
		  sb.begin();
		  for(Image i : images) {
			  sb.draw(i.texture, i.x, i.y, i.width, i.height);
		  }
		  sb.end();
		  Main.blend(true);
		//cam.apply(Gdx.graphics.getGL10());
		for(Mesh m : meshes) {
			Util.render(m, GL10.GL_TRIANGLES, matrix);
		}
	}
}

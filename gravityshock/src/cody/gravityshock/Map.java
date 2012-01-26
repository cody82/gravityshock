package cody.gravityshock;

import java.io.IOException;
import java.util.ArrayList;

import com.badlogic.gdx.physics.box2d.*;
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

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

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
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		try {
			dBuilder = dbFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		Document doc;
		try {
			doc = dBuilder.parse(stream);
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		doc.getDocumentElement().normalize();
		
		NodeList list = doc.getDocumentElement().getElementsByTagName("path");
		for(int i=0;i<list.getLength();++i){
			Node n=list.item(i);
			String d = n.getAttributes().getNamedItem("d").getTextContent();
			String style = n.getAttributes().getNamedItem("style").getTextContent();
			
			String[] split = d.split(" ");
			char cmd = ' ';
			Vector2 last = new Vector2(0,0);
			ArrayList<Vector2> array = new ArrayList<Vector2>();
			Color color = new Color();
			color.a = 1;
			
			String type = "";
			NodeList children = n.getChildNodes();
			for(int k=0;k<children.getLength();++k){
				Node n2 = children.item(k);
				if(n2.getNodeName().equals("title")){
					type = n2.getTextContent();
					break;
				}
			}
			
			String desc = "";
			for(int k=0;k<children.getLength();++k){
				Node n2 = children.item(k);
				if(n2.getNodeName().equals("desc")){
					desc = n2.getTextContent();
					break;
				}
			}
			
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
		

		list = doc.getDocumentElement().getElementsByTagName("tspan");
		for(int i=0;i<list.getLength();++i){
			Node n=list.item(i);
			float x = Float.parseFloat(n.getAttributes().getNamedItem("x").getTextContent());
			float y = Float.parseFloat(n.getAttributes().getNamedItem("y").getTextContent());
			String text = n.getTextContent();
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
		}
		
		list = doc.getDocumentElement().getElementsByTagName("image");
		for(int i=0;i<list.getLength();++i){
			Node n=list.item(i);
			float x = Float.parseFloat(n.getAttributes().getNamedItem("x").getTextContent());
			float y = Float.parseFloat(n.getAttributes().getNamedItem("y").getTextContent());
			float width = Float.parseFloat(n.getAttributes().getNamedItem("width").getTextContent());
			float height = Float.parseFloat(n.getAttributes().getNamedItem("height").getTextContent());
			String file = n.getAttributes().getNamedItem("xlink:href").getTextContent();
			Image img = new Image();
			img.x = x;
			img.y = -y - height;
			img.width = width;
			img.height = height;
			img.texture = new Texture(Gdx.files.internal("data" + file.substring(file.lastIndexOf("/"))));
			//img.texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
			images.add(img);
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

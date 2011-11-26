package cody.gravityshock;

import java.io.IOException;
import java.util.ArrayList;

import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.*;
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

public class Map {
	World world;
	ArrayList<ChainShape> shapes;
	Body body;
	ArrayList<Fixture> fixtures;
	ArrayList<ArrayList<Vector2>> points;
	Mesh mesh;
	ShaderProgram shader;
			  
	public void load(World _world, String filename) {
			    world = _world;
			    world.map = this;
			    create(filename);
	}
	
	void create(String filename) {
		points = new ArrayList<ArrayList<Vector2>>();
		fixtures = new ArrayList<Fixture>();
		shapes = new ArrayList<ChainShape>();
		
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
			String[] split = d.split(" ");
			char cmd = ' ';
			Vector2 last = new Vector2(0,0);
			ArrayList<Vector2> array = new ArrayList<Vector2>();
			
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
			
			points.add(array);
			ChainShape shape = new ChainShape();
			shape.createLoop(array.toArray(new Vector2[0]));
			shapes.add(shape);
			Fixture fixture = body.createFixture(shape, 1);
			fixtures.add(fixture);
		}
	}
	
	public void render(OrthographicCamera cam) {
	    //mesh.render(shader, graphics.GL20.GL_LINE_STRIP)
	    //var gl = Gdx.graphics.getGL10()
		ShapeRenderer sr = new ShapeRenderer();
	    sr.setProjectionMatrix(cam.combined);
	    
	      sr.begin(ShapeType.Line);
	    
	      sr.setColor(1, 1, 0, 1);
	    for(ArrayList<Vector2> array : points) {
	      for(int i = 0; i < array.size() - 1; ++i) {
	        sr.line(array.get(i).x, array.get(i).y, array.get(i+1).x, array.get(i+1).y);
	      }
	      sr.line(array.get(array.size()-1).x, array.get(array.size()-1).y, array.get(0).x, array.get(0).y);
	    }
	      sr.end();
		
	}
}

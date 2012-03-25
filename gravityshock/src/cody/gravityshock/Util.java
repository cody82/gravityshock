package cody.gravityshock;

import java.util.ArrayList;
import java.util.List;


import cody.svg.Svg;
import cody.svg.SvgPath;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.RayCastCallback;

class RayCaster implements RayCastCallback {
	public Fixture nearest;
	float dist = Float.MAX_VALUE;
	@Override
	public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
		Object obj = fixture.getUserData();

		if(fraction < dist){
			nearest = fixture;
			dist = fraction;
		}
		return fraction;
	}
	
}

class ActorRayCaster implements RayCastCallback {
	public ActorRayCaster(Actor _except, Class c) {
		except = _except;
		_class = c;
	}
	Class _class;
	Actor except;
	public Actor nearest;
	float dist = Float.MAX_VALUE;
	@Override
	public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
		Object obj = fixture.getUserData();
		if(obj instanceof Actor && fraction < dist /*&& _class.isAssignableFrom(obj.getClass())*/ && ((Actor)obj) != except){
			nearest = (Actor)obj;
			dist = fraction;
		}
		else {
			return fraction;
		}
		return fraction;
	}
	
}

public class Util {
	static Music music;
	public static void playMusic() {
		/*
		if(music == null) {
				music = Gdx.audio.newMusic(Gdx.files.internal("data/playin_old_games.ogg"));
		}
		

		try {
			if(!music.isPlaying())
				music.play();
		}
		catch(NullPointerException e) {
			music = Gdx.audio.newMusic(Gdx.files.internal("data/playin_old_games.ogg"));
			music.play();
		}*/
	}
    
	public static void stopMusic() {
		/*
		if(music != null) {
			music.stop();
		}*/
	}
	public static Fixture RayCastNearestFixture(World world, Vector2 from, Vector2 to) {
		RayCaster caster = new RayCaster();
		world.b2world.rayCast(caster, from, to);
		return caster.nearest;
	}

	public static Actor RayCastNearestActor(World world, Vector2 from, Vector2 to) {
		ActorRayCaster caster = new ActorRayCaster(null, Actor.class);
		world.b2world.rayCast(caster, from, to);
		return caster.nearest;
	}

	public static Actor RayCastNearestActor(World world, Vector2 from, Vector2 to, Actor except) {
		ActorRayCaster caster = new ActorRayCaster(except, Actor.class);
		world.b2world.rayCast(caster, from, to);
		return caster.nearest;
	}
	public static Actor RayCastNearestActor(World world, Vector2 from, Vector2 to, Actor except, Class c) {
		ActorRayCaster caster = new ActorRayCaster(except,c);
		world.b2world.rayCast(caster, from, to);
		return caster.nearest;
	}
	
	public static float[] createVertices(Vector2 from, Vector2 to, float thickness, Color color) {
		Vector2 dir = to.cpy();
		dir.sub(from);
		float dist = dir.len();
		dir.mul(1f/dist);
		Vector2 ortho = new Vector2(-dir.y, dir.x).mul(thickness);
		Vector2 from_left = from.cpy();
		from_left.add(ortho);
		Vector2 from_right = from.cpy();
		from_right.sub(ortho);
		Vector2 to_left = to.cpy();
		to_left.add(ortho);
		Vector2 to_right = to.cpy();
		to_right.sub(ortho);

		Color c2 = new Color(color);
		c2.a = 0;
		
		float color1 = color.toFloatBits();
		float color2 = c2.toFloatBits();
		
		return new float[]{
				from.x, from.y, 0, color1,
				from_left.x, from_left.y, 0, color2,
				to.x, to.y, 0, color1,
				from_left.x, from_left.y, 0, color2,
				to_left.x, to_left.y, 0, color2,
				to.x, to.y, 0, color1,

				from.x, from.y, 0, color1,
				from_right.x, from_right.y, 0, color2,
				to.x, to.y, 0, color1,
				from_right.x, from_right.y, 0, color2,
				to_right.x, to_right.y, 0, color2,
				to.x, to.y, 0, color1,
		};
		
	}
	public static Mesh createMesh(Svg svg, float thickness) {
		ArrayList<Vector2> points = new ArrayList<Vector2>();
		ArrayList<Color> colors = new ArrayList<Color>();
		

	    for(int i = 0; i < svg.pathCount();++i) {
	    	SvgPath svgpath = svg.getPath(i);

	    	for(int j=0;j<svgpath.points.length - 1;++j) {
	    		points.add(svgpath.points[j]);
	    		points.add(svgpath.points[j + 1]);
	    		colors.add(svgpath.color);
	    		colors.add(svgpath.color);
	    	}
	    	
    		points.add(svgpath.points[svgpath.points.length - 1]);
    		points.add(svgpath.points[0]);
    		colors.add(svgpath.color);
    		colors.add(svgpath.color);
	    }
		return createMesh(points.toArray(new Vector2[]{}), colors.toArray(new Color[]{}), thickness);
	}
	
	public static Mesh createMesh(Vector2[] points, Color[] colors, float thickness) {
		int numverts = points.length * 12;
		float[] verts = new float[numverts * 4];
		
		for(int i=0;i<points.length-1;i+=2) {
			float[] line = createVertices(points[i], points[i+1], thickness, colors[i]);
			for(int j=0;j<line.length;++j) {
				verts[i * 12 * 4 + j] = line[j];
			}
		}
		
		short[] indices = new short[numverts];
		for(int i=0;i<numverts;++i) {
			indices[i] = (short)i;
		}

		Mesh mesh = new Mesh(true, numverts, numverts, 
				new VertexAttribute(Usage.Position, 3, "a_position"), 
				new VertexAttribute(Usage.ColorPacked, 4,"a_color"));
		
		mesh.setVertices(verts);
		mesh.setIndices(indices);
		
		return mesh;
	}
	
	public static Mesh createMesh(Vector2[] path, Color color, float thickness) {
		return createMesh(path,color,thickness, false);
	}
	public static Mesh createMesh(Vector2[] path, Color color, float thickness, boolean loop) {
		if(loop) {
			//HACK: Slow
			Vector2[] newpath = new Vector2[path.length + 1];
			for(int i=0;i<path.length;++i) {
				newpath[i] = path[i];
			}
			newpath[path.length] = path[0];
			path = newpath;
		}
		
		int numverts = (path.length - 1) * 12;
		
		float[] verts = new float[numverts * 4];
		for(int i=0;i<path.length-1;++i) {
			float[] line = createVertices(path[i], path[i+1], thickness, color);
			for(int j=0;j<line.length;++j) {
				verts[i * 12 * 4 + j] = line[j];
			}
		}
		short[] indices = new short[numverts];
		for(int i=0;i<numverts;++i) {
			indices[i] = (short)i;
		}

		Mesh mesh = new Mesh(true, numverts, numverts, 
				new VertexAttribute(Usage.Position, 3, "a_position"), 
				new VertexAttribute(Usage.ColorPacked, 4,"a_color"));
		
		mesh.setVertices(verts);
		mesh.setIndices(indices);
		
		return mesh;
	}
	
	public static ShaderProgram getStandardShader() {
		GL20 gl = Gdx.graphics.getGL20();
		if(standardShader != null){
			return standardShader;
		}
		standardShader = new ShaderProgram(standardVertexShader, standardFragmentShader);
		
		return standardShader;
	}

	public static ShaderProgram getEtc1Shader() {
		GL20 gl = Gdx.graphics.getGL20();
		if(etc1Shader != null){
			return etc1Shader;
		}
		etc1Shader = new ShaderProgram(etc1VertexShader, etc1FragmentShader);
		
		return etc1Shader;
	}
	
	public static void render(Mesh mesh, int primitiveType, Matrix4 projModelView) {
		render(mesh, primitiveType, projModelView, 1f);
	}
	
	public static void render(Mesh mesh, int primitiveType, Matrix4 projModelView, float alpha) {
		if(Gdx.graphics.isGL20Available()) {
			ShaderProgram shader = getStandardShader();
			shader.begin();
			shader.setUniformf("u_alpha", alpha);
			shader.setUniformMatrix("u_projModelView", projModelView);
			mesh.render(shader, primitiveType);
			shader.end();
		}
		else {
			/*GL10 gl = Gdx.graphics.getGL10();
			gl.glMatrixMode(GL10.GL_PROJECTION);
			gl.glLoadIdentity();
			gl.glMatrixMode(GL10.GL_MODELVIEW);
			gl.glLoadMatrixf(projModelView.getValues(),0);
			mesh.render(primitiveType);*/
		}
	}

	static Texture background;
	static SpriteBatch batch = new SpriteBatch();
	static float x2 = 0;
	public static void drawMenuBackground(float dtime) {
        float window_width = Gdx.graphics.getWidth();
        float window_height = Gdx.graphics.getHeight();
        float y2 = 0;
        x2 += dtime;

        if(background == null) {
            background = Assets.getTexture("data/space2.png");
            background.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
        }
        
        batch.begin();
 		batch.enableBlending();
 		batch.setBlendFunction(GL20.GL_BLEND_SRC_ALPHA, GL20.GL_ONE);
        batch.draw(background, 0, 0, window_width, window_height, x2, y2, x2 + (window_width / 256), y2 +  + (window_height / 256));
        
        batch.draw(background, 0, 0, window_width, window_height, x2 * 2, 0.3f + y2 * 2, x2 * 2 + (window_width / 256), 0.3f + y2 * 2 + (window_height / 256));
		batch.end();
	}
	
	
	static ShaderProgram standardShader;
	static ShaderProgram etc1Shader;
	
	static final String standardVertexShader = "attribute vec4 a_position; \n" +
			"attribute vec4 a_color; \n" +
			"uniform mat4 u_projModelView; \n" + 
			"varying vec4 v_color; \n" + 
			"void main() \n" +
			"{ \n" +
			" gl_Position = u_projModelView * a_position; \n" +
			" v_color = a_color; \n" +
			"} \n";

	static final String standardFragmentShader = "#ifdef GL_ES\n" +
			"precision mediump float;\n" +
			"#endif\n" +
			"uniform float u_alpha; \n" + 
			"varying vec4 v_color; \n" + 
			"void main() \n" +
			"{ \n" +
			" gl_FragColor = v_color * u_alpha;\n" +
			"} \n";
	
	static final String etc1VertexShader = "attribute vec4 a_position; \n" +
			"attribute vec4 a_texture; \n" + 
			"uniform mat4 u_projModelView; \n" + 
			"varying vec4 v_texture; \n" + 
			"void main() \n" +
			"{ \n" +
			" gl_Position = u_projModelView * a_position; \n" +
			" v_texture = a_texture; \n" +
			"} \n";
	
	static final String etc1FragmentShader = "#ifdef GL_ES\n" +
			"precision mediump float;\n" +
			"#endif\n" +
			"uniform sampler2D u_colortexture; \n" +
			"uniform sampler2D u_alphatexture; \n" +
			"varying vec4 v_texture; \n" + 
			"void main() \n" +
			"{ \n" +
			" gl_FragColor = vec4(texture2D(u_colortexture,v_texture).xyz, texture2D(u_alphatexture,v_texture).x);\n" +
			"} \n";
}

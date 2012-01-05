package cody.gravityshock;

import java.util.ArrayList;
import java.util.List;


import cody.svg.Svg;
import cody.svg.SvgPath;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.PolygonShape;

public class Util {

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
	
	public static void render(Mesh mesh, int primitiveType, Matrix4 projModelView) {
		if(Gdx.graphics.isGL20Available()) {
			ShaderProgram shader = getStandardShader();
			shader.begin();
			String[] uniforms = shader.getUniforms();
			String[] attributes = shader.getAttributes();
			shader.setUniformMatrix("u_projModelView", projModelView);
			mesh.render(shader, primitiveType);
			shader.end();
		}
		else {
			GL10 gl = Gdx.graphics.getGL10();
			gl.glMatrixMode(GL10.GL_PROJECTION);
			gl.glLoadIdentity();
			gl.glMatrixMode(GL10.GL_MODELVIEW);
			gl.glLoadMatrixf(projModelView.getValues(),0);
			mesh.render(primitiveType);
		}
	}

	static ShaderProgram standardShader;
	
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
			"varying vec4 v_color; \n" + 
			"void main() \n" +
			"{ \n" +
			" gl_FragColor = v_color;\n" +
			"} \n";
}

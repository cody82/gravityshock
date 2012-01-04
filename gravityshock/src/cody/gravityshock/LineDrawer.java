package cody.gravityshock;



import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GL11;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.VertexBufferObject;
import com.badlogic.gdx.math.Vector2;

public class LineDrawer {
	public static void DrawLine(Vector2 from, Vector2 to, float thickness) {
		Vector2 dir = to.sub(to);
		float dist = dir.len();
		dir = dir.mul(1f/dist);
		Vector2 ortho = new Vector2(dir.y, dir.x).mul(thickness);
		Vector2 from_left = from.add(ortho);
		Vector2 from_right = from.sub(ortho);
		Vector2 to_left = to.add(ortho);
		Vector2 to_right = to.sub(ortho);


		VertexAttribute attrib1 = new VertexAttribute(VertexAttributes.Usage.Position, 2, "a_Position");
		//VertexAttribute attrib2 = new VertexAttribute(VertexAttributes.Usage.Color, 2, "color");
		VertexBufferObject vbo = new VertexBufferObject(true, 4, attrib1);
		vbo.setVertices(new float[]{from.x, from.y, from_left.x, from_left.y, to_left.x, to_left.y, to.x, to.y}, 0, 8);
		GL11 gl = Gdx.graphics.getGL11();

        //gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        //gl.glMatrixMode(GL.GL_MODELVIEW);
        //gl.glLoadIdentity();
		gl.glColor4f(1, 1, 1, 1);
		vbo.bind();
		gl.glDrawArrays(GL11.GL_TRIANGLE_FAN, 0, 3);

		vbo.unbind();
		vbo.dispose();
	}
}

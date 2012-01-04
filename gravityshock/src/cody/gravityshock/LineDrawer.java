package cody.gravityshock;



import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GL11;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.glutils.ImmediateModeRenderer10;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.VertexBufferObject;
import com.badlogic.gdx.math.Vector2;

public class LineDrawer {
	public static void DrawLine(Vector2 from, Vector2 to, float thickness, Color color) {
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

		Gdx.graphics.getGL10().glEnable(GL10.GL_BLEND);
		
		ImmediateModeRenderer10 renderer = new ImmediateModeRenderer10();
		renderer.begin(GL11.GL_TRIANGLE_STRIP);
		renderer.color(color.r, color.g, color.b, color.a);
		renderer.vertex(from.x, from.y, 0);
		renderer.color(color.r, color.g, color.b, 0);
		renderer.vertex(from_left.x, from_left.y, 0);
		renderer.color(color.r, color.g, color.b, color.a);
		renderer.vertex(to.x, to.y, 0);
		renderer.color(color.r, color.g, color.b, 0);
		renderer.vertex(to_left.x, to_left.y, 0);

		renderer.end();
		renderer.begin(GL11.GL_TRIANGLE_STRIP);

		renderer.color(color.r, color.g, color.b, color.a);
		renderer.vertex(from.x, from.y, 0);
		renderer.color(color.r, color.g, color.b, 0);
		renderer.vertex(from_right.x, from_right.y, 0);
		renderer.color(color.r, color.g, color.b, color.a);
		renderer.vertex(to.x, to.y, 0);
		renderer.color(color.r, color.g, color.b, 0);
		renderer.vertex(to_right.x, to_right.y, 0);
		
		renderer.end();
		renderer.dispose();
	}
}

package cody.svg;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

public class SvgPath {
	public SvgPath(Vector2[] p, Color c) {
		points = p;
		color = c;
	}
	public Vector2[] points;
	public Color color;
}

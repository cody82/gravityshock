package cody.gravityshock;


import java.util.HashMap;

import cody.svg.Svg;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;

public class Assets {
	
	static HashMap<String, Texture> textures = new HashMap<String, Texture>();
	static HashMap<String, Mesh> meshes = new HashMap<String, Mesh>();
	static HashMap<String, Svg> svgs = new HashMap<String, Svg>();
	static HashMap<String, Sound> sounds = new HashMap<String, Sound>();
	
	public static Texture getTexture(String name) {
		if(textures.containsKey(name)) {
			return textures.get(name);
		}
		else {
			Texture t = new Texture(Gdx.files.internal(name));
			textures.put(name, t);
			return t;
		}
	}
	
	public static Mesh getMesh(String name) {
		if(meshes.containsKey(name)) {
			return meshes.get(name);
		}
		else {
			Mesh t = Util.createMesh(getSvg(name), 3);
			meshes.put(name, t);
			return t;
		}
	}
	
	public static Svg getSvg(String name) {
		if(svgs.containsKey(name)) {
			return svgs.get(name);
		}
		else {
			Svg t = new Svg(name);
			svgs.put(name, t);
			return t;
		}
		
	}

	public static Sound getSound(String name) {
		if(sounds.containsKey(name)) {
			return sounds.get(name);
		}
		else {
			Sound t = Gdx.audio.newSound(Gdx.files.internal(name));
			sounds.put(name, t);
			return t;
		}
		
	}
}

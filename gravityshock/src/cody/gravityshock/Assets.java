package cody.gravityshock;


import java.util.HashMap;

import cody.svg.Svg;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

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

	public static boolean audio = true;
	
	public static Sound getSound(String name) {
		if(!audio)
			return null;
		
		if(sounds.containsKey(name)) {
			return sounds.get(name);
		}
		else {
			Sound t = Gdx.audio.newSound(Gdx.files.internal(name));
			sounds.put(name, t);
			return t;
		}
		
	}
	
	static BitmapFont font;
	
	public static BitmapFont getFont() {
		if(font == null) {
			font = new BitmapFont(Gdx.files.internal("data/default.fnt"), Gdx.files.internal("data/default.png"),false);
			font.setColor(Color.WHITE);
		}
		return font;
	}
	
	static Music music;
	public static void playMusic() {
		if(!audio)
			return;
		
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
		}
	}
    
	public static void stopMusic() {
		
		if(music != null) {
			music.stop();
		}
	}
	
	static Skin skin;
	
	public static Skin getSkin() {
		if(skin != null)
			return skin;

        skin = new Skin(Gdx.files.internal("data/uiskin.json"));
        return skin;
	}
}

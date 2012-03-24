package cody.svg;

import java.io.IOException;
import java.util.ArrayList;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class Svg {
	ArrayList<SvgPath> paths;
	ArrayList<SvgText> texts;
	public ArrayList<SvgImage> images;
	
	public Svg(String gdxfile) {
		this(Gdx.files.internal(gdxfile).read());
	}
	public Svg(java.io.InputStream stream){
		paths = new ArrayList<SvgPath>();
		texts = new ArrayList<SvgText>();
		images = new ArrayList<SvgImage>();
		
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
			paths.add(new SvgPath(array.toArray(new Vector2[]{}),color));
		}

		
		list = doc.getChildrenByNameRecursively("tspan");
		for(int i=0;i<list.size;++i){
			Element n=list.get(i);
			float x = Float.parseFloat(n.getAttribute("x"));
			float y = Float.parseFloat(n.getAttribute("y"));
			String text = n.getText();
			Vector2 pos = new Vector2(x, -y);
			texts.add(new SvgText(text, pos));
		}
		
		list = doc.getChildrenByNameRecursively("image");
		for(int i=0;i<list.size;++i){
			Element n=list.get(i);
			float x = Float.parseFloat(n.getAttribute("x"));
			float y = Float.parseFloat(n.getAttribute("y"));
			float width = Float.parseFloat(n.getAttribute("width"));
			float height = Float.parseFloat(n.getAttribute("height"));
			String file = n.getAttribute("xlink:href");
			SvgImage img = new SvgImage();
			img.x = x;
			img.y = -y - height;
			img.width = width;
			img.height = height;
			img.texture = new Texture(Gdx.files.internal("data" + file.substring(file.lastIndexOf("/"))));
			images.add(img);
		}
	}
	
	public int pathCount(){
		return paths.size();
	}
	
	public SvgPath getPath(int i) {
		return paths.get(i);
	}
	
	public int textCount(){
		return texts.size();
	}
	
	public SvgText getText(int i) {
		return texts.get(i);
	}
}

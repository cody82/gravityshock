package cody.svg;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

public class Svg {
	ArrayList<SvgPath> paths;
	
	public Svg(String gdxfile) {
		this(Gdx.files.internal(gdxfile).read());
	}
	public Svg(java.io.InputStream stream){
		paths = new ArrayList<SvgPath>();
		
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
	}
	
	public int pathCount(){
		return paths.size();
	}
	
	public SvgPath getPath(int i) {
		return paths.get(i);
	}
}

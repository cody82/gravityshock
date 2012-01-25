package cody.gravityshock;

import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class FileData implements PersistantData {

	final String dir = ".gravityshock";
	
	@Override
	public void SaveString(String key, String value) {
		FileHandle fh2 = Gdx.files.external(dir);
		if(!fh2.exists())
			fh2.mkdirs();
		FileHandle fh = Gdx.files.external(dir + "/" + key);
		try {
			Writer w = fh.writer(false);
			w.write(value);
			w.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public String LoadString(String key) {
		FileHandle fh = Gdx.files.external(dir + "/" + key);
		if(!fh.exists())
			return null;

		String data = fh.readString();
		
		return data;
	}

}

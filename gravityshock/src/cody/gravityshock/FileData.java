package cody.gravityshock;

import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class FileData implements PersistantData {

	@Override
	public void SaveString(String key, String value) {
		FileHandle fh = Gdx.files.external(".gravityshock/" + key);
		
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
		FileHandle fh = Gdx.files.external(".gravityshock/" + key);
		if(!fh.exists())
			return null;

		String data = fh.readString();
		
		return data;
	}

}

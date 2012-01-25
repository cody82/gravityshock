package cody.gravityshock;

public interface PersistantData {
	public void SaveString(String key, String value);
	public String LoadString(String key);
}

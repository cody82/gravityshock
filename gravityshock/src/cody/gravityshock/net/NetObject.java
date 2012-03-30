package cody.gravityshock.net;

import cody.gravityshock.Actor;

public class NetObject<T> {
	public NetObject(T obj, short id) {
		object = obj;
	}
	public short serverId;
	public T object;
}

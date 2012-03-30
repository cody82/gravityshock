package cody.gravityshock.net;

import com.esotericsoftware.kryo.Kryo;

public class NetBase {
	final int udpPort = 31555;
	final int tcpPort = 31556;
	protected void Register(Kryo kyro) {
		kyro.register(NetSpaceshipMessage.class);
		kyro.register(NetWorldMessage.class);
	}

}

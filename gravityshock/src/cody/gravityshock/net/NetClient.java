package cody.gravityshock.net;

import java.io.IOException;

import com.esotericsoftware.kryonet.Client;

public class NetClient extends NetBase {
	Client client;
	
	public NetClient(String host) throws IOException {
		client = new Client();

		client.start();
		client.connect(2000, host, udpPort, tcpPort);

		Register(client.getKryo());
	}
}

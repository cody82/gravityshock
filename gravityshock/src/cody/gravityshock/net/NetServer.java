package cody.gravityshock.net;

import java.io.IOException;

import cody.gravityshock.*;

import com.esotericsoftware.kryonet.Server;

public class NetServer extends NetBase {
	Server server;
	World world;
	Main main;
	public NetServer(World _world, Main _main) throws IOException {
		world = _world;
		main = _main;
		server = new Server();
		server.start();
		server.bind(udpPort, tcpPort);
		
		Register(server.getKryo());
	}
	
	short nextId;
	
	public void tick(int dtime) throws IOException {
		server.update(dtime);
		NetWorldMessage msg = GetState(world, main);
		server.sendToAllUDP(msg);
	}
	//protected object 
	protected NetWorldMessage GetState(World world, Main main) {
		NetWorldMessage msg = new NetWorldMessage();
		msg.level = (byte)main.level;
		NetSerializeVisitor v = new NetSerializeVisitor();
		for(Actor a : world.actors) {
			if(a.net == null) {
				a.net = new NetObject<Actor>(a, ++nextId);
			}
			v.id = nextId;
			v.message = null;
			a.visit(v);
			if(v.message != null) {
				msg.actors.add(v.message);
			}
		}
		return msg;
	}
}

package autodetect;

import java.net.*;
import java.io.*;

public class DServer extends Thread {
	public static final String MESSAGE = "Game server (group 22) running on port ";
	public static final int DSERVER_PORT = 4567;

	private int gameServerPort;
	
	public DServer(int port) {
		this.gameServerPort = port;
	}
	
	public void run() {
		try {
			MulticastSocket ms = new MulticastSocket(DSERVER_PORT);
			InetAddress ia = InetAddress.getByName("experiment.mcast.net");
			ms.joinGroup(ia);
			while(!interrupted()) {
				byte[] buf = new byte[65536];
				DatagramPacket dp = new DatagramPacket(buf,buf.length);
				ms.receive(dp);
				String s = new String(dp.getData(),0,dp.getLength());
				InetAddress cAddr = dp.getAddress();
				int cPort = dp.getPort();
				System.out.println("Received: " + s + " from " + cAddr.toString() + ":" + cPort);
				DatagramSocket socket = new DatagramSocket();
				buf = (MESSAGE + gameServerPort).getBytes();
				dp = new DatagramPacket(buf, buf.length, cAddr, cPort);
				socket.send(dp);
			}
		} catch(IOException e) {
			System.out.println("Exception:"+e);
		}
	}

	public static void main(String args[]) {
		DServer d = new DServer(1234);
		d.start();
    }
}

package autodetect;

import java.net.*;
import java.io.*;

public class DServer extends Thread {
	public static final String MESSAGE = "Game server (group 22) running on port ";
	public static final int DSERVER_PORT = 4567;

	private int gameServerPort;
	private int waitmillis;
	
	public DServer(int port) {
		this(port, 60000);
	}
	
	/**
	 * A server answering searches for open servers. port is the port number of
	 *  the open server. Will check the interrupt flag every waitmillis 
	 *  milliseconds.
	 * @param port
	 * @param waitmillis
	 */
	public DServer(int port, int waitmillis) {
		this.gameServerPort = port;
		this.waitmillis = waitmillis;
	}
	
	public void run() {
		if (waitmillis <= 0) {
			waitmillis = 60000;
		}
		try {
			MulticastSocket ms = new MulticastSocket(DSERVER_PORT);
			InetAddress ia = InetAddress.getByName("experiment.mcast.net");
			ms.joinGroup(ia);
			ms.setSoTimeout(waitmillis);
			while(!interrupted()) {
				byte[] buf = new byte[65536];
				DatagramPacket dp = new DatagramPacket(buf,buf.length);
				try {
					ms.receive(dp);
					String s = new String(dp.getData(),0,dp.getLength());
					InetAddress cAddr = dp.getAddress();
					int cPort = dp.getPort();
					//System.out.println("Received: " + s + " from " + cAddr.toString() + ":" + cPort);
					DatagramSocket socket = new DatagramSocket();
					buf = (MESSAGE + gameServerPort).getBytes();
					dp = new DatagramPacket(buf, buf.length, cAddr, cPort);
					socket.send(dp);
//					socket.close();
				} catch (SocketTimeoutException e) {
					//check interrupt and wait again
				}
			}
//			ms.close();
		} catch(IOException e) {
			System.out.println("Exception:"+e);
		}
	}

	public static void main(String args[]) {
		DServer d = new DServer(1234, 10000);
		d.start();
    }
}

package autodetect;

import java.net.*;
import java.util.ArrayList;
import java.io.*;

public class ServerFinder {
	private ArrayList<InetAddress> servers;
	private ArrayList<Integer> ports;

	
	public ServerFinder() {
		servers = new ArrayList<InetAddress>();
		ports = new ArrayList<Integer>();
	}
	
	public void findServers(int waitmillis) {
		try {
		    MulticastSocket ms = new MulticastSocket();
		    ms.setTimeToLive(1);
		    InetAddress ia = InetAddress.getByName("experiment.mcast.net");
		    String s = "Looking for servers (group 22)";
			byte[] buf = s.getBytes();
			DatagramPacket dp = new DatagramPacket(buf,buf.length,ia,DServer.DSERVER_PORT);
			ms.send(dp);
		   
			// wait for answer(s)
			if (waitmillis <= 0) {
				waitmillis = 2000;
			}
			ms.setSoTimeout(waitmillis);
			while(true) {	
				buf = new byte[65507];
				dp = new DatagramPacket(buf, buf.length);
				ms.receive(dp);
				s = new String(dp.getData(), 0, dp.getLength());
				InetAddress cAddr = dp.getAddress();
				//System.out.println("Received: \"" + s + "\" from " + cAddr.toString() + ":" + dp.getPort());
				if (s.startsWith(DServer.MESSAGE)) {
					String portStr = s.substring(DServer.MESSAGE.length(), s.length());
					try{
						ports.add(Integer.valueOf(portStr));
						servers.add(cAddr);
					} catch (NumberFormatException nfe) {
						//misbehaving server, ignore
					}
				}
			}
		} catch(SocketTimeoutException e) {
			// stop waiting for answer
		} catch(IOException e) {
		    System.out.println("Exception:"+e);
		    //TODO skriv bättre felutskrift
		}
	}
	
	public ArrayList<InetAddress> getServerAddresses() {
		return servers;		
	}
	
	public ArrayList<Integer> getServerPorts() {
		return ports;
	}

    public static void main(String args[]) {
    	System.out.println("Looking for servers...");
    	ServerFinder sf = new ServerFinder();
    	sf.findServers(2000);
    	ArrayList<InetAddress> addr = sf.getServerAddresses();
    	ArrayList<Integer> ports = sf.getServerPorts();
		
		System.out.println("Servers found:");
		for(int i = 0; i < addr.size(); i++) {
			System.out.println(addr.get(i) + ":" + ports.get(i));
		}
    }
}

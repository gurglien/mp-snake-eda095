package autodetect;

import java.net.*;
import java.util.ArrayList;
import java.io.*;

public class ServerFinder {

    public static void main(String args[]) {
    	ArrayList<InetAddress> servers = new ArrayList<InetAddress>();
    	ArrayList<Integer> ports = new ArrayList<Integer>();
    	
		try {
		    MulticastSocket ms = new MulticastSocket();
		    ms.setTimeToLive(1);
		    InetAddress ia = InetAddress.getByName("experiment.mcast.net");
		   
		    String s = "Looking for servers (group 22)";

			System.out.println("Sending message: " + s);
			byte[] buf = s.getBytes();
			DatagramPacket dp = new DatagramPacket(buf,buf.length,ia,DServer.DSERVER_PORT);
			ms.send(dp);
		   
			//wait for answer(s)
			int waittime = 2000; // 2 seconds
			ms.setSoTimeout(waittime);
			while(true) {	
				buf = new byte[65507];
				dp = new DatagramPacket(buf, buf.length);
				ms.receive(dp);
				s = new String(dp.getData(), 0, dp.getLength());
				InetAddress cAddr = dp.getAddress();
				int cPort = dp.getPort();
				System.out.println("Received: \"" + s + "\" from " + cAddr.toString() + ":" + cPort);
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
		    System.out.println("Receive timeout");
		} catch(IOException e) {
		    System.out.println("Exception:"+e);
		}
		
		System.out.println("Servers found:");
		for(int i = 0; i < servers.size(); i++) {
			System.out.println(servers.get(i) + ":" + ports.get(i));
		}
    }

}

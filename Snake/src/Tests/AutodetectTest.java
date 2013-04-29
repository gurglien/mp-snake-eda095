package Tests;

import java.util.ArrayList;

import autodetect.*;

public class AutodetectTest extends Thread {

	public void run() {
		boolean success = true;
		DServer s1 = new DServer(1234, 1000);
		DServer s2 = new DServer(5678);
		s1.start();
		s2.start();

		for (int i = 0; i < 1000; i++) {
			System.out.println("ignore me, just waiting for time to pass...");
		}
		
		ServerFinder sf = new ServerFinder();
		sf.findServers(2000);
		if (sf.getServerAddresses().size() != 2) {
			System.err.println("Got wrong number of server addresses (" + sf.getServerAddresses().size() + ").");
			success = false;
		}
		ArrayList<Integer> ports = sf.getServerPorts();
		if (ports.size() != 2) {
			System.err.println("Got wrong number of server ports.");
			success = false;
		}
		if (!ports.contains(1234) || !ports.contains(5678)) {
			System.err.println("Got wrong port numbers.");
			success = false;
		}
		
		if (!success) {
			System.err.println("Aaah! Too many errors, abort!");
			System.exit(0);
		}
		
		//part 2
		s1.interrupt();
		for (int i = 0; i < 5000; i++) {
			System.out.println("ignore me, just waiting for time to pass... (again)");
		}
		sf.findServers(2000);
		if (sf.getServerAddresses().size() != 1) {
			System.err.println("Got wrong number of server addresses (" + sf.getServerAddresses().size() + ").");
			success = false;
		}
		ports = sf.getServerPorts();
		if (ports.size() != 1) {
			System.err.println("Got wrong number of server ports.");
			success = false;
		}
		if (!ports.contains(5678)) {
			System.err.println("Got wrong port number.");
			success = false;
		}
		
		if (success) {
			System.out.println("Test passed!");
		}		
	}
	
	public static void main(String[] args) {
		new AutodetectTest().run();
	}
}

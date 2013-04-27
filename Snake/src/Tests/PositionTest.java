package Tests;

import client.Position;

public class PositionTest {

	/**
	 * 
	 * JAJA NÅN GÅNG SKA JAG LÄRA MIG ATT SKRIVA KORREKTA TEST
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Position pos1 = new Position(1,2);
		Position pos2 = new Position("0102");
		
		
		System.out.println(pos1.toString());
		System.out.println(pos2.toString());

	}

}

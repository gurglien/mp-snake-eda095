package client;

public class Position {
	public int x;
	public int y;
	
	public Position(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public Position(String pos) {
		this.x = Integer.parseInt(pos.substring(0,2));
		this.y = Integer.parseInt(pos.substring(2,4));
	}
	
	public String toString(){
		String res = "";
		if (x < 10) {
			res += "0" + Integer.toString(x);
		} else {
			res += Integer.toString(x);
		}
		
		if (y < 10) {
			res += "0" + Integer.toString(y);
		} else {
			res += Integer.toString(y); 
		}
		return res;
	}
	
	public boolean equals(Position p){
		if(x == p.x && y == p.y){
			return true;
		}else{
			return false;
		}
	}

}

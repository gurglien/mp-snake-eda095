package client;
import java.util.LinkedList;


public class Player {
	public static enum Move{LEFT, RIGHT, UP, DOWN};	
	private Move prevDirection;
	private int snakeLength = 3;
	private boolean shouldGrow = false;
	private LinkedList<Position> snake = new LinkedList<Position>();
	private int playfieldWidth;

	public Player(int player, int playfieldWidth) throws IllegalArgumentException{
		if(player < 1 || player > 2){
			throw new IllegalArgumentException("Only player 1 or 2 allowed.");
		}
		this.playfieldWidth = playfieldWidth;
		prevDirection = (player == 1) ? Move.RIGHT : Move.LEFT;
		Position head = (player == 1) ? new Position(playfieldWidth/4, playfieldWidth/2) : new Position((3*playfieldWidth)/4, playfieldWidth/2);

		for(int i = 0; i < snakeLength; ++i){
			snake.add(head);
		}
	}

	public void move(Move direction) throws IllegalArgumentException{
		Position oldHead = snake.getFirst();
		Position newHead;

		// If we try to move in the same or opposite direction as before
		if(direction == oppositeDirection(prevDirection)){
			direction = prevDirection;
		}

		switch(direction){
		case LEFT : 
			newHead = new Position(oldHead.x - 1, oldHead.y);
			break;
		case RIGHT : 
			newHead = new Position(oldHead.x + 1, oldHead.y);
			break;
		case UP : 
			newHead = new Position(oldHead.x, oldHead.y - 1);
			break;
		case DOWN : 
			newHead = new Position(oldHead.x, oldHead.y + 1);
			break;
		default : 
			throw new IllegalArgumentException("Incorrect movement direction.");
		}

		snake.addFirst(newHead);
		if(!shouldGrow){
			snake.removeLast();
		}else{
			shouldGrow = false;
			snakeLength++;
		}

		prevDirection = direction;
	}

	public void grow(){
		shouldGrow = true;
	}

	public LinkedList<Position> getSnake(){
		return snake;
	}
	
	public int getSnakeLength(){
		return snakeLength;
	}
	
	public boolean checkCollision(LinkedList<Position> enemySnake){
		Position head = snake.getFirst();
		// Wall collision
		if(head.x < 0 || head.x + 1 > playfieldWidth || head.y < 0 || head.y + 1 > playfieldWidth){
			return true;
		}

		// Collision with enemy
		for(Position p : enemySnake){
			if(head.x == p.x && head.y == p.y){
				return true;
			}
		}
		
		// Collision with self
		for(int i = 1; i < snake.size(); ++i){
			if(head.x == snake.get(i).x && head.y == snake.get(i).y){
				return true;
			}
		}
		
		return false;
	}

	private Move oppositeDirection(Move direction) throws IllegalArgumentException{
		switch(direction){
		case LEFT : 
			return Move.RIGHT;
		case RIGHT : 
			return Move.LEFT;
		case UP : 
			return Move.DOWN;
		case DOWN : 
			return Move.UP;
		default : 
			throw new IllegalArgumentException("Incorrect movement direction.");
		}
	}

}

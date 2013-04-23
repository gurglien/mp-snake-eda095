package client;
import java.awt.event.KeyEvent;
import java.util.ArrayDeque;


public class Player {
	public static final int MOVE_LEFT = KeyEvent.VK_LEFT;
	public static final int MOVE_RIGHT = KeyEvent.VK_RIGHT;
	public static final int MOVE_UP = KeyEvent.VK_UP;
	public static final int MOVE_DOWN = KeyEvent.VK_DOWN;
	
	private int prevDirection;
	private int snakeLength = 3;
	private boolean shouldGrow = false;
	private ArrayDeque<Position> snake = new ArrayDeque<Position>();

	public Player(int player) throws IllegalArgumentException{
		if(player < 1 || player > 2){
			throw new IllegalArgumentException("Only player 1 or 2 allowed.");
		}
		prevDirection = (player == 1) ? MOVE_RIGHT : MOVE_LEFT;
		Position head = (player == 1) ? new Position(100,100) : new Position(200,100);
		
		for(int i = 0; i < snakeLength; ++i){
			snake.add(head);
		}
	}
	
	public void move(int direction) throws IllegalArgumentException{
		Position oldHead = snake.getFirst();
		Position newHead;
		
		// If we try to move in the same or opposite direction as before
		if(direction == oppositeDirection(prevDirection)){
			direction = prevDirection;
		}
		
		switch(direction){
		case MOVE_LEFT : 
			newHead = new Position(oldHead.x - 10, oldHead.y);
			break;
		case MOVE_RIGHT : 
			newHead = new Position(oldHead.x + 10, oldHead.y);
			break;
		case MOVE_UP : 
			newHead = new Position(oldHead.x, oldHead.y - 10);
			break;
		case MOVE_DOWN : 
			newHead = new Position(oldHead.x, oldHead.y + 10);
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
	
	public ArrayDeque<Position> getSnake(){
		return snake;
	}
	
	private int oppositeDirection(int direction) throws IllegalArgumentException{
		switch(direction){
		case MOVE_LEFT : 
			return MOVE_RIGHT;
		case MOVE_RIGHT : 
			return MOVE_LEFT;
		case MOVE_UP : 
			return MOVE_DOWN;
		case MOVE_DOWN : 
			return MOVE_UP;
		default : 
			throw new IllegalArgumentException("Incorrect movement direction.");
		}
	}

}

package client;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import client.ClientMonitor.GameState;
import client.Player.Move;

public class GamePanel extends JPanel{
	private LinkedList<Position> snake1 = null;
	private LinkedList<Position> snake2 = null;
	private Position food = null;
	private int width;
	private GameState currentState = GameState.NOT_READY;

	public GamePanel(final ClientMonitor monitor, int playfieldWidth){
		width = playfieldWidth*10;
		setBackground(Color.black);
		addKeyListener(new InputHandler(monitor, this));
		setIgnoreRepaint(true);
		setFocusable(true);
	}

	public void updateGameState(GameState s){
		currentState = s;
		repaint();
	}

	public void updatePositions(LinkedList<Position> s1, LinkedList<Position> s2, Position f){
		snake1 = s1;
		snake2 = s2;
		food = f;
		if(currentState == GameState.PLAY){
			repaint();
		}
	}

	public void paint(Graphics g){
		//super.paint(g);
		g.setColor(Color.black);
		g.fillRect(0, 0, width, width);

		if(snake1 != null && snake2 != null){
			paintGame(g);
		}
		paintMessage(g);
		g.dispose();
	}

	private void paintGame(Graphics g){
		// Paint heads
		g.setColor(Color.green);
		g.fillOval(snake1.getFirst().x*10, snake1.getFirst().y*10, 10, 10);
		g.setColor(Color.red);
		g.fillOval(snake2.getFirst().x*10, snake2.getFirst().y*10, 10, 10);

		// Paint bodies
		g.setColor(Color.white);
		for(int i = 1; i < snake1.size(); ++i){
			g.fillOval(snake1.get(i).x*10, snake1.get(i).y*10, 10, 10);
		}
		for(int i = 1; i < snake2.size(); ++i){
			g.fillOval(snake2.get(i).x*10, snake2.get(i).y*10, 10, 10);
		}

		// Paint food
		g.setColor(Color.orange);
		g.fillOval(food.x*10, food.y*10, 10, 10);

		// Paint scores
		g.setColor(Color.green);
		int score1 = snake1.size()-3;
		int score2 = snake2.size()-3;
		g.drawString(Integer.toString(score1), 5, 10);
		g.setColor(Color.red);
		g.drawString(Integer.toString(score2), 5, 20);
	}

	private void paintMessage(Graphics g){		
		String text = "";
		switch(currentState){
		case WIN : text = "You win!";
		break;
		case LOSE : text = "You lose!";
		break;
		case DRAW : text = "Draw!";
		break;
		case READY : text = "Waiting for other player to become ready.";
		break;
		case NOT_READY : text = "Press SPACE when you are ready.";
		}
		g.setColor(Color.gray);
		g.drawString(text, width/4, width/2);
	}

	private class InputHandler  extends KeyAdapter{
		ClientMonitor monitor;
		GamePanel panel;

		public InputHandler(ClientMonitor m, GamePanel p){
			monitor = m;
			panel = p;
		}

		public void keyPressed(KeyEvent e) {
			final int key = e.getKeyCode();
			Move m = null;
			switch(key){
			case KeyEvent.VK_LEFT : m = Move.LEFT;
			break;
			case KeyEvent.VK_RIGHT : m = Move.RIGHT;
			break;
			case KeyEvent.VK_UP : m = Move.UP;
			break;
			case KeyEvent.VK_DOWN : m = Move.DOWN;
			break;
			case KeyEvent.VK_SPACE : 
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						monitor.setState(GameState.READY);
					}
				});
				panel.updateGameState(GameState.READY);
				break;
			}
			if(m != null){
				final Move move = m;
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						try {
							monitor.putNextMove(move);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				});
			}
		}

	}
}
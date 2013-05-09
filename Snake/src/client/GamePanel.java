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

	public GamePanel(final ClientMonitor m, int playfieldWidth){
		width = playfieldWidth*10;
		setBackground(Color.black);
		addKeyListener(new InputHandler(m));
		setIgnoreRepaint(true);
		setFocusable(true);
	}

	public void updatePositions(LinkedList<Position> s1, LinkedList<Position> s2, Position f){
		snake1 = s1;
		snake2 = s2;
		food = f;
		repaint();
	}

	public void paint(Graphics g){
		if(snake1 != null && snake2 != null){
			//super.paint(g);
			g.setColor(Color.black);
			g.fillRect(0, 0, width, width);

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

			g.dispose();
		}else{
			g.setColor(Color.black);
			g.fillRect(0, 0, width, width);
			g.setColor(Color.white);
			g.drawString("TEMP: Press SPACE to play.", 100, 100);
			g.dispose();
		}
	}

	private class InputHandler  extends KeyAdapter{
		ClientMonitor monitor;

		public InputHandler(ClientMonitor m){
			monitor = m;
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
						monitor.setState(GameState.PLAY);
					}
				});
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
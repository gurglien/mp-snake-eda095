package client;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import client.Player.Move;

public class GamePanel extends JPanel{
	private LinkedList<Position> snake1 = null;
	private LinkedList<Position> snake2 = null;
	private ArrayList<Position> food = null;

	public GamePanel(final ClientMonitor m){
		setBackground(Color.black);
		addKeyListener(new InputHandler(m));
		setIgnoreRepaint(true);
		setFocusable(true);
	}

	public void updatePositions(LinkedList<Position> s1, LinkedList<Position> s2, ArrayList<Position> f){
		snake1 = s1;
		snake2 = s2;
		food = f;
		repaint();
	}

	public void paint(Graphics g){
		if(snake1 != null && snake2 != null){
			//super.paint(g);
			g.setColor(Color.black);
			g.fillRect(0, 0, 500, 500);

			// Paint heads
			g.setColor(Color.green);
			g.fillOval(snake1.getFirst().x, snake1.getFirst().y, 10, 10);
			g.setColor(Color.red);
			g.fillOval(snake2.getFirst().x, snake2.getFirst().y, 10, 10);
			
			// Paint bodies
			g.setColor(Color.white);
			for(int i = 1; i < snake1.size(); ++i){
				g.fillOval(snake1.get(i).x, snake1.get(i).y, 10, 10);
			}
			for(int i = 1; i < snake2.size(); ++i){
				g.fillOval(snake2.get(i).x, snake2.get(i).y, 10, 10);
			}

			// Paint food
			g.setColor(Color.pink);
			for(Position p : food){
				g.fillOval(p.x, p.y, 10, 10);
			}

			g.dispose();
		}else{
			g.setColor(Color.black);
			g.fillRect(0, 0, 500, 500);
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
			}
			final Move move = m;
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					monitor.putNextMove(1, move);
				}
			});
		}

	}
}
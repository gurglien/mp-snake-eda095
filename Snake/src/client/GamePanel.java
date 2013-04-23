package client;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayDeque;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class GamePanel extends JPanel{
	private ArrayDeque<Position> snake1 = null;
	private ArrayDeque<Position> snake2 = null;

	public GamePanel(final ClientMonitor m){
		setBackground(Color.black);
		addKeyListener(new InputHandler(m));
		setIgnoreRepaint(true);
		setFocusable(true);
	}

	public void updateSnake(ArrayDeque<Position> s1, ArrayDeque<Position> s2){
		snake1 = s1;
		snake2 = s2;
		repaint();
	}

	public void paint(Graphics g){
		if(snake1 != null && snake2 != null){
			//super.paint(g); NOT SURE IF NEEDED
			g.setColor(Color.black);
			g.fillRect(0, 0, 500, 500);
			g.setColor(Color.white);
			for(Position p : snake1){
				g.fillOval(p.x, p.y, 10, 10);
			}
			for(Position p : snake2){
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
			if(key == KeyEvent.VK_LEFT || key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_UP || key == KeyEvent.VK_DOWN){
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						monitor.putNewMove(1, key);
					}
				});
			}
		}

	}
}
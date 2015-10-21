import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


public class Yard extends Frame {

	PaintThread pt = new PaintThread();
	
	private boolean gameOver = false;
	
	public static final int ROWS = 50;
	public static final int COLS = 50;
	public static final int BLOCK_SIZE = 15;
	
	Snake s = new Snake(this);
	Egg e = new Egg();
	
	
	Image offScreenImage = null;
	
	public void launch() {
		this.setLocation(200, 200);
		this.setSize(COLS * BLOCK_SIZE, ROWS * BLOCK_SIZE);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
				System.exit(0);
			}			
		});
		this.setVisible(true);
		this.addKeyListener(new KeyMonitor());
		
		new Thread(pt).start();
	}
	
	public static void main(String[] args) {
		new Yard().launch();
		System.out.println("OK");
	}
	
	public void stop() {
		gameOver = true;	
	}
	
	@Override
	public void paint(Graphics g) {
		Color c = g.getColor();
		g.setColor(Color.lightGray);
		g.fillRect(0, 0, COLS * BLOCK_SIZE, ROWS * BLOCK_SIZE);
		g.setColor(Color.DARK_GRAY);
		for(int i = 1; i < ROWS; ++i) {
			g.drawLine(BLOCK_SIZE, BLOCK_SIZE * i, (COLS - 1) * BLOCK_SIZE, BLOCK_SIZE * i);
		}
		for(int i = 1; i < COLS; ++i) {
			g.drawLine(BLOCK_SIZE * i, 0, BLOCK_SIZE * i, ROWS * BLOCK_SIZE);
		}		
				
		g.setColor(c);
		
		s.eat(e);
		s.draw(g);
		e.draw(g);
		
		if(gameOver == true) {
			g.setColor(Color.PINK);
			g.setFont(new Font("DIALOG", Font.BOLD | Font.HANGING_BASELINE, 100));
			g.drawString("Game Over", Yard.COLS * Yard.BLOCK_SIZE / 7, Yard.ROWS * Yard.BLOCK_SIZE/ 2);
			g.setColor(c);
			pt.gameOver();
		}
	}
	
	@Override
	public void update(Graphics g) {
		if(offScreenImage == null) {
			offScreenImage = this.createImage(COLS *BLOCK_SIZE, ROWS * BLOCK_SIZE);
		}
		Graphics gOff = offScreenImage.getGraphics();
		paint(gOff);
		g.drawImage(offScreenImage, 0, 0, null);
	}
	
	private class PaintThread implements Runnable {
		private boolean running = true;
		public void run() {
			while(running) {
				repaint();
				try {
					Thread.sleep(125);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
		public void gameOver() {
			running = false;
		}
	}
	
	private class KeyMonitor extends KeyAdapter {
		
		@Override
		public void keyPressed(KeyEvent e) {
			s.keyPressed(e);
		}
		
	}
	
	
	
}
  
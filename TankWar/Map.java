import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Properties;


public class Map extends Frame {
	public static final int GAME_WIDTH = 800;
	public static final int GAME_HEIGHT = 600;


	Shape myShape = new Shape(50, 50, true, Direction.STOP, this);

	Wall w1 = new Wall(100, 200, 20 , 150, this), w2 = new Wall(300, 250, 300, 20, this);
	
	Blood b1 = new Blood();
	
	List<Missile> missiles = new ArrayList<Missile>();
	List<Explode> explodes = new ArrayList<Explode>();
	List<Shape> shapes = new ArrayList<Shape>();
	
	Image offScreenImage = null;
	
	public void paint(Graphics g) {
		g.drawString("missiles count: " + missiles.size(), 680, 50);
		g.drawString("explodes count: " + explodes.size(), 680, 70);
		g.drawString("shapes count: " + shapes.size(), 680, 90);
		g.drawString("myShape life: " + myShape.getLife(), 680, 110);
		
		if(shapes.size() == 0) {
			for(int i = 0; i < Integer.parseInt(PropertyMgr.getProperty("reProduceCount")); ++i) {
				shapes.add(new Shape(100 + 40 * (i + 1), 50 + 20 * i, false, Direction.D, this));
			}
		}
		
		myShape.collidesWithWall(w1);
		myShape.collidesWithWall(w2);
		myShape.eat(b1);
		
		for(int i = 0; i < missiles.size(); ++i) {
			Missile m = missiles.get(i);
			m.hitShapes(shapes);
			m.hitShape(myShape);
			m.hitWall(w1);
			m.hitWall(w2);
			m.draw(g);
		}
		
		for(int i = 0; i < explodes.size(); ++i) {
			Explode e = explodes.get(i);
			e.draw(g);
		}
		
		for(int i = 0; i < shapes.size(); ++i) {
			Shape s = shapes.get(i);
			s.collidesWithWall(w1);
			s.collidesWithWall(w2);
			s.collidesWithShapes(shapes);
			s.draw(g);
		}
		
		myShape.draw(g);
		w1.draw(g);
		w2.draw(g);
		b1.draw(g);
	}
	
	public static void main(String[] args) {
		Map mapClient = new Map();
		mapClient.launchFrame();
	}
	
	
	public void update(Graphics g) {
		if(offScreenImage == null) { 
			offScreenImage = this.createImage(GAME_WIDTH, GAME_HEIGHT);
		}
		Graphics gOffScreen = offScreenImage.getGraphics();
		Color c = gOffScreen.getColor();
		gOffScreen.setColor(new Color(227, 237, 205));
		gOffScreen.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
		gOffScreen.setColor(c);
		paint(gOffScreen);
		g.drawImage(offScreenImage, 0, 0, null);
		 
	}
	
	
	public void launchFrame() {
		
		int initCount = Integer.parseInt(PropertyMgr.getProperty("initCount"));
		
		for(int i = 0; i < initCount; ++i) {
			shapes.add(new Shape(100 + 40 * (i + 1), 50 + 20 * i, false, Direction.D, this));
		}
		
		setTitle("Shape War");
		setLocation(550, 200);
		setSize(GAME_WIDTH, GAME_HEIGHT);
		//pack();

		this.addWindowListener(new WindowAdapter() {			
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}			
		});
		this.addKeyListener(new KeyMonitor());
		
		setResizable(false);
		setBackground(new Color(227, 237, 205));
		setVisible(true);	
		
		new Thread(new PaintThread()).start();
	}
	
	public class PaintThread implements Runnable {
		
		public void run() {
			while(true) {
				repaint();
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private class KeyMonitor extends KeyAdapter {
		public void keyPressed(KeyEvent e) {
			myShape.keyPressed(e);
		}
		
		public void keyReleased(KeyEvent e) {
			myShape.keyReleased(e);
		}
	}

	
}

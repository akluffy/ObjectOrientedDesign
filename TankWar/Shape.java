import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.util.*;


public class Shape {
	public static final int XSPEED = 5;
	public static final int YSPEED = 5;
	public static final int WIDTH = 30;
	public static final int HEIGHT = 30;
	
	
	Map mapClient;
	
	private boolean alive = true;
	public boolean isAlive() {
		return alive;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
	}

	private BloodBar bb = new BloodBar();
	private int life = 100;	
	
	public int getLife() {
		return life;
	}

	public void setLife(int life) {
		this.life = life;
	}

	private boolean good;
	
	private int x, y;
	private int oldX, oldY;
	
	private static Random r = new Random();
	
	private int step = r.nextInt(12) + 3;
	
	private boolean bL = false, bU = false, bR = false, bD = false;

	
	private Direction dir = Direction.STOP;	
	private Direction faceDir = Direction.D;
	
	private static Toolkit tk = Toolkit.getDefaultToolkit();
	
	private static Image[] tankImages = null;
	private static HashMap<String, Image> imgs = new HashMap<String, Image>();
	
	static {
		tankImages = new Image[] {				
			tk.getImage(Shape.class.getClassLoader().getResource("images/tankL.gif")),
			tk.getImage(Shape.class.getClassLoader().getResource("images/tankLU.gif")),
			tk.getImage(Shape.class.getClassLoader().getResource("images/tankU.gif")),
			tk.getImage(Shape.class.getClassLoader().getResource("images/tankRU.gif")),
			tk.getImage(Shape.class.getClassLoader().getResource("images/tankR.gif")),
			tk.getImage(Shape.class.getClassLoader().getResource("images/tankRD.gif")),
			tk.getImage(Shape.class.getClassLoader().getResource("images/tankD.gif")),
			tk.getImage(Shape.class.getClassLoader().getResource("images/tankLD.gif")),			
		};
		
		imgs.put("L", tankImages[0]);
		imgs.put("LU", tankImages[1]);
		imgs.put("U", tankImages[2]);
		imgs.put("RU", tankImages[3]);
		imgs.put("R", tankImages[4]);
		imgs.put("RD", tankImages[5]);
		imgs.put("D", tankImages[6]);
		imgs.put("LD", tankImages[7]);		
	}
	
	public Shape(int x, int y, boolean good) {
		this.x = x;
		this.y = y;
		this.oldX = x;
		this.oldY = y;
		this.good = good;
	}
	
	public Shape(int x, int y, boolean good, Direction dir, Map map) {
		this(x, y, good);
		this.dir = dir;
		this.mapClient = map;
	}
	
	
	public void draw(Graphics g) {
		if(good && this.isAlive()) bb.draw(g);
		if(!alive) {
			if(!good) {
				mapClient.shapes.remove(this);
			}
			return;
		}
				
		switch(faceDir) {
		case L:
			g.drawImage(imgs.get("L"), x, y, null);
			break;
		case LU:
			g.drawImage(imgs.get("LU"), x, y, null);			y -= YSPEED;
			break;
		case U:
			g.drawImage(imgs.get("U"), x, y, null);			
			break;
		case RU:
			g.drawImage(imgs.get("RU"), x, y, null);			
			break;
		case R:
			g.drawImage(imgs.get("R"), x, y, null);			
			break;
		case RD:
			g.drawImage(imgs.get("RD"), x, y, null);			
			break;
		case D:
			g.drawImage(imgs.get("D"), x, y, null);			
			break;
		case LD:
			g.drawImage(imgs.get("LD"), x, y, null);			
			break;
		}
		
		move();
	}
	
	public boolean isGood() {
		return good;
	}

	public void move() {
		this.oldX = x;
		this.oldY = y;
		
		switch(dir) {
		case L:
			x -= XSPEED;
			break;
		case LU:
			x -= XSPEED;
			y -= YSPEED;
			break;
		case U:
			y -= YSPEED;
			break;
		case RU:
			x += XSPEED;
			y -= YSPEED;
			break;
		case R:
			x += XSPEED;
			break;
		case RD:
			x += XSPEED;
			y += YSPEED;
			break;
		case D:
			y += YSPEED;
			break;
		case LD:
			x -= XSPEED;
			y += YSPEED;
			break;
		case STOP:
			break;
		}
		
		if(this.dir != Direction.STOP) {
			this.faceDir = this.dir;
		}
		
		if(x < 0) x = 0;
		if(y < 30) y = 30;
		if(x + Shape.WIDTH > Map.GAME_WIDTH) x = Map.GAME_WIDTH - Shape.WIDTH;
		if(y + Shape.HEIGHT > Map.GAME_HEIGHT) y = Map.GAME_HEIGHT - Shape.HEIGHT;	
		
		if(!good) {
			Direction[] dirs = Direction.values();
			if(step == 0) {
				step = r.nextInt(12) + 3;
				int rn = r.nextInt(dirs.length);
				dir = dirs[rn];
			}			
			step--;
			if(r.nextInt(100) < 6) this.fire();
		}
	}
	
	private void stay() {
		x = oldX;
		y = oldY;
	}
	
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		switch(key) {
		case KeyEvent.VK_CONTROL :
			fire();
			break;
		case KeyEvent.VK_RIGHT :
			bR = true;
			break;
		case KeyEvent.VK_LEFT :
			bL = true;
			break;
		case KeyEvent.VK_UP :
			bU = true;
			break;
		case KeyEvent.VK_DOWN :
			bD = true;
			break;
		case KeyEvent.VK_F2 :
			if(!this.alive)  {
				this.alive = true;
				this.life = 100;
			}
		}
		locateDirection();
	}
	
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		switch(key) {
		case KeyEvent.VK_RIGHT :
			bR = false;
			break;
		case KeyEvent.VK_LEFT :
			bL = false;
			break;
		case KeyEvent.VK_UP :
			bU = false;
			break;
		case KeyEvent.VK_DOWN :
			bD = false;
			break;		
		case KeyEvent.VK_A :
			superFire();
			break;
		}
		locateDirection();		
	};
	
	public void locateDirection() {
		if(bL && !bU && !bR && !bD) dir = Direction.L;
		else if(bL && bU && !bR && !bD) dir = Direction.LU;
		else if(!bL && bU && !bR && !bD) dir = Direction.U;
		else if(!bL && bU && bR && !bD) dir = Direction.RU;
		else if(!bL && !bU && bR && !bD) dir = Direction.R;
		else if(!bL && !bU && bR && bD) dir = Direction.RD;
		else if(!bL && !bU && !bR && bD) dir = Direction.D;
		else if(bL && !bU && !bR && bD) dir = Direction.LD;
		else if(!bL && !bU && !bR && !bD) dir = Direction.STOP;
	}
	
	public Missile fire() {
		if(!alive) return null;
		int x = this.x + Shape.WIDTH/2 - Missile.WIDTH/2;
		int y = this.y + Shape.HEIGHT/2 - Missile.HEIGHT/2;
		Missile m = new Missile(x, y, good, faceDir, mapClient);
		mapClient.missiles.add(m);
		return m;
	}
	
	public Missile fire(Direction dir) {
		if(!alive) return null;
		int x = this.x + Shape.WIDTH/2 - Missile.WIDTH/2;
		int y = this.y + Shape.HEIGHT/2 - Missile.HEIGHT/2;
		Missile m = new Missile(x, y, good, dir, mapClient);
		mapClient.missiles.add(m);
		return m;
	}
	
	public Rectangle getRect() {
		return new Rectangle(x, y, WIDTH, HEIGHT);
	}
	
	public boolean collidesWithWall(Wall w) {
		if(this.isAlive() && this.getRect().intersects(w.getRect())) {
			this.stay();
			return true;
		}
		return false;
	}
	
	public boolean collidesWithShapes(java.util.List<Shape> shapes) {
		for(int i = 0; i < shapes.size(); ++i) {
			Shape s = shapes.get(i);
			if(this != s) {
				if(this.isAlive() && s.isAlive() && this.getRect().intersects(s.getRect())) {
					this.stay();
					s.stay();
					return true;
				}
			}
		}
		return false;
	}
	
	private void superFire() {
		Direction[] dirs = Direction.values();
		for(int i = 0; i < dirs.length - 1; ++i) {
			fire(dirs[i]);
		}
	}
	
	private class BloodBar {
		public void draw(Graphics g) {
			g.drawRect(x - 1, y - 11, WIDTH, 6);
			Color c = g.getColor();
			g.setColor(Color.RED);
			int w = WIDTH * life / 100;
			g.fillRect(x, y - 10, w, 5);
			g.setColor(c);			
		}
	}
	
	public boolean eat(Blood b) {
		if(this.isAlive() && b.isLive() && this.getRect().intersects(b.getRect())) {
			this.life = 100;
			b.setLive(false);
			return true;
		}
		return false;
	}
	
	
}

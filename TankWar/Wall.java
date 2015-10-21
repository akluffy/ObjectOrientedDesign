import java.awt.*;


public class Wall {
	int x, y, w, h;
	Map mapClient;
	
	public Wall(int x, int y, int w, int h, Map mapClient) {
		super();
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.mapClient = mapClient;
	}
	
	public void draw(Graphics g) {
		g.fillRect(x, y, w, h);
	}
	
	public Rectangle getRect() {
		return new Rectangle(x, y, w, h);
	}
	
}

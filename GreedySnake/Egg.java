import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Random;


public class Egg {
	int row, col;
	int w = Yard.BLOCK_SIZE - 1;
	int h = Yard.BLOCK_SIZE - 1;
	private static Random r = new Random();
	Color color = Color.CYAN;
	
	public Egg(int row, int col) {
		this.row = row;
		this.col = col;
	}
	
	public Egg() {
		this(r.nextInt(Yard.ROWS - 3) + 3, r.nextInt(Yard.COLS));
	}
	
	public void newPosition() {
		this.row = r.nextInt(Yard.ROWS - 3) + 3;
		this.col = r.nextInt(Yard.COLS);
	}
	
	public Rectangle getRect() {
		return new Rectangle(Yard.BLOCK_SIZE * this.col, Yard.BLOCK_SIZE * this.row, this.w, this.h);
	}
	
	public void draw(Graphics g) {
		Color c = g.getColor();
		g.setColor(color);
		g.fillOval(Yard.BLOCK_SIZE * col, Yard.BLOCK_SIZE * row, w, h);
		g.setColor(c);
		if(color == Color.CYAN) color = Color.MAGENTA;
		else if(color == Color.MAGENTA )color = Color.ORANGE;
		else color = Color.CYAN;
	}
	
}

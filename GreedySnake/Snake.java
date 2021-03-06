import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;


public class Snake {
	private Node head = null;
	private Node tail = null;
	private int size = 0;
	private Node lastNode;
	
	private Node n = new Node(20, 30, Dir.L);
	private Yard y;
	
	public Snake(Yard y) {
		head = n;
		tail = n;
		size = 1;
		this.y = y;
	}
	
	
	public void addToTail() {
		Node node = null;
	
		switch(tail.dir) {
		case L :
			node = new Node(tail.row, tail.col + 1, tail.dir);
			break;
		case U :
			node = new Node(tail.row + 1, tail.col, tail.dir);
			break;
		case R :
			node = new Node(tail.row, tail.col - 1, tail.dir);
			break;
		case D :
			node = new Node(tail.row - 1, tail.col, tail.dir);
			break;
		}
		tail.next = node;
		node.prev = tail;
		tail = node;
		size++;
	}
	
	public void addToHead() {
		Node node = null;
	
		switch(head.dir) {
		case L :
			node = new Node(head.row, head.col - 1, head.dir);
			break;
		case U :
			node = new Node(head.row - 1, head.col, head.dir);
			break;
		case R :
			node = new Node(head.row, head.col + 1, head.dir);
			break;
		case D :
			node = new Node(head.row + 1, head.col, head.dir);
			break;
		}
		node.next = head;
		head.prev = node;
		head = node;
		size++;
	}
	
	public void draw(Graphics g) {
		if(size <= 0) return;
		move();
		for(Node n = head; n != null; n = n.next) {
			n.draw(g);
		}
	}
	
	
	private void move() {
		addToHead();
		deleteFromTail();
		checkDead();
	}


	private void deleteFromTail() {
		if(size == 0) return;
		tail = tail.prev;
		tail.next = null;		
	}
	
	public void eat(Egg e) {
		if(e.getRect().intersects(this.getRect())) {
			e.newPosition();
			this.addToHead();
		};		
	}
	
	private Rectangle getRect() {
		return new Rectangle(Yard.BLOCK_SIZE * head.col, Yard.BLOCK_SIZE * head.row, head.w, head.h);
	}
	
	private void checkDead() {
		if(head.row < 0 || head.col < 4 || head.row > Yard.ROWS || head.col > Yard.COLS) {
			y.stop();
		}
	}
	

	private class Node {

		int w = Yard.BLOCK_SIZE;
		int h = Yard.BLOCK_SIZE;
		int row, col;
		Dir dir = Dir.L;
		Node next = null;
		Node prev = null;
		
		Node(int row, int col, Dir dir) {
			this.row = row;
			this.col = col;
			this.dir = dir;
		}
		
		void draw(Graphics g) {
			Color c = g.getColor();
			g.setColor(Color.WHITE);
			g.fillRect(Yard.BLOCK_SIZE * col + 1, Yard.BLOCK_SIZE * row + 1, w - 2, h - 2);
			g.setColor(c);			
		}
	}


	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		switch(key) {
		case KeyEvent.VK_LEFT :
			if(lastNode != head && head.dir != Dir.R) {
				head.dir = Dir.L;
				lastNode = this.head;
			}
			break;
		case KeyEvent.VK_UP :
			if(lastNode != head && head.dir != Dir.D) {
				head.dir = Dir.U;
				lastNode = this.head;
			}
			break;
		case KeyEvent.VK_RIGHT :
			if(lastNode != head && head.dir != Dir.L) {
				head.dir = Dir.R;
				lastNode = this.head;
			}
			break;
		case KeyEvent.VK_DOWN :
			if(lastNode != head && head.dir != Dir.U) {
				head.dir = Dir.D;
				lastNode = this.head;
			}
			break;
		}
	}
}

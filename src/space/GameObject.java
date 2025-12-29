package space;

import java.awt.Rectangle;

class GameObject {
	int x, y, width, height, speed, health;
	
	public boolean isDead() {
		return health <= 0;
	}
	
	public Rectangle getBounds() {
		return new Rectangle(x, y, width, height);
	}
}

package space;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

class Meteor extends Enemy{
	BufferedImage meteorImage;
	BufferedImage explosionImage;
	public int explosionCounter;
	
	Meteor(){
		this.health = 3;
		this.speed = 2;
		
		try {
			meteorImage = ImageIO.read(getClass().getResource("/res/Asteroid.png"));
			this.width = meteorImage.getWidth();
			this.height = meteorImage.getHeight();
			explosionImage = ImageIO.read(getClass().getResource("/res/MeteorExplosion.png"));
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void move() {
		y += speed;
	}
	
	public boolean isExploding() {
		if (explosionCounter > 0) return true;
		return false;
	}
	
	public Rectangle getBounds() {
		return new Rectangle(x, y, width, height);
	}
}

package space;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

class Invader extends Enemy{
	BufferedImage explosionImage;
	int explosionCounter;
	Random random;
	int moveCounter;
	int xDirection;
	boolean isMovingHorizontally;
	BufferedImage alienImage;
	
	Invader(){
		super();
		random = new Random();
		xDirection = 1;
		speed = 1;
		moveCounter = random.nextInt(150) + 50;;
		
		try {
			explosionImage = ImageIO.read(getClass().getResource("/res/Explosion.png"));
			alienImage = ImageIO.read(getClass().getResource("/res/Alien.png"));
			this.height = alienImage.getHeight();
			this.width = alienImage.getWidth();
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void move() {
		moveCounter--;
		
		if (moveCounter <= 0) {
			isMovingHorizontally = !isMovingHorizontally;
			
			if (!isMovingHorizontally) {
				//xDirection = 0;
				moveCounter = random.nextInt(100) + 50;
			}else {
				xDirection = (random.nextInt(2) == 0) ? 1 : -1;
				moveCounter = random.nextInt(40) + 20;
			}
		}
		
		if (isMovingHorizontally) {
			x += xDirection*speed;
			if (x <= 0 || x + width >= 1250) {
				xDirection *= -1;
			}
		}else {
			y += speed;
		}
	}
	
	public boolean isExploding() {
		if (explosionCounter > 0) return true;
		return false;
	}
}

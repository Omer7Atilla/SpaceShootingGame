package space;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

class Projectile extends GameObject{
	BufferedImage bulletImage;
	
	Projectile(int x, int y, int speed, String fileName){
		this.x = x;
		this.y = y;
		this.speed = speed;
		health = 1;
		
		try {
			bulletImage = ImageIO.read(getClass().getResource("/res/"+fileName+".png"));
			width = bulletImage.getWidth();
			height = bulletImage.getHeight();
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public Rectangle getBounds() {
		return new Rectangle(x+25, y, width+4, height+4);
	}
}

package space;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

class SpaceShip extends GameObject{
	public int score = 0;
	boolean isExploding;
	int explosionCounter;
	BufferedImage explosionImage;
	BufferedImage heartImage;
	BufferedImage shipImage;
	
	SpaceShip(int x, int y, int sp){
		isExploding = false;
		explosionCounter = 0;
		this.x = x;
		this.y = y;
		speed = sp;
		health = 3;
		
		try {
			shipImage = ImageIO.read(getClass().getResource("/res/SpaceShip.png"));
			width = shipImage.getWidth();
			height = shipImage.getHeight();
			explosionImage = ImageIO.read(getClass().getResource("/res/ShipExplosion.png"));
			heartImage = ImageIO.read(getClass().getResource("/res/Heart.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public boolean isShipExploding() {
		return isExploding;
	}
	
	public void setExploding(boolean expl) {
		isExploding = expl;
	}
}

package space;

class Enemy extends GameObject{
	Enemy(){
		health = 1;
	}
	
	public void move() {
		y += speed;
	}
}

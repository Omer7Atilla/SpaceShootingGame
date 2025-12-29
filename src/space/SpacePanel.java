package space;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.*;

class SpacePanel extends JPanel implements KeyListener, ActionListener{
	private ArrayList<Meteor> meteors;
	BufferedImage collisionExplosionImage;
	private int collisionExplosionX;
	private int collisionExplosionY;
	private int collisionExplosionTimer;
	private final int COLLISION_EXPLOTION_DURATION = 10;
	private Font arcadeFont;
	boolean gameOver;
	boolean win;
	private int spawnCounter = 0;
	private Random random = new Random();
	private ArrayList<Invader> invaders;
	private final long FIRE_RATE = 200;
	private long lastShotTime;
	private ArrayList<Projectile> projectiles;
	private ArrayList<Projectile> alienProjectiles;//Added this for alien shots
	Timer gameLoop;
	SpaceShip ship;
	SpacePanel(){
		try {
			arcadeFont = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("/res/GameOverFont.ttf")).deriveFont(60f);
		}catch(IOException | FontFormatException e) {
			e.printStackTrace();
			arcadeFont = new Font("Monospaced", Font.BOLD, 60);
		}
		try {
			collisionExplosionImage = ImageIO.read(getClass().getResource("/res/CollisionExplosion.png"));
		}catch(IOException e) {
			e.printStackTrace();
			System.err.println("Collision explosion image could not loaded");
		}
		gameOver = false;
		win = false;
		meteors = new ArrayList<Meteor>();
		invaders = new ArrayList<Invader>();
		lastShotTime = 0;
		alienProjectiles = new ArrayList<Projectile>();
		projectiles = new ArrayList<Projectile>();
		this.setBackground(Color.BLACK);
		this.addKeyListener(this);
		ship = new SpaceShip(500, 490, 0);
		gameLoop = new Timer(16, this);
		this.setFocusable(true);
		this.requestFocusInWindow(true);
		gameLoop.start();
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		if (ship.isShipExploding()) {
			if (ship.explosionImage != null) {
				g.drawImage(ship.explosionImage, ship.x, ship.y, this);
			}
		}
		else {
			if (ship.shipImage != null) {
				g.drawImage(ship.shipImage, ship.x, ship.y, this);
			}
		}
		
		for (Projectile proj : projectiles) {
			g.drawImage(proj.bulletImage, proj.x+25, proj.y, this);
		}
		
		for (Projectile alienproj : alienProjectiles) {
			g.drawImage(alienproj.bulletImage, alienproj.x + alienproj.width / 2, alienproj.y, this);
		}
		
		for (Invader invader : invaders) {
			if (invader.explosionCounter > 0) {
				g.drawImage(invader.explosionImage, invader.x, invader.y, this);
			}
			else {
				g.drawImage(invader.alienImage, invader.x, invader.y, this);
			}
		}
		
		for (Meteor meteor : meteors) {
			if (meteor.explosionCounter > 0) {
				g.drawImage(meteor.explosionImage, meteor.x, meteor.y, this);
			}
			else {
				g.drawImage(meteor.meteorImage, meteor.x, meteor.y, this);
			}
		}
		
		for (int i=0; i < ship.health; i++) {
			g.drawImage(ship.heartImage, 10+30*i, 600, this);
		}
		
		g.setColor(Color.WHITE);
		g.setFont(new Font("Monospaced", Font.BOLD, 20));
		g.drawString("SCORE "+ship.score, getWidth() - 150, 25);
		
		//Adding collision explosion
		if (collisionExplosionTimer > 0) {
			g.drawImage(collisionExplosionImage, collisionExplosionX, collisionExplosionY, this);
		}
		//Adding collision explosion
		
		if (gameOver == true) {
			g.setColor(Color.RED);
			g.setFont(arcadeFont);
			//g.setFont(new Font("Monospaced", Font.BOLD, 60));
			
			String gameOverText = "GAME OVER";
			FontMetrics fm = g.getFontMetrics(g.getFont());
			
			int x = (getWidth() - fm.stringWidth(gameOverText)) / 2;
			int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
			
			g.drawString(gameOverText, x, y);
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		if (gameOver || ship.isShipExploding()) {
			return;
		}
		
		if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			ship.speed = -5;
		}
		else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			ship.speed = 5;
		}
		else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			long currentTime = System.currentTimeMillis();
			
			if (currentTime - lastShotTime > FIRE_RATE) {
				Projectile bullet = new Projectile(ship.x, ship.y, -10, "SingleFireBall");
				projectiles.add(bullet);
				lastShotTime = currentTime;
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		if (gameOver) {
			return;
		}
		
		if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_RIGHT) {
			ship.speed = 0;
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
	    if (ship.isExploding) {
	        ship.explosionCounter--;
	        if (ship.explosionCounter <= 0) {
	            ship.isExploding = false;
	            if (ship.health <= 0) {
	                gameOver = true;
	            } else {
	                resetLevel();
	            }
	        }
	    }

	    if (!gameOver && !ship.isExploding) {
	        ship.x += ship.speed;
	        
	        if (ship.x + ship.width >= this.getWidth()) {
	            ship.x = this.getWidth() - ship.width;
	        }
	        else if (ship.x <= 0) {
	            ship.x = 0;
	        }
	        
	        // Tüm oyuncu mermilerini hareket ettir ve düşmanlarla çarpışmasını kontrol et
	        Iterator<Projectile> projectileIterator = projectiles.iterator();
	        while (projectileIterator.hasNext()) {
	            Projectile playerBullet = projectileIterator.next();
	            playerBullet.y += playerBullet.speed;
	            
	            if (playerBullet.y + playerBullet.height < 0) {
	                projectileIterator.remove();
	                continue; // Bir sonraki mermiye geç
	            }

	            // Mermi - Uzaylı Çarpışma Kontrolü
	            Iterator<Invader> invaderIterator = invaders.iterator();
	            boolean hitInvader = false;
	            while (invaderIterator.hasNext()) {
	                Invader invader = invaderIterator.next();
	                if (playerBullet.getBounds().intersects(invader.getBounds())) {
	                    projectileIterator.remove();
	                    invader.health--;
	                    if (invader.health <= 0) {
	                        invader.explosionCounter = 30;
	                        ship.score += 100;
	                    }
	                    hitInvader = true;
	                    break;
	                }
	            }
	            if(hitInvader) continue; // Bir sonraki mermiye geç

	            // Mermi - Meteor Çarpışma Kontrolü
	            Iterator<Meteor> meteorIterator = meteors.iterator();
	            boolean hitMeteor = false;
	            while (meteorIterator.hasNext()) {
	                Meteor meteor = meteorIterator.next();
	                if (playerBullet.getBounds().intersects(meteor.getBounds())) {
	                    projectileIterator.remove();
	                    meteor.health--;
	                    if (meteor.health <= 0) {
	                        meteor.explosionCounter = 30;
	                        ship.score += 300;
	                    }
	                    hitMeteor = true;
	                    break;
	                }
	            }
	            if(hitMeteor) continue; // Bir sonraki mermiye geç

	            // Mermi - Uzaylı Mermisi Çarpışma Kontrolü
	            Iterator<Projectile> alienBulletIterator = alienProjectiles.iterator();
	            while (alienBulletIterator.hasNext()) {
	                Projectile alienBullet = alienBulletIterator.next();
	                if(playerBullet.getBounds().intersects(alienBullet.getBounds())) {
	                    collisionExplosionX = (playerBullet.x + alienBullet.x) / 2;
	                    collisionExplosionY = (playerBullet.y + alienBullet.y) / 2;
	                    collisionExplosionTimer = COLLISION_EXPLOTION_DURATION;
	                    
	                    projectileIterator.remove();
	                    alienBulletIterator.remove();
	                    break;
	                }
	            }
	        }

	        // Uzaylı ve meteorları hareket ettir, çarpışmalarını kontrol et
	        Iterator<Invader> invaderIterator = invaders.iterator();
	        while (invaderIterator.hasNext()) {
	            Invader invader = invaderIterator.next();
	            
	            if (invader.explosionCounter > 0) {
	                invader.explosionCounter--;
	                if (invader.explosionCounter <= 0) {
	                    invaderIterator.remove();
	                }
	            } else {
	                invader.move();
	                if (random.nextInt(1000) < 2) {
	                    Projectile alienBullet = new Projectile(invader.x, invader.y, 5, "LaserBeam");
	                    alienProjectiles.add(alienBullet);
	                }
	                
	                if (invader.y + invader.height >= this.getHeight()) {
	                    gameOver = true;
	                    gameLoop.stop();
	                    repaint();
	                }
	                
	                if (invader.getBounds().intersects(ship.getBounds())) {
	                    invaderIterator.remove();
	                    ship.health--;
	                    ship.isExploding = true;
	                    ship.explosionCounter = 30;
	                    break;
	                }
	            }
	        }
	        
	        Iterator<Meteor> meteorIterator = meteors.iterator();
	        while (meteorIterator.hasNext()) {
	            Meteor meteor = meteorIterator.next();
	            
	            if (meteor.explosionCounter > 0) {
	                meteor.explosionCounter--;
	                if (meteor.explosionCounter == 0) {
	                    meteorIterator.remove();
	                }
	            } else {
	                meteor.move();
	                
	                if (meteor.height + meteor.y >= this.getHeight()) {
	                    gameOver = true;
	                    gameLoop.stop();
	                    repaint();
	                }
	                
	                if (meteor.getBounds().intersects(ship.getBounds())) {
	                    meteorIterator.remove();
	                    ship.health--;
	                    ship.isExploding = true;
	                    ship.explosionCounter = 30;
	                    break;
	                }
	            }
	        }

	        // Düşman oluşturma mantığı
	        if (random.nextInt(1000) < 10) {
	            if (random.nextInt(10) < 8) {
	                Invader alien = new Invader();
	                int randomX = random.nextInt(getWidth());
	                alien.x = randomX;
	                if (alien.x + alien.width > getWidth()) {
	                    alien.x = getWidth() - alien.width;
	                }
	                alien.y = 0;
	                invaders.add(alien);
	            } else {
	                Meteor meteor = new Meteor();
	                int randomX = random.nextInt(getWidth());
	                meteor.x = randomX;
	                if (meteor.x + meteor.width > getWidth()) {
	                    meteor.x = getWidth() - meteor.width;
	                }
	                meteor.y = 0;
	                meteors.add(meteor);
	            }
	        }
	        
	        // Uzaylı mermilerini hareket ettir ve gemiyle çarpışmasını kontrol et
	        Iterator<Projectile> alienProjectileIterator = alienProjectiles.iterator();
	        while (alienProjectileIterator.hasNext()) {
	            Projectile alienBullet = alienProjectileIterator.next();
	            alienBullet.y += alienBullet.speed;
	            
	            if (alienBullet.y > this.getHeight()) {
	                alienProjectileIterator.remove();
	            }
	            
	            if (alienBullet.getBounds().intersects(ship.getBounds())) {
	                alienProjectileIterator.remove();
	                ship.health--;
	                ship.isExploding = true;
	                ship.explosionCounter = 30;
	                break;
	            }
	        }
	    }
	    
	    // Çarpışma patlama sayacını düşür
	    if (collisionExplosionTimer > 0) {
	        collisionExplosionTimer--;
	    }
	    
	    repaint();
	}
	
	public void resetLevel() {
		meteors.clear();
		invaders.clear();
		projectiles.clear();
		alienProjectiles.clear();
		ship.x = 500;
		ship.y = 490;
		ship.speed = 0;
		ship.isExploding = false;
		ship.explosionCounter = 0;
		gameLoop.start();
	}
	
	public ArrayList<Projectile> getProjectiles() {
		return projectiles;
	}

	public void setProjectiles(ArrayList<Projectile> projectiles) {
		this.projectiles = projectiles;
	}

	public long getLastShotTime() {
		return lastShotTime;
	}

	public void setLastShotTime(long lastShotTime) {
		this.lastShotTime = lastShotTime;
	}

	public long getFIRE_RATE() {
		return FIRE_RATE;
	}

	public ArrayList<Invader> getInvaders() {
		return invaders;
	}

	public void setInvaders(ArrayList<Invader> invaders) {
		this.invaders = invaders;
	}

	public int getSpawnCounter() {
		return spawnCounter;
	}

	public void setSpawnCounter(int spawnCounter) {
		this.spawnCounter = spawnCounter;
	}

	public Font getArcadeFont() {
		return arcadeFont;
	}

	public void setArcadeFont(Font arcadeFont) {
		this.arcadeFont = arcadeFont;
	}

	public ArrayList<Projectile> getAlienProjectiles() {
		return alienProjectiles;
	}

	public void setAlienProjectiles(ArrayList<Projectile> alienProjectiles) {
		this.alienProjectiles = alienProjectiles;
	}

	public int getCollisionExplosionX() {
		return collisionExplosionX;
	}

	public void setCollisionExplosionX(int collisionExplosionX) {
		this.collisionExplosionX = collisionExplosionX;
	}

	public int getCollisionExplosionY() {
		return collisionExplosionY;
	}

	public void setCollisionExplosionY(int collisionExplosionY) {
		this.collisionExplosionY = collisionExplosionY;
	}

	public int getCollisionExplosionTimer() {
		return collisionExplosionTimer;
	}

	public void setCollisionExplosionTimer(int collisionExplosionTimer) {
		this.collisionExplosionTimer = collisionExplosionTimer;
	}

	public int getCOLLISION_EXPLOTION_DURATION() {
		return COLLISION_EXPLOTION_DURATION;
	}

	public ArrayList<Meteor> getMeteors() {
		return meteors;
	}

	public void setMeteors(ArrayList<Meteor> meteors) {
		this.meteors = meteors;
	}
}

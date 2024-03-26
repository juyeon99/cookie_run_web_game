package Canvas_CookieRun;

import java.awt.Image;

public class Player {
	Image img;
	double width, height, posX, posY, speedX, speedY;
	int life;
	int jumpCount;
	boolean floating;
	boolean secondJump;
	boolean slide;
	boolean isOnBrick;
	
	public Player(Image img, double posX, double posY, double speedX, double speedY, double width, double height) {
		this.img = img;
		this.posX = posX;
		this.posY = posY;
		this.speedX = speedX;
		this.speedY = speedY;
		this.width = width;
		this.height = height;
		floating = false;
		secondJump = false;
		slide = false;
		isOnBrick = false;
		jumpCount = 0;
		life = 3;
	}
}

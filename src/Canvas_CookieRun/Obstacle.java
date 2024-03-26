package Canvas_CookieRun;

import java.awt.Image;

public class Obstacle {
	Image img;
	int width, height, posX, posY, speedX, speedY;
	boolean crash;
	boolean passed;
	int type;
	
	public Obstacle(Image img, int posX, int posY, int speedX, int speedY, int width, int height, int type) {
		this.img = img;
		this.posX = posX;
		this.posY = posY;
		this.speedX = speedX;
		this.speedY = speedY;
		this.width = width;
		this.height = height;
		passed = false;
		crash = false;
		this.type = type;
	}
}

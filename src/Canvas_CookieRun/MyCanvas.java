package Canvas_CookieRun;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.*;

import javax.imageio.ImageIO;

public class MyCanvas extends Canvas {
	Player p;
	ArrayList<Obstacle> obs = new ArrayList<>();

	int score = 0;
	
	int jump = 0;
	int jump2 = 0;
	
	int bgPosX = 0;
	int bgPosX2 = 0;
	int bgSpeed = 3;
	
	Image buffImage;
	Graphics buffg;
	
	public MyCanvas() {
		Image image = Toolkit.getDefaultToolkit().getImage("cookie.png");
		p = new Player(image, 300, 360, 4, 4, 80, 120);
		repaint();
	}
	
	public void start() {
		MyThread th = new MyThread();
		th.start();
		CrashThread th2 = new CrashThread();
		th2.start();
	}
	
	@Override
	public void paint(Graphics g) {
		if (buffg == null) {
			buffImage = createImage(getWidth(), getHeight());
			if (buffImage == null) {
				System.out.println("버퍼 생성 실패");
			} else {
				buffg = buffImage.getGraphics();
			}
		}
		update(g);
	}

	public void addObs(int n, int x) {
		Image img = Toolkit.getDefaultToolkit().getImage("obs.png");
		if(n == 0) {
			obs.add(new Obstacle(img,x, (getHeight()/2)-80 + 155, 3, 3, 80, 80,1));
		} else if(n == 1) {
			obs.add(new Obstacle(img,x, (getHeight()/2)-150 + 155, 3, 3, 150, 150,2));
		} else if (n == 2){
			obs.add(new Obstacle(img,x, 205, 3, 3, 200, 200,3));
		} else if (n == 3){
			img = Toolkit.getDefaultToolkit().getImage("brick.png");
			obs.add(new Obstacle(img,x, 300, 3, 3, 170, 30, 4));
//		} else if (n == 4) {
//			img = Toolkit.getDefaultToolkit().getImage("hole.png");
//			obs.add(new Obstacle(img,x, 470, 3, 3, 100, 230, 5));
		}
	}
	
	@Override
	public void update(Graphics g) {
		buffg.drawImage(buffImage, 0, 0, this);
		/** 여기부터 **/
		
		// 배경
		Image img = Toolkit.getDefaultToolkit().getImage("bg.png");
		buffg.drawImage(img,bgPosX,0,getWidth(),getHeight(),this);
		buffg.drawImage(img,bgPosX2,0,getWidth(),getHeight(),this);
		
		buffg.setFont(new Font("궁서체", Font.BOLD, 30));
		buffg.drawString("Score: " + score, 390, 50);
		buffg.drawString("Life: " + p.life, 390, 85);

		// 내 캐릭터
		buffg.drawImage(p.img, (int)p.posX, (int)p.posY, (int)p.width, (int)p.height, this);
//		buffg.fillRect((int)p.posX+10, (int)p.posY+31, (int)p.width-28, (int)p.height-40);
		
		// 장애물
		for(int i = 0; i < obs.size(); i++) {
			Obstacle o = obs.get(i);
			buffg.drawImage(o.img,o.posX, o.posY, o.width, o.height,this);
			
			// 충돌 부분 체크
//			if(o.type == 1) {
//				buffg.fillRect(o.posX + 18, o.posY + 35, o.width - 35, o.height - 35);
//			} else if(o.type == 2) {
//				buffg.fillRect(o.posX + 30, o.posY + 55, o.width - 60, o.height - 60);
//			} else if(o.type == 3) {
//				buffg.fillRect(o.posX + 30, o.posY + 51, o.width - 60, o.height - 60);
//			}
			
		}
		
		if(p.life <= 0) {
			Image image = Toolkit.getDefaultToolkit().getImage("gameover.jpg");
			buffg.drawImage(image,getWidth()/2-200,getHeight()/2-125,400,250,this);
		}
		
		/** 여기까지 **/
		g.drawImage(buffImage, 0, 0, this);
	}
	
	public void crash() {
		synchronized (obs) {
			for (int i = 0; i < obs.size(); i++) {
				Obstacle o = obs.get(i);
				if(o.type == 4) {	// brick
					Rectangle myRect = new Rectangle((int)p.posX, (int)(p.posY+p.height - 1), (int)p.width, 1);
					Rectangle obsRect = new Rectangle(o.posX, o.posY, o.width, o.height);
					if(myRect.intersects(obsRect) && p.isOnBrick == false) {
						p.posY = o.posY - p.height;
						p.isOnBrick = true;
						p.jumpCount = 0;
						p.secondJump = false;
						p.floating = false;
					} else if(o.posX+o.width < p.posX) {
						p.isOnBrick = false;
					}
//				} else if(o.type == 5) {
//					Rectangle myRect = new Rectangle((int)p.posX, (int)(p.posY+p.height - 1), (int)p.width, 1);
//					Rectangle obsRect = new Rectangle(o.posX, o.posY, o.width, o.height);
//					if(myRect.intersects(obsRect)) {
//						p.posY -= (p.speedY*3);
//						
//					}
				} else if ((o.type == 1 || o.type == 2 || o.type == 3) && o.crash != true) { // crashed
					Rectangle myRect = new Rectangle((int)p.posX+10, (int)p.posY+31, (int)p.width-28, (int)p.height-40);
					Rectangle obsRect = null;
					if(o.type == 1) {
						obsRect = new Rectangle(o.posX+18, o.posY+35, o.width-35, o.height-35);
					} else if(o.type == 2) {
						obsRect = new Rectangle(o.posX+30, o.posY+55, o.width-60, o.height-60);
					} else if(o.type == 3) {
						obsRect = new Rectangle(o.posX+30, o.posY+51, o.width-60, o.height-60);
					}
					
					if(myRect.intersects(obsRect)) {
						o.img = Toolkit.getDefaultToolkit().getImage("obsCrashed.png");
						o.crash = true;
						p.life--;
					}
				}
			}
		}
	}
	
	boolean isRunning = true;
	class CrashThread extends Thread {
		@Override
		public void run() {
			while(isRunning) {
				crash();
				
				if(p.life <= 0) {
					break;
				}
				
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void jump() {
		if(p.jumpCount == 2){
			p.floating = true;
			p.secondJump = true;
			if(jump2++ < 24) {
				p.posY -= p.speedY;
			} else {
				jump2 = 0;
				p.jumpCount--;
			}
		} else if(p.jumpCount == 1) {
			p.floating = true;
			if(jump++ < 33) {
				p.posY -= p.speedY;
			} else {
				jump = 0;
				p.jumpCount--;
			}
		} else if(p.jumpCount == 0 && p.isOnBrick == false){
			if(p.posY < 360) {
				p.posY += p.speedY;
			} else {
				p.jumpCount = 0;
				p.secondJump = false;
				p.floating = false;
			}
		}
	}
	
	public void slide() {
		if(p.slide) {
			p.img = Toolkit.getDefaultToolkit().getImage("cookieRotated.png");
			double temp = p.height;
			p.height = p.width;
			p.width = temp;
			p.posY += 40;
		} else if(p.slide == false && p.floating == false && p.img == Toolkit.getDefaultToolkit().getImage("cookieRotated.png")) {
			p.img = Toolkit.getDefaultToolkit().getImage("cookie.png");
			double temp = p.height;
			p.height = p.width;
			p.width = temp;
			p.posY -= 40;
		}
	}
	
	class MyThread extends Thread {
		
		@Override
		public void run() {
			while (getWidth() < 1) {	// 화면이 올라옴
				try {
					Thread.sleep(300);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			bgPosX2 = getWidth();
			while (true) {
				// 장애물 추가
				if(obs.size() < 5) {
					Random rd = new Random();
					int rand = rd.nextInt(4);
					if(obs.size() == 0) {
						addObs(rand,900);
					} else {
						Obstacle o = obs.get(obs.size()-1);
						if(rand == 2 && o.type == 3) {	// obs type 3 in a row
							addObs(rand,o.posX + o.width + 2);
						} else if(o.type == 2){		// previous obstacle: obs type 2
							addObs(rand,o.posX + o.width + 280);
						} else{
							addObs(rand,o.posX + o.width + 230);
						} 
					}
				}
				
				// 배경이동
				bgPosX -= bgSpeed;
				bgPosX2 -= bgSpeed;
			
				if(bgPosX <= getWidth()*-1) {
					bgPosX = getWidth();
				}
				if(bgPosX2 <= -getWidth()) {
					bgPosX2 = getWidth();
				}
				
				// 캐릭터 점프
				jump();

				// 장애물 이동
				for (int i = 0; i < obs.size(); i++) {
					Obstacle o = obs.get(i);
					o.posX -= o.speedX;
					
					// 점수 관리
					if(o.passed == false && p.posX >= o.posX + o.width && o.img == Toolkit.getDefaultToolkit().getImage("obs.png")) {
						if(o.type == 1) {
							score += 150;
						} else if (o.type == 2) {
							score += 200;
						} else if (o.type == 3) {
							score += 100;
						}
						o.passed = true;
					} 
					
					if (o.posX < -200) { // 메모리 관리(나간 장애물 삭제)
						obs.remove(i);
						i--;
					}
				}
				
				if(p.life <= 0) {
					break;
				}

				repaint();

				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

}

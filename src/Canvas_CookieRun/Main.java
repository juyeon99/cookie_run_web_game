package Canvas_CookieRun;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Main {

	public static void main(String[] args) {
		Dimension scr = Toolkit.getDefaultToolkit().getScreenSize();
		int width = scr.width;
		int height = scr.height;
		
		JFrame frame = new JFrame("Cookie Run");
		frame.setSize(1000,750);		// 창 사이즈
		frame.setLocation((width - 1000)/2,(height - 750)/2);	// 모니터 중간에 위치 정렬
		frame.setResizable(false);	// 사이즈 fix
		frame.setVisible(true);
		frame.setLayout(null);		// 자유 형식
		
		// MyCanvas can = new MyCanvas();
		final MyCanvas can = new MyCanvas();
		can.setSize(900, 650);
		can.setLocation(50, 65);		
		frame.add(can);	
		
		JLabel label = new JLabel("Cookie Run");
		label.setFont(new Font("궁서체", Font.BOLD, 30));
		label.setSize(500,50);
		label.setLocation(250,10);
		label.setBackground(Color.BLUE);
		label.setForeground(Color.white);
		label.setOpaque(true);		
		frame.add(label);
		
		JButton btn = new JButton("Pause");
		btn.setSize(150,50);
		btn.setLocation(55,10);
		btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {		
				can.p.speedX = 0;
				can.p.speedY = 0;
				can.bgSpeed = 0;
				for(int i = 0; i < can.obs.size(); i++) {
					can.obs.get(i).speedX = 0;
					can.obs.get(i).speedY = 0;
				}
			}
		});
		frame.add(btn);
		
		JButton btn2 = new JButton("Resume");
		btn2.setSize(150,50);
		btn2.setLocation(790,10);
		btn2.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {	
				try {
					Thread.sleep(1500);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				can.p.speedX = 4;
				can.p.speedY = 4;
				can.bgSpeed = 3;
				for(int i = 0; i < can.obs.size(); i++) {
					can.obs.get(i).speedX = 3;
					can.obs.get(i).speedY = 3;
				}
			}
		});
		frame.add(btn2);
		
		can.addKeyListener(new KeyListener() {			
			@Override
			public void keyTyped(KeyEvent e) {
				
			}			
			@Override
			public void keyReleased(KeyEvent e) {
			
				if(e.getKeyCode() == 32) {
					if(can.p.jumpCount < 2 && can.p.secondJump == false) {
						can.p.jumpCount++;
					}
//					label.setText("점수: " + can.score);
				}
				if(e.getKeyCode() == 40) {
					can.p.slide = false;
					can.slide();
				}
			}			
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == 40) {
					if(can.p.slide == false && can.p.floating == false) {
						can.p.slide = true;
						can.slide();
					}
				}
			}
		});
		
		can.start();
		
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

}

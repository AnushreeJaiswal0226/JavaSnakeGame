import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import javax.swing.*;
public class GamePanel extends JPanel implements ActionListener 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -9187252812244233406L;
	JButton restart;
	static final int SCREEN_WIDTH = 500;
	static final int SCREEN_HEIGHT = 500+100;
	static final int UNIT_SIZE = 25;
	static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
	static final int DELAY = 75;
	static final Color color = Color.YELLOW;
	final int x[] = new int[GAME_UNITS];
	final int y[] = new int[GAME_UNITS];
	int bodyParts = 2;
	int applesEaten;
	int appleX;
	int appleY;
	char direction = 'R';
	boolean running = false;
	Timer timer;
	Random random;
	
	GamePanel() {
		random = new Random();
		this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
		this.setBackground(Color.BLACK);
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter());
		restart = new JButton("Restart");
		restart.setBorderPainted(false);
		restart.setFont(new Font("Ink Free",Font.BOLD,45));
		restart.setForeground(color);
		restart.setOpaque(false);
		restart.setBackground(new Color(0,0,0,0));
		restart.addActionListener(this);
		restart.setVisible(false);
		restart.setBounds(100, SCREEN_HEIGHT-150, 300, 100);
		this.setLayout(null);
		this.add(restart);
		startGame();
	}
	
	void startGame() {
		newApple();
		running = true;
		timer = new Timer(DELAY,this);
		timer.start();
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}
	
	void draw(Graphics g) {
		if(running) {
			for(int i=0;i<=(SCREEN_HEIGHT-100)/UNIT_SIZE;i++) {
				g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_HEIGHT-100);
				g.drawLine(0, i*UNIT_SIZE, SCREEN_WIDTH, i*UNIT_SIZE);
			}
			
			g.setColor(Color.RED);
			g.fillOval(appleX,appleY, UNIT_SIZE, UNIT_SIZE);
			
			for(int i=0;i<bodyParts;i++) {
				if(i==0) {
					g.setColor(Color.GREEN);
					g.fillRoundRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE,10,10);
				}
				else {
					//g.setColor(new Color(45,180,0));
					g.setColor(new Color(random.nextInt(225), random.nextInt(225), random.nextInt(225)));
					g.fillOval(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				}
			}
			
			g.setColor(color);
			g.setFont(new Font("Ink Free",Font.BOLD,40));
			FontMetrics metrics = getFontMetrics(g.getFont());
			g.drawString("Score: "+applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: "+applesEaten))/2, (SCREEN_HEIGHT-100)+g.getFont().getSize());
		}
		else {
			gameOver(g);
		}
	}
	
	void newApple() {
		appleX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
		appleY = random.nextInt((int)((SCREEN_HEIGHT-100)/UNIT_SIZE))*UNIT_SIZE;
	}
	
	void move() {
		for(int i=bodyParts;i>0;i--) {
			x[i] = x[i-1];
			y[i] = y[i-1];
		}
		
		switch(direction) {
		case 'U':
			y[0] = y[0] - UNIT_SIZE;
			break;
		case 'D':
			y[0] = y[0] + UNIT_SIZE;
			break;
		case 'R':
			x[0] = x[0] + UNIT_SIZE;
			break;
		case 'L':
			x[0] = x[0] - UNIT_SIZE;
			break;
		}
	}
	
	void checkApple() {
		if((x[0] == appleX) && (y[0] == appleY)) {
			bodyParts++;
			applesEaten++;
			newApple();
		}
	}
	
	void checkCollision() {
		//this checks if head collides with body
		for(int i=bodyParts;i>0;i--) {
			if((x[0] == x[i]) && (y[0] == y[i])) {
				running = false;
			}
		}
		//check if head touches left, right, top, bottom border
		if((x[0] < 0) || (x[0] > SCREEN_WIDTH) || (y[0] < 0) || (y[0] > (SCREEN_HEIGHT-100))) {
			running = false;
			restart.setVisible(true);
		}
		
		if(!running) {
			timer.stop();
			//repaint();
		}
	}
	
	void gameOver(Graphics g) {
		//score
		g.setColor(color);
		g.setFont(new Font("Ink Free",Font.BOLD,40));
		FontMetrics metrics1 = getFontMetrics(g.getFont());
		g.drawString("Score: "+applesEaten, (SCREEN_WIDTH - metrics1.stringWidth("Score: "+applesEaten))/2, (SCREEN_HEIGHT-260)+g.getFont().getSize());
		
		//Game Over text
		g.setColor(color);
		g.setFont(new Font("Ink Free",Font.BOLD,75));
		FontMetrics metrics2 = getFontMetrics(g.getFont());
		g.drawString("Game Over", (SCREEN_WIDTH - metrics2.stringWidth("Game Over"))/2, SCREEN_HEIGHT/2);
	}
		
	@Override
	public void actionPerformed(ActionEvent e) {
		if(running) {
			move();
			checkApple();
			checkCollision();	
		}
		repaint();
		if(e.getSource()==restart) {
			JFrame pre = Snake_Game.frame;
			pre.dispose();
			Snake_Game.gameFrame();
		}
	}
	
	public class MyKeyAdapter extends KeyAdapter{
		@Override
		public void keyPressed(KeyEvent e) {
			switch(e.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				if(direction != 'R') {
					direction = 'L';
				}
				break;
				
			case KeyEvent.VK_RIGHT:
				if(direction != 'L') {
					direction = 'R';
				}
				break;
				
			case KeyEvent.VK_UP:
				if(direction != 'D') {
					direction = 'U';
				}
				break;
				
			case KeyEvent.VK_DOWN:
				if(direction != 'U') {
					direction = 'D';
				}
				break;
			}
			
		}
	}

}

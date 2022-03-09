import javax.swing.*;

public class Snake_Game
{
	static JFrame frame;
	static void gameFrame() {
		frame = new JFrame();
		frame.add(new GamePanel());
		frame.setTitle("Snake");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.pack();
		frame.setVisible(true);
		frame.setLocationRelativeTo(null); //sets the screen in the center of the window
	}
	public static void main(String[] args) 
	{
		gameFrame();
	}
}

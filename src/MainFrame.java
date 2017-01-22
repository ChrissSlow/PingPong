import java.awt.Color;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.ImageObserver;
import java.util.ArrayList;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {

	public static final int imageWidth = 330;
	public static final int imageHeight = 360;
	public InputOutput inputOutput = new InputOutput(this);
	public boolean stop = false;
	public boolean learning = true;
	ImagePanel canvas = new ImagePanel();
	Agent ai = Agent.getInstance();

	public void run() {

		int xBall = 5, yBall = 6, xSchlaeger = 5, xV = 1, yV = 1;
		int score = 0;
		Color ballColor = Color.green;

		while (!stop) {
			// double action=(2.0*Math.random()-1.0);
			// ai.getActionWithoutLearning()
			// action 0 = link //action 1 = stehen bleiben //action 2 = rechts
			int action;

			action = ai.getAction(learning);

			if (action == 0) {
				xSchlaeger--;
			}
			if (action == 2) {
				xSchlaeger++;
			}
			if (xSchlaeger < 0) {
				xSchlaeger = 0;
			}
			if (xSchlaeger > 8) {
				xSchlaeger = 8;
			}

			xBall += xV;
			yBall += yV;
			if (xBall > 9 || xBall < 1) {
				xV = -xV; // change direction if at wall
			}
			if (yBall > 10 || yBall < 1) {
				yV = -yV; // change direction if at wall
			}

			inputOutput.fillRect(0, 0, imageWidth, imageHeight, Color.black);
			inputOutput.fillRect(xBall * 30, yBall * 30, 30, 30, ballColor);
			inputOutput.fillRect(xSchlaeger * 30, 11 * 30 + 26, 90, 10, Color.orange);
			repaint();
			validate();

			if (yBall == 11) {
				if (xSchlaeger == xBall || xSchlaeger == xBall - 1 || xSchlaeger == xBall - 2) {
					// set state with positive reward
					ai.setNewStateAndUpdateQValueOfLastState(xBall, yBall, xSchlaeger, xV, yV, 1.0, learning);

					Statistik.getInstance().positivepp();
					ballColor = Color.green;
				} else {
					// set state with negative reward
					ai.setNewStateAndUpdateQValueOfLastState(xBall, yBall, xSchlaeger, xV, yV, -1.0, learning);

					Statistik.getInstance().failurepp();
					ballColor = Color.red;
				}
			} else {
				// set state without reward

				ai.setNewStateAndUpdateQValueOfLastState(xBall, yBall, xSchlaeger, xV, yV, 0.0, learning);

				// ballColor = Color.white;
			}

			try {
				if (Statistik.getInstance().getCount() < 5000) {
					Thread.sleep(0);
				} else if (Statistik.getInstance().getCount() < 10000) {
					learning = false;
					Thread.sleep(0);
				} else {
					learning = false;
					Thread.sleep(100);
				}
			} catch (InterruptedException ex) {
				Thread.currentThread().interrupt();
			}
		}

		setVisible(false);
		dispose();
	}

	public MainFrame(String[] args) {
		super("PingPong");

		getContentPane().setSize(imageWidth, imageHeight);
		setSize(imageWidth + 50, imageHeight + 50);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);

		canvas.img = createImage(imageWidth, imageHeight);

		add(canvas);

		run();
	}

	/**
	 * Construct main frame
	 * 
	 * @param args
	 *            passed to MainFrame
	 */
	public static void main(String[] args) {
		new MainFrame(args);
	}
}

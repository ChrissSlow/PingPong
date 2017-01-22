import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class InputOutput {

	public MainFrame	mainFrame	= null;
	Graphics			graphics	= null;

	public InputOutput(MainFrame frame) {
		this.mainFrame = frame;
	}

	public synchronized void drawPixel(int x, int y, Color color) {
		if (graphics == null)
			graphics = mainFrame.canvas.img.getGraphics();

		graphics.setColor(color);
		graphics.fillRect(x, y, 1, 1);

	}

	public synchronized void fillRect(int x, int y, int width, int height,
			Color color) {
		if (graphics == null)
			graphics = mainFrame.canvas.img.getGraphics();

		graphics.setColor(color);
		graphics.fillRect(x, y, width, height);
	}
}

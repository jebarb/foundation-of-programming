package a7;

import java.awt.BorderLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.DecimalFormat;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class PixelInspectorWidget extends JPanel implements MouseListener {

	private static final DecimalFormat NUM_FORMAT = new DecimalFormat("#.##");

	private FrameView frame_view;
    private JLabel info;

	public PixelInspectorWidget(Frame2D f) {
		setLayout(new BorderLayout());
		
		frame_view = new FrameView(f.createObservable());
		frame_view.addMouseListener(this);
		add(frame_view, BorderLayout.CENTER);

		info = new JLabel();
        updateInfo(0, 0);
		add(info, BorderLayout.WEST);
	}

    private void updateInfo(int x, int y) {
        info.setText("<html>X: " + x +
                "<br><br>Y: " + y +
                "<br><br>Red: " + NUM_FORMAT.format(getPixel(x, y).getRed()) +
                "<br><br>Green: " + NUM_FORMAT.format(getPixel(x, y).getGreen()) +
                "<br><br>Blue: " + NUM_FORMAT.format(getPixel(x, y).getBlue()) +
                "<br><br>Brightness: " + NUM_FORMAT.format(getPixel(x, y).getIntensity()) + "</html>");
    }

	@Override
	public void mouseClicked(MouseEvent e) {
		updateInfo(e.getX(), e.getY());
	}

	private Pixel getPixel(int x, int y) {
		return this.frame_view.getFrame().getPixel(x, y);
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

}

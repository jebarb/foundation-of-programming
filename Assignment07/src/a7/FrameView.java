package a7;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class FrameView extends Canvas implements ROIObserver {

	private ObservableFrame2D frame;
	private BufferedImage buffered_image;
	
	public FrameView(ObservableFrame2D f) {
		setFrame(f);
	}

	public void setFrame(ObservableFrame2D f) {
		if (frame == f) {
			return;
		}
		
		if (frame != null) {
			frame.unregisterROIObserver(this);
		}
		
		frame = f;
		frame.registerROIObserver(this);
		buffered_image = new BufferedImage(f.getWidth(), f.getHeight(), BufferedImage.TYPE_INT_RGB);
		this.setPreferredSize(new Dimension(f.getWidth(), f.getHeight()));
		this.setSize(new Dimension(f.getWidth(), f.getHeight()));
		notify(frame, new Region(new Coordinate(0,0), new Coordinate(f.getWidth()-1,f.getHeight()-1)));		
	}
	
	public ObservableFrame2D getFrame() {
		return frame;
	}
	
	public void paint(Graphics g) {
		g.drawImage(buffered_image, 0, 0, this);
	}

	public void notify(ObservableFrame2D frame, Region area) {
		for (int x=area.getLeft(); x<=area.getRight(); x++) {
			for (int y=area.getTop(); y<=area.getBottom(); y++) {
				buffered_image.setRGB(x, y, A7Helper.pixelToRGB(frame.getPixel(x, y)));
			}
		}
		repaint();
	}
}

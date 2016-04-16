package a7;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


public class ImageAdjusterWidget extends JPanel implements ChangeListener {

    private Frame2D original;
    private FrameView frame_view;
    private JSlider blur, brightness, saturation;

    public ImageAdjusterWidget(Frame2D f) {
        setLayout(new BorderLayout());

        this.original = f;

        frame_view = new FrameView(f.createObservable());
        add(frame_view, BorderLayout.CENTER);

        JPanel sliderPanel = new JPanel();
        sliderPanel.setLayout(new BoxLayout(sliderPanel, BoxLayout.Y_AXIS));
        JPanel blurPanel = new JPanel();
        blurPanel.setLayout(new BoxLayout(blurPanel, BoxLayout.LINE_AXIS));
        JPanel brightnessPanel = new JPanel();
        brightnessPanel.setLayout(new BoxLayout(brightnessPanel, BoxLayout.LINE_AXIS));
        JPanel saturationPanel = new JPanel();
        saturationPanel.setLayout(new BoxLayout(saturationPanel, BoxLayout.LINE_AXIS));


        blur = new JSlider(0, 5, 0);
        blur.setSnapToTicks(true);
        blur.setPaintTicks(true);
        blur.setPaintLabels(true);
        blur.setMajorTickSpacing(1);
        blur.addChangeListener(this);
        blurPanel.add(new JLabel("Blur"));
        blurPanel.add(blur);

        brightness = new JSlider(-100, 100, 0);
        brightness.setPaintTicks(true);
        brightness.setPaintLabels(true);
        brightness.setMajorTickSpacing(25);
        brightness.addChangeListener(this);
        brightnessPanel.add(new JLabel("Brightness"));
        brightnessPanel.add(brightness);

        saturation = new JSlider(-100, 100, 0);
        saturation.setPaintTicks(true);
        saturation.setPaintLabels(true);
        saturation.setMajorTickSpacing(25);
        saturation.addChangeListener(this);
        saturationPanel.add(new JLabel("Saturation"));
        saturationPanel.add(saturation);

        sliderPanel.add(blurPanel);
        sliderPanel.add(brightnessPanel);
        sliderPanel.add(saturationPanel);

        add(sliderPanel, BorderLayout.SOUTH);
    }

    private void updateImage() {
        frame_view.setFrame(original.lightenOrDarken((double)brightness.getValue()/100.0)
                .blur(blur.getValue())
                .saturate((double)saturation.getValue()/100.0).createObservable());
    }

    public void stateChanged(ChangeEvent e) {
        JSlider source = (JSlider) e.getSource();
        if (!source.getValueIsAdjusting()) {
            updateImage();
        }
    }

}



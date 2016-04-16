package a7;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class FramePuzzleWidget extends JPanel implements KeyListener, MouseListener {

    private FrameView[][] frames;
    private int solid_x, solid_y;

    public FramePuzzleWidget(Frame2D f) {
        setLayout(new GridLayout(5, 5));

        this.frames = new FrameView[5][5];
        this.solid_x = 4;
        this.solid_y = 4;
        int width = f.getWidth() - f.getWidth() % 5;
        int height = f.getHeight() - f.getHeight() % 5;

        for (int y = 0; y * (f.getHeight() / 5) < height; y++) {
            for (int x = 0; x * (f.getWidth() / 5) < width; x++) {
                frames[x][y] = new FrameView(f.extract(x * (f.getWidth() / 5), y * (f.getHeight() / 5),
                        width / 5, height / 5).createObservable());
                frames[x][y].addKeyListener(this);
                frames[x][y].addMouseListener(this);
                add(frames[x][y]);
            }
        }
        frames[4][4].setFrame(frames[4][4].getFrame().lightenOrDarken(-1).createObservable());
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP && solid_y != 4) {
                swap_solid(solid_x, solid_y+1);
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN && solid_y != 0) {
                swap_solid(solid_x, solid_y-1);
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT && solid_x != 4) {
                swap_solid(solid_x+1, solid_y);
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT&& solid_x != 0) {
                swap_solid(solid_x-1, solid_y);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int x = 0;
        int y = 0;
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (e.getSource().equals(frames[j][i])) {
                    x = j;
                    y = i;
                }
            }
        }
        if (x == solid_x) {
            if (y < solid_y) {
                for (int i = solid_y-1; i >= y; i--) {
                    swap_solid(x, i);
                }
            } else if (y > solid_y) {
                for (int i = solid_y+1; i <= y; i++) {
                    swap_solid(x, i);
                }
            }
        } else if (y == solid_y) {
            if (x < solid_x) {
                for (int i = solid_x-1; i >= x; i--) {
                    swap_solid(i, y);
                }
            } else if (x > solid_x) {
                for (int i = solid_x+1; i <= x; i++) {
                    swap_solid(i, y);
                }
            }
        }
    }

    private void swap_solid(int x, int y) {
        frames[solid_x][solid_y].setFrame(frames[x][y].getFrame().createObservable());
        frames[x][y].setFrame(frames[x][y].getFrame().lightenOrDarken(-1).createObservable());
        solid_x = x;
        solid_y = y;
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

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

}

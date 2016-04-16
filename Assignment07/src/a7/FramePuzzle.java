package a7;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class FramePuzzle {

    public static void main(String[] args) throws IOException {
        //Frame2D f = A7Helper.readFromURL("http://www.cs.unc.edu/~kmp/kmp.jpg");
        Frame2D f = A7Helper.readFromURL("https://upload.wikimedia.org/wikipedia/commons/thumb/7/7b/Richard_Stallman_-_F%C3%AAte_de_l%27Humanit%C3%A9_2014_-_010.jpg/512px-Richard_Stallman_-_F%C3%AAte_de_l%27Humanit%C3%A9_2014_-_010.jpg");
        //Frame2D f = A7Helper.readFromURL("https://i.imgur.com/9ngb1KN.jpg");
        FramePuzzleWidget puzzle_widget = new FramePuzzleWidget(f);

        JFrame main_frame = new JFrame();
        main_frame.setTitle("Assignment07 Frame Puzzle");
        main_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel top_panel = new JPanel();
        top_panel.setLayout(new BorderLayout());
        top_panel.add(puzzle_widget, BorderLayout.CENTER);
        main_frame.setContentPane(top_panel);

        main_frame.pack();
        main_frame.setVisible(true);
    }

}

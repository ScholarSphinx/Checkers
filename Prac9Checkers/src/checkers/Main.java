package checkers;

import java.awt.Dimension;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        JFrame checkersFrame = new JFrame();
        checkersFrame.setLocationRelativeTo(null);
        checkersFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        checkersFrame.setTitle("CheckerBoard");
        checkersFrame.add(new Board());
        checkersFrame.pack();
        checkersFrame.setLocationRelativeTo(null);
        checkersFrame.setVisible(true);
    }
}

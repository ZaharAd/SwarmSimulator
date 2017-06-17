package gui;

import javax.swing.*;
import java.awt.*;

/**
 * Created by zahar on 16/06/17.
 */

public class AgentPaintedPanel extends JPanel {
    private final int MARGIN = 3;
    private int X_1, X_2;// width
    private int Y_1, Y_2;// height

    public void paintPixel(int x1 , int y1 , int x2, int y2){
        X_1 = x1;
        Y_1 = y1;

        X_2 = x2;
        Y_2 = y2;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        for (int x = X_1 - MARGIN; x < X_1 + MARGIN; x++) {
            for (int y = Y_1 - MARGIN; y < Y_1 + MARGIN; y++) {
                g.setColor(Color.RED);
                g.drawLine(x, y, x, y);
            }
        }

        for (int x = X_2 - MARGIN; x < X_2 + MARGIN; x++) {
            for (int y = Y_2 - MARGIN; y < Y_2 + MARGIN; y++) {
                g.setColor(Color.RED);
                g.drawLine(x, y, x, y);
            }
        }
    }
}

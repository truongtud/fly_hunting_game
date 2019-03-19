package Model;


import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author truongtud
 */
public class Fly extends JButton {

    public double x;
    public double y;

    public Fly(double[] points) {
        this.x = points[0];
        this.y = points[1];
        this.setBackground(new java.awt.Color(255, 255, 255));
        InputStream in = getClass().getResourceAsStream("/fliege.jpg");
        try {
            ImageIcon img = new ImageIcon(ImageIO.read(in));
            this.setIcon(img);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        this.setBorder(null);
        this.setBorderPainted(false);
        this.setContentAreaFilled(false);
    }
}

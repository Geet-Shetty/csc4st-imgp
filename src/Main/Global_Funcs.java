package Main;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Global_Funcs {
    public static void frame_settings(JFrame jf, int width, int height, LayoutManager ls, String Title, boolean exit, boolean vis, int x, int y){
        jf.setSize(width, height);
        jf.setLayout(ls);
        jf.setTitle(Title);
        jf.setLocationRelativeTo(null);// Center a frame
        if(exit)
            jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setVisible(vis); // Display the frame
        if(x > -1 && y > -1)
            jf.setLocation(x, y);
    }

    public static int scale(int x, int h_min, int h_max, int a, int b){
        return (int)( (((b-a)*(x-h_min))/(double)(h_max-h_min)) + a );
    }

    public static int clamp(int val){
        if(val < 0)
            return 0;
        else if(val > 255)
            return 255;
        else
            return val;
    }

    public static BufferedImage resize(BufferedImage img, int width)
    {
        double aspRatio = (double) img.getHeight() / (double) img.getWidth();
        double height = width * aspRatio;
        Image tmp = img.getScaledInstance(width, (int) height, Image.SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage(width, (int) height, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return dimg;
    }

    public static void main(String[] args) {
        System.out.println(scale(50,0,100,0,30));
    }
}

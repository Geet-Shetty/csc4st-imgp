package Histogram;

import javax.swing.*;
import java.awt.*;

public class HistogramGUI extends JPanel {

    private boolean draw_all = true;
    private int d_i = 0;

    private int[][] scaled_hist;
    private int w = 0;
    private int h = 0;

    public HistogramGUI(Color c, int w, int h){ // black,256,100
        if (w > -1 || h > -1)
            setPreferredSize(new Dimension(w,h));
        setBackground(c);
        this.w = w;
        this.h = h;
    }

    public void setHistos(int[][] s_h){
        scaled_hist = s_h;
    }

    public void setDraw_all(boolean flag){ draw_all = flag; }

    public void setD_i(int i) { d_i = i; }

    public void setColor(Graphics2D g2d, int num){
        switch (num) {
            case 0:
                g2d.setColor(Color.RED);
                break;
            case 1:
                g2d.setColor(Color.GREEN);
                break;
            case 2:
                g2d.setColor(Color.BLUE);
                break;
        }
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setPaintMode();
        if(scaled_hist != null){
            if(draw_all) {
                for (int i = 0; i < 3; i++) {
                    setColor(g2d,i);
                    for (int j = 0; j < scaled_hist[0].length; j++) {
                        g2d.drawLine(j, h+60-1, j, -scaled_hist[i][j]+h-1);
                    }
                }
            } else {
                setColor(g2d,d_i);
                for (int j = 0; j < scaled_hist[0].length; j++) {
                    g2d.drawLine(j, h+60-1, j, -scaled_hist[d_i][j]+h-1);
                }
            }
        }
    }

}

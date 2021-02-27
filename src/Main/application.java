package Main;

import Histogram.HistogramFrame;
import Image.Image;
import Image.ImageObjectAbstract;
import Menu.*;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class application extends JFrame{

    private Image picture = new Image();

    // Actual.Main-Frame elements
    private JPanel west_panel = new JPanel(new GridLayout(1,1,0,0));
    private MainIP center_panel = new MainIP(null, Color.PINK,-1,-1); // image display
    private IconIP inner_panel = new IconIP(null, Color.darkGray,70,70);

    // Sub-Frame elements
    private JFrame sub_frame = new JFrame();
    private ComponentIP sub_panel = new ComponentIP(null, Color.PINK,-1,-1); // sub image display
    private JScrollBar h_sb = new JScrollBar(JScrollBar.HORIZONTAL);
    private JScrollBar v_sb = new JScrollBar(JScrollBar.VERTICAL);

    private boolean sf_vis = true;

    // Histo-Frame elements
    HistogramFrame hist_frame = new HistogramFrame(272,270, new BorderLayout(), "Histogram", false,true, getX(), getY());

    // MENU
    MainMenu menu = new MainMenu(picture,hist_frame,this,sub_frame);

    public application()  {

        // Actual.Main Frame
        Global_Funcs.frame_settings(this, 500,500, new BorderLayout(), "Main Window", true,true, -1, -1);
        west_panel.setBackground(Color.darkGray);
        west_panel.setPreferredSize(new Dimension(105,500));
        west_panel.add(inner_panel);
        add(west_panel, BorderLayout.WEST);
        add(center_panel, BorderLayout.CENTER);

        // File MENU
        setJMenuBar(menu.getMainDropDown());

        // Sub Frame
        Global_Funcs.frame_settings(sub_frame, 400,400, new BorderLayout(), "Component Window", false,true, getX() + getWidth(), getY());
        sub_frame.add(sub_panel,BorderLayout.CENTER);
        sub_frame.add(v_sb,BorderLayout.EAST);
        sub_frame.add(h_sb,BorderLayout.SOUTH);

        // Histo Settings
        hist_frame.setLocation(new Point(getX() - 15 - (getWidth()/2), getY()));
        hist_frame.toggleVis();

        // Sub Frame Listeners
        v_sb.addAdjustmentListener(
            new AdjustmentListener() {
                @Override
                public void adjustmentValueChanged(AdjustmentEvent e) {
                    double value = v_sb.getValue() * 3;
                    v_sb.setMaximum(sub_panel.getHeight());
                    double maximumValue = v_sb.getMaximum();
                    double newY = value * sub_panel.getHeight() * 3 / maximumValue;
                    sub_panel.setYCoordinate((int)newY);
                }
            }
        );

        h_sb.addAdjustmentListener(
                new AdjustmentListener() {
                    @Override
                    public void adjustmentValueChanged(AdjustmentEvent e) {
                        double value = h_sb.getValue() * 3;
                        h_sb.setMaximum(sub_panel.getWidth());
                        double maximumValue = h_sb.getMaximum();
                        double newX = (value * sub_panel.getWidth() * 3/ maximumValue);
                        sub_panel.setXCoordinate((int)newX);
                    }
                }
        );

    }

    public class ImagePanel extends JPanel {
        public ImagePanel(LayoutManager L, Color c, int w, int h){
            super(L);
            if (w > -1 || h > -1)
                this.setPreferredSize(new Dimension(w,h));
            this.setBackground(c);
        }
    }

    public class MainIP extends ImagePanel {
        public MainIP(LayoutManager L, Color c, int w, int h) {
            super(L, c, w, h);
        }

        @Override
        public void paintComponent(Graphics g){
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setPaintMode();
            if(!Objects.isNull(picture.getImage())) {
                g2d.drawImage(picture.toBI(), 0, 0, picture.getWidth(), picture.getHeight(), null);
            }
        }
    }

    public class IconIP extends ImagePanel {
        public IconIP(LayoutManager L, Color c, int w, int h) {
            super(L, c, w, h);
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setPaintMode();
            if (!Objects.isNull(picture.getImage())) {
                BufferedImage bi = Global_Funcs.resize(picture.toBI(), 100);
                setSize(new Dimension(bi.getWidth(), bi.getHeight()));
                g2d.drawImage(bi, 0, 0, bi.getWidth(), bi.getHeight(), null);
            }
        }
    }

    public class ComponentIP extends ImagePanel{
        int x;
        int y;

        public ComponentIP(LayoutManager L, Color c, int w, int h) {
            super(L, c, w, h);
        }

        public void setXCoordinate(int x) {
            this.x = x;
            repaint();
        }

        public void setYCoordinate(int y) {
            this.y = y;
            repaint();
        }

        @Override
        public void paint(Graphics g) {
            super.paint(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setPaintMode();
            if (!Objects.isNull(picture.getImage())) {
                try {
                    g2d.drawImage(picture.toBIC(ImageObjectAbstract.COMPONENTS.RED), 0 - x, 0 - y, picture.getWidth(), picture.getHeight(), null);
                    g2d.drawImage(picture.toBIC(ImageObjectAbstract.COMPONENTS.GREEN), 0 - x, picture.getHeight() - y, picture.getWidth(), picture.getHeight(), null);
                    g2d.drawImage(picture.toBIC(ImageObjectAbstract.COMPONENTS.BLUE), 0 - x, picture.getHeight() + picture.getHeight() - y, picture.getWidth(), picture.getHeight(), null);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String args[]){
    }
}

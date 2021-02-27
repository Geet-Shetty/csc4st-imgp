package Histogram;


import Main.Global_Funcs;
import Menu.Menu;
import Image.ImageObjectAbstract;
import Image.Image;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HistogramFrame extends JFrame {

    private Histogram histo = new Histogram();

    private HistogramGUI hist_panel = new HistogramGUI(Color.black, 256,100);

    private String[] r_names = {"min","max","mode","mean","std"};
    private String[] c_names = {"R", "G", "B"};
    private StatsGridUI stats = new StatsGridUI(4,6,1,1);

    private String[] modes = {"RGB","R", "G", "B"};
    private Menu hist_menu = new Menu("mode", null, modes,null);

    private boolean vis = true;

    public HistogramFrame(int width, int height, LayoutManager L, String title, boolean exit, boolean vis, int x, int y){
        Global_Funcs.frame_settings(this, width,height, L, title, exit, false, x, y);

        add(hist_panel,BorderLayout.SOUTH);
        add(stats, BorderLayout.NORTH);

        // menu
        setJMenuBar(hist_menu.getDropdown());

        JMenu h_jm = hist_menu.getFileD();
        hist_menu.getItem("RGB").addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        hist_panel.setDraw_all(true);
                        hist_panel.repaint();
                    }
                }
        );

        hist_menu.getItem("R").addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        hist_panel.setDraw_all(false);
                        hist_panel.setD_i(ImageObjectAbstract.COMPONENTS.RED.ordinal());
                        hist_panel.repaint();
                    }
                }
        );

        hist_menu.getItem("G").addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        hist_panel.setDraw_all(false);
                        hist_panel.setD_i(ImageObjectAbstract.COMPONENTS.GREEN.ordinal());
                        hist_panel.repaint();
                    }
                }
        );

        hist_menu.getItem("B").addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        hist_panel.setDraw_all(false);
                        hist_panel.setD_i(ImageObjectAbstract.COMPONENTS.BLUE.ordinal());
                        hist_panel.repaint();
                    }
                }
        );

    }

    public void load(Image picture){
        histo.reload(picture);
        hist_panel.setHistos(histo.getScaledHist());
        stats.loadTable(c_names,r_names, histo.getData());
        stats.revalidate();
        repaint();
    }

    public Histogram getHisto(){
        return histo;
    }

    public void toggleVis(){
        setVisible(vis);
    }
}

package Menu;

import Dialogs.Dialog;
import Histogram.HistogramFrame;
import Image.Image;
import Main.application;
import Menu.Menu;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class MainMenu {
    private String[] items = {"Open", "Save", "Terminate", "CW VIS", "HW VIS"};
    private Menu menu = new Menu("File","D:/stuff/image", items,null);

    private String[] items1 = {"Gamma Correction"};
    private Menu menu1 = new Menu("Point Ops",null,items1,menu.getDropdown());

    private String[] sub1_items1 = {"Add", "Sub", "Mult", "Div"};
    private Menu sub1_menu1 = new Menu("Pixel Math", null, sub1_items1, menu.getDropdown());

    private String[] sub2_items2 = {"Stretch", "Stretch Auto"};
    private Menu sub2_menu2 = new Menu("Constrast", null, sub2_items2, menu.getDropdown());

    private String[] items2 = {"Alpha Blend"};
    private Menu menu2 = new Menu("Image Ops", "D:/stuff/image", items2, menu.getDropdown());

    private String[] sub3_items3 = {"Add", "Sub"};
    private Menu sub3_menu3 = new Menu("Image Math", null, sub3_items3, menu.getDropdown());

    private Menu[] sub_menus = {sub1_menu1, sub2_menu2};

    public MainMenu(Image picture, HistogramFrame hist_frame, application main, JFrame sub_frame){
        menu2.addSubMenu(sub3_menu3);
        menu1.addSubMenus(sub_menus);

        // Menu Listeners
        JFileChooser jfile =  menu.getChooser();
        JMenu jm = menu.getFileD();
        menu.getItem("Open").addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        jfile.showOpenDialog(jm);
                        try {
                            String path = jfile.getSelectedFile().toString();
                            picture.loadImage(path);

                            hist_frame.load(picture);

                            main.repaint();
                            sub_frame.repaint();
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                    }
                }
        );

        menu.getItem("Save").addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        jfile.showSaveDialog(jm);
                        try {
                            picture.saveImage(jfile.getSelectedFile().toString(),"PNG");
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        } catch (NullPointerException nullPointerException){
                            nullPointerException.printStackTrace();
                        }
                    }
                }
        );

        menu.getItem("Terminate").addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        System.exit(0);
                    }
                }
        );

        menu.getItem("CW VIS").addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        sub_frame.setVisible(true);
                    }
                }
        );

        menu.getItem("HW VIS").addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
//                        hf_vis = !hf_vis;
//                        hist_frame.setVisible(hf_vis);
                        hist_frame.toggleVis();
                    }
                }
        );

        // PIXEL OPS

        String[] inputs = {"R", "G", "B"};
        menu1.getItem("Gamma Correction").addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Dialogs.Dialog d = new Dialog(inputs,"G C");

                        try {
                            picture.setGamma_table(Double.parseDouble(d.getField("R")), Double.parseDouble(d.getField("G")), Double.parseDouble(d.getField("B")));
                        } catch(Exception Null) {}
                        hist_frame.load(picture);

                        main.repaint();
                        sub_frame.repaint();
                    }
                }
        );

        sub1_menu1.getItem("Add").addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Dialogs.Dialog d = new Dialog(inputs,"Add");
                        try {
                            picture.add(Integer.parseInt(d.getField("R")), Integer.parseInt(d.getField("G")), Integer.parseInt(d.getField("B")));
                        } catch(Exception Null) {}
                        hist_frame.load(picture);

                        main.repaint();
                        sub_frame.repaint();
                    }
                }
        );

        sub1_menu1.getItem("Sub").addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Dialogs.Dialog d = new Dialog(inputs,"Sub");
                        try {
                            picture.sub(Integer.parseInt(d.getField("R")), Integer.parseInt(d.getField("G")), Integer.parseInt(d.getField("B")));
                        } catch(Exception Null) {}
                        hist_frame.load(picture);

                        main.repaint();
                        sub_frame.repaint();
                    }
                }
        );

        sub1_menu1.getItem("Mult").addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Dialogs.Dialog d = new Dialog(inputs,"Mult");
                        try {
                            picture.mult(Double.parseDouble(d.getField("R")), Double.parseDouble(d.getField("G")), Double.parseDouble(d.getField("B")));
                        } catch(Exception Null) {}
                        hist_frame.load(picture);

                        main.repaint();
                        sub_frame.repaint();
                    }
                }
        );

        sub1_menu1.getItem("Div").addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Dialogs.Dialog d = new Dialog(inputs,"Div");
                        try {
                            picture.div(Double.parseDouble(d.getField("R")), Double.parseDouble(d.getField("G")), Double.parseDouble(d.getField("B")));
                        } catch(Exception Null) {}
                        hist_frame.load(picture);

                        main.repaint();
                        sub_frame.repaint();
                    }
                }
        );

        sub2_menu2.getItem("Stretch").addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String[] inputs = {"Low", "High"};
                        Dialog d = new Dialog(inputs, "Contrast Enhancement");
                        try {
                            picture.stretch(Integer.parseInt(d.getField("Low")), Integer.parseInt(d.getField("High")));
                        } catch(Exception Null) {}
                        hist_frame.load(picture);

                        main.repaint();
                        sub_frame.repaint();
                    }
                }
        );

        sub2_menu2.getItem("Stretch Auto").addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            picture.stretch_auto(hist_frame.getHisto());
                        } catch(Exception Null) {}
                        hist_frame.load(picture);

                        main.repaint();
                        sub_frame.repaint();
                    }
                }
        );

        // IMAGE OPS

        JFileChooser jfile2 =  menu2.getChooser();
        JMenu jm2 = menu2.getFileD();
        menu2.getItem("Alpha Blend").addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        jfile2.showOpenDialog(jm2);
                        try {
                            String path = jfile2.getSelectedFile().toString();
                            String[] inputs = {"Alpha"};
                            Image picture2 = new Image();
                            picture2.loadImage(path);
                            Dialog d = new Dialog(inputs, "Alpha Blend");
                            picture.blend(Double.parseDouble(d.getField("Alpha")), picture2);
                        } catch (Exception Null) {
                        }
                        hist_frame.load(picture);

                        main.repaint();
                        sub_frame.repaint();
                    }
                }
        );

        sub3_menu3.getItem("Add").addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        jfile2.showOpenDialog(jm2);
                        try {
                            String path = jfile2.getSelectedFile().toString();
                            Image picture2 = new Image();
                            picture2.loadImage(path);
                            picture.addImage(picture2);
                        } catch (Exception Null) {
                        }
                        hist_frame.load(picture);

                        main.repaint();
                        sub_frame.repaint();
                    }
                }
        );

        sub3_menu3.getItem("Sub").addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        jfile2.showOpenDialog(jm2);
                        try {
                            String path = jfile2.getSelectedFile().toString();
                            Image picture2 = new Image();
                            picture2.loadImage(path);
                            picture.subImage(picture2);
                        } catch (Exception Null) {
                        }
                        hist_frame.load(picture);

                        main.repaint();
                        sub_frame.repaint();
                    }
                }
        );
    }

    public JMenuBar getMainDropDown(){
        return menu.getDropdown();
    }
}

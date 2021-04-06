package Menu;

import Dialogs.Dialog;
import Dialogs.DialogOption;
import Histogram.HistogramFrame;
import Image.FilterOps;
import Image.Image;
import Main.application;
import Menu.Menu;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import static Image.FilterOps.*;

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

    private String[] bin_items = {"Input", "Otsu"};
    private Menu bin_menu = new Menu("Binarize", null, bin_items, menu.getDropdown());

    private String[] hop_items = {"Equalize", "Match", "Luminance", "Desaturation"};
    private Menu histo_op_menu = new Menu("Histo Ops", "D:/stuff/image", hop_items, menu.getDropdown());

    private String[] f_items = {"General Convo", "Median", "Outlier", "Gauss"};
    private Menu filter_ops_menu = new Menu("Filter Ops", "D:/stuff/image",f_items, menu.getDropdown());

    private Menu[] sub_menus = {sub1_menu1, sub2_menu2};

    public MainMenu(Image picture, HistogramFrame hist_frame, application main, JFrame sub_frame){
        histo_op_menu.addSubMenu(bin_menu);
        menu2.addSubMenu(sub3_menu3);
        menu1.addSubMenus(sub_menus);

        // Menu Listeners
        JFileChooser jfile =  menu.getChooser();
        JMenu jm = menu.getFileD();
        menu.getItem("Open").addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        int result = jfile.showOpenDialog(jm);
                        try {
                            if(result == JFileChooser.APPROVE_OPTION) {
                                String path = jfile.getSelectedFile().toString();
                                picture.loadImage(path);

                                hist_frame.load(picture);

                                main.repaint();
                                sub_frame.repaint();
                            }
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
                        int result = jfile.showSaveDialog(jm);
                        try {
                            if(result == JFileChooser.APPROVE_OPTION) {
                                picture.saveImage(jfile.getSelectedFile().toString(), "PNG");
                            }
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
                        int result = jfile2.showOpenDialog(jm2);
                        if(result == JFileChooser.APPROVE_OPTION) {
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
                }
        );

        sub3_menu3.getItem("Sub").addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        int result = jfile2.showOpenDialog(jm2);
                        if(result == JFileChooser.APPROVE_OPTION) {
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
                }
        );

        // HISTO OPS

        JFileChooser jfile3 =  histo_op_menu.getChooser();
        JMenu jm3 = histo_op_menu.getFileD();
        histo_op_menu.getItem("Equalize").addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        hist_frame.getHisto().Equalization(picture);
                        hist_frame.load(picture);

                        main.repaint();
                        sub_frame.repaint();
                    }
                }
        );

        histo_op_menu.getItem("Match").addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        int result = jfile3.showOpenDialog(jm3);
                        if(result == JFileChooser.APPROVE_OPTION) {
                            try {
                                String path = jfile3.getSelectedFile().toString();
                                Image picture2 = new Image();
                                picture2.loadImage(path);
                                picture.match(hist_frame.getHisto(), picture2);
                            } catch (Exception Null) {
                            }
                            hist_frame.load(picture);

                            main.repaint();
                            sub_frame.repaint();
                        }
                    }
                }
        );

        histo_op_menu.getItem("Luminance").addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        picture.setLuminance();
                        hist_frame.load(picture);

                        main.repaint();
                        sub_frame.repaint();
                    }
                }
        );

        histo_op_menu.getItem("Desaturation").addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String[] inputs = {"s"};
                        Dialog d = new Dialog(inputs,"Desaturation");
                        picture.deSaturation(Double.parseDouble(d.getField("s")));
                        hist_frame.load(picture);

                        main.repaint();
                        sub_frame.repaint();
                    }
                }
        );

        bin_menu.getItem("Input").addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String[] inputs = {"Threshold"};
                        Dialog d = new Dialog(inputs,"Binarize");
                        picture.binarize(Integer.parseInt(d.getField("Threshold")));
                        hist_frame.load(picture);

                        main.repaint();
                        sub_frame.repaint();

                    }
                }
        );

        bin_menu.getItem("Otsu").addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        picture.Otsu_Binarize(hist_frame.getHisto());
                        hist_frame.load(picture);

                        main.repaint();
                        sub_frame.repaint();
                    }
                }
        );

        // FILTER OPS
        String[] options = {"Ignore","Extend","Mirror"};

        JFileChooser jfile4 =  filter_ops_menu.getChooser();
        JMenu jm4 = filter_ops_menu.getFileD();
        filter_ops_menu.getItem("General Convo").addActionListener( // TODO: decide on making sub menus here for padding option
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        int result = jfile4.showOpenDialog(jm4);
                        if(result == JFileChooser.APPROVE_OPTION) {
                            try {
                                String path = jfile4.getSelectedFile().toString();

                                fileData f = FilterOps.read_kernal(path);

                                DialogOption d = new DialogOption(options, "Pick Padding Method:", "Padding Selection");
                                switch(d.getSelected()){
                                    case "Ignore":
                                        apply_convolve(picture,f.kernel,f.origin, padding.IGNORE);
                                        break;
                                    case "Extend":
                                        apply_convolve(picture,f.kernel,f.origin, padding.EXTEND);
                                        break;
                                    case "Mirror":
                                        apply_convolve(picture,f.kernel,f.origin, padding.MIRROR);
                                }
                                hist_frame.load(picture);
                                main.repaint();
                                sub_frame.repaint();
                            } catch (Exception Null) {
                            }
                        }
                    }
                }
        );

        filter_ops_menu.getItem("Median").addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        DialogOption d_o = new DialogOption(options, "Pick Padding Method:", "Padding Selection");
                        String[] dim = {"Row:", "Col:"};
                        Dialog d = new Dialog(dim, "Dimensions");
                        switch(d_o.getSelected()){
                            case "Ignore":
                                apply_median(picture,new int[]{Integer.parseInt(d.getField("Row:")), Integer.parseInt(d.getField("Col:"))}, padding.IGNORE);
                                break;
                            case "Extend":
                                apply_median(picture,new int[]{Integer.parseInt(d.getField("Row:")), Integer.parseInt(d.getField("Col:"))}, padding.EXTEND);
                                break;
                            case "Mirror":
                                apply_median(picture,new int[]{Integer.parseInt(d.getField("Row:")), Integer.parseInt(d.getField("Col:"))}, padding.MIRROR);
                        }
                        hist_frame.load(picture);
                        main.repaint();
                        sub_frame.repaint();
                    }
                }
        );

        filter_ops_menu.getItem("Outlier").addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        DialogOption d_o = new DialogOption(options, "Pick Padding Method:", "Padding Selection");
                        String[] dim = {"Row:", "Col:", "Threshold:"};
                        Dialog d = new Dialog(dim, "Dimensions");
                        switch(d_o.getSelected()){
                            case "Ignore":
                                apply_outlier(picture,new int[]{Integer.parseInt(d.getField("Row:")), Integer.parseInt(d.getField("Col:"))}, Integer.parseInt(d.getField("Threshold:")) ,padding.IGNORE);
                                break;
                            case "Extend":
                                apply_outlier(picture,new int[]{Integer.parseInt(d.getField("Row:")), Integer.parseInt(d.getField("Col:"))}, Integer.parseInt(d.getField("Threshold:")) ,padding.EXTEND);
                                break;
                            case "Mirror":
                                apply_outlier(picture,new int[]{Integer.parseInt(d.getField("Row:")), Integer.parseInt(d.getField("Col:"))}, Integer.parseInt(d.getField("Threshold:")) ,padding.MIRROR);
                        }
                        hist_frame.load(picture);
                        main.repaint();
                        sub_frame.repaint();
                    }
                }
        );

        filter_ops_menu.getItem("Gauss").addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        DialogOption d_o = new DialogOption(options, "Pick Padding Method:", "Padding Selection");
                        String[] dim = {"Row:", "Col:","Sigma X:", "Sigma Y:"};
                        Dialog d = new Dialog(dim, "Dimensions");
                        switch(d_o.getSelected()){
                            case "Ignore":
                               apply_gauss(picture, Integer.parseInt(d.getField("Row:")), Integer.parseInt(d.getField("Col:")), Integer.parseInt(d.getField("Sigma X:")), Integer.parseInt(d.getField("Sigma Y:")),padding.IGNORE);
                                break;
                            case "Extend":
                                apply_gauss(picture, Integer.parseInt(d.getField("Row:")), Integer.parseInt(d.getField("Col:")), Integer.parseInt(d.getField("Sigma X:")), Integer.parseInt(d.getField("Sigma Y:")),padding.EXTEND);
                                break;
                            case "Mirror":
                                apply_gauss(picture, Integer.parseInt(d.getField("Row:")), Integer.parseInt(d.getField("Col:")), Integer.parseInt(d.getField("Sigma X:")), Integer.parseInt(d.getField("Sigma Y:")),padding.MIRROR);
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

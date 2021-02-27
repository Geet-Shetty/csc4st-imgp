package Histogram;

import javax.swing.*;
import java.awt.*;

public class StatsGridUI extends JPanel {

    int col = 0;
    int row = 0;

    public StatsGridUI(int c, int r, int hgap, int vgap){
        this.col = c;
        this.row = r;
        setLayout(new GridLayout(r,c,hgap,vgap));
    }

    public void loadTable(String[] c_names, String[] r_names, double[][] data){
        removeAll();
        add(new JLabel(""));
        for(int i = 1; i < col; i++){
            add(new JLabel(c_names[i-1]));
        }

        for(int i = 0; i < row-1; i++){
            for(int j = 0; j < col; j++){
                if(j == 0){
                    add(new JLabel(r_names[i]));
                } else {
                    add(new JLabel(String.valueOf(data[i][j-1])));
                }
            }
        }
    }

}

package Dialogs;

import Main.Global_Funcs;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

public class Dialog {

    JFrame frame;
    JPanel panel;
    String title;

    HashMap<String, JTextField> Fields;

    public Dialog(String[] inputs, String title){
        frame = new JFrame();
        panel = new JPanel();
        this.title = title;
        Fields = new HashMap<>();

        panel.setLayout(new GridLayout(inputs.length,2,0,0));

        if(inputs != null) {
            for (String s : inputs) {
                Fields.put(s, new JTextField(0));
                panel.add(new JLabel(s));
                panel.add(Fields.get(s));
            }
        }

        frame.add(panel);
        int option = JOptionPane.showConfirmDialog(frame, panel, title, JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
        if(option == JOptionPane.CANCEL_OPTION){
            Fields = null;
        }
    }

    public String getField(String s){
        return Fields.get(s).getText();
    }

    public static void main(String[] args) {}

}

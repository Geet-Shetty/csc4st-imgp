package Dialogs;

import javax.swing.*;
import java.util.HashMap;

public class DialogOption {

    JFrame frame;
    String title;

    String selected;

    public DialogOption(String[] options, String title, String message){
        frame = new JFrame();
        this.title = title;

        Object selectionObject = JOptionPane.showInputDialog(frame, message, title, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
        String selectionString = selectionObject.toString();
        if(selectionObject != null){
            selected = selectionString;
        }
    }

    public String getSelected(){
        return selected;
    }
}

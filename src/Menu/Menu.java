package Menu;

import javax.swing.*;
import java.util.HashMap;

public class Menu {
    private JMenuBar dropdown;
    private JMenu fileD;
    private JFileChooser chooser;  // to be added for Jmenu

    private HashMap<String, JMenuItem> items = new HashMap<>();

    public Menu (String menu_name, String path, String[] menu_items, JMenuBar b){
        if(b != null)
            dropdown = b;
        else
            dropdown = new JMenuBar();
        fileD = new JMenu(menu_name);
        if(path != null)
            chooser = new JFileChooser(path);
        if(menu_items != null) {
            for (String s : menu_items) {
                items.put(s, new JMenuItem(s));
                fileD.add(items.get(s));
            }
        }
        dropdown.add(fileD);
    }

    public void addSubMenu(Menu j){
        fileD.add(j.getFileD());
    }

    public void addSubMenus(Menu[] js){
        for(Menu j: js){
            fileD.add(j.getFileD());
        }
    }

    public JMenuItem getItem(String name){
        return items.get(name);
    }

    public JMenu getFileD() {
        return fileD;
    }

    public JFileChooser getChooser() {
        return chooser;
    }

    public JMenuBar getDropdown() {
        return dropdown;
    }
}

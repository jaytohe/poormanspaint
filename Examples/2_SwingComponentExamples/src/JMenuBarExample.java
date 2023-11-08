import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

public class JMenuBarExample extends JFrame {

    JMenuBar menu;
    JFrame mainFrame;

    public JMenuBarExample() {
        super("JMenuBar Example");
        mainFrame = this;
        menu = new JMenuBar();
        JMenu file = new JMenu("File");
        JMenu edit = new JMenu("Edit");
        JMenuItem load = new JMenuItem("Load");
        file.add(load);
        menu.add(file);
        menu.add(edit);
        load.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        JOptionPane.showMessageDialog(mainFrame, "Not implemented ;-(");
                    }
                });
        this.setJMenuBar(menu);
        setSize(500, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] args) {
        new JMenuBarExample();
    }
}

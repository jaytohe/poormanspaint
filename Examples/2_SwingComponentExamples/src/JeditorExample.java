import java.io.IOException;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

public class JeditorExample extends JFrame {

    JEditorPane jep;

    public JeditorExample() {
        jep = new JEditorPane();
        jep.setEditable(false);
        getContentPane().add(new JScrollPane(jep));
        setSize(500, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);

        jep.addHyperlinkListener(
                new HyperlinkListener() {
                    public void hyperlinkUpdate(HyperlinkEvent e) {
                        if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                            try {
                                jep.setPage(e.getURL());
                            } catch (IOException ex) {
                            }
                        }
                    }
                });

        try {
            jep.setPage("http://www.cs.st-andrews.ac.uk/");
        } catch (IOException e) {
        }
    }

    public static void main(String[] args) {
        new JeditorExample();
    }
}

import java.io.File;

import javax.swing.JFileChooser;

public class FileChooserExample {

    public static void main(String[] args) {
        JFileChooser fc = new JFileChooser();
        int returnVal = fc.showOpenDialog(fc);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            try {
                System.out.println("File is " + file.toString());
            } catch (Exception e) {
            }
        } else {
            System.out.println("User didn't select and Ok file choice");
        }
    }
}

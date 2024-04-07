package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * A custom JDialog for connecting to a drawing server.
 * It contains three fields:
 * 1. The server hostname field.
 * 2. The server port field.
 * 3. The token field where the user inputs their authentication token.
 * 
 * The dialog has two buttons:
 * 1. Connect button
 * 2. Exit button
 * 
 * The actions of the buttons are configurable by the setConnectButtonListener and setExitButtonListener methods respectively.
 */
public class NetworkConnectDialog extends JDialog {
    private JTextField serverUrlField;
    private JTextField portField;
    private JTextField tokenField;
    private JButton connectButton;
    private JButton exitButton;

    public NetworkConnectDialog(Frame parent, String title) {
        super(parent, title, true);
        initializeUI();
    }

    private void initializeUI() {
        JPanel dialogPanel = new JPanel(new GridLayout(4, 2));

        // Server URL input field
        JLabel serverUrlLabel = new JLabel("Server Hostname:");
        serverUrlField = new JTextField();
        dialogPanel.add(serverUrlLabel);
        dialogPanel.add(serverUrlField);

        // Port number input field
        JLabel portLabel = new JLabel("Port number:");
        portField = new JTextField();
        dialogPanel.add(portLabel);
        dialogPanel.add(portField);

        // Token input field
        JLabel tokenLabel = new JLabel("Auth Token:");
        tokenField = new JTextField();
        dialogPanel.add(tokenLabel);
        dialogPanel.add(tokenField);

        // Connect button
        connectButton = new JButton("Connect");
        dialogPanel.add(connectButton);

        // Exit button
        exitButton = new JButton("Close");
        dialogPanel.add(exitButton);

        setContentPane(dialogPanel);
        setSize(300, 150);
    }

    public void setConnectButtonListener(ActionListener listener) {
        connectButton.addActionListener(listener);
    }

    public void setExitButtonListener(ActionListener listener) {
        exitButton.addActionListener(listener);
    }

    public String getServerUrl() {
        return serverUrlField.getText();
    }
    public String getToken() {
        return tokenField.getText();
    }

    public String getPort() {
        return portField.getText();
    }
}

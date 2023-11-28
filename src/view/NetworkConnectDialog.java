package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

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
        exitButton = new JButton("Exit");
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

    // Example method for retrieving the server URL entered by the user
    public String getServerUrl() {
        return serverUrlField.getText();
    }

    // Example method for retrieving the token entered by the user
    public String getToken() {
        return tokenField.getText();
    }
}
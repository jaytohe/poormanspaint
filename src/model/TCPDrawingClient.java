package model;

import java.awt.Frame;
import java.io.*;
import java.net.*;

import javax.json.*;
import javax.swing.JOptionPane;
import java.util.List;

public class TCPDrawingClient {

    private Model model;

    private Socket socket;
    private String authToken;

    private Frame parent;

    public TCPDrawingClient(String hostname, String portString, String authToken, Model model, Frame parent)
        throws UnknownHostException, NumberFormatException, IOException
    {
        final int port = Integer.parseInt(portString);

        this.socket = new Socket(hostname, port);
        this.authToken = authToken;

        model.setTCPDrawingClient(this);
        this.model = model;

        this.parent = parent;

        TCPDrawingClientInputReader reader = new TCPDrawingClientInputReader(socket, model, parent);
        reader.start();

        authenticate();
    }


    private void authenticate() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    PrintWriter out = new PrintWriter(socket.getOutputStream());
                    JsonBuilderFactory builderFactory = Json.createBuilderFactory(null);
                    JsonObjectBuilder b1 = builderFactory.createObjectBuilder();
                    JsonObjectBuilder b2 = builderFactory.createObjectBuilder();
                    b1.add("action", "login");
                    b2.add("token", authToken); // token: "26e87036-877a-4e0d-90a3-17df98737ea3"
                    b1.add("data", b2);

                    out.println(b1.build().toString());
                    out.flush();
                    //System.out.println("Wrote :" + b1.build().toString());

                }
                catch (IOException ie) {
                    ie.printStackTrace();
                }
            }
        }).start();
    }
    public void fetchShapes() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    PrintWriter out = new PrintWriter(socket.getOutputStream());
                    JsonObjectBuilder b1 = Json.createObjectBuilder();
                    b1.add("action", "getDrawings");

                    out.println(b1.build().toString());
                    out.flush();
                }
                catch (IOException ie) {
                    ie.printStackTrace();
                }
            }
        }).start();
    }


    public void pushShapes() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    PrintWriter out = new PrintWriter(socket.getOutputStream());

                    List<JsonObject> jsonShapes = model.createJsonFromDrawingPanelState();

                    for (JsonObject jsonShape : jsonShapes) {
                        System.out.println(jsonShape.toString());
                        out.println(jsonShape.toString());
                        out.flush();
                    }
                }
                catch (IOException ie) {
                    ie.printStackTrace();
                }
            }
        }).start();
    }
}

class TCPDrawingClientInputReader extends Thread {
    private Socket socket;
    private Model model;
    private Frame parent;

    public TCPDrawingClientInputReader(Socket socket, Model model, Frame parent) {
        this.socket = socket;
        this.model = model;
        this.parent = parent;
    }

    @Override
    public void run() {
        String line;
        boolean authenticated = false;
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while ((line = in.readLine()) != null) {

                //DEUG
                System.out.println("Received: " + line);

                JsonReader reader = Json.createReader(new StringReader(line));
                JsonStructure json = reader.read();
                reader.close();

                //Check if the response is object or array
                if (json instanceof JsonObject) {
                    JsonObject jsonResponse = (JsonObject) json;

                    String responseStatus = jsonResponse.getString("result");

                    if (responseStatus != null) {
                        if (responseStatus.equals("ok") && !authenticated) {
                            JOptionPane.showMessageDialog(parent, "Authentication Sucessful!", "Connection", JOptionPane.INFORMATION_MESSAGE);
                            authenticated = true;
                        } else if (responseStatus.equals("error") && !authenticated) {
                            JOptionPane.showMessageDialog(parent, "Authentication Failed!", "Connection Error", JOptionPane.ERROR_MESSAGE);
                        }
                        else {
                            JsonString message = jsonResponse.getJsonString("message");
                            if (message != null)
                                JOptionPane.showMessageDialog(parent, message.getString(), "Connection Error", JOptionPane.ERROR_MESSAGE);
                        }

                    }
                }
                else if (json instanceof JsonArray) {
                    JsonArray jsonResponse = (JsonArray) json;
                    model.createDrawingPanelStateFromJson(jsonResponse);
                }

            }
        } 
        catch(IOException ie) {ie.printStackTrace();}
    }
}



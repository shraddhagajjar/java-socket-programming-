/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignmnet4;

import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 *
 * @author shraddha
 */
public class MessageClient extends JFrame {

    // Text field for receiving radius

    private JTextField jtName = new JTextField();
    private JTextField jtText = new JTextField();
    private JTextArea jta = new JTextArea();

    private JTextField jtf = new JTextField();

  // Text area to display contents
    // IO streams
    private DataOutputStream toServer;
    private DataInputStream fromServer;

    public static void main(String[] args) {
        new MessageClient();
    }

    public MessageClient() {
        // Panel p to hold the label and text field
        JPanel p = new JPanel(new BorderLayout());
        p.setLayout(new GridLayout(2, 2));
        p.add(new JLabel("Enter Name"), BorderLayout.WEST);
        p.add(jtName, BorderLayout.CENTER);
        jtName.setHorizontalAlignment(JTextField.RIGHT);

        p.add(new JLabel("Enter Text"), BorderLayout.WEST);
        p.add(jtText, BorderLayout.CENTER);
        jtText.setHorizontalAlignment(JTextField.RIGHT);

        setLayout(new BorderLayout());
        add(p, BorderLayout.NORTH);
        add(new JScrollPane(jta), BorderLayout.CENTER);

        jtText.addActionListener(new Listener()); // Register listener

        setTitle("Client");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true); // It is necessary to show the frame here!
        //jta.append("shraddha : hello");
        try {
            Socket socket = new Socket("localhost", 8000);
            fromServer = new DataInputStream(
                    socket.getInputStream());
            toServer
                    = new DataOutputStream(socket.getOutputStream());
        } catch (IOException ex) {
            jta.append(ex.toString() + '\n');
        }
    }

    private class Listener extends Thread implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                String username = jtName.getText().trim();
                toServer.writeUTF(username);
                String message = jtText.getText().trim();
                toServer.writeUTF(message);
                toServer.flush();
                new fromServerData().start();
                String serverMessage = fromServer.readUTF();
                
                jta.append(serverMessage + "\n");
            } catch (IOException ex) {
                System.err.println(ex);
            }
        }

    }

    public class fromServerData extends Thread {

        public void run() {
            while (true) {
                try {
                    String msg = fromServer.readUTF();
                    jta.append(msg);

                } catch (IOException e) {
                }
            }
        }
    }

}

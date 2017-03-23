/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignmnet4;

import java.io.*;
import java.net.*;
import java.util.*;
import java.awt.*;
import static java.lang.Thread.sleep;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

/**
 *
 * @author shraddha
 */
public class MessageServer extends JFrame {
    // Text area for displaying contents

    private JTextArea jta = new JTextArea();
    private DataInputStream inputFromClient;
    private DataOutputStream outputToClient;
    private ArrayList<HandleAClient> al;

    public static void main(String[] args) {
        new MessageServer();
    }

    public MessageServer() {
        // Place text area on the frame
        setLayout(new BorderLayout());
        add(new JScrollPane(jta), BorderLayout.CENTER);
        setTitle("Server");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true); // It is necessary to show the frame here!

        try {
            //Hashtable<Socket, DataOutput > stable = new Hashtable<Socket, DataOutput>;
            al = new ArrayList<HandleAClient>();
            ServerSocket serverSocket = new ServerSocket(8000);
            jta.append("Chat Server started at " + new Date() + '\n');
            int clientNo = 1;

            while (true) {
                Socket socket = serverSocket.accept();
                jta.append("Starting thread for client " + clientNo + " at " + new Date() + '\n');
                InetAddress inetAddress = socket.getInetAddress();
                jta.append("Client " + clientNo + "'s host name is " + inetAddress.getHostName() + "\n");
                jta.append("Client " + clientNo + "'s IP Address is " + inetAddress.getHostAddress() + "\n");
                HandleAClient task = new HandleAClient(socket);
                //socket.
                //Thread work = new Thread();
                al.add(task);
                
                new Thread(task).start();
                
                clientNo++;

            }
        } catch (IOException ex) {
            System.err.println(ex);
        }
    }

    class HandleAClient implements Runnable {
        
        private Socket socket; // A connected socket
        
                DataInputStream inputFromClient;// = new DataInputStream(socket.getInputStream());
                DataOutputStream outputToClient; //= new DataOutputStream(socket.getOutputStream());
                
        public HandleAClient(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                DataInputStream inputFromClient = new DataInputStream(socket.getInputStream());
                DataOutputStream outputToClient = new DataOutputStream(socket.getOutputStream());
                
                while (true) {

                    String username = inputFromClient.readUTF();
                    String message = inputFromClient.readUTF();
                    String userMessage = username + " : " + message;
                    outputToClient.writeUTF(userMessage);
                    jta.append(userMessage + '\n');
                    broadcast(userMessage);
                }
            } catch (IOException e) {
                System.err.println(e);
            }
        }
    }
    private synchronized void broadcast(String message) throws IOException {
        System.out.println("Working");
        for (int i = 0; i < al.size(); i++) {
                        HandleAClient ct = al.get(i);
                       ct.outputToClient.writeUTF(message);
                }

    }

}

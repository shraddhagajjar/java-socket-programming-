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
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

/**
 *
 * @author shraddha
 */
public class Client extends JFrame{
      // Text field for receiving loan amount Number of years annual rate
  private JTextField jloan = new JTextField();
  private JTextField jloanyear = new JTextField();
  private JTextField jloanrate = new JTextField();
  // Text area to display total rate
  private JTextArea jta = new JTextArea();
  public static JButton jbtsubmit = new JButton("submit");
  // IO streams
  private DataOutputStream toServer;
  private DataInputStream fromServer;

      
  public static void main(String[] args) {
    new Client();
  }

  public Client() {
    // Panel p to hold the label and text field
    JPanel pone = new JPanel(new BorderLayout());
    pone.setLayout(new GridLayout(3,1));
    pone.add(new JLabel("loan amount"));
    pone.add(new JLabel("Number of Years"));
    pone.add(new JLabel("Annual Interest Rate"));
    JPanel ptwo = new JPanel(new BorderLayout());
    ptwo.setLayout(new GridLayout(3,1));
    ptwo.add(jloan);
    ptwo.add(jloanyear);
    ptwo.add(jloanrate);
    JPanel pthree = new JPanel(new BorderLayout());
    pthree.setLayout(new GridLayout(1,1));
    pthree.add(jbtsubmit);
    JPanel pfinal = new JPanel(new BorderLayout());
    pfinal.setLayout(new GridLayout(1,1));
    setLayout(new BorderLayout());
    pfinal.add(pone);
    pfinal.add(ptwo);
    pfinal.add(pthree);
    add(pfinal, BorderLayout.NORTH);
    add(new JScrollPane(jta), BorderLayout.CENTER);

    /*
    JPanel p = new JPanel();
    p.setLayout(new GridLayout(4,2));
    p.add(new JLabel("loan amount"));
    p.add(jloan);
    p.add(new JLabel("Number of Years"));
    p.add(jloanyear);
    p.add(new JLabel("Annual Interest Rate"));
    p.add(jloanrate);
    p.add(jbtsubmit);
    setLayout(new BorderLayout());
    add(p, BorderLayout.NORTH);
    add(new JScrollPane(jta), BorderLayout.CENTER);

   */
    setTitle("Client");
    setSize(500, 300);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setVisible(true); // It is necessary to show the frame here!

    try {
      // Create a socket to connect to the server
      Socket socket = new Socket("localhost", 8000);
      fromServer = new DataInputStream(socket.getInputStream());
      toServer = new DataOutputStream(socket.getOutputStream());
    }
    catch (IOException ex) {
      jta.append(ex.toString() + '\n');
    }
    
    jbtsubmit.addActionListener(new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent ae) {
            try{
                jta.append("Loan amount"+jloan.getText()+ "\n");
                jta.append("Total number of year"+jloanyear.getText()+ "\n");
                jta.append("Annual Interest Rate"+jloanrate.getText()+ "\n");
                Loan l = new Loan(Float.parseFloat(jloanrate.getText().trim()),Integer.parseInt(jloanyear.getText().trim()),Double.parseDouble((jloan.getText().trim())));
                toServer.writeDouble(Double.parseDouble(jloan.getText().trim()));
                toServer.writeInt(Integer.parseInt(jloanyear.getText().trim()));
                toServer.writeFloat(Float.parseFloat(jloanrate.getText().trim()));
                jta.append("Monthly payment" + String.valueOf(fromServer.readDouble()) + "\n");
                jta.append("Total payment" +String.valueOf(fromServer.readInt())+ "\n");
            }
            catch(Exception e)
            {
                System.out.println(e);
            }
        }
    });
  }
}

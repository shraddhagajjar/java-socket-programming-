/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignmnet4;

/**
 *
 * @author shraddha
 */

import java.io.*;
import java.net.*;
import java.util.*;
import java.awt.*;
import javax.swing.*;

public class Server extends JFrame {
  // Text area for displaying contents
  private JTextArea jta = new JTextArea();

  public static void main(String[] args) {
    new Server();
  }

  public Server() {
    // Place text area on the frame
    setLayout(new BorderLayout());
    add(new JScrollPane(jta), BorderLayout.CENTER);
    setTitle("Server");
    setSize(500, 300);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setVisible(true); // It is necessary to show the frame here!

    try {
      // Create a server socket
      ServerSocket serverSocket = new ServerSocket(8000);
      jta.append("Server started at " + new Date() + '\n');
        // Listen for a connection request
      Socket socket = serverSocket.accept();

      // Create data input and output streams
      DataInputStream inputFromClient = new DataInputStream(socket.getInputStream());
      DataOutputStream outputToClient = new DataOutputStream(socket.getOutputStream());
      jta.append("server working\n");
      while (true) {
       jta.append("Server inside from while \n");
                
        double loanamount = inputFromClient.readDouble();
        int NumberOfYears  = inputFromClient.readInt();
        float totalInterestRate = inputFromClient.readFloat();
        
        // Compute area
        Loan l = new Loan(totalInterestRate, NumberOfYears, loanamount);
        double monthlyPayment = l.getMonthlyPayment();
        double totalPayment = l.getTotalPayment();
        // Send area back to the client
        outputToClient.writeDouble(monthlyPayment);
        outputToClient.writeDouble(totalPayment);
        jta.append("Loan amount received from client: " +String.valueOf(loanamount)+'\n');
        jta.append("Interest Rate received from client: " +String.valueOf(totalInterestRate) + '\n');
        jta.append("Total Number of  years reserived from client: " + String.valueOf(NumberOfYears) + '\n');
        jta.append("Monthly Payment : " + String.valueOf(monthlyPayment) + '\n');
        jta.append("Total  Payment : " + String.valueOf(totalPayment) + '\n');
      }
    }
    catch(IOException ex) {
      System.err.println(ex);
    }
  }
}

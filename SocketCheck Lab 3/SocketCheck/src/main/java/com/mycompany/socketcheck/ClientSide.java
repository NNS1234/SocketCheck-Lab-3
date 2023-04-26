//Nudrat Nawal Saber
//UTA ID :1001733394
package com.mycompany.socketcheck;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import javax.swing.JFrame;
import static javax.swing.JOptionPane.ERROR_MESSAGE;
import static javax.swing.JOptionPane.showMessageDialog;
import javax.swing.JTextArea;


public class ClientSide extends javax.swing.JFrame {

    Socket requestSocket;
    PrintWriter out;
    BufferedReader in, input, serverInput;
    String message, serverMessage,curClient;
    private final int port = 5678;
    private JFrame jf;
    public ClientSide() {
        
    }
    public ClientSide(String clientName) {
        curClient=clientName;
        initComponents();
        this.setTitle("Client : "+curClient);
        this.setVisible(true);
        this.setResizable(false);
        jf=this;
        // For menubar cross button action
        jf.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                exitClient();
            }
        });
        taclient.append("Attempting Connection .....\n");   
        taclient.append("Connected to Server!\n"); 
        taclient.append("------------------------------\n");
        taclient.setCaretPosition(taclient.getText().length());
    }
    
    public void start_client(String clientName){
       curClient=clientName;
       // Connecting to server
       try
       { 
            requestSocket = new Socket(InetAddress.getByName(null),port);
            new ReadThread(requestSocket, this).start();
            try {
                    out = new PrintWriter(requestSocket.getOutputStream(), true);
                    out.println("?"+curClient);
                    out.flush();
                    serverInput = new BufferedReader(new InputStreamReader(requestSocket.getInputStream()));
		} catch (IOException e) {
                        showMessageDialog(null, "Server is closed", "Error", ERROR_MESSAGE);
			e.printStackTrace();
		}
       }
       catch(Exception e) {
            e.printStackTrace();
     
       }
    }
    
    // To return client textarea to reader class
    public javax.swing.JTextArea getJTextArea(){
        return taclient;
    }
    
    // To return client queue to reader class
    public javax.swing.JTextArea getClientQueue(){
        return clientQueue;
    }
    
     // To return client printwriter to reader class
    public PrintWriter getPrintWriter(){
        return out;
    }
    private void sendToServer(String message){
        try {
                taclient.append("Text sent to server\n");
                //Sending to server
                out.println(message);
                out.flush();
                taclient.append("My Request > " + message + "\n");
                taclient.setCaretPosition(taclient.getText().length());
        } catch (Exception e) { 
                e.printStackTrace();
        }
    }
   
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        taclient = new javax.swing.JTextArea();
        tfclient = new javax.swing.JTextField();
        btnsend = new javax.swing.JButton();
        clientexit = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        lexiconArea = new javax.swing.JTextField();
        lexiconAdd = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        clientQueue = new javax.swing.JTextArea();
        jLabel4 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        taclient.setEditable(false);
        taclient.setColumns(20);
        taclient.setFont(new java.awt.Font("Monospaced", 0, 14)); // NOI18N
        taclient.setRows(5);
        jScrollPane1.setViewportView(taclient);

        btnsend.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        btnsend.setText("Send");
        btnsend.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnsendActionPerformed(evt);
            }
        });

        clientexit.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        clientexit.setText("Exit");
        clientexit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clientexitActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel1.setText("Server");

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel2.setText("Enter your text here");

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel3.setText("Enter text to add in lexicon");

        lexiconAdd.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        lexiconAdd.setText("Add");
        lexiconAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lexiconAddActionPerformed(evt);
            }
        });

        clientQueue.setColumns(20);
        clientQueue.setRows(5);
        jScrollPane2.setViewportView(clientQueue);

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel4.setText("Queue");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 569, Short.MAX_VALUE)
                .addComponent(clientexit, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tfclient, javax.swing.GroupLayout.PREFERRED_SIZE, 319, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lexiconArea, javax.swing.GroupLayout.PREFERRED_SIZE, 319, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(51, 51, 51)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnsend, javax.swing.GroupLayout.DEFAULT_SIZE, 112, Short.MAX_VALUE)
                    .addComponent(lexiconAdd, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 409, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27))
            .addGroup(layout.createSequentialGroup()
                .addGap(145, 145, 145)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(90, 90, 90))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, 90, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 40, Short.MAX_VALUE)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 18, Short.MAX_VALUE)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lexiconAdd, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                    .addComponent(lexiconArea))
                .addGap(18, 18, 18)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfclient, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnsend, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addComponent(clientexit, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnsendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnsendActionPerformed
        //Reading from text field
        message = tfclient.getText();
        tfclient.setText("");
        if (message.isEmpty()) {
                taclient.append("Error: Enter text before sending to server. \n");
                taclient.append("------------------------------\n");
                taclient.setCaretPosition(taclient.getText().length());
                return;
        }
        sendToServer(message);
    }//GEN-LAST:event_btnsendActionPerformed
    private void exitClient(){
        // Sending remove request to sever
        out.println("!"+curClient);
        System.exit(0);   
    }
    private void clientexitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clientexitActionPerformed
           exitClient();
    }//GEN-LAST:event_clientexitActionPerformed

    private void lexiconAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lexiconAddActionPerformed
        // Adding the words to client queue
        String lexiconText=lexiconArea.getText();
        ArrayList<String>clientWords = new ArrayList<String>(Arrays.asList(lexiconText.split(" ")));
        for(String cw : clientWords){
            if(cw.length()!=0){
                clientQueue.append(cw+"\n");
            }
        }
        clientQueue.setCaretPosition(clientQueue.getText().length());
        lexiconArea.setText("");
    }//GEN-LAST:event_lexiconAddActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ClientSide.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ClientSide.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ClientSide.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ClientSide.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
      
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnsend;
    private javax.swing.JTextArea clientQueue;
    private javax.swing.JButton clientexit;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JButton lexiconAdd;
    private javax.swing.JTextField lexiconArea;
    private static javax.swing.JTextArea taclient;
    private javax.swing.JTextField tfclient;
    // End of variables declaration//GEN-END:variables
}

// Seperate class to handle reading operations for client
class ReadThread extends Thread {
    private BufferedReader reader;
    private PrintWriter writer;
    private Socket socket;
    private ClientSide client;
    private JTextArea jTextArea,clientQueueArea;
 
    public ReadThread(Socket socket, ClientSide client) {
        this.socket = socket;
        this.client = client;
 
        try {
            InputStream input = socket.getInputStream();
            reader = new BufferedReader(new InputStreamReader(input));
        } catch (IOException ex) {
            System.out.println("Error getting input stream: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
 
    public void run() {
        while (true) {
            try {
                 //Reading response from server
                String response = reader.readLine();
                if(response==null){
                    // Primary server disconnected
                    break;
                }
                else {
                    jTextArea=client.getJTextArea();
                    writer=client.getPrintWriter();
                    if(response.charAt(0)=='#'){
                        // Server is polling
                        jTextArea.append("Server is polling\n");
                        String clientQueueWords =  getPolledText();
                        if(clientQueueWords.length()<=1){
                            jTextArea.append("Queue is empty\nNo words sent to server.\n");
                        } else {
                            ArrayList<String> clientWords = new ArrayList<String>(Arrays.asList(clientQueueWords.split(" ")));
                            // Sending the words to server
                            writer.println("#"+clientQueueWords);
                            jTextArea.append("Following words sent to server\n");
                            // Seperating the unique words
                            String showWords="[";
                            for(int i=0;i<clientWords.size();i++){
                                showWords+=clientWords.get(i);
                                if(i<clientWords.size()-1){
                                    showWords+=",";
                                }
                            }
                            showWords+="]";
                            jTextArea.append(showWords+"\n");                  
                        } 
                        jTextArea.append("Polling Completed\n");
                        jTextArea.append("------------------------------\n");
                        jTextArea.setCaretPosition(jTextArea.getText().length());

                    } else {
                        // Server's response after spell checking
                        jTextArea.append("Server Response > " + response + "\n");
                        jTextArea.append("Spell check sequence completed\n");
                        jTextArea.append("------------------------------\n");
                        jTextArea.setCaretPosition(jTextArea.getText().length());
                    }
                }
            } catch (IOException ex) {
                System.out.println("Error reading from server: " + ex.getMessage());
                ex.printStackTrace();
                break;
            }
        }
        jTextArea.append("Primary Server disconnected\nConnecting to Backup Server now...\n");
        jTextArea.append("Connected to Backup server\n");
        jTextArea.append("------------------------------\n");
        
        // Attempt to connect with backup server
        client.start_client(client.curClient);
        jTextArea.setCaretPosition(jTextArea.getText().length());        
    }
    public String getPolledText(){
        // Getting the words to be add from queue
        clientQueueArea = client.getClientQueue();
        String clientQueueWords=clientQueueArea.getText();
        HashSet<String>temp = new HashSet<>();
        
        String lines[] = clientQueueWords.split("\\r?\\n");
        String newStr="";
        for(String s:lines){
            if(!s.equals(" ")){
                // Adding unique words only
                if(!temp.contains(s)){
                    temp.add(s);
                    newStr += s+" ";
                }
            }
        }
        // Clearing client queue
        clientQueueArea.setText("");
        return newStr;
    }
}
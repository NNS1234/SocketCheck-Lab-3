//Nudrat Nawal Saber
//UTA ID :1001733394
package com.mycompany.socketcheck;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;
import javax.swing.JFrame;
import static javax.swing.JOptionPane.ERROR_MESSAGE;
import static javax.swing.JOptionPane.showMessageDialog;
import javax.swing.Timer;


public class BackupServerSide extends javax.swing.JFrame {
    ServerSocket serverSocket = null;
    Socket socket = null;
    boolean ServerOn = true;
    static DataInputStream din;
    static DataOutputStream dout;
    private final int port = 5678;
    public static String currentClient;
    Socket requestSocket;
    PrintWriter out;
    BufferedReader in, input, serverInput;
    String message, serverMessage,curClient;
    JFrame jf;
    HashSet<String> hset=new HashSet();  
    HashSet<String> lexiconWords=new HashSet();  
    public Set<ClientBackupServiceThread> clientThreads = new HashSet<>();
    static boolean primaryServerIsOn = true;
    public BackupServerSide() {
        initComponents();
        this.setTitle("Backup Server");
        this.setVisible(true);
        this.setResizable(false);
        tabserver.setEditable(false);
        tabconn.setEditable(false);
        jf=this;
        // For menubar cross button action
        jf.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                closeServer();
            }
        });
    }
    
    public void start(){
        tabserver.setText("Backup Server Initiated"+"\nConnecting to Primary Server...");
        try
        { 
            requestSocket = new Socket(InetAddress.getByName(null),port);
            new BackupReadThread(requestSocket, this).start();
            try {
                    out = new PrintWriter(requestSocket.getOutputStream(), true);
                    out.println("$");
                    out.flush();
                    tabserver.append("\nConnected to Primary Server\n------------------------------\n");
                    serverInput = new BufferedReader(new InputStreamReader(requestSocket.getInputStream()));
		} catch (IOException e) {
                        showMessageDialog(null, "Server is closed", "Error", ERROR_MESSAGE);
			e.printStackTrace();
		}
        } catch(Exception e) {
            e.printStackTrace();
        }
          
        while(true)
        {
            System.out.println("Primary server running");
            if(!primaryServerIsOn){
                start_polling();
                try
                {
                    serverSocket=new ServerSocket(port);
                    while(true)
                    {
                        System.out.println("Backup server running");
                        
                        try
                        {
                            updateClients();
                            try{
                                socket=serverSocket.accept();
                                BufferedReader intemp = null; 
                                PrintWriter outtemp = null;
                                intemp = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                                outtemp = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
                                String givenName = intemp.readLine();

                                // New client starts with ?
                                if(givenName.charAt(0)=='?'){
                                    String newUser = givenName.substring(1);
                                    welcomeClient(givenName);
                                    updateClients();
                                    // Fork new thread for each new client
                                    ClientBackupServiceThread cliThread = new ClientBackupServiceThread(socket,newUser);
                                    clientThreads.add(cliThread);
                                    cliThread.start();
                                } else {
                                    updateClients();         
                                }                       
                            }catch(Exception e){
                                 e.printStackTrace();
                            }   
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                }
                catch(IOException ioException){
                    ioException.printStackTrace();
                }
            } 
        }
    }
    private void welcomeClient(String username){
        String welcomeUsername = username.substring(1);
        hset.add(welcomeUsername);
        tabserver.append("Client '"+welcomeUsername+"' entered\n------------------------------\n");
    }
    private void updateClients(){
        tabconn.setText("");
        // Show current clients from clients.txt
        try {
            File myObj = new File("clients.txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
              String data = myReader.nextLine();
              tabconn.append(data+"\n");
            }
            myReader.close();
        } catch (FileNotFoundException e) {
          e.printStackTrace();
        }
    }
    
    private void removeClient(String req){
        // Removing client from clients.txt
        // First stoing all names without removing name in temporary arraylist
        String removeUsername = req.substring(1);
       
        //Iterating over all active threads and removing the one that matches with name
        for (Iterator<ClientBackupServiceThread> i = clientThreads.iterator(); i.hasNext();) {
            ClientBackupServiceThread c = i.next();
            if(c.curUsername.equals(removeUsername)){
                // Removing the client thread
                i.remove();
                break;
            }
            
        }
        ArrayList<String> temp = new ArrayList<String>();
        try {
            File myObj = new File("clients.txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
              String data = myReader.nextLine();
              if(!data.equals(removeUsername)){
                  temp.add(data);
              }
            }
            myReader.close();
        } catch (FileNotFoundException e) {
          e.printStackTrace();
        }
        
        // Then clearing the txt file
        try{
            File myObj = new File("clients.txt");
            PrintWriter writer = new PrintWriter(myObj);
            writer.print("");
            writer.close();
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
        // Then adding to the arratylist's name in txt file
        try{
            String filename= "clients.txt";
            FileWriter fw = new FileWriter(filename,true); //the true will append the new data
            for (int i = 0; i < temp.size(); i++) {
                fw.write(temp.get(i)+"\n");//appends the string to the file
              } 
            fw.close(); 
        }
        catch(IOException ioe){
            System.err.println("IOException: " + ioe.getMessage());
        }
        tabserver.append("Client '"+removeUsername+"' left\n");
        tabserver.append("------------------------------\n");
        tabserver.setCaretPosition(tabserver.getText().length());
        hset.remove(removeUsername);
        updateClients();
    }

    private void start_polling() {
        // Polling function
        Timer timer = new Timer(60000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                lexiconWords.clear();
                try {
                    lexiconWords = getLexiconWords();
                    tabserver.append("Polling all connected clients\n");
                    for(ClientBackupServiceThread ct : clientThreads){
                        ct.sendMessage("#");
                    }
                    tabserver.append("Polling Completed\n");
                    tabserver.append("------------------------------\n");
                    tabserver.setCaretPosition(tabserver.getText().length());
                } catch (IOException ex) {
                    ex.printStackTrace();
                } 
            }
            
            private HashSet<String> getLexiconWords() throws FileNotFoundException, IOException {
                FileInputStream fstream = new FileInputStream("backupLexicon.txt");
                BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
                HashSet<String> temp = new HashSet<String>();
                String textFileLine;
                //Read every lexicon words
                while ((textFileLine = br.readLine()) != null)   {
                        textFileLine = textFileLine.toLowerCase();
                        String s[]=textFileLine.split("[ ]+");

                        for(int i=0;i<s.length;i++){
                            temp.add(s[i]);                            
                        }
                }
                return temp;
            }
          });
          timer.setRepeats(true); 
          timer.start();
    }
    // To return client textarea to reader class
    public javax.swing.JTextArea getJTextArea(){
        return tabserver;
    }
    
    class ClientBackupServiceThread extends Thread { 
		Socket myClientSocket;
		BufferedReader in = null; 
		PrintWriter out = null;
		String textFileLine, serverResponse="";
		ArrayList<String> clientWords;
                String curUsername;

		public ClientBackupServiceThread() { 
                    super();     	
		} 
		
		ClientBackupServiceThread(Socket s,String username) { 
			myClientSocket = s; 
                        curUsername = username;
		} 

		@Override
		public void run() {
                    while(ServerOn){
                        
                        try { 
                                in = new BufferedReader(new InputStreamReader(myClientSocket.getInputStream()));
                                out = new PrintWriter(new OutputStreamWriter(myClientSocket.getOutputStream()));

                                String clientRequest = in.readLine();
                                if(clientRequest.charAt(0)=='!'){
                                    // Removing the client
                                    removeClient(clientRequest);
                                    updateClients();
                                }else if(clientRequest.charAt(0)=='#'){
                                    // This request is for polling
                                    addToLexicon(clientRequest);
                                } else {
                                    tabserver.append("Client '"+curUsername+"' Request > " + clientRequest+ " \n");

                                    // Spliting names from client's input
                                    clientWords = new ArrayList<String>(Arrays.asList(clientRequest.split(" ")));

                                    // Open the Dictionary File for checking all the words
                                    FileInputStream fstream = new FileInputStream("backupLexicon.txt");
                                    BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
                                    TreeSet<String> ts = new TreeSet<String>();
                                    //Read every lexicon words
                                    while ((textFileLine = br.readLine()) != null)   {
                                            textFileLine = textFileLine.toLowerCase();
                                            String s[]=textFileLine.split("[ ]+");   
                                            for(int i=0;i<s.length;i++){
                                                ts.add(s[i]);                       
                                            }                            
                                    }
                                    if(!clientWords.isEmpty()){
                                            for (String element : clientWords) {
                                                // Adding the result to final string
                                                if(ts.contains(element.toLowerCase())){
                                                    serverResponse+="["+element+"] ";
                                                } else {
                                                    serverResponse+=element+" ";
                                                }
                                            }
                                    }else{
                                            serverResponse = textFileLine;
                                            break;
                                    }
                                    // Show message in server window
                                    tabserver.append("Response > " + serverResponse+ " \n");
                                    tabserver.append("------------------------------\n");
                                    tabserver.setCaretPosition(tabserver.getText().length());
                                    // Send responce back to client
                                    out.println(serverResponse);
                                    out.flush();
                                    // Clearing variables
                                    serverResponse = "";
                                    clientWords.clear();
                                }

                        } catch(Exception e) { 
                                e.printStackTrace(); 
                        } 
                        
                    }
                    // Closing input output
                    try {
                            in.close(); 
                            out.close(); 
                            myClientSocket.close(); 
                    } catch(IOException ioe) { 
                            ioe.printStackTrace(); 
                    } 
		} 

            private void sendMessage(String msg) {
                    try {
                        out = new PrintWriter(new OutputStreamWriter(myClientSocket.getOutputStream()));
                        out.println(msg);
                        out.flush();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
            }
            
            private void addToLexicon(String clientRequest) {
                // Adding the given words to lexicon.txt
                Set<String> words = new HashSet<>();
                String wordsToAdd=clientRequest.substring(1);
                ArrayList<String>givenWords = new ArrayList<String>(Arrays.asList(wordsToAdd.split(" ")));
                for(String s:givenWords){
                    if(!lexiconWords.contains(s)){
                        // Adding only new words
                        words.add(s);
                    }
                }
               
                // Adding new words in lexicon.txt
                try{
                    String filename= "backupLexicon.txt";
                    FileWriter fw = new FileWriter(filename,true);
                    // Appending the words
                    for(String word:words){
                        fw.write(" "+word);
                    }
                    fw.close();            
                }
                catch(IOException ioe){
                    System.err.println("IOException: " + ioe.getMessage());
                }
            }
	}
        /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tabserver = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        tabconn = new javax.swing.JTextArea();
        backupExit = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        tabserver.setColumns(20);
        tabserver.setFont(new java.awt.Font("Monospaced", 0, 14)); // NOI18N
        tabserver.setRows(5);
        jScrollPane1.setViewportView(tabserver);

        tabconn.setColumns(20);
        tabconn.setFont(new java.awt.Font("Monospaced", 0, 14)); // NOI18N
        tabconn.setRows(5);
        jScrollPane2.setViewportView(tabconn);

        backupExit.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        backupExit.setText("Exit");
        backupExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backupExitActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel1.setText("Backup Server");

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel2.setText("Connected Clients");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 412, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 41, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(backupExit, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
            .addGroup(layout.createSequentialGroup()
                .addGap(128, 128, 128)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addGap(54, 54, 54))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 123, Short.MAX_VALUE)
                        .addComponent(backupExit, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void closeServer() {
        System.exit(0);
    }
    
    private void backupExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backupExitActionPerformed
        closeServer();
    }//GEN-LAST:event_backupExitActionPerformed
    
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
            java.util.logging.Logger.getLogger(BackupServerSide.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(BackupServerSide.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(BackupServerSide.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(BackupServerSide.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new BackupServerSide().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton backupExit;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea tabconn;
    private javax.swing.JTextArea tabserver;
    // End of variables declaration//GEN-END:variables
}
// Seperate class to handle reading operations for client
class BackupReadThread extends Thread {
    private BufferedReader reader;
    private PrintWriter writer;
    private Socket socket;
    private BackupServerSide client;
    HashSet<String> backupLexiconWords=new HashSet();
    public BackupReadThread(Socket socket, BackupServerSide client) {
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
    
    private HashSet<String> getBackupLexiconWords() throws FileNotFoundException, IOException {
                FileInputStream fstream = new FileInputStream("backupLexicon.txt");
                BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
                HashSet<String> temp = new HashSet<String>();
                String textFileLine;
                //Read every lexicon words
                while ((textFileLine = br.readLine()) != null)   {
                        textFileLine = textFileLine.toLowerCase();
                        String s[]=textFileLine.split("[ ]+");
                        for(int i=0;i<s.length;i++){
                            temp.add(s[i]);                            
                        }
                }
                return temp;
            }
 
    public void run() {
        while (BackupServerSide.primaryServerIsOn) {
            try {
                 //Reading response from server
                String response = reader.readLine();
                        
                if(response==null){
                    break;
                } else {
                    backupLexiconWords.clear();
                    System.out.println("res = "+response);

                    ArrayList<String>givenWords = new ArrayList<String>(Arrays.asList(response.split(" ")));
                    backupLexiconWords=getBackupLexiconWords();
                    ArrayList<String>newWords = new ArrayList<String>();
                    for(String word:givenWords){
                        if(!backupLexiconWords.contains(word)){
                            newWords.add(word);
                        }
                    }
                    try{
                        String filename= "backupLexicon.txt";
                        FileWriter fw = new FileWriter(filename,true);
                        // Appending the words
                        for(String word:newWords){
                            fw.write(" "+word);
                        }
                        fw.close();            
                    }
                    catch(IOException ioe){
                        System.err.println("IOException: " + ioe.getMessage());
                    }
                }
            } catch (IOException ex) {
                System.out.println("Error reading from server: " + ex.getMessage());
                ex.printStackTrace();
                break;
            }
        }
        System.out.println("Primary server is off");
        BackupServerSide.primaryServerIsOn=false;
        client.getJTextArea().append("Backup Server is active\nListening to port 5678\n------------------------------\n");
    }
}
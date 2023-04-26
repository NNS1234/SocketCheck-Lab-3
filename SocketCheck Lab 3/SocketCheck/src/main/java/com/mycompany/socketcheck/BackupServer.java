//Nudrat Nawal Saber
//UTA ID :1001733394
package com.mycompany.socketcheck;

import java.io.File;
import java.io.PrintWriter;


public class BackupServer {
    public static void main(String[] args) {
        // Attempt to create 'backupClients.txt' file to store current clients
        try{
            File myObj = new File("backupLexicon.txt");
            // If the file does not exists, create new
            if (myObj.createNewFile()) {
              System.out.println("File created: " + myObj.getName());
            } else {
                // If the file exists, delete all the contents of the file
                PrintWriter writer = new PrintWriter(myObj);
                writer.print("");
                writer.close();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        // Start backup server
        BackupServerSide bsside = new BackupServerSide();
        bsside.start();
    }
}

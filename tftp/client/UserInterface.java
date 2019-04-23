/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tftp.client;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import tftp.shared.FileParser;

/**
 *
 * @author alawren3 the purpose of this class is to get all the files and then
 * pass that file to the correct protocol
 */
public class UserInterface {

    /**
     * @param args the command line arguments
     */
    public static void main(String args) {
        Scanner s = new Scanner(System.in);
        while (true) {
            System.out.println("enter a file");
            String input = s.nextLine();
            if (input.equals("quit")) {
                break;
            }
            ArrayList<File> files = new FileParser().getAllFiles(input); //throwing io for this line
            for (int i = 0; i < files.size(); i++) {
                
                try {//need half second delay between each file otherwise the client and server arent sync.
                    Thread.sleep(500);
                } catch (InterruptedException ex) {
                    Logger.getLogger(UserInterface.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                if (args.equals("n")) {
                    TFTPN.main(files.get(i),input);
                } else if (args.equals("w")) {
                    TFTPW.main(files.get(i),input);
                } else if (args.equals("d")) {
                    TFTPD.main(files.get(i),input);
                } else if (args.equals("6")) {
                    TFTP6.main(files.get(i),input);
                }
            }

        }
    }

}

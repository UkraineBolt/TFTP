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
            
        System.out.println("enter a file");
        String input = "/home/alawren3/Pictures/b";//s.nextLine();

        ArrayList<File> files = new FileParser().getAllFiles(input); //throwing io for this line

        long start = System.nanoTime();
        for (int i = 0; i < files.size(); i++) {
            if (args.equals("n")) {
                TFTPN.main(files.get(i),input);
            } else if (args.equals("w")) {
                TFTPW.main(files.get(i),input);
            } else if (args.equals("6")) {
                TFTP6.main(files.get(i),input);
            }
        }
        long time = System.nanoTime()-start;
        System.out.println(time * .000000001);
        System.exit(0);
        
    }

}

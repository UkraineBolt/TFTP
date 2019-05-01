/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tftp;

import java.io.IOException;
import java.util.Scanner;
/**
 *
 * @author alawren3
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        String in1;
        do{
            System.out.println("s or c");
            in1 = //"c";
                s.nextLine();
            if(in1.equals("s")){
                System.out.println("n, w or d");
                in1 = s.nextLine();
                if(in1.equals("n")){
                    tftp.server.S_TFTPN.main();
                }else if(in1.equals("w")){
                    tftp.server.S_TFTPW.main();
                }else if(in1.equals("d")){
                    tftp.server.S_TFTPD.main();
                }
            }else if(in1.equals("c")){
                System.out.println("n, w, or 6");
                in1 = //"6";
                   s.nextLine();
                if(in1.equals("n")||in1.equals("w")||in1.equals("d")||in1.equals("6")){
                    tftp.client.UserInterface.main(in1);
                }
            }
        }while(!in1.equals("quit"));
    }
    
}

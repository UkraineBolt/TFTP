/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tftp;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import tftp.shared.Constants;
import tftp.shared.FileParser;
/**
 *
 * @author alawren3
 */
public class test {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        /*FileParser s = new FileParser();
        ArrayList<File> b = s.getAllFiles("/home/alawren3/Desktop/project 2/tftp");
        System.out.println(b.toString());
        System.out.println(b.get(0).toString());
        System.out.println(b.get(0).getName());*/
        
        
        /*ByteBuffer bb = ByteBuffer.allocate(12);
        bb.putInt(5);bb.putInt(10);bb.putInt(17);
        
        /*bb.position(0);
        bb.getInt();
        byte[] left = new byte[8];
        bb.position(0);
        bb.get(left, 0, left.length);
        /*for(int i=0;i<left.length;i++){
            System.out.println(left[i]);
        }*/
        
        /*byte[] temp = bb.array();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        stream.write(temp);
        temp = stream.toByteArray();
        for(int i=0;i<temp.length;i++){
            System.out.println(temp[i]);
        }*/
        
        /*ArrayList<Integer> bark = new ArrayList<>();
        bark.add(4);
        bark.add(2);
        bark.add(0);
        bark.add(5);
        bark.add(3);
        Collections.sort(bark);
        System.out.println(bark.toString());*/
        
        System.out.println(15%16);
        System.out.println(31%16);
        System.out.println(4%16);
        System.out.println(20%16);
        
    }
    
}

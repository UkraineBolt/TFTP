/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tftp.server;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import tftp.shared.*;

/**
 *
 * @author alawren3
 */
public class S_TFTPN {
    private static final Constants C= new Constants();
    //private static final String PATH_TO_STORAGE = "/home/alawren3/Desktop/storage";
    private static DatagramSocket socket;
    private static DatagramPacket reciever;
    private static DatagramPacket sender;
    private static EnCodeNode enCodeNode;
    private static DeCodeNode deCodeNode;
    private static FileParser fileParser;
    private static String fileName;
    private static ByteArrayOutputStream buffer;
    private static int index;
    
    public static void main() {
        
        try {
            constructor();
        } catch (SocketException ex) {
            Logger.getLogger(S_TFTPN.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
        while(true){
            index = 0;
            reciever = new DatagramPacket(new byte[C.DATA_SIZE*5],C.DATA_SIZE*5);
            
            while(true){
                try {
                    socket.receive(reciever);
                    deCodeNode = new DeCodeNode(reciever.getData(),reciever.getLength());
                    if(deCodeNode.blockNum==index){
                        System.out.println("Packet: "+deCodeNode.blockNum);
                        fileName = C.STORAGE+deCodeNode.fileName;
                        System.out.println("file: "+fileName);
                        index++;
                        System.out.println("ack: "+index);
                        System.out.println("");
                        enCodeNode = new EnCodeNode(index);
                        sender = new DatagramPacket(enCodeNode.getHeader(),enCodeNode.getSize(),reciever.getAddress(),reciever.getPort());
                        socket.send(sender);
                        break;
                    }
                    System.out.println("looking for index 0 got: "+deCodeNode.blockNum);
                } catch (IOException ex) {
                    System.out.println("IO exception?");
                    return;
                }
            }
            HashMap<Integer,byte[]> data = new HashMap<>();
            reciever = new DatagramPacket(new byte[C.DATA_SIZE+8], C.DATA_SIZE+8);
            do{
                try{
                    socket.receive(reciever);
                    deCodeNode = new DeCodeNode(reciever.getData(),reciever.getLength());
                    System.out.println("packet: "+deCodeNode.blockNum);
                    System.out.println("index: "+index);
                    System.out.println("");
                    if(deCodeNode.blockNum==index){
                        index++;
                        data.put(deCodeNode.blockNum,deCodeNode.data);
                        System.out.println("going to send ack: "+index);
                        enCodeNode = new EnCodeNode(index);
                        sender = new DatagramPacket(enCodeNode.getHeader(),enCodeNode.getSize(),reciever.getAddress(),reciever.getPort());
                        socket.send(sender);
                    }else if(deCodeNode.blockNum<=index || deCodeNode.blockNum>=index){//this handles the case of the last ack being dropped.
                        enCodeNode = new EnCodeNode(index);
                        sender = new DatagramPacket(enCodeNode.getHeader(),enCodeNode.getSize(),reciever.getAddress(),reciever.getPort());
                        socket.send(sender);
                    }
                }catch (IOException ex) {
                    System.out.println("IO exception?");
                    return;
                }
            }while(deCodeNode.data.length==C.DATA_SIZE);
            
            //make file
            fileParser = new FileParser(fileName);
            try{
                for(int i=1;i<=data.size();i++){
                    buffer.write(data.get(i));
                }
                fileParser.fillFile(buffer.toByteArray());
                buffer = new ByteArrayOutputStream();
            } catch (IOException ex) {
                Logger.getLogger(S_TFTPN.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("didnt get all data rip");
            }
            System.out.println("done");
        }
    }
    
    private static void constructor() throws SocketException{
        socket = new DatagramSocket(C.PORT);
        fileParser = new FileParser();
        buffer = new ByteArrayOutputStream();
    }
    
    
    
}

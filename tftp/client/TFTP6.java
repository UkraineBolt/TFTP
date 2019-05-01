/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tftp.client;

import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import tftp.shared.*;

/**
 *
 * @author alawren3
 */
public class TFTP6 {
    private static final Constants C = new Constants();
    private static DeCodeNode deCodeNode;
    private static EnCodeNode enCodeNode;
    private static DatagramSocket socket;
    private static DatagramPacket sender;
    private static DatagramPacket reciever;
    private static int index;
    
    /**
     * @param args the command line arguments
     * @param dir
     */
    public static void main(File args, String dir) {
        try {
            constructor();
            index = 0;
        } catch (SocketException ex) {
            return;
        }
        //send request
        
        ByteBuffer data;
        try {
            data = ByteBuffer.wrap(Files.readAllBytes(args.toPath()));
        } catch (IOException ex) {
            System.out.println("couldnt get the data");
            return;
        }
        
        while(true){
            try{
                if(args.toString().equals(dir)){
                    enCodeNode = new EnCodeNode(index,args.toString());
                    System.out.println(args.toString());
                }else{
                    enCodeNode = new EnCodeNode(index,args.toString()+" *@* "+dir);
                    System.out.println(args.toString());
                    System.out.println(dir);
                }
                sender = new DatagramPacket(enCodeNode.getHeader(),enCodeNode.getSize(),C.inet6,C.PORT);
                socket.send(sender);
                socket.receive(reciever);
                deCodeNode = new DeCodeNode(reciever.getData(),reciever.getLength());
                System.out.println("ack: "+deCodeNode.blockNum+"\n");
                if(deCodeNode.blockNum!=index+1){
                    System.out.println("unexpected block num: "+deCodeNode.blockNum);
                    return;
                }
                index = deCodeNode.blockNum;
                break;
            } catch (SocketTimeoutException  ex) {
                System.out.println("time out for wqr");
            } catch (IOException ex) {
                System.out.println("io error with request");
                return;
            }
        }
        
        
        //send all but last packet
        byte[] buffer = new byte[C.DATA_SIZE];
        while(C.DATA_SIZE<data.remaining()){
            data.get(buffer);
            while(true){
                try{
                    System.out.println("packet: "+index);
                    enCodeNode = new EnCodeNode(index,buffer);
                    sender = new DatagramPacket(enCodeNode.getHeader(),enCodeNode.getSize(),C.inet6,C.PORT);
                    socket.send(sender);
                    socket.receive(reciever);
                    deCodeNode = new DeCodeNode(reciever.getData(),reciever.getLength());
                    if(deCodeNode.blockNum==index+1){
                        index = deCodeNode.blockNum;
                        break;
                    }else{ //if they dont match
                        data.position(deCodeNode.blockNum-1);
                        index = deCodeNode.blockNum;
                        break;
                    }
                    
                }catch (SocketTimeoutException  ex) {
                    System.out.println("time out for packet: "+deCodeNode.blockNum);
                } catch (IOException ex) {
                    System.out.println("io exception with regular packets");
                    ex.printStackTrace();
                } 
            }
        }
        
        //send last packet
        buffer = new byte[data.remaining()];
        data.get(buffer);
        while(true){
            try{
                enCodeNode = new EnCodeNode(deCodeNode.blockNum,buffer);
                sender = new DatagramPacket(enCodeNode.getHeader(),enCodeNode.getSize(),C.inet6,C.PORT);
                socket.send(sender);
                socket.receive(reciever);
                deCodeNode = new DeCodeNode(reciever.getData(),reciever.getLength());
                if(deCodeNode.blockNum==index+1){
                    index = deCodeNode.blockNum;
                    break;
                }else{
                    
                }
            }catch (SocketTimeoutException  ex) {
                System.out.println("time out for packet: "+deCodeNode.blockNum);
            } catch (IOException ex) {
                System.out.println("io exception");
            } 
        }
        System.out.println("done with file: "+args.getName());
        
    }
    
    
    private static void constructor() throws SocketException{
        socket = new DatagramSocket();
        socket.setSoTimeout(C.TIMEOUT_WINDOW);
        reciever = new DatagramPacket(new byte[C.DATA_SIZE],C.DATA_SIZE);
    }
    
    
    
    
}

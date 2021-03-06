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
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import tftp.shared.*;

/**
 *
 * @author alawren3
 */
public class S_TFTPW {
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
    
    @SuppressWarnings("empty-statement")
    public static void main() {
        
        while(true){
            try {
                constructor();
            } catch (SocketException ex) {
                Logger.getLogger(S_TFTPN.class.getName()).log(Level.SEVERE, null, ex);
                return;
            }
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
                    System.out.println("IO exception? for receiveing write block");
                    ex.printStackTrace();
                    return;
                }
            }
            
            try {
                socket.setSoTimeout(C.TIMEOUT_WINDOW);
            } catch (SocketException ex) {
                Logger.getLogger(S_TFTPW.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            index = 0;
            int lastPacketNum = -2;
            HashMap<Integer,byte[]> data = new HashMap<>();
            int lastPacketRemainder = C.WINDOW_SIZE-1;
            int growth = 0;
            do{
                if(lastPacketNum+1==index){
                    break;
                }
                int recievedPackets = 0;
                try{
                    
                    for(int i=0;i<C.WINDOW_SIZE+growth;i++){
                        socket.receive(reciever);
                        recievedPackets++;
                        deCodeNode = new DeCodeNode(reciever.getData(),reciever.getLength());
                        System.out.println("got packet: "+deCodeNode.blockNum);
                        if(!data.containsKey(deCodeNode.blockNum)){
                            data.put(deCodeNode.blockNum, deCodeNode.data);
                        }

                        if(deCodeNode.blockNum==index){//increment index if order is maintained
                            index++;
                        }
                        
                        if(deCodeNode.data.length<C.DATA_SIZE){//check if last packet in system
                            lastPacketNum=deCodeNode.blockNum;
                            break;
                        }
                        
                        /*if(deCodeNode.blockNum%C.WINDOW_SIZE==lastPacketRemainder){//check if last packet in window 
                            break;
                        }*/
                    }
                    
                    if(recievedPackets==C.WINDOW_SIZE+growth){
                        growth++;
                    }else if(growth>0){
                        growth--;
                    }
                    
                    enCodeNode = new EnCodeNode(index);
                    sender = new DatagramPacket(enCodeNode.getHeader(),enCodeNode.getSize(),reciever.getAddress(),reciever.getPort());
                    socket.send(sender);
                    
                } catch(SocketTimeoutException ex){//to handle last packet in window dropped;
                    try{
                    enCodeNode = new EnCodeNode(index);
                    sender = new DatagramPacket(enCodeNode.getHeader(),enCodeNode.getSize(),reciever.getAddress(),reciever.getPort());
                    socket.send(sender);
                    } catch (IOException ex1) {
                        Logger.getLogger(S_TFTPW.class.getName()).log(Level.SEVERE, null, ex1);
                    }
                }catch (IOException ex) {
                    Logger.getLogger(S_TFTPW.class.getName()).log(Level.SEVERE, null, ex);
                }
            }while(true);
            
            
            //make file
            fileParser = new FileParser(fileName);
            try{
                for(int i=0;i<data.size();i++){
                    buffer.write(data.get(i));
                }
                fileParser.fillFile(buffer.toByteArray());
                buffer = new ByteArrayOutputStream();
            } catch (IOException ex) {
                Logger.getLogger(S_TFTPN.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("didnt get all data rip");
            }
            System.out.println("done");
            socket.close();
        }
    }
    
    private static void constructor() throws SocketException{
        socket = new DatagramSocket(C.PORT);
        fileParser = new FileParser();
        buffer = new ByteArrayOutputStream();
    }
    
    
    
}

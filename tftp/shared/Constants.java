/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tftp.shared;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author alex
 * This class holds all constants that are shared between client and server
 */
public class Constants {
    public final int PORT = 2691;
    public final String HOST = "pi.cs.oswego.edu";
    public final int DATA_SIZE = 512;
    public final int WINDOW_SIZE = 16;
    public final int TIMEOUT_WINDOW = 32;//seconds *1000
    public InetAddress inet6;
    public InetAddress inet4 ;
    public String STORAGE = "/home/alawren3/Desktop/storage";
    
    public final byte[] opc1 = ByteBuffer.allocate(4).putInt(1).array();
    //read
    public final byte[] opc2 = ByteBuffer.allocate(4).putInt(2).array();
    //write
    public final byte[] opc3 = ByteBuffer.allocate(4).putInt(3).array();
    //data
    public final byte[] opc4 = ByteBuffer.allocate(4).putInt(4).array();
    //ack
    public final byte[] opc5 = ByteBuffer.allocate(4).putInt(5).array();
    //error

    public Constants()  {
        try {
            this.inet6 =  InetAddress.getByName("fe80::225:90ff:fe4d:f030");
            this.inet4 = InetAddress.getByName("129.3.20.26");
        } catch (UnknownHostException ex) {
            Logger.getLogger(Constants.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
}

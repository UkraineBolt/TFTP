/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tftp.shared;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 *
 * @author alex
 */
/**
 * 
 * helper code
 * int myInt = myBoolean ? 1 : 0;
 */
public class EnCodeNode {
    //byte[] header;
    private final ByteArrayOutputStream stream = new ByteArrayOutputStream();
    
    public EnCodeNode(int blockNum, String filename) throws IOException{
        
        stream.write(intToBytes(2));
        stream.write(intToBytes(blockNum));
        stream.write(filename.getBytes());
    }
    public EnCodeNode(int blockNum, byte[] data)throws IOException{
        stream.write(intToBytes(3));
        stream.write(intToBytes(blockNum));
        stream.write(data);
    }
    public EnCodeNode(int blockNum)throws IOException{
        stream.write(intToBytes(4));
        stream.write(intToBytes(blockNum));
    }
    
    private int booleanToIntConverter(boolean b){
        return b ? 1 : 0;
    }
    
    private byte[] intToBytes(int i){
        return ByteBuffer.allocate(4).putInt(i).array();
    }
    
    public byte[] getHeader(){
        return stream.toByteArray();
    }
    
    public int getSize(){
        return stream.toByteArray().length;
    }
    
    
    
}

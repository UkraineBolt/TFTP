/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tftp.shared;

import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 *
 * @author alex
 * This class is to parse all data for both client and server
 * 
 * 
 */

/**
 * 
 * helper code
 * int myInt = myBoolean ? 1 : 0;
 */

public class DeCodeNode {
    public int opcode;
    public byte[] data;
    public int blockNum;
    
    public String fileName;
    /**
     * 
     * @param unParsedData = all bytes including filler
     * @param usefulData = the length of byte actually sent
     */
    public DeCodeNode(byte[] unParsedData, int usefulData){
        ByteBuffer bb = ByteBuffer.wrap(unParsedData);
        opcode = bb.getInt();
        if(opcode==2){
            blockNum = bb.getInt();
            byte[] localData = new byte[usefulData-8];
            bb.get(localData);
            String fullString = new String(localData);
            System.out.println(fullString);
            if(fullString.contains(" *@* ")){
                String dir = fullString.substring(fullString.indexOf(" *@* ")+5);
                String filePath = fullString.substring(0,fullString.indexOf(" *@* "));
                String headFolder = dir.substring(dir.lastIndexOf("/"));
                fileName = filePath.substring(filePath.lastIndexOf(headFolder));
                //fileName = filePath.substring(filePath.indexOf(dir));
                System.out.println(dir);
                System.out.println(filePath);
                System.out.println(fileName);
            }else{
                fileName = fullString.substring(fullString.lastIndexOf("/"));
            }
        }else if(opcode==3){
            blockNum = bb.getInt();
            data = new byte[usefulData-8];
            bb.get(data);
        }else if(opcode==4){
            blockNum = bb.getInt();
        }else{
           throw new IndexOutOfBoundsException("opcode out of range");
        }
    }
    
    @Override
    public String toString(){
        String output = "";
        output = output + opcode + " "+blockNum;
        if(data!=null){
            output= output+" "+data.length;
        }
        if(fileName!=null){
            output= output+" "+fileName;
        }
        return output;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tftp.shared;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import tftp.server.S_TFTPN;

/**
 *
 * @author alex
 */
public class FileParser {
    
    public File file;
    public File dir;
    
    public FileParser(){}
    public FileParser(String name){
        file = new File(name);
        dir = new File(file.getParent());
    }
    
    
    public String getName(String fileName){
        return new File(fileName).getName();
    }
    
    public boolean checkIfExists(String fileName){
        return new File(fileName).exists();
    }
    
    public boolean checkIfFileExists(String fileName){
        return new File(fileName).isFile();
    }
    
    public boolean checkIfDirExists(String fileName){
        return new File(fileName).isDirectory();
    }
    
    public boolean makeFile(String fileName) throws IOException{
        return new File(fileName).createNewFile();
    }
    
    public boolean makeDir(String fileName){
        return new File(fileName).mkdirs();
    }
    
    public byte[] getBytes(String fileName) throws IOException{
        return Files.readAllBytes(Paths.get(fileName));
    }
    
    public ArrayList<File> getAllFiles(String dir) {
        ArrayList<File> list = new ArrayList<>(); 
        try{
            Files.walk(Paths.get(dir)).filter(Files::isRegularFile).forEach((n)->list.add(n.toFile()));
            return list;
        }catch(IOException e){
            System.out.println("error with pulling files: you get empty list");
            return list;
        }
    }
    
    public void fillFile(byte[] data){
        try{
            if(file.exists()){
                file.delete();
            }
            file.getParentFile().mkdirs();
            file.createNewFile();
            try (FileOutputStream fos = new FileOutputStream(file)) {
                fos.write(data);
            }
        } catch (IOException ex) {
            Logger.getLogger(S_TFTPN.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public String findPath(String fileName){
        return new File(fileName).getPath();
    }
    
    public static void printData(byte[] data){
        System.out.println("");
        for(int i=0;i<data.length;i++){
            System.out.print(data[i]);
        }
    }
    
}

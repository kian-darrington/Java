package Neural_Network;

import java.io.*;
import java.util.Scanner;

public class Training {
    public static void getData(){
        InputStream f;
        try {
            f = new FileInputStream(new File("src/Neural_Network/t10k-images"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        try {
            byte[] magicNumber = f.readNBytes(4);
            
            f.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void main (String[] args){
        getData();
    }
}

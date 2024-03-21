package Neural_Network;

import java.io.*;
import java.math.BigInteger;
import java.util.Scanner;

public class Training {
    static Scanner console = new Scanner(System.in);
    static int row = 0, num = 0, column, pixelCount = 0;
    static int[][] allInfo;

    public static void getData(){
        InputStream f;
        try {
            f = new FileInputStream(new File("src/Neural_Network/train-images"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        try {
            int magic = new BigInteger(f.readNBytes(4)).intValue();
            num = new BigInteger(f.readNBytes(4)).intValue();
            row = new BigInteger(f.readNBytes(4)).intValue();
            column = new BigInteger(f.readNBytes(4)).intValue();
            pixelCount = row * column;
            byte[] temp = f.readAllBytes();
            allInfo = new int[num][pixelCount];
            int size = temp.length;
            int count = 0, subCount = 0;
            for (int i = 0; i < num; i++) {
                if (temp[i] < 0)
                    allInfo[count][subCount] = 128 - temp[i];

                else
                    allInfo[count][subCount] = temp[i];
                subCount++;
                if ((i + 1) % pixelCount == 0) {
                    count++;
                    subCount = 0;
                }
            }
            System.out.println(magic + ", " + num + ", " + row +" by " + column);
            String cont = "";
            int n = 0;
            while (n < allInfo.length) {
                for (int i = 0; i < pixelCount; i++) {
                    if (allInfo[n][i] != 0)
                        System.out.print((char) 0x2588);
                    else
                        System.out.print(' ');
                    if ((i + 1) % row == 0)
                        System.out.println();
                }
                n++;
                cont = console.nextLine();
            }
            f.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void main (String[] args){
        getData();
    }
}

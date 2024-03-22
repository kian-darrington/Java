package Neural_Network;

import java.io.*;
import java.math.BigInteger;
import java.util.*;

public class Training {
    static Scanner console = new Scanner(System.in);
    static int row = 0, num = 0, column, pixelCount = 0;
    static int[][] allInfo;
    static final Random rand = new Random();
    static byte[] labels;

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
            for (long i = 0; i < size; i++) {
                if (temp[(int) i] < 0)
                    allInfo[count][subCount] = 128 - temp[(int) i];

                else
                    allInfo[count][subCount] = temp[(int) i];
                subCount++;
                if ((i + 1) % pixelCount == 0) {
                    count++;
                    subCount = 0;
                }
            }
            System.out.println(magic + ", " + num + ", " + row +" by " + column);
            f.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            f = new FileInputStream(new File("src/Neural_Network/train-labels"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        try {
            int magic = new BigInteger(f.readNBytes(4)).intValue();
            int labelNum = new BigInteger(f.readNBytes(4)).intValue();
            byte[] temp = f.readAllBytes();
            labels = new byte[labelNum];
            int size = temp.length;
            for (long i = 0; i < size; i++) {
                labels[(int) i] = temp[(int)i];
            }
            System.out.println(magic + ", " + labelNum);
            f.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    void shuffleData(){
        ArrayList<Integer> nums = new ArrayList<>();
        for (int i = 0; i < num; i++)
            nums.add(i);
    }
    public static void main (String[] args){
        getData();
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
            System.out.print(labels[n]);
            n++;
            cont = console.nextLine();
        }
    }
}

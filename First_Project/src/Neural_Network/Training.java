package Neural_Network;

import java.io.*;
import java.math.BigInteger;
import java.util.*;

public class Training {
    static final String BACKBONE = "src/Neural_Network/", TRAIN_IMAGES = "train-images", TRAIN_LABELS = "train-labels";
    static final String TEST_IMAGES = "t10k-images", TEST_LABELS = "t10k-labels";
    static Scanner console = new Scanner(System.in);
    static int row = 0, num = 0, column, pixelCount = 0;
    static int[][] allInfo;
    static final Random rand = new Random();
    static byte[] labels;
    static int[] networkFormat;
    public static final int SECOND_LAYER_COUNT = 32;
    public static void getData(){
        InputStream f;
        try {
            f = new FileInputStream(new File(BACKBONE + TRAIN_IMAGES));
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
            f = new FileInputStream(new File(BACKBONE + TRAIN_LABELS));
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
        networkFormat = new int[] {pixelCount, SECOND_LAYER_COUNT, 10};
    }
    static void shuffleData(){
        ArrayList<Integer> nums = new ArrayList<>(), ran = new ArrayList<>();
        for (int i = 0; i < num; i++)
            nums.add(i);
        while (!nums.isEmpty())
            ran.add(nums.remove(rand.nextInt(nums.size())));
        byte[] tempLabel = new byte[num];
        int [][] tempNums = new int[num][pixelCount];
        for (int i = 0; i < num; i++){
            tempLabel[i] = labels[ran.get(i)];
            tempNums[i] = allInfo[ran.get(i)];
        }
        labels = tempLabel;
        allInfo = tempNums;
    }
    public static void main (String[] args){
        getData();
        shuffleData();
    }
    static void outputNum(int n){
        for (int i = 0; i < pixelCount; i++) {
            if (allInfo[n][i] != 0)
                System.out.print((char) 0x2588);
            else
                System.out.print(' ');
            if ((i + 1) % row == 0)
                System.out.println();
        }
        System.out.println("Correct Answer: " +labels[n]);
    }
    static double sigmoid(double x){
        return (1 / (1 + Math.exp(-x)));
    }
}

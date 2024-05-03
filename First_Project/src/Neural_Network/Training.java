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
    static int[][] testInfo;
    static final Random rand = new Random();
    static byte[] labels;
    static byte[] testLabels;
    static int[] networkFormat;
    public static final int MINI_BATCH_SIZE = 100;
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
        getTrainingData();
        Network net = new Network(networkFormat);
        System.out.println("Got all data and initialized network");
        System.out.println("Before: %" + errorCheck(net));
        while (true) {
            for (int i = 0; i < allInfo.length / MINI_BATCH_SIZE; i++) {
                int[][] miniBatch = new int[MINI_BATCH_SIZE][pixelCount];
                int[] answers = new int[MINI_BATCH_SIZE];
                for (int j = 0; j < MINI_BATCH_SIZE; j++) {
                    miniBatch[j] = allInfo[i * MINI_BATCH_SIZE + j];
                    answers[j] = labels[i * MINI_BATCH_SIZE + j];
                }
                net.backPropagate(miniBatch, answers);
            }
            System.out.println("After: %" + errorCheck(net));
        }
    }
    static double errorCheck (Network net){
        double numCorrect = 0;
        int size = testInfo.length;
        for (int i = 0; i < size; i++){
            double[] ans = net.feedForward(testInfo[i]);
            double totalAns = 0;
            int index = 0;
            for (int d = 0; d < 10; d++) {
                if (ans[d] > totalAns) {
                    totalAns = ans[d];
                    index = d;
                }
            }
            if (index == testLabels[i])
                numCorrect++;
        }
        return 100 * numCorrect / size;
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
    static void getTrainingData(){
        InputStream f;
        try {
            f = new FileInputStream(new File(BACKBONE + TEST_IMAGES));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        try {
            int magic = new BigInteger(f.readNBytes(4)).intValue();
            int NUM = new BigInteger(f.readNBytes(4)).intValue();
            int ROW = new BigInteger(f.readNBytes(4)).intValue();
            int COL = new BigInteger(f.readNBytes(4)).intValue();
            byte[] temp = f.readAllBytes();
            testInfo = new int[NUM][pixelCount];
            int size = temp.length;
            int count = 0, subCount = 0;
            for (long i = 0; i < size; i++) {
                if (temp[(int) i] < 0)
                    testInfo[count][subCount] = 128 - temp[(int) i];

                else
                    testInfo[count][subCount] = temp[(int) i];
                subCount++;
                if ((i + 1) % pixelCount == 0) {
                    count++;
                    subCount = 0;
                }
            }
            System.out.println(magic + ", " + NUM + ", " + row +" by " + column);
            f.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            f = new FileInputStream(new File(BACKBONE + TEST_LABELS));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        try {
            int magic = new BigInteger(f.readNBytes(4)).intValue();
            int labelNum = new BigInteger(f.readNBytes(4)).intValue();
            byte[] temp = f.readAllBytes();
            testLabels = new byte[labelNum];
            int size = temp.length;
            for (long i = 0; i < size; i++) {
                testLabels[(int) i] = temp[(int)i];
            }
            System.out.println(magic + ", " + labelNum);
            f.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

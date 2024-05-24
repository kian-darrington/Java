package Neural_Network;

import java.io.*;
import java.math.BigInteger;
import java.util.*;

public class Training {
    static final String BACKBONE = "src/Neural_Network/", TRAIN_IMAGES = "train-images", TRAIN_LABELS = "train-labels";
    static final String TEST_IMAGES = "t10k-images", TEST_LABELS = "t10k-labels";
    static Scanner console = new Scanner(System.in);
    static int row = 0, num = 0, column, pixelCount = 0;
    static double[][] allInfo;
    static double[][] testInfo;
    static final Random rand = new Random();
    static byte[] labels;
    static byte[] testLabels;
    static int[] networkFormat;
    public static final int MINI_BATCH_SIZE = 10;
    public static final int SECOND_LAYER_COUNT = 30;
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
            allInfo = new double[num][pixelCount];
            int size = temp.length;
            int count = 0, subCount = 0;
            for (long i = 0; i < size; i++) {
                if (temp[(int) i] < 0)
                    allInfo[count][subCount] = (double)(128 - temp[(int) i]) /255.0;
                else
                    allInfo[count][subCount] = (double) temp[(int) i] / 255.0;
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
        double [][] tempNums = new double[num][pixelCount];
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
        int epoch = 0;
        while (true) {
            epoch++;
            for (int i = 0; i < allInfo.length / MINI_BATCH_SIZE; i++) {
                double[][] miniBatch = new double[MINI_BATCH_SIZE][pixelCount];
                double[][] answers = new double[MINI_BATCH_SIZE][10];
                for (int j = 0; j < MINI_BATCH_SIZE; j++) {
                    miniBatch[j] = allInfo[i * MINI_BATCH_SIZE + j];
                    answers[j][labels[i * MINI_BATCH_SIZE + j]] = 1.0;
                }
                net.backPropagate(miniBatch, answers);
            }
            System.out.println("Epoch " + epoch + ": " + errorCheck(net) + "%");
            shuffleData();
        }
        /*Matrix one = new Matrix(new double[][] {new double[] {1, 2}, new double[] {4, 5}, new double[] {3, 4}});
        Matrix two = new Matrix(new double[][] {new double[] {5, 4, 5}, new double[] {2, 8, 1}});
        System.out.println(one);
        System.out.println(one.add(one));
        System.out.println(one.transpose());*/
    }
    static double errorCheck (Network net){
        double numCorrect = 0;
        int size = testInfo.length;
        for (int i = 0; i < size; i++){
            double[][] ans = net.feedForward(testInfo[i]);
            double totalAns = Double.NEGATIVE_INFINITY;
            int index = 0;
            for (int d = 0; d < 10; d++) {
                if (ans[d][0] > totalAns) {
                    totalAns = ans[d][0];
                    index = d;
                }
            }
            if (index == testLabels[i])
                numCorrect++;
        }
        int temp = rand.nextInt(testInfo.length);
        //outputNum(testInfo[temp]);
        //System.out.println(testLabels[temp]);
        //System.out.println(Arrays.deepToString(net.feedForward(testInfo[temp])));
        return 100 * numCorrect / size;
    }
    static void outputNum(int[] n){
        for (int i = 0; i < pixelCount; i++) {
            if (n[i] != 0)
                System.out.print((char) 0x2588);
            else
                System.out.print(' ');
            if ((i + 1) % row == 0)
                System.out.println();
        }
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
            testInfo = new double[NUM][pixelCount];
            int size = temp.length;
            int count = 0, subCount = 0;
            for (long i = 0; i < size; i++) {
                if (temp[(int) i] < 0)
                    testInfo[count][subCount] = (double)(128 - temp[(int) i]) / 255;
                else
                    testInfo[count][subCount] = (double) temp[(int) i] / 255;
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

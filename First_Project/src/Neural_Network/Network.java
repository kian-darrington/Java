package Neural_Network;

import java.util.Random;

public class Network {
    final static double BIAS_RANGE = 20; //Random num from -10 to 10
    final static double WEIGHT_RANGE = 10; // Random num from -5 to 5
    static Node[] firstLayer;
    static Node[] secondLayer;
    static Node[] output = new Node[10];
    static final Random rand = new Random();
    Network(int first, int second){
        firstLayer = new Node[first];
        secondLayer = new Node[second];
        for (Node n : firstLayer) {
            n.setBias((rand.nextDouble() - .5) * BIAS_RANGE);
            n.setWeights(randWeights(1));
        }
        for (Node n : secondLayer){
            n.setBias((rand.nextDouble() - .5) * BIAS_RANGE);
            n.setWeights(randWeights(first));
        }
        for (Node n :output){
            n.setBias((rand.nextDouble() - .5) * BIAS_RANGE);
            n.setWeights(randWeights(second));
        }
    }
    double[] randWeights(int n){
        double[] temp = new double[n];
        for (int i = 0; i < n; i++)
            temp[i] = (rand.nextDouble() - .5) * WEIGHT_RANGE;
        return temp;
    }
    static int feedForward(int[] picture){
        double[] input = new double[picture.length];
        for (int i = 0; i < picture.length; i++){
            input[i] = (double)picture[i] / 255;
        }
        return 0;
    }
}
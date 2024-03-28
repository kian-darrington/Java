package Neural_Network;

import java.util.Random;

public class Network {
    final static double BIAS_RANGE = 20; //Random num from -10 to 10
    final static double WEIGHT_RANGE = 4; // Random num from -2 to 2
    static Node[][] nodes;
    static final Random rand = new Random();
    Network(int layerCount, int[] layerNums){
        nodes = new Node[layerCount][];

        for (int i = 1; i < layerCount; i++){
            nodes[i] = new Node[layerNums[i]];
            for (int j = 0; j < nodes[i].length; j++){
                nodes[i][j].setBias((rand.nextDouble() - .5) * BIAS_RANGE);
                nodes[i][j].setWeights(randWeights(layerNums[i - 1]));
            }
        }
    }
    //Creates a list of random values for length of n in the range of WEIGHT_RANGE / 2
    double[] randWeights(int n){
        double[] temp = new double[n];
        for (int i = 0; i < n; i++)
            temp[i] = (rand.nextDouble() - .5) * WEIGHT_RANGE;
        return temp;
    }
    //Returns an array of the last output of the neural network, 10 elements with an ideal of 1 of those elements
    //being a 1 with the rest being a 0, meaning that the picture is that number
    static double[] feedForward(int[] picture){
        //Changes the format to the accepted input of the Nodes (a double[])
        //Due to the fact that the input is only one number, this a giant column vector
        double[][] input = new double[picture.length][1];
        for (int i = 0; i < picture.length; i++){
            input[i][0] = (double)picture[i] / 255;
        }
        // Next receives the output of the entire first layer
        double[] next = new double[picture.length];
        for (int i = 0; i < picture.length; i++)
            next[i] = nodes[0][i].output(input[i]);
        // Next continues feeding forward through the network until the last layer is reached
        for (int i = 1; i < nodes.length; i++){
            double[] temp = new double[nodes[i].length];
            for (int j = 0; j < temp.length; j++){
                temp[j] = nodes[i][j].output(next);
            }
            next = temp;
        }

        return next;
    }
    static void backPropagate(int[][] miniBatch){
        for (int[] picture : miniBatch) {
            double[][][] outputs = new double[3][nodes.length][];
            double[][] input = new double[picture.length][1];
            for (double[][] d : outputs){
                for (int i = 0; i< d.length; i++)
                    d[i] = new double[nodes[i].length];
            }
            for (int i = 0; i < picture.length; i++) {
                input[i][0] = (double) picture[i] / 255;
            }
            // Next receives the output of the entire first layer
            double[] next = new double[picture.length];
            for (int i = 0; i < picture.length; i++) {
                outputs[0][0][i] = nodes[0][i].rawOutput(input[i]);
                outputs[1][0][i] = sigmoid(outputs[0][0][i]);
                outputs[2][0][i] = sigmoidPrime(outputs[0][0][i]);
            }
            // Next continues feeding forward through the network until the last layer is reached
            for (int i = 1; i < nodes.length; i++) {
                int size = nodes[i].length;
                for (int j = 0; j < size; j++) {
                    outputs[0][i][j] = nodes[0][i].rawOutput(input[i]);
                    outputs[1][i][j] = sigmoid(outputs[0][0][i]);
                    outputs[2][i][j] = sigmoidPrime(outputs[0][0][i]);
                }
            }

        }
    }
    static double sigmoid(double x){ return (1 / (1 + Math.exp(-x)));}
    static double sigmoidPrime(double x) { return sigmoid(x)*(1-sigmoid(x)); }
}
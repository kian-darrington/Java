package Neural_Network;

import java.util.Arrays;
import java.util.Random;

public class Network {
    final static double WEIGHT_RANGE = 2; // Random num from -2 to 2
    static Node[][] nodes;
    static final Random rand = new Random();
    static final double LEARNING_RATE = 0.0;
    static int layerNum;
    static int[] layerCounts;
    public Network(int[] layerNums){
        layerNum = layerNums.length;
        layerCounts = layerNums;
        nodes = new Node[layerNum][];
        nodes[0] = new Node[layerNums[0]];
        for(int i = 0; i < layerNums[0]; i++)
            nodes[0][i] = new Node();
        for (int i = 1; i < layerNum; i++){
            nodes[i] = new Node[layerNums[i]];
            for (int j = 0; j < nodes[i].length; j++){
                nodes[i][j] = new Node();
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
    public double[] feedForward(int[] picture){
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
        for (int i = 1; i < layerNum; i++){
            double[] temp = new double[nodes[i].length];
            for (int j = 0; j < temp.length; j++){
                temp[j] = nodes[i][j].output(next);
            }
            next = temp;
        }

        return next;
    }
    public void backPropagate(int[][] miniBatch, int[] ans) {
        int batchSize = ans.length;
        double[][] answers = new double[batchSize][10];
        for (int i = 0; i < batchSize; i++)
            answers[i][ans[i]] = 1.0;
        Matrix[][] biasError = new Matrix[batchSize][layerNum - 1]; // First is biases
        Matrix[][] weightError = new Matrix[batchSize][layerNum - 1]; // Second is weights
        Matrix[] weights = new Matrix[layerNum - 1];
        Matrix[] weightsTranspose = new Matrix[layerNum - 1];
        for (int i = 0; i < layerNum - 1; i++) {
            double[][] temp = new double[layerCounts[i + 1]][];
            for (int o = 0; o < layerCounts[i + 1]; o++)
                temp[o] = nodes[i + 1][o].getWeights();
            weights[i] = new Matrix(temp);
            weightsTranspose[i] = weights[i].transpose();
        }

        for (int count = 0; count < batchSize; count++) {
            int[] picture = miniBatch[count];
            double[][][] outputs = new double[2][layerNum][]; // 0 is sigmoid data, 1 is sigPrime
            double[][] input = new double[picture.length][1];
            for (int j = 0; j < outputs.length; j++) {
                for (int i = 0; i < outputs[j].length; i++)
                    outputs[j][i] = new double[layerCounts[i]];
            }
            for (int i = 0; i < picture.length; i++) {
                input[i][0] = (double) picture[i] / 255.0;
            }
            // Next receives the output of the entire first layer
            double[] next = new double[picture.length];
            for (int i = 0; i < picture.length; i++) {
                double raw = nodes[0][i].rawOutput(input[i]);
                outputs[0][0][i] = sigmoid(raw);
                outputs[1][0][i] = sigmoidPrime(raw);
            }
            // Next continues feeding forward through the network until the last layer is reached
            for (int i = 1; i < layerNum; i++) {
                for (int j = 0; j < layerCounts[i]; j++) {
                    double raw = nodes[i][j].rawOutput(outputs[0][i - 1]);
                    outputs[0][i][j] = sigmoid(raw);
                    outputs[1][i][j] = sigmoidPrime(raw);
                }
            }
            Matrix[][] outputMatrix = new Matrix[2][layerNum];
            for (int j = 0; j < 2; j++)
                for (int q = 0; q < layerNum; q++)
                    outputMatrix[j][q] = new Matrix(new double[][] {outputs[j][q]});
            double[] costs = new double[layerCounts[layerNum - 1]];
            for (int i = 0; i < layerCounts[layerNum - 1]; i++) {
                //Gets you the rate of change of the output based off of the activation
                costs[i] *= (answers[count][i] - outputs[0][layerNum - 1][i]) * outputs[1][layerNum - 1][i];
            }
            Matrix[] costMatrix = new Matrix[layerNum - 1];
            costMatrix[layerNum - 1] = new Matrix(new double[][] {costs} );
            for (int i = layerNum - 1; i > 0; i--) {
                if (i < layerNum - 1)
                    costMatrix[i - 1] = costMatrix[i].dotProd(weightsTranspose[i]).elemProd(outputMatrix[1][i]);
                biasError[count][i - 1] = costMatrix[i - 1];
                weightError[count][i - 1] = costMatrix[i].dotProd(outputMatrix[0][i].transpose());
            }
        }
        Matrix[] totalWeights = new Matrix[layerNum - 1];
        Matrix[] totalBiases = new Matrix[layerNum - 1];
        for (int i = 0; i < layerNum - 1; i++) {
            totalWeights[i] = weightError[0][i];
            totalBiases[i] = biasError[0][i];
            for (int count = 1; count < batchSize; count++){
                totalWeights[i] = totalWeights[i].add(weightError[count][i]);
                totalBiases[i] = totalBiases[i].add(biasError[count][i]);
            }
        }
        for (int i = 0; i < layerNum - 1; i++) {
            double[][] rawWeight = totalWeights[i].getValues();
            double[][] rawBias = totalBiases[i].getValues();
            for (int j = 0; j < layerCounts[i + 1]; j++){
                
            }
        }
    }
    static double sigmoid(double x){ return (1.0 / (1.0 + Math.exp(-x)));}
    static double sigmoidPrime(double x) { return sigmoid(x)*(1.0 - sigmoid(x)); }
}
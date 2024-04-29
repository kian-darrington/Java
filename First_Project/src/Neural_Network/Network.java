package Neural_Network;

import java.util.Random;

public class Network {
    final static double WEIGHT_RANGE = 4; // Random num from -2 to 2
    static Node[][] nodes;
    static final Random rand = new Random();
    static final double LEARNING_RATE = 0.02;
    static int layerNum;
    static int[] layerCounts;
    Network(int[] layerNums){
        layerNum = layerNums.length;
        layerCounts = layerNums;
        nodes = new Node[layerNum][];
        nodes[0] = new Node[layerNums[0]];
        for (int i = 1; i < layerNum; i++){
            nodes[i] = new Node[layerNums[i]];
            for (int j = 0; j < nodes[i].length; j++){
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
    public static double[] feedForward(int[] picture){
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
        for (int i =0; i < batchSize; i++)
            answers[i][ans[i]] = ans[i];
        double[][][] biasError = new double[batchSize][layerNum - 1][]; // First is biases
        double[][][][] weightError = new double[batchSize][layerNum - 1][][]; // Second is weights
        double[][][] weights = new double[layerNum - 1][][];
        for (int i = 0; i < layerNum - 1; i++) {
            weights[i] = new double[layerCounts[i + 1]][];
            for (int o = 0; o < layerCounts[i + 1]; o++)
                weights[i][o] = nodes[i + 1][o].getWeights();
        }
        double[][][] weightsTranspose = new double[layerNum - 1][][];
        for (int i = 0; i < layerNum - 1; i++) {
            weightsTranspose[i] = new double[layerCounts[i]][layerCounts[i + 1]];
            for (int o = 0; o < layerCounts[i]; o++) {
                for (int q = 0; q < layerCounts[i + 1]; q++)
                    weightsTranspose[i][o][q] = weights[i][q][o];
            }
        }

        for (int count = 0; count < batchSize; count++) {
            int[] picture = miniBatch[count];
            double[][][] outputs = new double[3][layerNum][]; // 0 is raw data, 1 is sigmoid data, 2 is sigPrime
            double[][] input = new double[picture.length][1];
            for (int j = 0; j < outputs.length; j++) {
                for (int i = 0; i < outputs[j].length; i++)
                    outputs[j][i] = new double[layerCounts[i]];
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
            for (int i = 1; i < layerNum; i++) {
                for (int j = 0; j < layerCounts[i]; j++) {
                    outputs[0][i][j] = nodes[i][j].rawOutput(outputs[1][i - 1]);
                    outputs[1][i][j] = sigmoid(outputs[0][i][j]);
                    outputs[2][i][j] = sigmoidPrime(outputs[0][i][j]);
                }
            }
            double[][] costs = new double[layerNum - 1][];
            for (int i = 1; i < layerNum; i++)
                costs[i - 1] = outputs[2][i].clone(); //Gathers the primes of outputs of all the network past the input
            for (int i = 0; i < layerCounts[layerNum - 1]; i++) {
                //Gets you the rate of change of the output based off of the activation
                costs[layerNum - 2][i] *= outputs[1][layerNum - 1][i] - answers[count][i];
            }
            for (int i = layerNum - 1; i > 0; i--) {
                if (i < layerNum - 1) {
                    for (int j = 0; j < layerCounts[i]; j++) {
                        double temp = 0;
                        for (int d = 0; d < layerCounts[i + 1]; d++) {
                            double forwardCost = costs[i][d];
                            for (double t : weightsTranspose[i][d])
                                temp += forwardCost * t;
                        }
                        costs[i][j] *= temp;
                    }
                }
                biasError[count][i - 1] = costs[i - 1];
                double[][] weightChange = new double[layerCounts[i]][layerCounts[i - 1]];
                for (int j = 0; j < layerCounts[i]; j++) {
                    double temp = costs[i][j];
                    for (int h = 0; h < layerCounts[i - 1]; h++)
                        weightChange[j][h] = temp * outputs[2][i - 1][h];
                }

                weightError[count][i - 1] = weightChange;
            }
        }

        for (int i = 0; i < layerNum - 1; i++) {
            double[] biasAverage = new double[layerCounts[i + 1]];
            double[][] weightAverage = new double[layerCounts[i + 1]][layerCounts[i]];
            for (int count = 0; count < miniBatch.length; count++) {
                for (int r = 0; r < layerCounts[i + 1]; r++) {
                    biasAverage[r] += biasError[count][i][r];
                    for (int g = 0; g < layerCounts[i]; g++)
                        weightAverage[r][g] += weightError[count][i][r][g];
                }
            }
            for (int r = 0; r < layerCounts[i + 1]; r++) {
                biasAverage[r] *= LEARNING_RATE / (double) batchSize;
                for (int k = 0; k < layerCounts[i]; k++)
                    weightAverage[r][k] *= LEARNING_RATE / (double) batchSize;
                nodes[i + 1][r].changeBias(biasAverage[r]);
                nodes[i + 1][r].changeWeights(weightAverage[r]);
            }
        }
    }
    static double sigmoid(double x){ return (1 / (1 + Math.exp(-x)));}
    static double sigmoidPrime(double x) { return sigmoid(x)*(1-sigmoid(x)); }
}
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
        for (int i = 1; i < layerNum; i++){
            double[] temp = new double[nodes[i].length];
            for (int j = 0; j < temp.length; j++){
                temp[j] = nodes[i][j].output(next);
            }
            next = temp;
        }

        return next;
    }
    public static void backPropagate(int[][] miniBatch, double[] answer) {
        double[][][] biasError = new double[answer.length][][]; // First is biases
        double[][][][] weightError = new double[answer.length][][][]; // Second is weights

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

        for (int count = 0; count < miniBatch.length; count++) {
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
//            for (int i = 1; i < layerNum; i++) //Remember this
//                costs[i - 1] = new double[layerCounts[i]];
            for (int i = 1; i < layerNum; i++)
                costs[i - 1] = outputs[1][i].clone(); //Gathers the outputs of all the network past the input
            for (int i = 0; i < layerCounts[layerNum - 1]; i++) {
                costs[layerNum - 1][i] -= answer[i]; //Gets you the rate of change of the output based off of the activation
                costs[layerNum - 1][i] *= outputs[2][layerNum - 1][i];
            }
            for (int i = layerNum - 2; i > -1; i--) {
                for (int j = 0; j < layerCounts[i]; j++) {
                    double temp = 0;
                    for (int d = 0; d < costs[i + 1].length; d++) {
                        double constant = costs[i + 1][d];
                        for (double t : weightsTranspose[i][d])
                            temp += constant * t;
                    }
                    costs[i][j] *= temp;
                }
                biasError[count][i] = costs[i];
                double[][] weightChange = new double[layerCounts[i]][];
                for (int j = 0; j < costs[i].length; j++)
                    weightChange[j] *= outputs[1][i][j];

                totalError[1][count][i] = weightChange;
            }
        }

        for (int i = 0; i < layerNum - 1; i++) {
            double[] biasAverage = new double[layerCounts[i]];
            for (int count = 0; count < miniBatch.length; count++) {
                for (int r = 0; r < layerCounts[i]; r++) {
                    biasAverage[r] += biasError[count][i][r];
                }
            }
            for (int r = 0; r < layerCounts[i]; r++) {
                biasAverage[r] *= LEARNING_RATE / (double) answer.length;
                nodes[i + 1][r].changeBias(biasAverage[r]);
            }
        }
    }
    static double sigmoid(double x){ return (1 / (1 + Math.exp(-x)));}
    static double sigmoidPrime(double x) { return sigmoid(x)*(1-sigmoid(x)); }
}
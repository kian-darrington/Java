package Neural_Network;

import java.util.Arrays;
import java.util.Random;

public class Network {
    final static double WEIGHT_RANGE = 2; // Random num from -1 to 1 for standard distribution
    static Matrix[] biases;
    static Matrix[] weights;
    static final Random rand = new Random();
    static final double LEARNING_RATE = 3;
    static int layerNum;
    static int[] layerCounts;
    public Network(int[] layerNums){
        layerNum = layerNums.length;
        layerCounts = layerNums;
        biases = new Matrix[layerNum -1];
        weights = new Matrix[layerNum - 1];
        for (int i = 0; i < layerNum - 1; i++) {
            biases[i] = new Matrix(gaussianArray(layerCounts[i + 1], 1));
            weights[i] = new Matrix(gaussianArray(layerCounts[i + 1], layerCounts[i]));
        }
    }
    //Creates a double[][] with random Gaussian numbers centered around 0
    double[][] gaussianArray(int row, int col){
        double[][] value = new double[row][col];
        for (int i = 0; i < row; i++)
            for (int j = 0; j < col; j++)
                value[i][j] = rand.nextGaussian();
        return value;
    }
    //Returns an array of the last output of the neural network, 10 elements with an ideal of 1 of those elements
    //being a 1 with the rest being a 0, meaning that the picture is that number
    public double[][] feedForward(double[] picture){
        Matrix inputs = new Matrix(new double[][] {picture}).transpose();
        for (int i = 0; i < layerNum - 1; i++){
            inputs = weights[i].dotProd(inputs).add(biases[i]).applyFunction(this::sigmoid);
        }
        return inputs.getValues();
    }
    final int RAW = 0, SIGMOID = 1, SIG_PRIME = 2;
    public void backPropagate(double[][] miniBatch, double[][] ans) {
        int batchSize = ans.length;

        Matrix[][] biasError = new Matrix[batchSize][layerNum - 1]; // Total bias changes
        Matrix[][] weightError = new Matrix[batchSize][layerNum - 1]; // Total weight changes

        Matrix[] weightsTranspose = new Matrix[layerNum - 1];
        for (int i = 0; i < layerNum - 1; i++)
            weightsTranspose[i] = weights[i].transpose();

        for (int count = 0; count < batchSize; count++) {
            Matrix[][] outputs = new Matrix[3][layerNum]; // 0 is raw data, 1 is sigmoid data, 2 is sigPrime

            Matrix answer = new Matrix(new double[][] {ans[count]}).transpose();
            // Next receives the output of the entire first layer
            outputs[SIGMOID][0] = new Matrix(new double[][] {miniBatch[count]}).transpose();
            // Next continues feeding forward through the network until the last layer is reached
            for (int i = 0; i < layerNum - 1; i++) {
                outputs[RAW][i + 1] = weights[i].dotProd(outputs[SIGMOID][i]).add(biases[i]);
                outputs[SIGMOID][i + 1] = outputs[RAW][i + 1].applyFunction(this::sigmoid);
                outputs[SIG_PRIME][i + 1] = outputs[RAW][i + 1].applyFunction(this::sigmoidPrime);
            }
            Matrix[] costMatrix = new Matrix[layerNum - 1];
            costMatrix[layerNum - 2] = answer.subtract(outputs[SIGMOID][layerNum - 1]).elemProd(
                    outputs[SIG_PRIME][layerNum - 1]);
            for (int i = layerNum - 1; i > 0; i--) {
                if (i < layerNum - 1)
                    costMatrix[i - 1] = weightsTranspose[i].dotProd(costMatrix[i]).elemProd(outputs[SIG_PRIME][i]);
                biasError[count][i - 1] = costMatrix[i - 1];
                weightError[count][i - 1] = costMatrix[i - 1].dotProd(outputs[SIGMOID][i - 1].transpose());
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
        Matrix[] weightGradient = new Matrix[layerNum - 1];
        Matrix[] biasGradient = new Matrix[layerNum - 1];
        for (int i = 0; i < layerNum - 1; i++) {
            weightGradient[i] = totalWeights[i].elementMultiply(LEARNING_RATE / (double)batchSize);
            biasGradient[i] = totalBiases[i].elementMultiply(LEARNING_RATE / (double)batchSize);
            weights[i].alterSub(weightGradient[i]);
            biases[i].alterSub(biasGradient[i]);
        }
    }
    double sigmoid(double x){ return (1.0 / (1.0 + Math.exp(-x)));}
    double sigmoidPrime(double x) { return sigmoid(x)*(1.0 - sigmoid(x)); }
}
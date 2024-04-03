package Neural_Network;


public class Node {
    double[] weights = new double[] {1};
    int size = 1;
    double bias = 0;
    void setBias(double b) {bias = b;}
    void setWeights(double [] w) {weights = w; size = w.length;}
    double [] getWeights () {return weights;}
    double output(double[] input) {
        double total = 0;
        for (int i = 0; i < size; i++)
            total += weights[i] * input[i];
        return sigmoid(total + bias);
    }
    double rawOutput(double[] input){
        double total = 0;
        for (int i = 0; i < size; i++)
            total += weights[i] * input[i];
        return total + bias;
    }
    double sigmoid(double x){ return (1 / (1 + Math.exp(-x)));}
}
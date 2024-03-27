package Neural_Network;


public class Node {
    double[] weights = null;
    int size;
    double bias;
    void setBias(double b) {bias = b;}
    void setWeights(double [] w) {weights = w; size = w.length;}
    double output(double[] input) {
        double total = 0;
        for (int i = 0; i < size; i++)
            total += weights[i] * input[i];
        return sigmoid(total + bias);
    }
    static double sigmoid(double x){
        return (1 / (1 + Math.exp(-x)));
    }
}
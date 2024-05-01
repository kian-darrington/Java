package Neural_Network;


public class Node {
    double[] weights = new double[] {1};
    int size = 1;
    double bias = 0;
    void setBias(double b) {bias = b;}
    public void setWeights(double [] w) {weights = w; size = w.length;}
    public double [] getWeights () {return weights;}
    public double output(double[] input) {
        double total = 0;
        for (int i = 0; i < size; i++)
            total += weights[i] * input[i];
        return sigmoid(total + bias);
    }
    public double rawOutput(double[] input){
        double total = 0;
        for (int i = 0; i < size; i++)
            total += weights[i] * input[i];
        return total + bias;
    }
    public void changeBias(double amount) {bias -= amount;}
    public void changeWeights(double[] w){
        for (int i = 0; i < weights.length; i++)
            weights[i] -= w[i];
    }
    Node(){}
    public double sigmoid(double x){ return (1 / (1 + Math.exp(-x)));}
}
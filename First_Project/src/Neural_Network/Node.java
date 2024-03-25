package Neural_Network;


public class Node {
    double input = 0;
    double[] weights = null;
    double bias;
    void setBias(double b) {bias = b;}
    double getInput() {return input;}
    void setWeights(double [] w) {weights = w;}
    void setInput(double i) {input = i;}
    double[] getWeights () {return weights;}
    static double sigmoid(double x){
        return (1 / (1 + Math.exp(-x)));
    }
}
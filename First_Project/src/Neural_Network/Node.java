package Neural_Network;

public class Node {
    double input = 0;
    double[] weights = null;
    double Input() {return input;}
    void setWeights(double [] w) {weights = w;}
    void setInput(double i) {input = i;}
    double[] getWeights () {return weights;}
}
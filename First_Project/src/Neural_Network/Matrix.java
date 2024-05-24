package Neural_Network;

import java.util.Arrays;
import java.util.function.DoubleUnaryOperator;
public class Matrix {
    private final double[][] values;
    private final int row;
    private final int col;
    Matrix(double[][] temp){
        values = temp;
        row = values.length;
        col = values[0].length;
    }
    public Matrix dotProd(Matrix other){
        if (other.row != this.col) {
            System.out.println(this.row + " " + this.col + " " + other.row + " " + other.col);
            throw new IllegalArgumentException("BAD CODE IDIOT, IT NO WORK... for dot prod that is");
        }
        double[][] product = new double[this.row][other.col];
        for (int i = 0; i < this.row; i++) {
            for (int j = 0; j < other.col; j++){
                double added = 0.0;
                for (int d = 0; d < this.col; d++){
                    //System.out.println(other.values[d][j] + " * " + this.values[i][d]);
                    added += other.values[d][j] * this.values[i][d];
                }
                //System.out.println(added);
                product[i][j] = added;
            }
        }
        return new Matrix(product);
    }
    public Matrix elemProd(Matrix other) {
        if (this.row != other.row || this.col != other.col) {
            System.out.println(this.row + " " + this.col + " " + other.row + " " + other.col);
            throw new IllegalArgumentException("Matrix dimensions do not match for element-wise multiplication.");
        }

        double[][] product = new double[this.row][this.col];
        for (int i = 0; i < this.row; i++) {
            for (int j = 0; j < this.col; j++) {
                product[i][j] = this.values[i][j] * other.values[i][j];
            }
        }

        return new Matrix(product);
    }
    public Matrix add(Matrix other) {
        if (this.row != other.row || this.col != other.col) {
            System.out.println(this.row + " " + this.col + " " + other.row + " " + other.col);
            throw new IllegalArgumentException("Matrix dimensions do not match for Matrix addition.");
        }

        double[][] product = new double[this.row][this.col];
        for (int i = 0; i < this.row; i++)
            for (int j = 0; j < this.col; j++)
                product[i][j] = this.values[i][j] + other.values[i][j];

        return new Matrix(product);
    }
    public Matrix elementMultiply(double mult){
        double[][] product = new double[this.row][this.col];
        for (int i = 0; i < row; i++)
            for (int j = 0; j < col; j++)
                product[i][j] = values[i][j] * mult;

        return new Matrix(product);
    }
    public Matrix subtract(Matrix other) {
        if (this.row != other.row || this.col != other.col) {
            System.out.println(this.row + " " + this.col + " " + other.row + " " + other.col);
            throw new IllegalArgumentException("Matrix dimensions do not match for Matrix subtraction.");
        }

        double[][] product = new double[this.row][this.col];
        for (int i = 0; i < this.row; i++)
            for (int j = 0; j < this.col; j++)
                product[i][j] = this.values[i][j] - other.values[i][j];

        return new Matrix(product);
    }
    public Matrix transpose(){
        double[][] trans = new double[col][row];
        for (int i = 0; i < col; i++)
            for (int j = 0; j < row; j++)
                trans[i][j] = values[j][i];

        return new Matrix(trans);
    }
    public Matrix applyFunction(DoubleUnaryOperator function){
        double[][] product = new double[this.row][this.col];
        for (int i = 0; i < row; i++)
            for (int j = 0; j < col; j++)
                product[i][j] = function.applyAsDouble(values[i][j]);

        return new Matrix(product);
    }
    public String toString(){
        StringBuilder string = new StringBuilder();
        for (double[] d : values) {
            string.append(Arrays.toString(d));
            string.append('\n');
        }
        return string.toString();
    }
    int[] largestValue(){
        int[] index = new int[2];
        double highest = Double.NEGATIVE_INFINITY;
        for (int i = 0; i < row; i++){
            for (int j = 0; j < col; j++){
                if (values[i][j] > highest)
                    index = new int[] {row, col};
            }
        }
        return index;
    }
    public double[][] getValues() {
        return values;
    }
}

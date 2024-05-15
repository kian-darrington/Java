package Neural_Network;

public class Matrix {
    private final double[][] values;
    private final int row;
    private final int col;
    Matrix(double[][] temp){
        values = temp;
        row = values.length;
        col = values[0].length;
    }
    public Matrix multiply(Matrix other){
        if (other.row != col)
            throw new IllegalArgumentException("BAD CODE IDIOT, IT NO WORK");
        double[][] raw = other.values;
        double[][] product = new double[row][other.col];
        for (int i = 0; i < col; i++) {
            double added = 0;
        }
        return new Matrix(product);
    }
    public Matrix transpose(){
        double[][] trans = new double[col][row];
        for (int i = 0; i < col; i++)
            for (int j = 0; j < row; j++)
                trans[i][j] = values[j][i];
        return new Matrix(trans);
    }
}

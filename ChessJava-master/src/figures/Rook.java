package figures;

public class Rook extends Figure {
    public Rook(char color) {
        super("R", color);
    }

    @Override
    public boolean canMove(int row, int col, int row1, int col1) {
        if (!super.canMove(row, col, row1, col1)) {
            return false;
        }

        return (row == row1 || col == col1) && (row != row1 || col != col1);
    }

    @Override
    public boolean canAttack(int row, int col, int row1, int col1) {
        return (row == row1 || col == col1) && (row != row1 || col != col1);
    }
}

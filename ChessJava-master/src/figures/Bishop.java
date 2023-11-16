package figures;

public class Bishop extends Figure{
    public Bishop(char color) {
        super("B", color);
    }

    @Override
    public boolean canMove(int row, int col, int row1, int col1) {
        if (!super.canMove(row, col, row1, col1)) {
            return false;
        }
        return Math.abs(row - row1) == Math.abs(col-col1);
    }

    @Override
    public boolean canAttack(int row, int col, int row1, int col1) {
        return this.canMove(row, col, row1, col1);
    }
}

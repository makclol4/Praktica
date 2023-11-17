import figures.*;

import java.util.ArrayList;

public class Board {
    //TODO: Список фигур и начальное положение всех фигур
    final Figure[][] fields = new Figure[8][8];
    private final ArrayList<String> takeWhite = new ArrayList<>(16);
    private final ArrayList<String> takeBlack = new ArrayList<>(16);

    public char getColorGaming() {
        return colorGaming;
    }

    public char getOppositeColorGaming() {
        if (colorGaming == 'w')
            return 'b';
        else
            return 'w';
    }

    public void setColorGaming(char colorGaming) {
        this.colorGaming = colorGaming;
    }

    private char colorGaming;

    public void init() {
        this.fields[0] = new Figure[]{
                new Rook('w'), new Knight('w'),
                new Bishop('w'), new Queen('w'),
                new King('w'), new Bishop('w'),
                new Knight('w'), new Rook('w')
        };
        this.fields[1] = new Figure[]{
                new Pawn('w'), new Pawn('w'),
                new Pawn('w'), new Pawn('w'),
                new Pawn('w'), new Pawn('w'),
                new Pawn('w'), new Pawn('w'),
        };

        this.fields[7] = new Figure[]{
                new Rook('b'), new Knight('b'),
                new Bishop('b'), new Queen('b'),
                new King('b'), new Bishop('b'),
                new Knight('b'), new Rook('b')
        };
        this.fields[6] = new Figure[]{
                new Pawn('b'), new Pawn('b'),
                new Pawn('b'), new Pawn('b'),
                new Pawn('b'), new Pawn('b'),
                new Pawn('b'), new Pawn('b'),
        };
    }

    static int blackKingRow = 7;
    static int blackKingCol = 4;
    static int whiteKingRow = 0;
    static int whiteKingCol = 4;

    public String getCellForPrinting(int row, int col) {
        Figure figure = this.fields[row][col];
        if (figure == null) {
            return "    ";
        }
        return " " + figure.getColor() + figure.getName() + " ";
    }

    public ArrayList<String> getTakeWhite() {
        return takeWhite;
    }

    public ArrayList<String> getTakeBlack() {
        return takeBlack;
    }

    public boolean move_figure(int row1, int col1, int row2, int col2) {

        Figure figure = this.fields[row1][col1];

        if (figure.canMove(row1, col1, row2, col2)
                && isCellEmpty(row2, col2)
                && isWayClear(row1, col1, row2, col2)
                && getPositionOfFirstFigureWhichCanAttackByRowAndCol(copyFieldsAndMove(row1, col1, row2, col2), getCurrentKingRow(), getCurrentKingCol(), colorGaming) == null) {
            System.out.println("move");
            updateKingPositionIfNeedeed(row1, col1, row2, col2);
            this.fields[row2][col2] = figure;
            this.fields[row1][col1] = null;
            return true;

        } else if (figure.canAttack(row1, col1, row2, col2)
                && !isCellEmpty(row2, col2)
                && this.fields[row2][col2].getColor() != this.fields[row1][col1].getColor()
                && isWayClear(row1, col1, row2, col2)
                && !(this.fields[row2][col2] instanceof King)
                && getPositionOfFirstFigureWhichCanAttackByRowAndCol(copyFieldsAndMove(row1, col1, row2, col2), getCurrentKingRow(), getCurrentKingCol(), colorGaming) == null) {
            System.out.println("attack");
            updateKingPositionIfNeedeed(row1, col1, row2, col2);
            switch (this.fields[row2][col2].getColor()) {
                case 'w':
                    this.takeWhite.add(this.fields[row2][col2].getColor() + this.fields[row2][col2].getName());
                    break;
                case 'b':
                    this.takeBlack.add(this.fields[row2][col2].getColor() + this.fields[row2][col2].getName());
                    break;
            }
            this.fields[row2][col2] = figure;
            this.fields[row1][col1] = null;
            return true;

        }

        return false;
    }

    private void updateKingPositionIfNeedeed(int row1, int col1, int row2, int col2) {
        if (fields[row1][col1] instanceof King) {
            switch (colorGaming) {
                case 'w': {
                    whiteKingRow = row2;
                    whiteKingCol = col2;
                }
                case 'b': {
                    blackKingRow = row2;
                    blackKingCol = col2;

                }
            }
        }
    }

    public boolean isGameEnd() {
        int rowK = getCurrentKingRow();
        int colK = getCurrentKingCol();

        RowAndCol kingAttacker = getPositionOfFirstFigureWhichCanAttackByRowAndCol(this.fields, rowK, colK, colorGaming);
        if (kingAttacker == null) {
            return false;
        }

        if (!isKingCantMove(this.fields, rowK, colK)) {
            return false;
        }

        RowAndCol attackerOfKingsAttacker = getPositionOfFirstFigureWhichCanAttackByRowAndCol(this.fields, kingAttacker.row(), kingAttacker.col(), getOppositeColorGaming());
        if (attackerOfKingsAttacker != null) {
            return false;
        }

        RowAndCol[] wayForAttack = getWayQueen(kingAttacker.row, kingAttacker.col, rowK, colK);
        return !canProtect(wayForAttack);
    }

    private boolean canProtect(RowAndCol[] wayForAttack) {
        boolean flag = false;
        for (RowAndCol rowAndCol : wayForAttack) {
            int rowForA = rowAndCol.row;
            int colForA = rowAndCol.col;
            for (int row = 0; row < 8; row++) {
                for (int col = 0; col < 8; col++) {
                    Figure figure = fields[row][col];
                    if (figure != null
                            && figure.getColor() == colorGaming
                            && !(figure instanceof King)
                            && figure.canMove(row, col, rowForA, colForA)) {
                        flag = true;
                        System.out.println("Вы можете защитить короля фигурой " + row + ' ' + col);
                    }
                }
            }
        }
        return flag;
    }

    private boolean isKingCantMove(Figure[][] fields, int rowK, int colK) {
        for (int row = Math.max(rowK - 1, 0); row < Math.min(rowK + 2, 8); row++) {
            for (int col = Math.max(colK - 1, 0); col < Math.min(colK + 2, 8); col++) {
                if (fields[row][col] == null
                        && getPositionOfFirstFigureWhichCanAttackByRowAndCol(copyFieldsAndMove(rowK, colK, row, col), row, col, getColorGaming()) == null) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isCellEmpty(int row, int col) {
        return fields[row][col] == null;
    }

    private record RowAndCol(int row, int col) {
    }

    private static RowAndCol getPositionOfFirstFigureWhichCanAttackByRowAndCol(Figure[][] fields, int rowFigure, int colFigure, char colorGaming) {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (fields[row][col] != null && fields[row][col].getColor() != colorGaming) {
                    Figure figure = fields[row][col];
                    if (figure.canAttack(row, col, rowFigure, colFigure)) {
                        return new RowAndCol(row, col);
                    }
                }
            }
        }
        return null;
    }

    public boolean isKingUnderAttack() {
        return getPositionOfFirstFigureWhichCanAttackByRowAndCol(fields, getCurrentKingRow(), getCurrentKingCol(), colorGaming) != null;
    }

    private Figure[][] copyFieldsAndMove(int row1, int col1, int row2, int col2) {
        Figure[][] newFields = new Figure[8][8];
        for (int row = 0; row < 8; row++) {
            System.arraycopy(this.fields[row], 0, newFields[row], 0, 8);
        }
        newFields[row2][col2] = newFields[row1][col1];
        newFields[row1][col1] = null;
        return newFields;
    }


    private boolean isWayClear(int row1, int col1, int row2, int col2) {
        Figure figure = this.fields[row1][col1];
        if (figure instanceof Pawn) {
            switch (figure.getColor()) {
                case 'w':
                    return fields[row1 + 1][col1] == null;
                case 'b':
                    return fields[row1 - 1][col1] == null;
            }
        }
        if (figure instanceof Rook) {
            return isWayClearRook(row1, col1, row2, col2);
        }
        if (figure instanceof Bishop) {
            return isWayClearBishop(row1, col1, row2, col2);
        }
        if (figure instanceof Queen) {
            return isWayClearBishop(row1, col1, row2, col2) || isWayClearRook(row1, col1, row2, col2);
        }
        return figure instanceof King || figure instanceof Knight;

    }

    private RowAndCol[] getWayRook(int row1, int col1, int row2, int col2) {
        int len = Math.max(Math.abs(row1 - row2) - 1, Math.abs(col1 - col2) - 1);
        RowAndCol[] result = new RowAndCol[len];

        int minRow = Math.min(row1, row2);
        int maxRow = Math.max(row1, row2);
        int minCol = Math.min(col1, col2);
        int maxCol = Math.max(col1, col2);

        if (minRow == maxRow) {
            for (int i = minCol + 1; i < maxCol; i++) {
                if (fields[minRow][i] == null) {
                    result[i - minCol - 1] = new RowAndCol(minRow, i);
                }
            }
        } else {
            for (int i = minRow + 1; i < maxRow; i++) {
                if (fields[i][minCol] == null) {
                    result[i - minRow - 1] = new RowAndCol(i, minCol);
                }
            }
        }

        return result;
    }

    private RowAndCol[] getWayQueen(int row1, int col1, int row2, int col2) {
        if (row1 == row2 || col1 == col2) {
            return getWayRook(row1, col1, row2, col2);
        } else {
            return getWayBishop(row1, col1, row2, col2);
        }
    }

    private RowAndCol[] getWayBishop(int row1, int col1, int row2, int col2) {
        int len = Math.max(Math.abs(row1 - row2) - 1, Math.abs(col1 - col2) - 1);
        RowAndCol[] result = new RowAndCol[len];
        int counter = 0;

        int sum1 = row1 + col1;
        int sum2 = row2 + col2;
        if (sum1 == sum2) {
            if (row1 > row2) {
                for (int i = row1 - 1, j = col1 + 1; i > row2 && j < col2; i--, j++) {
                    result[counter++] = new RowAndCol(i, j);
                }
            }
            if (row1 < row2) {
                for (int i = row1 + 1, j = col1 - 1; i < row2 && j > col2; i++, j--) {
                    result[counter++] = new RowAndCol(i, j);
                }
            }
        } else {
            if (sum2 > sum1) {
                for (int i = row1 + 1, j = col1 + 1; i < row2 && j < col2; i++, j++) {
                    result[counter++] = new RowAndCol(i, j);
                }
            }
            if (sum2 < sum1) {
                for (int i = row1 - 1, j = col1 - 1; i > row2 && j > col2; i--, j--) {
                    result[counter++] = new RowAndCol(i, j);
                }
            }
        }
        return result;
    }

    private boolean isWayClearRook(int row1, int col1, int row2, int col2) {
        int minRow = Math.min(row1, row2);
        int maxRow = Math.max(row1, row2);
        int minCol = Math.min(col1, col2);
        int maxCol = Math.max(col1, col2);

        if (minRow == maxRow) {
            for (int i = minCol + 1; i < maxCol; i++) {
                if (fields[minRow][i] != null) {
                    return false;
                }
            }
        } else {
            for (int i = minRow + 1; i < maxRow; i++) {
                if (fields[i][minCol] != null) {
                    return false;
                }
            }
        }

        return true;
    }

    private boolean isWayClearBishop(int row1, int col1, int row2, int col2) {
        int sum1 = row1 + col1;
        int sum2 = row2 + col2;
        if (sum1 == sum2) {
            if (row1 > row2) {
                for (int i = row1 - 1, j = col1 + 1; i > row2 && j < col2; i--, j++) {
                    if (fields[i][j] != null) return false;
                }
            }
            if (row1 < row2) {
                for (int i = row1 + 1, j = col1 - 1; i < row2 && j > col2; i++, j--) {
                    if (fields[i][j] != null) return false;
                }
            }
        } else {
            if (sum2 > sum1) {
                for (int i = row1 + 1, j = col1 + 1; i < row2 && j < col2; i++, j++) {
                    if (fields[i][j] != null) return false;
                }
            }
            if (sum2 < sum1) {
                for (int i = row1 - 1, j = col1 - 1; i > row2 && j > col2; i--, j--) {
                    if (fields[i][j] != null) return false;
                }
            }
        }
        return true;
    }

    private int getCurrentKingRow() {
        if (colorGaming == 'w')
            return whiteKingRow;
        else
            return blackKingRow;
    }

    private int getCurrentKingCol() {
        if (colorGaming == 'w')
            return whiteKingCol;
        else
            return blackKingCol;
    }

    public void print_board() {
        System.out.println(" +----+----+----+----+----+----+----+----+");
        for (int row = 7; row > -1; row--) {
            System.out.print(row);
            for (int col = 0; col < 8; col++) {
                System.out.print("|" + getCellForPrinting(row, col));
            }
            System.out.println("|");
            System.out.println(" +----+----+----+----+----+----+----+----+");
        }

        for (int col = 0; col < 8; col++) {
            System.out.print("    " + col);
        }

        System.out.println();
    }
}

import Figures.Bishop;
import Figures.Figure;
import Figures.King;
import Figures.Knight;
import Figures.Pawn;
import Figures.Queen;
import Figures.Rook;

import java.util.ArrayList;

public class Board {
    //TODO: Список фигур и начальное положение всех фигур
    private Figure fields[][] = new Figure[8][8];
    private ArrayList<String> takeWhite = new ArrayList(16);
    private ArrayList<String> takeBlack = new ArrayList(16);

    public char getColorGaming() {
        return colorGaming;
    }

    public void setColorGaming(char colorGaming) {
        this.colorGaming = colorGaming;
    }

    private char colorGaming;

    public void init() {
        this.fields[0] = new Figure[]{
                new Rook("R", 'w'), new Knight("N", 'w'),
                new Bishop("B", 'w'), new Queen("Q", 'w'),
                new King("K", 'w'), new Bishop("B", 'w'),
                new Knight("N", 'w'), new Rook("R", 'w')
        };
        /*this.fields[1] = new Figure[]{
                new Pawn("P", 'w'), new Pawn("P", 'w'),
                new Pawn("P", 'w'), new Pawn("P", 'w'),
                new Pawn("P", 'w'), new Pawn("P", 'w'),
                new Pawn("P", 'w'), new Pawn("P", 'w'),
        };*/

        this.fields[7] = new Figure[]{
                new Rook("R", 'b'), new Knight("N", 'b'),
                new Bishop("B", 'b'), new Queen("Q", 'b'),
                new King("K", 'b'), new Bishop("B", 'b'),
                new Knight("N", 'b'), new Rook("R", 'b')
        };
        this.fields[6] = new Figure[]{
                new Pawn("P", 'b'), new Pawn("P", 'b'),
                new Pawn("P", 'b'), new Pawn("P", 'b'),
                new Pawn("P", 'b'), new Pawn("P", 'b'),
                new Pawn("P", 'b'), new Pawn("P", 'b'),
        };
    }

    public String getCell(int row, int col) {
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
        //System.out.println(figure + " " + row1 + " " + col1 + " " + row2 + " " + col2);
        //System.out.println((figure.canMove(row1, col1, row2, col2)) + " " + (this.fields[row2][col2] == null) + " " + isWayClear(row1, col1, row2, col2));
        if (figure.canMove(row1, col1, row2, col2) && (this.fields[row2][col2] == null) && isWayClear(row1, col1, row2, col2)) {
            System.out.println("move");
            this.fields[row2][col2] = figure;
            this.fields[row1][col1] = null;
            return true;
        } else if (figure.canAttack(row1, col1, row2, col2) && this.fields[row2][col2] != null && this.fields[row2][col2].getColor() != this.fields[row1][col1].getColor() && isWayClear(row1, col1, row2, col2)) {
            System.out.println("attack");
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
                for (int i = row1-1, j = col1+1; i > row2 && j < col2; i--, j++) {
                    if (fields[i][j] != null) return false;
                }
            }
            if (row1 < row2) {
                for (int i = row1+1, j = col1-1; i < row2 && j > col2; i++, j--) {
                    if (fields[i][j] != null) return false;
                }
            }
        } else {
            if (sum2 > sum1) {
                for (int i = row1+1, j = col1+1; i < row2 && j < col2; i++, j++) {
                    if (fields[i][j] != null) return false;
                }
            }
            if (sum2 < sum1) {
                for (int i = row1-1, j = col1-1; i > row2 && j > col2; i--, j--) {
                    if (fields[i][j] != null) return false;
                }
            }
        }
        return true;
    }

    public void print_board() {
        System.out.println(" +----+----+----+----+----+----+----+----+");
        for (int row = 7; row > -1; row--) {
            System.out.print(row);
            for (int col = 0; col < 8; col++) {
                System.out.print("|" + getCell(row, col));
            }
            System.out.println("|");
            System.out.println(" +----+----+----+----+----+----+----+----+");
        }

        for (int col = 0; col < 8; col++) {
            System.out.print("    " + col);
        }


    }


}

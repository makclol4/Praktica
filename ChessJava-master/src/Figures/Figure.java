package Figures;

// 1(w) - White 2(b) - Black;

public abstract class Figure {
    public char getColor() {
        return color;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setColor(char color) {
        this.color = color;
    }

    private String name;

    public String getName() {
        return name;
    }

    private char color;

    //private int[] position = new int[2];  // 0- row ; 1 - col


    public Figure(String name, char color) {
        this.name = name;
        this.color = color;
    }

    public boolean canMove(int row, int col, int row1, int col1) {
        return (row1 < 8 && row1 >= 0 && col1 < 8 && col1 >= 0);
    }

    public boolean canAttack(int row, int col, int row1, int col1) {
        return this.canMove(row, col, row1, col1);
    }

}

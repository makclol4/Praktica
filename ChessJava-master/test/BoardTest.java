import figures.King;
import figures.Knight;
import figures.Queen;
import figures.Rook;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class BoardTest {

    private Board board;

    @Before
    public void seyUp() {
        board = new Board();
        board.setColorGaming('w');
    }

    @Test
    public void test_get_color() {
        Assert.assertEquals(board.getColorGaming(), 'w');
    }

    @Test
    public void test_is_game_end_true_1() {
        King wK = new King('w');
        Rook bR1 = new Rook('b');
        Rook bR2 = new Rook('b');
        Queen bQ = new Queen('b');

        board.fields[4][4] = wK;
        board.fields[1][4] = bR1;
        board.fields[2][5] = bR2;
        board.fields[0][3] = bQ;

        Board.whiteKingRow = 4;
        Board.whiteKingCol = 4;

        Assert.assertTrue(board.isGameEnd());
    }

    @Test
    public void test_is_game_end_false_1() {
        King wK = new King('w');
        Knight wN = new Knight('w');
        Rook wR = new Rook('w');
        Rook bR1 = new Rook('b');
        Rook bR2 = new Rook('b');
        Queen bQ = new Queen('b');


        board.fields[4][4] = wK;
        board.fields[5][5] = wN;
        board.fields[1][0] = wR;
        board.fields[1][4] = bR1;
        board.fields[2][5] = bR2;
        board.fields[0][3] = bQ;

        Board.whiteKingRow = 4;
        Board.whiteKingCol = 4;

        Assert.assertFalse(board.isGameEnd());
    }

    @Test
    public void test_is_game_end_false_2() {
        King wK = new King('w');
        Rook bR2 = new Rook('b');
        Queen bQ = new Queen('b');


        board.fields[4][4] = wK;
        board.fields[2][4] = bR2;
        board.fields[0][3] = bQ;

        Board.whiteKingRow = 4;
        Board.whiteKingCol = 4;

        Assert.assertFalse(board.isGameEnd());
    }

    @Test
    public void test_is_game_end_false_3() {
        King wK = new King('w');
        Knight wN = new Knight('w');
        Rook bR1 = new Rook('b');
        Rook bR2 = new Rook('b');
        Queen bQ = new Queen('b');


        board.fields[4][4] = wK;
        board.fields[5][5] = wN;
        board.fields[1][4] = bR1;
        board.fields[2][5] = bR2;
        board.fields[0][3] = bQ;

        Board.whiteKingRow = 4;
        Board.whiteKingCol = 4;

        Assert.assertFalse(board.isGameEnd());
    }

}

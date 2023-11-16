package figures;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class KnightTest {
    private Knight knight;

    @Before
    public void setUp() {
        knight = new Knight('w');
    }

    @Test
    public void test_can_move_1() {
        Assert.assertTrue(knight.canMove(0, 0, 2, 1));
    }

    @Test
    public void test_cant_move_1() {
        Assert.assertFalse(knight.canMove(0, 0, 2, 0));
    }
}

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Board board = new Board();
        board.setColorGaming('w');
        board.init();

        boolean game = true;

        Scanner in = new Scanner(System.in);

        while (game) {
            board.print_board();
            System.out.println();
            System.out.println("Команды: ");
            System.out.println("----- exit: Выход из игры");
            System.out.println("----- row1,col1, row2,col2: Ход фигуры из клетки y1, x1 в клекту y2, x2");


            System.out.println("Взятые Белые:" + board.getTakeWhite().toString());
            System.out.println("Взятые Черные:" + board.getTakeBlack().toString());

            switch (board.getColorGaming()) {
                case 'w':
                    System.out.println("Ход Белых:");
                    break;
                case 'b':
                    System.out.println("Ход Черных:");
                    break;
            }


            String inputLine = in.nextLine();
            while (inputLine.isBlank()) {
                System.out.println("Ошибка хода, повторите ввод хода!");
                inputLine = in.nextLine();
            }
            if (inputLine.equals("exit")) {
                System.out.println("Игра завршена.");
                in.close();
                break;
            }
            int col1, row1, col2, row2;
            String[] coords = inputLine.split(" ");
            row1 = Integer.parseInt(coords[0]);
            col1 = Integer.parseInt(coords[1]);
            row2 = Integer.parseInt(coords[2]);
            col2 = Integer.parseInt(coords[3]);


            while (!board.move_figure(row1, col1, row2, col2)) {
                System.out.println("Ошибка хода, повторите ввод хода!");
                inputLine = in.nextLine();
                coords = inputLine.split(" ");
                row1 = Integer.parseInt(coords[0]);
                col1 = Integer.parseInt(coords[1]);
                row2 = Integer.parseInt(coords[2]);
                col2 = Integer.parseInt(coords[3]);
            }

            game = !board.isGameEnd();

            switch (board.getColorGaming()) {
                case 'w':
                    board.setColorGaming('b');
                    break;
                case 'b':
                    board.setColorGaming('w');
                    break;
            }
        }
        System.out.println("Конец игры!!!");
    }
}
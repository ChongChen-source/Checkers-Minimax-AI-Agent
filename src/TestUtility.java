import java.util.List;
import java.util.Scanner;

public class TestUtility {
    public static void printBoardInfo(Board board) {
        System.out.println("--------------------------------------");

        System.out.println("[Board Params]");
        System.out.println("Number of Black Pieces: " + board.getNumBlackPieces());
        System.out.println("Number of Black Kings: " + board.getNumBlackKings());
        System.out.println("Number of White Pieces: " + board.getNumWhitePieces());
        System.out.println("Number of White Kings: " + board.getNumWhiteKings());

        System.out.println("Current score: " + MinimaxAlg.evalBoard(board));

        System.out.println("--------------------------------------");

        System.out.println("[Board Cells]");

        String[] lines = cellsToString(board.getCells());

        for (String line: lines) {
            System.out.println(line);
        }

        System.out.println("--------------------------------------");
    }

    public static void printBoard(Board board) {
        String[] lines = cellsToString(board.getCells());

        for (String line: lines) {
            System.out.println(line);
        }
    }

    public static String[] cellsToString(Piece[][] cells) {
        String[] lines = new String[BoardUtility.BOARD_SIZE];

        for (int i = 0; i< BoardUtility.BOARD_SIZE; i++) {
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < BoardUtility.BOARD_SIZE; j++) {
                char c = '.';
                Piece piece = cells[i][j];
                if (piece != null) {
                    PlayColor color = piece.getColor();
                    if (color.equals(PlayColor.BLACK)) {
                        if (piece.isKing()) {
                            c = 'B';
                        } else {
                            c = 'b';
                        }
                    } else {
                        if (piece.isKing()) {
                            c = 'W';
                        } else {
                            c = 'w';
                        }
                    }
                }
                sb.append(c);
            }
            lines[i] = sb.toString();
        }

        return lines;
    }

    public static void printMovePlayed(Move movePlayed) {
        System.out.println("Move Played: ");

        if (movePlayed == null || movePlayed.isEmpty()) {
            System.out.println("No valid move to play!!! Lose th game!!!");
            return;
        }

        MoveType moveType = movePlayed.getMoveType();
        List<Step> steps = movePlayed.getSteps();
        for (Step step : steps) {
            System.out.println(moveType.name() + " " + step.toString());
        }
    }

    public static Move getMoveByStr(String str) {
        // Str e.g. E g7 f6
        MoveType moveType = (str.charAt(0) == 'E') ? MoveType.E : MoveType.J;

        Move move = new Move(moveType);

        int i = 2;
        while (i < str.length()) {
            int fromPosCol = str.charAt(i) - 'a';

            i++;
            int fromPosRow = '8' - str.charAt(i);

            i++;

            i++;
            int toPosCol = str.charAt(i) - 'a';

            i++;
            int toPosRow = '8' - str.charAt(i);

            i += 2;

            move.appendStep(new Step(fromPosRow, fromPosCol, toPosRow, toPosCol));
        }

        return move;
    }
}

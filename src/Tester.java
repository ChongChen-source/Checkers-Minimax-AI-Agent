import java.util.Scanner;

public class Tester {

    public static void main(String[] args) {

//        playAgainstSelf("src/Test Cases/NSGame.txt");

        playAgainstAgent("src/Test Cases/NSGame.txt");

//        long start = System.nanoTime();
//        localTest("src/Test Cases/Reference.txt", "src/outputs/output.txt");
//        long end = System.nanoTime();
//        long durationInMilliseconds = (end - start) / 1000000;
//        System.out.println("Time exclude IO in ms: " + (durationInMilliseconds));
    }

    private static void localTest(String inputFilePath, String outputFilePath) {
        // Read from input file
        PlayState playState = FileUtility.getPlayState(inputFilePath);
        PlayMode playMode = playState.getPlayMode();
        PlayColor playColor = playState.getPlayColor();
        double timeLeft = playState.getTimeLeft();
        Board board = playState.getBoard();

        // Record time
        long start = System.nanoTime();

        // determine depth
        int depth = FileUtility.determineDepth(timeLeft, "src/calibration.txt");
        System.out.println("Depth = " + depth);

        // Play!
        Move moveToPlay = board.play(playMode, playColor, depth);

        // Print duration
        long end = System.nanoTime();
        long durationInMilliseconds = (end - start) / 1000000;
        System.out.println("Time include IO in ms: " + (durationInMilliseconds));

        // Actually execute the move
        board.executeMove(moveToPlay);

        // Print test info
        if (moveToPlay == null || moveToPlay.isEmpty()) {
            System.out.println("No move was played!");
            return;
        }
        TestUtility.printMovePlayed(moveToPlay);
        TestUtility.printBoard(board);

        // Generate output file
        FileUtility.generateOutput(outputFilePath, moveToPlay);
    }

    private static void playAgainstSelf(String inputFilePath) {
        PlayState playState = FileUtility.getPlayState(inputFilePath);

        PlayMode playMode = playState.getPlayMode();

        Board board = playState.getBoard();

        double blackTimeLeft = playState.getTimeLeft();
        double whiteTimeLeft = playState.getTimeLeft();

        int roundCount = 1;

        int drawCount = 50;
        boolean isDraw = false;

        int numBlackPieces = board.getNumBlackPieces();
        int numWhitePieces = board.getNumWhitePieces();
        int numBlackKings = board.getNumBlackKings();
        int numWhiteKings = board.getNumWhiteKings();

        Move blackMove = null;
        Move whiteMove = null;

        while (board.getNumWhitePieces() > 0 && board.getNumBlackPieces() > 0) {
            System.out.println("************************* Round #" + roundCount + "*************************");
            System.out.println("timeLeft = " + blackTimeLeft);
            int blackDepth = FileUtility.determineDepth(blackTimeLeft, "src/calibration.txt");
            System.out.println("Depth = " + blackDepth);

            long blackStart = System.nanoTime();
            blackMove = board.play(playMode, PlayColor.BLACK, blackDepth);
            long blackEnd = System.nanoTime();
            double blackDurationInSeconds = (blackEnd - blackStart) / 1000000000.0;
            blackTimeLeft -= blackDurationInSeconds;

            board.executeMove(blackMove);
            System.out.print("Black ");
            TestUtility.printMovePlayed(blackMove);
            TestUtility.printBoard(board);

            /************************************************************/

            System.out.println("timeLeft = " + whiteTimeLeft);
            int whiteDepth = FileUtility.determineDepth(whiteTimeLeft, "src/calibration.txt");
            System.out.println("Depth = " + whiteDepth);

            long whiteStart = System.nanoTime();
            whiteMove = board.play(playMode, PlayColor.WHITE, whiteDepth);
            long whiteEnd = System.nanoTime();
            double whiteDurationInSeconds = (whiteEnd - whiteStart) / 1000000000.0;
            whiteTimeLeft -= whiteDurationInSeconds;

            board.executeMove(whiteMove);
            System.out.print("White ");
            TestUtility.printMovePlayed(whiteMove);
            TestUtility.printBoard(board);

            roundCount++;

            if (numBlackPieces == board.getNumBlackPieces() &&
                    numWhitePieces == board.getNumWhitePieces() &&
                    numBlackKings == board.getNumBlackKings() &&
                    numWhiteKings == board.getNumWhiteKings()) {
                drawCount--;
            } else {
                drawCount = 50;
                numBlackPieces = board.getNumBlackPieces();
                numWhitePieces = board.getNumWhitePieces();
                numBlackKings = board.getNumBlackKings();
                numWhiteKings = board.getNumWhiteKings();
            }

            if (drawCount <= 0) {
                isDraw = true;
                break;
            }
        }

        System.out.println("************************* Game Ends!!! *************************");
        String winner;
        if (isDraw) {
            System.out.println("*************************** Is Draw ****************************");
        } else {
            if (numWhitePieces == 0 || whiteMove == null || whiteMove.isEmpty()) {
                winner = "BLACK";
            } else {
                winner = "WHITE";
            }
            System.out.println("************************* " + winner + " Wins!!! ************************");
        }
    }

    private static void playAgainstAgent(String inputFilePath) {
        PlayColor firstPlayer = PlayColor.BLACK;

        PlayState playState = FileUtility.getPlayState(inputFilePath);

        PlayMode playMode = playState.getPlayMode();

        Board board = playState.getBoard();

        PlayColor playColor= playState.getPlayColor();

        PlayColor opponentColor = playColor.equals(PlayColor.BLACK) ? PlayColor.WHITE : PlayColor.BLACK;

        double timeLeft = playState.getTimeLeft();

        int roundCount = 1;

        int drawCount = 50;

        int numBlackPieces = board.getNumBlackPieces();
        int numWhitePieces = board.getNumWhitePieces();
        int numBlackKings = board.getNumBlackKings();
        int numWhiteKings = board.getNumWhiteKings();

        Scanner sc = new Scanner(System.in);

        while (board.getNumWhitePieces() > 0 && board.getNumBlackPieces() > 0) {
            System.out.println("************************* Round #" + roundCount + "*************************");

            if (playColor.equals(firstPlayer)) {
                timeLeft = selfAgentPlay(playMode, board, playColor, timeLeft);
                otherAgentPlay(playMode, board, opponentColor, sc);
            }

            else {
                otherAgentPlay(playMode, board, opponentColor, sc);
                timeLeft = selfAgentPlay(playMode, board, playColor, timeLeft);
            }

            roundCount++;

            if (numBlackPieces == board.getNumBlackPieces() &&
                    numWhitePieces == board.getNumWhitePieces() &&
                    numBlackKings == board.getNumBlackKings() &&
                    numWhiteKings == board.getNumWhiteKings()) {
                drawCount--;
            } else {
                drawCount = 50;
                numBlackPieces = board.getNumBlackPieces();
                numWhitePieces = board.getNumWhitePieces();
                numBlackKings = board.getNumBlackKings();
                numWhiteKings = board.getNumWhiteKings();
            }

            if (drawCount <= 0) {
                System.out.println("GAME OVER: Game ends in draw");
                return;
            }
        }

        System.out.println("************************* Game Ends!!! *************************");
        if (board.getNumWhitePieces() == 0) {
            System.out.println("GAME OVER: BLACK wins!!!");
        } else {
            System.out.println("GAME OVER: WHITE wins!!!");
        }
    }

    private static double selfAgentPlay(PlayMode playMode, Board board, PlayColor playColor, double timeLeft) {
        // record time
        long start = System.nanoTime();

        // determine depth
        int depth = FileUtility.determineDepth(timeLeft, "src/calibration.txt");
        System.out.println("Depth = " + depth);
        Move myMove = board.play(playMode, playColor, depth);

        // record time
        long end = System.nanoTime();
        double durationInSeconds = (end - start) / 1000000000.0;
        System.out.println("Time taken in sec: " + durationInSeconds);
        timeLeft -= durationInSeconds;
        System.out.println("Time left in sec: " + timeLeft);

        board.executeMove(myMove);
        System.out.print(playColor.name() + " ");
        TestUtility.printMovePlayed(myMove);
        TestUtility.printBoard(board);

        return timeLeft;
    }

    private static void otherAgentPlay(PlayMode playMode, Board board, PlayColor opponentColor, Scanner sc) {
        System.out.println();
        System.out.println(opponentColor.name() + " to Play:");

        String stepStr = sc.nextLine();
        Move opponentMove = TestUtility.getMoveByStr(stepStr);
        board.executeMove(opponentMove);
        System.out.print(opponentColor.name() + " ");
        TestUtility.printMovePlayed(opponentMove);
        TestUtility.printBoard(board);
    }
}

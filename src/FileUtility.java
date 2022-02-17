import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class FileUtility {
    public static PlayState getPlayState(String inputFilePath) {
        PlayState playState = new PlayState();
        try {
            File inputFile = new File(inputFilePath);
            Scanner in = new Scanner(inputFile);

            // 1st line: Play Mode
            playState.setPlayMode(PlayMode.valueOf(in.nextLine()));

            // 2nd line: Play Color
            playState.setPlayColor(PlayColor.valueOf(in.nextLine()));

            // 3rd line: Time Left (in seconds)
            playState.setTimeLeft(Double.valueOf(in.nextLine()));

            // Next BOARD_SIZE lines: description of the game board
            Piece[][] cells = new Piece[BoardUtility.BOARD_SIZE][BoardUtility.BOARD_SIZE];
            Map<String, Piece> blackPieces = new HashMap<>();
            Map<String, Piece> whitePieces = new HashMap<>();

            int numBlackPieces = 0;
            int numBlackKings = 0;
            int numWhitePieces = 0;
            int numWhiteKings = 0;

            for (int i = 0; i < BoardUtility.BOARD_SIZE; i++) {
                String row = in.nextLine();
                for (int j = 0; j < BoardUtility.BOARD_SIZE; j++) {
                    char cellInfo = row.charAt(j);
                    switch (cellInfo) {
                        case 'b':
                            numBlackPieces++;
                            Piece piece_b = new Piece(i, j, PlayColor.BLACK, false);
                            blackPieces.put(piece_b.getPosition(), piece_b);
                            cells[i][j] = piece_b;
                            break;
                        case 'B':
                            numBlackKings++;
                            numBlackPieces++;
                            Piece piece_B = new Piece(i, j, PlayColor.BLACK, true);
                            blackPieces.put(piece_B.getPosition(), piece_B);
                            cells[i][j] = piece_B;
                            break;
                        case 'w':
                            numWhitePieces++;
                            Piece piece_w = new Piece(i, j, PlayColor.WHITE, false);
                            whitePieces.put(piece_w.getPosition(), piece_w);
                            cells[i][j] = piece_w;
                            break;
                        case 'W':
                            numWhiteKings++;
                            numWhitePieces++;
                            Piece piece_W = new Piece(i, j, PlayColor.WHITE, true);
                            whitePieces.put(piece_W.getPosition(), piece_W);
                            cells[i][j] = piece_W;
                            break;
                    }
                }
            }
            playState.setBoard(new Board(cells, numBlackPieces, numBlackKings,
                    numWhitePieces, numWhiteKings,
                    blackPieces, whitePieces));

            // Close the scanner
            in.close();
        } catch (IOException e) {
            System.out.println("Failed to read from: " + inputFilePath);
            e.printStackTrace();
        }
        return playState;
    }

    public static void generateOutput(String outputFilePath, Move movePlayed) {
        try {
            FileWriter writer = new FileWriter(outputFilePath);
            if (movePlayed == null) {
                writer.close();
                return;
            }
            MoveType moveType = movePlayed.getMoveType();
            int length = movePlayed.size();
            List<Step> steps = movePlayed.getSteps();
            for (int i = 0; i < length; i++) {
                writer.write(moveType.name() + " " + steps.get(i).toString());
                if (i != length - 1) {
                    writer.write(System.lineSeparator());
                }
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("Failed to write into: " + outputFilePath);
            e.printStackTrace();
        }
    }

    public static PlayState getInitPlayState() {
        PlayState initPlayState = new PlayState();

        initPlayState.setPlayMode(PlayMode.GAME);

        initPlayState.setPlayColor(PlayColor.BLACK);

        initPlayState.setTimeLeft(300.0);

        Piece[][] cells = new Piece[BoardUtility.BOARD_SIZE][BoardUtility.BOARD_SIZE];
        Map<String, Piece> blackPieces = new HashMap<>();
        Map<String, Piece> whitePieces = new HashMap<>();

        int numBlackPieces = 12;
        int numBlackKings = 0;
        int numWhitePieces = 12;
        int numWhiteKings = 0;

        // init black pieces
        int row_b = 0;
        int col_b = -1;
        for (int i = 0; i < numBlackPieces; i++) {
            col_b = col_b + 2;
            if (col_b > BoardUtility.BOARD_SIZE - 1) {
                row_b++;
                col_b = (col_b == BoardUtility.BOARD_SIZE) ? 1 : 0;
            }
            Piece piece_b = new Piece(row_b, col_b, PlayColor.BLACK, false);
            blackPieces.put(piece_b.getPosition(), piece_b);
            cells[row_b][col_b] = piece_b;
        }

        // init white pieces
        int row_w = 5;
        int col_w = -2;
        for (int i = 0; i < numWhitePieces; i++) {
            col_w = col_w + 2;
            if (col_w > BoardUtility.BOARD_SIZE - 1) {
                row_w++;
                col_w = (col_w == BoardUtility.BOARD_SIZE) ? 1 : 0;
            }
            Piece piece_w = new Piece(row_w, col_w, PlayColor.WHITE, false);
            whitePieces.put(piece_w.getPosition(), piece_w);
            cells[row_w][col_w] = piece_w;
        }

        initPlayState.setBoard(new Board(cells, numBlackPieces, numBlackKings,
                numWhitePieces, numWhiteKings,
                blackPieces, whitePieces));

        return initPlayState;
    }

    public static void generatePlayData(String path, int count, Board board) {

    }

    private static double[] getCalibrationInfo(String path) {
        double[] calibrationInfo = new double[calibrate.DEPTH_LIMIT + 1];

        try {
            File inputFile = new File(path);
            Scanner in = new Scanner(inputFile);
            for (int i = 0; i < calibrate.DEPTH_LIMIT + 1; i++) {
                int depth = in.nextInt();
                calibrationInfo[depth] = in.nextDouble();
            }
        }  catch (IOException e) {
            System.out.println("Failed to read from: " + path);
            e.printStackTrace();
        }

        return calibrationInfo;
    }

    public static int determineDepth(double timeLeft, String calibrationPath) {
        double[] calibrationInfo = getCalibrationInfo(calibrationPath);
        int depth = calibrate.DEPTH_LIMIT;
        for (int i = 1; i <= calibrate.DEPTH_LIMIT; i++) {
            if (timeLeft < calibrationInfo[i] / 2) {
                depth = i - 1;
                break;
            }
        }
        return depth;
    }
}

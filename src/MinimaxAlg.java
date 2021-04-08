import java.util.List;
import java.util.Map;

public class MinimaxAlg {
    private static final PlayColor MAX_COLOR = PlayColor.BLACK;
    private static final PlayColor MIN_COLOR = PlayColor.WHITE;

    public static double evalBoard(Board board) {
        double weightRegularPieces = 5;
        double weightKings = 7.75;
        double weightBackRowPieces = 4;
        double weightMiddleBoxPieces = 2.5;
        double weightMiddleTwoRowPieces = 0.5;
//        double weightVulnerablePieces = -3;
//        double weightProtectedPieces = 3;

        Map<String, Piece> blackPieces = board.getBlackPieces();
        Map<String, Piece> whitePieces = board.getWhitePieces();
        int numBlackBackRowPieces = 0, numBlackMiddleBoxPieces = 0, numBlackMiddleTwoRowPieces = 0;
        int numWhiteBackRowPieces = 0, numWhiteMiddleBoxPieces = 0, numWhiteMiddleTwoRowPieces = 0;

        for (Map.Entry<String, Piece> entry: blackPieces.entrySet()) {
            Piece piece = entry.getValue();
            int row = piece.getRow();
            int col = piece.getCol();
            if (row == 0) {
                numBlackBackRowPieces++;
            } else if (row == 3 || row == 4) {
                if (col == 3 || col == 4) {
                    numBlackMiddleBoxPieces++;
                } else {
                    numBlackMiddleTwoRowPieces++;
                }
            }
        }

        for (Map.Entry<String, Piece> entry: whitePieces.entrySet()) {
            Piece piece = entry.getValue();
            int row = piece.getRow();
            int col = piece.getCol();
            if (row == 7) {
                numWhiteBackRowPieces++;
            } else if (row == 3 || row == 4) {
                if (col == 3 || col == 4) {
                    numWhiteMiddleBoxPieces++;
                } else {
                    numWhiteMiddleTwoRowPieces++;
                }
            }
        }

        if (board.getNumBlackPieces() < 4 || board.getNumWhitePieces() < 4) {
            weightMiddleBoxPieces = 0;
            weightMiddleTwoRowPieces = 0;
        }

        return weightRegularPieces * (board.getNumBlackPieces() - board.getNumWhitePieces()) +
                weightKings * (board.getNumBlackKings() - board.getNumWhiteKings()) +
                weightBackRowPieces * (numBlackBackRowPieces - numWhiteBackRowPieces) +
                weightMiddleBoxPieces * (numBlackMiddleBoxPieces - numWhiteMiddleBoxPieces) +
                weightMiddleTwoRowPieces * (numBlackMiddleTwoRowPieces - numWhiteMiddleTwoRowPieces);
    }

    public static Move minimax(PlayColor playColor, Board board, int depth) {
        Move bestMove = new Move();

        if (playColor.equals(MAX_COLOR)) {
            maxPlayer(board, depth, bestMove);
        }
        else {
            minPlayer(board, depth, bestMove);
        }

        return bestMove;
    }

    private static double maxPlayer(Board board, int depth, Move bestMove) {
        if (depth == 0 || board.getNumBlackPieces() == 0 || board.getNumWhitePieces() == 0) {
            return evalBoard(board);
        }

        List<Move> allJMoves = board.getAllJMoves(MAX_COLOR);
        List<Move> validMoves = allJMoves.isEmpty() ? board.getAllEMoves(MAX_COLOR) : allJMoves;

        // Terminate State
        if (validMoves.isEmpty()) {
            return evalBoard(board);
        }

        double maxScore = - Double.MAX_VALUE;

        for (Move move: validMoves) {
            Board newBoard = BoardUtility.getDeepCopyBoard(board);
            newBoard.executeMove(move);
            double score = minPlayer(newBoard, depth - 1, new Move());
            if (score > maxScore) {
                maxScore = score;
                bestMove.copyMove(move);
            }
        }

        return maxScore;
    }

    private static double minPlayer(Board board, int depth, Move bestMove) {
        if (depth == 0 || board.getNumBlackPieces() == 0 || board.getNumWhitePieces() == 0) {
            return evalBoard(board);
        }

        List<Move> allJMoves = board.getAllJMoves(MIN_COLOR);
        List<Move> validMoves = allJMoves.isEmpty() ? board.getAllEMoves(MIN_COLOR) : allJMoves;

        // Terminate State
        if (validMoves.isEmpty()) {
            return evalBoard(board);
        }

        double minScore = Double.MAX_VALUE;

        for (Move move: validMoves) {
            Board newBoard = BoardUtility.getDeepCopyBoard(board);
            newBoard.executeMove(move);
            double score = maxPlayer(newBoard, depth - 1, new Move());
            if (score < minScore) {
                minScore = score;
                bestMove.copyMove(move);
            }
        }

        return minScore;
    }
}

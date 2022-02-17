import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BoardUtility {
    public static final int BOARD_SIZE = 8;
    public static final int NUM_INIT_PIECES = 12;
    public static final int NUM_INIT_KINGS = 0;

    public static String[][] cellsName = {
            {"a8", "b8", "c8", "d8", "e8", "f8", "g8", "h8"},
            {"a7", "b7", "c7", "d7", "e7", "f7", "g7", "h7"},
            {"a6", "b6", "c6", "d6", "e6", "f6", "g6", "h6"},
            {"a5", "b5", "c5", "d5", "e5", "f5", "g5", "h5"},
            {"a4", "b4", "c4", "d4", "e4", "f4", "g4", "h4"},
            {"a3", "b3", "c3", "d3", "e3", "f3", "g3", "h3"},
            {"a2", "b2", "c2", "d2", "e2", "f2", "g2", "h2"},
            {"a1", "b1", "c1", "d1", "e1", "f1", "g1", "h1"}
    };


    public static Piece[][] getDeepCopyCells(Piece[][] cells) {
        Piece[][] copy = new Piece[BoardUtility.BOARD_SIZE][BoardUtility.BOARD_SIZE];
        for (int i = 0; i < BoardUtility.BOARD_SIZE; i++) {
            for (int j = 0; j < BoardUtility.BOARD_SIZE; j++) {
                Piece piece = cells[i][j];
                if (piece == null) {
                    copy[i][j] = null;
                } else {
                    copy[i][j] = new Piece(piece.getRow(), piece.getCol(), piece.getColor(), piece.isKing());
                }
            }
        }
        return copy;
    }

    public static Map<String, Piece> getDeepCopyPieceMap(Board board, PlayColor color) {
        Map<String, Piece> pieces = board.getPiecesByColor(color);
        Map<String, Piece> newPieces = new HashMap<>();
        for (Map.Entry<String, Piece> pieceEntry : pieces.entrySet()) {
            Piece piece = pieceEntry.getValue();
            Piece newPiece = new Piece(piece.getRow(), piece.getCol(), color, piece.isKing());
            newPieces.put(newPiece.getPosition(), newPiece);
        }
        return newPieces;
    }

    public static Board getDeepCopyBoard(Board board) {
        Board newBoard = new Board();

        newBoard.setCells(getDeepCopyCells(board.getCells()));
        newBoard.setBlackPieces(getDeepCopyPieceMap(board, PlayColor.BLACK));
        newBoard.setWhitePieces(getDeepCopyPieceMap(board, PlayColor.WHITE));

        newBoard.setNumBlackPieces(board.getNumBlackPieces());
        newBoard.setNumBlackKings(board.getNumBlackKings());
        newBoard.setNumWhitePieces(board.getNumWhitePieces());
        newBoard.setNumWhiteKings(board.getNumWhiteKings());

        return newBoard;
    }

    public static boolean isWithinBoard(int row, int col) {
        return row >= 0 && row < BOARD_SIZE && col >= 0 && col < BOARD_SIZE;
    }

    public static boolean canJump(Piece piece, Piece[][] currCells, int[] dir) {
        PlayColor color = piece.getColor();
        int row = piece.getRow();
        int col = piece.getCol();

        int adjacentRow = row + dir[0];
        int destRow = row + 2 * dir[0];
        int adjacentCol = col + dir[1];
        int destCol = col + 2 * dir[1];

        if (BoardUtility.isWithinBoard(adjacentRow, adjacentCol) &&
                currCells[adjacentRow][adjacentCol] != null &&
                !currCells[adjacentRow][adjacentCol].getColor().equals(color)) {
            // has an adjacent opponent piece
            if (BoardUtility.isWithinBoard(destRow, destCol) && currCells[destRow][destCol] == null) {
                // has an empty target to jump
                return true;
            }
        }
        return false;
    }

    /**
     * Helper method for getJMoves()
     * @param piece
     * @param preCells
     * @param jump
     * @param jMoves
     */
    public static void getJMovesDFS(Piece piece, Piece[][] preCells, Move jump, List<Move> jMoves) {
        boolean canJump = false;
        int row = piece.getRow();
        int col = piece.getCol();
        for (int[] dir : piece.getMoveDirs()) {
            if (BoardUtility.canJump(piece, preCells, dir)) {
                canJump = true;
                int destRow = row + 2 * dir[0];
                int destCol = col + 2 * dir[1];
                int adjacentRow = row + dir[0];
                int adjacentCol = col + dir[1];

                jump.appendStep(new Step(row, col, destRow, destCol));

                Piece newPiece = new Piece(destRow, destCol, piece.getColor(), piece.isKing());
                Piece[][] newCells = getDeepCopyCells(preCells);
                newCells[row][col] = null;
                newCells[destRow][destCol] = newPiece;
                newCells[adjacentRow][adjacentCol] = null;

                getJMovesDFS(newPiece, newCells, jump, jMoves);

                jump.removeLastStep();
            }
        }
        if (!canJump) {
            if (!jump.isEmpty()) {
                jMoves.add(new Move(jump));
            }
        }
        return;
    }
}

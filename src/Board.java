import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Board {
    private int numBlackPieces;
    private int numBlackKings;
    private int numWhitePieces;
    private int numWhiteKings;

    private Map<String, Piece> blackPieces = new HashMap<>();
    private Map<String, Piece> whitePieces = new HashMap<>();

    private Piece[][] cells;

    public Board() {

    }

    public Board(Piece[][] cells, int numBlackPieces, int numBlackKings,
                 int numWhitePieces, int numWhiteKings,
                 Map<String, Piece> blackPieces, Map<String, Piece> whitePieces) {
        this.cells = cells;

        this.numBlackPieces = numBlackPieces;
        this.numBlackKings = numBlackKings;
        this.numWhitePieces = numWhitePieces;
        this.numWhiteKings = numWhiteKings;

        this.blackPieces = blackPieces;
        this.whitePieces = whitePieces;
    }

    public Map<String, Piece> getBlackPieces() {
        return blackPieces;
    }

    public void setBlackPieces(Map<String, Piece> blackPieces) {
        this.blackPieces = blackPieces;
    }

    public Map<String, Piece> getWhitePieces() {
        return whitePieces;
    }

    public void setWhitePieces(Map<String, Piece> whitePieces) {
        this.whitePieces = whitePieces;
    }

    public Map<String, Piece> getPiecesByColor(PlayColor color) {
        return color.equals(PlayColor.BLACK) ? blackPieces : whitePieces;
    }

    public int getNumBlackPieces() {
        return numBlackPieces;
    }

    public void setNumBlackPieces(int numBlackPieces) {
        this.numBlackPieces = numBlackPieces;
    }

    public int getNumWhitePieces() {
        return numWhitePieces;
    }

    public void setNumWhitePieces(int numWhitePieces) {
        this.numWhitePieces = numWhitePieces;
    }

    public int getNumBlackKings() {
        return numBlackKings;
    }

    public void setNumBlackKings(int numBlackKings) {
        this.numBlackKings = numBlackKings;
    }

    public int getNumWhiteKings() {
        return numWhiteKings;
    }

    public void setNumWhiteKings(int numWhiteKings) {
        this.numWhiteKings = numWhiteKings;
    }

    public Piece[][] getCells() {
        return cells;
    }

    public void setCells(Piece[][] cells) {
        this.cells = cells;
    }

    public List<Move> getAllEMoves(PlayColor color) {
        Map<String, Piece> pieces = (color.equals(PlayColor.BLACK)) ? this.blackPieces : this.whitePieces;
        List<Move> allEMoves = new LinkedList<>();

        for (Map.Entry<String, Piece> pieceEntry: pieces.entrySet()) {
            allEMoves.addAll(getEMoves(pieceEntry.getValue()));
        }

        return allEMoves;
    }

    /**
     *
     * @param color
     * @return
     */
    public List<Move> getAllJMoves(PlayColor color) {
        Map<String, Piece> pieces = (color.equals(PlayColor.BLACK)) ? this.blackPieces : this.whitePieces;

        List<Move> allJMoves = new LinkedList<>();

        for (Map.Entry<String, Piece> pieceEntry: pieces.entrySet()) {
            allJMoves.addAll(getJMoves(pieceEntry.getValue()));
        }

        return allJMoves;
    }

    /**
     * Get all valid move of type E for a piece
     * @param piece
     * @return list of all valid E moves
     */
    public List<Move> getEMoves(Piece piece) {
        List<Move> eMoves = new LinkedList<>();

        int row = piece.getRow();
        int col = piece.getCol();

        int[][] moveDirs = piece.getMoveDirs();

        for (int[] dir: moveDirs) {
            int destRow = row + dir[0];
            int destCol = col + dir[1];

            if (BoardUtility.isWithinBoard(destRow, destCol) && cells[destRow][destCol] == null) {
                Move eMove = new Move(MoveType.E);
                Step step = new Step(row, col, destRow, destCol);
                eMove.appendStep(step);
                eMoves.add(eMove);
            }
        }

        return eMoves;
    }

    /**
     * Get all valid jump sequences for a piece
     * @param piece
     * @return
     */
    public List<Move> getJMoves(Piece piece) {
        List<Move> jMoves = new LinkedList<>();
        Move jump = new Move(MoveType.J);

        BoardUtility.getJMovesDFS(piece, this.cells, jump, jMoves);

        return jMoves;
    }

    public Move play(PlayMode playMode, PlayColor playColor, int depth) {
        Map<String, Piece> pieces = (playColor.equals(PlayColor.BLACK)) ? this.blackPieces : this.whitePieces;

        /**
         * PlayMode: SINGLE
         */
        if (playMode.equals(PlayMode.SINGLE)) {
             return quickTurn(pieces);
        }

        /**
         * PlayMode: GAME
         */
        else {
            if (depth == 0) {
                return quickTurn(pieces);
            } else {
                return MinimaxAlg.minimax(playColor, this, depth);
            }
        }
    }

    private Move quickTurn(Map<String, Piece> pieces) {
        // No need to actually execute the moves!
        // Just pick the first found valid move!

        // Firstly must jump
        for (Map.Entry<String, Piece> pieceEntry: pieces.entrySet()) {
            List<Move> jMoves = getJMoves(pieceEntry.getValue());
            if (jMoves.size() > 0) {
                Move jMove = jMoves.get(0);
                return jMove;
            }
        }

        // If cannot perform jump, then move to an adjacent empty cell
        for (Map.Entry<String, Piece> pieceEntry: pieces.entrySet()) {
            List<Move> eMoves = getEMoves(pieceEntry.getValue());
            if (eMoves.size() > 0) {
                Move eMove = eMoves.get(0);
                return eMove;
            }
        }

        return new Move();
    }

    public List<String> executeMove(Move move) {
        List<String> moveRecord = new LinkedList<>();
        List<Step> steps = move.getSteps();

        for (Step step : steps) {
            int fromPosRow = step.getFromPosRow();
            int fromPosCol = step.getFromPosCol();
            int toPosRow = step.getToPosRow();
            int toPosCol = step.getToPosCol();

            Piece piece = cells[fromPosRow][fromPosCol];

            PlayColor color = piece.getColor();
            Map<String, Piece> playPieces, opponentPieces;
            if (color.equals(PlayColor.BLACK)) {
                playPieces = this.blackPieces;
                opponentPieces = this.whitePieces;
            } else {
                playPieces = this.whitePieces;
                opponentPieces = this.blackPieces;
            }

            boolean isKing = piece.isKing();
            if (!isKing && toPosRow == color.getKingRow()) {
                piece.crown();
                if (color.equals(PlayColor.BLACK)) {
                    numBlackKings++;
                } else {
                    numWhiteKings++;
                }
            }

            playPieces.remove(piece.getPosition());

            piece.move(toPosRow, toPosCol);

            playPieces.put(piece.getPosition(), piece);

            cells[fromPosRow][fromPosCol] = null;
            cells[toPosRow][toPosCol] = piece;

            // E: Move to an adjacent empty location
            if (move.getMoveType().equals(MoveType.E)) {
                moveRecord.add("E " + step.toString());
                return moveRecord;
            }

            // J: Jump over an adjacent opponent's piece to an empty location
            // IMPORTANT: Can do continuous jump!
            else {
                // capture an opponent piece
                int capturedRow = (fromPosRow + toPosRow) / 2;
                int capturedCol = (fromPosCol + toPosCol) / 2;
                Piece capturedPiece = cells[capturedRow][capturedCol];
                opponentPieces.remove(capturedPiece.getPosition());
                cells[capturedRow][capturedCol] = null;
                if (capturedPiece.getColor().equals(PlayColor.BLACK)) {
                    numBlackPieces--;
                    if (capturedPiece.isKing()) {
                        numBlackKings--;
                    }
                } else {
                    numWhitePieces--;
                    if (capturedPiece.isKing()) {
                        numWhiteKings--;
                    }
                }
//                updateScore();
            }

            moveRecord.add("J " + step.toString());
        }

        return moveRecord;
    }
}

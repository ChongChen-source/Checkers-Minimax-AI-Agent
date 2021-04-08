public class PlayState {
    private PlayMode playMode;
    private PlayColor playColor;
    private PlayColor opponentColor;
    private double timeLeft;
    private Board board;

    private PlayColor winnerColor;
    private boolean isDraw = false;

    public PlayState() {
    }

    public PlayState(PlayMode playMode, PlayColor playColor, double timeLeft, Board board) {
        this.playMode = playMode;
        this.playColor = playColor;
        this.opponentColor = (playColor.equals(PlayColor.BLACK)) ? PlayColor.WHITE : PlayColor.BLACK;
        this.timeLeft = timeLeft;
        this.board = board;
    }

    public PlayMode getPlayMode() {
        return playMode;
    }

    public void setPlayMode(PlayMode playMode) {
        this.playMode = playMode;
    }

    public PlayColor getPlayColor() {
        return playColor;
    }

    public void setPlayColor(PlayColor playColor) {
        this.playColor = playColor;
        this.opponentColor = (playColor.equals(PlayColor.BLACK)) ? PlayColor.WHITE : PlayColor.BLACK;
    }

    public PlayColor getOpponentColor() {
        return opponentColor;
    }

    public double getTimeLeft() {
        return timeLeft;
    }

    public void setTimeLeft(double timeLeft) {
        this.timeLeft = timeLeft;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }
}

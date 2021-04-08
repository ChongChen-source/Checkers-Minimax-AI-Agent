public class Piece {
    private int row;
    private int col;
    private PlayColor color;
    private boolean isKing;

    private String position;

    private int[][] moveDirs;

    public Piece(int row, int col, PlayColor color, boolean isKing) {
        this.row = row;
        this.col = col;
        this.color = color;
        this.isKing = isKing;

        this.position = BoardUtility.cellsName[row][col];

        if (!this.isKing) {
            moveDirs = color.getForwardDirs();
        } else {
            moveDirs = color.getKingDirs();
        }
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public void move(int destRow, int destCol) {
        this.row = destRow;
        this.col = destCol;
        this.position = BoardUtility.cellsName[row][col];
    }

    public PlayColor getColor() {
        return color;
    }

    public boolean isKing() {
        return this.isKing;
    }

    public void crown() {
        this.isKing = true;
        this.moveDirs = color.getKingDirs();
    }

    public int[][] getMoveDirs() {
        return moveDirs;
    }

    public String getPosition() {
        return this.position;
    }
}

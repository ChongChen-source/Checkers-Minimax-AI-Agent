public class Step {
    private int fromPosRow;
    private int fromPosCol;

    private int toPosRow;
    private int toPosCol;

    public Step(int fromPosRow, int fromPosCol, int toPosRow, int toPosCol) {
        this.fromPosRow = fromPosRow;
        this.fromPosCol = fromPosCol;
        this.toPosRow = toPosRow;
        this.toPosCol = toPosCol;
    }

    @Override
    public String toString() {
        return BoardUtility.cellsName[fromPosRow][fromPosCol] + " " + BoardUtility.cellsName[toPosRow][toPosCol];
    }

    public int getFromPosRow() {
        return fromPosRow;
    }

    public int getFromPosCol() {
        return fromPosCol;
    }

    public int getToPosRow() {
        return toPosRow;
    }

    public int getToPosCol() {
        return toPosCol;
    }
}

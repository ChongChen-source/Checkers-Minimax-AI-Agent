public enum PlayColor {
    BLACK(1, 7),
    WHITE(-1, 0);

    private int[][] forwardDirs;
    private int[][] backwardDirs;
    private int[][] kingDirs;

    private int kingRow;

    private PlayColor(int forwardRowDir, int kingRow) {
        this.forwardDirs = new int[2][2];
        this.backwardDirs = new int[2][2];

        forwardDirs[0] = new int[]{forwardRowDir, 1};
        forwardDirs[1] = new int[]{forwardRowDir, -1};

        backwardDirs[0] = new int[]{-forwardRowDir, 1};
        backwardDirs[1] = new int[]{-forwardRowDir, -1};

        kingDirs = new int[4][2];
        kingDirs[0] = forwardDirs[0];
        kingDirs[1] = forwardDirs[1];
        kingDirs[2] = backwardDirs[0];
        kingDirs[3] = backwardDirs[1];

        this.kingRow = kingRow;
    }

    public int[][] getForwardDirs() {
        return forwardDirs;
    }

    public int[][] getBackwardDirs() {
        return backwardDirs;
    }

    public int[][] getKingDirs() {
        return kingDirs;
    }

    public int getKingRow() {
        return kingRow;
    }
}

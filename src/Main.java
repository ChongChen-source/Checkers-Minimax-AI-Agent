public class Main {

    /**
     * For Submission
     * @param args
     */
    public static void main(String[] args) {
        long start = System.nanoTime();

        run("input.txt", "output.txt");

        long end = System.nanoTime();
        long durationInMilliseconds = (end - start) / 1000000;
        System.out.println("Time exclude IO in ms: " + (durationInMilliseconds));
    }

    private static void run(String inputFilePath, String outputFilePath) {
        // Read from input file
        PlayState playState = FileUtility.getPlayState(inputFilePath);
        PlayMode playMode = playState.getPlayMode();
        PlayColor playColor = playState.getPlayColor();
        double timeLeft = playState.getTimeLeft();
        Board board = playState.getBoard();

        // Record time
        long start = System.nanoTime();

        // determine depth
        int depth = FileUtility.determineDepth(timeLeft, "calibration.txt");

        // Play!
        Move moveToPlay = board.play(playMode, playColor, depth);

        // Print duration
        long end = System.nanoTime();
        long durationInMilliseconds = (end - start) / 1000000;
        System.out.println("Time include IO in ms: " + (durationInMilliseconds));

        // Generate output
        FileUtility.generateOutput(outputFilePath, moveToPlay);
    }
}

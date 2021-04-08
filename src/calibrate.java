import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class calibrate {
    public static final int DEPTH_LIMIT = 5;

    public static void main(String[] args) {
        generateCalibration(calibrate());
    }

    public static final String CALIBRATION_PATH = "src/HW2/calibration.txt";

    private static void generateCalibration(List<String> infos) {
        try {
            FileWriter writer = new FileWriter(CALIBRATION_PATH);
            for (String line: infos) {
                writer.write(line);
                writer.write(System.lineSeparator());
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("Failed to write into: " + CALIBRATION_PATH);
            e.printStackTrace();
        }
    }

    /**
     * Get info for depth from 0 to DEPTH_LIMIT
     * @return
     */
    private static List<String> calibrate() {
        List<String> infos = new LinkedList<>();

        for (int depth = 0; depth <= DEPTH_LIMIT; depth++) {
            infos.add(depth + "");
            double durationInSeconds = getDurationInSeconds(depth);
            infos.add(durationInSeconds + "");
        }

        return infos;
    }

    private static double getDurationInSeconds(int depth) {
        PlayState playState = FileUtility.getInitPlayState();

        long start = System.nanoTime();

        PlayMode playMode = playState.getPlayMode();
        PlayColor playColor = playState.getPlayColor();
        Board board = playState.getBoard();

        // Play!
        Move moveToPlay = board.play(playMode, playColor, depth);

        long end = System.nanoTime();
        double durationInSeconds = (end - start) / 1000000000.0;
        return  durationInSeconds;
    }
}

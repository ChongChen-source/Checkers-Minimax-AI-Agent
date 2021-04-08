import java.util.LinkedList;
import java.util.List;

public class Move {
    private MoveType moveType;

    private List<Step> steps;

    private int size;

    public Move() {
        this.steps = new LinkedList<>();
        this.size = 0;
    }

    public Move(MoveType moveType) {
        this.moveType = moveType;
        this.steps = new LinkedList<>();
        this.size = 0;
    }

    public Move(Move inMove) {
        this.moveType = inMove.getMoveType();
        this.steps = new LinkedList<>();
        this.size = 0;

        List<Step> inSteps = inMove.getSteps();
        for (Step step : inSteps) {
            // TODO Deep Copy
            steps.add(step);
            this.size++;
        }
    }

    public void appendStep(Step step) {
        this.steps.add(step);
        this.size++;
    }

    public Step removeLastStep() {
        Step stepRemoved = this.steps.remove(this.size - 1);
        this.size--;
        return stepRemoved;
    }

    public List<String> getStepStrList() {
        List<String> stepStrList = new LinkedList<>();

        for (Step step: this.steps) {
            stepStrList.add(this.moveType.name() + " " + step.toString());
        }

        return stepStrList;
    }

    public MoveType getMoveType() {
        return moveType;
    }

    public boolean isEmpty() {
        return (size == 0);
    }

    public int size() {
        return size;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public void copyMove(Move move) {
        this.moveType = move.moveType;
        this.steps = move.steps;
        this.size = move.size;
    }
}

enum MoveType {
    E, // Move to an adjacent empty location
    J; // Jump over an adjacent opponent's piece to an empty location
}

public enum Direction {

    UP, DOWN, LEFT, RIGHT;



    public static Direction fromInt(int direction) {

        switch (direction) {

            case 0: return UP;

            case 1: return DOWN;

            case 2: return LEFT;

            case 3: return RIGHT;

            default: throw new IllegalArgumentException("Invalid direction: " + direction);

        }
    }
}
package ntu.mdp.pathfinding;

public enum CarFacingDir {
    WEST,
    NORTH,
    EAST,
    SOUTH;

    public static CarFacingDir getNextDir(CarFacingDir currentDir, boolean isClockWiseTurn) {
        return switch (currentDir) {
            case WEST -> isClockWiseTurn ? NORTH : SOUTH;
            case NORTH -> isClockWiseTurn ? EAST : WEST;
            case EAST -> isClockWiseTurn ? SOUTH : NORTH;
            case SOUTH -> isClockWiseTurn ? WEST : EAST;
        };
    }

    public static CarFacingDir getDirFromDegree(int degree) {
        return switch (degree) {
            case 0 -> EAST;
            case 90 -> NORTH;
            case 180 -> WEST;
            case 270 -> SOUTH;
            default -> throw new IllegalStateException("Unexpected value: " + degree);
        };
    }
}

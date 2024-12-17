package utils.constants;

public enum StageCoordination {
    CLOUD_FIRST_VERTEX(280,90),
    CLOUD_SECOND_VERTEX(1000,90),
    CLOUD_THIRD_VERTEX(280, 650),
    CLOUD_FOURTH_VERTEX(1000, 650);

    private final int x;
    private final int y;

    StageCoordination(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}

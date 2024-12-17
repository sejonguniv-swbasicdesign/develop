package utils.constants;

import java.awt.*;

public enum ComponentSize {

    BOSSSTAGEWIDTH(1400),
    BOSSSTAGEHEIGHT(800);

    private int size;

    ComponentSize(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }

    public Dimension getBossDimension() {
        return new Dimension(BOSSSTAGEWIDTH.getSize(), BOSSSTAGEHEIGHT.getSize());
    }
}

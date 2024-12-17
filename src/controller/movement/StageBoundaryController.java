package controller.movement;

import model.characters.Characters;
import utils.constants.StageCoordination;

import java.awt.*;

public class StageBoundaryController {
    private final Polygon stageBoundary;

    public StageBoundaryController() {
        stageBoundary = createStageBoundary();
    }

    public boolean canMove(Characters character, int deltaX, int deltaY) {
        Rectangle futureBounds = new Rectangle(
                character.x + deltaX,
                character.y + deltaY,
                character.getWidth(),
                character.getHeight()
        );

        return stageBoundary.contains(futureBounds);
    }

    private Polygon createStageBoundary() {
        Polygon polygon = new Polygon();
        for (StageCoordination coord : StageCoordination.values()) {
            polygon.addPoint(coord.getX(), coord.getY());
        }
        return polygon;
    }
}

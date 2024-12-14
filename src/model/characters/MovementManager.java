package model.characters;

import controller.movement.StageBoundaryController;

public class MovementManager {
    private final StageBoundaryController boundaryController;

    public MovementManager() {
        boundaryController = new StageBoundaryController();
    }

    public void moveWithBoundary(Characters character, int deltaX, int deltaY) {
        if (boundaryController.canMove(character, deltaX, deltaY)) {
            character.move(deltaX, deltaY);
        }
    }
}

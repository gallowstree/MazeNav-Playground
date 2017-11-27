package edu.galileo.mazenav.rendering;

import edu.galileo.mazenav.common.Direction;
import edu.galileo.mazenav.common.Utils;
import edu.galileo.mazenav.common.Vec2;
import edu.galileo.mazenav.rendering.Renderable;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class SimpleAgent implements Renderable {

    public Color color = Color.GREENYELLOW;
    public Vec2 position = new Vec2(0,0);
    public Direction direction = Direction.N;

    @Override
    public void render(GraphicsContext gc, double padding, double tileSize) {
        gc.setFill(color);
        double centerX = padding + position.y * tileSize + tileSize / 2;
        double centerY = padding + position.x * tileSize + tileSize / 2;
        double halfAgentBase = tileSize * 0.4 / 2;
        double halfAgentHeight = tileSize * 0.5 / 2;
        gc.save();
        Utils.rotate(gc, Utils.degrees(direction), centerX, centerY);
        gc.fillPolygon(new double[]{centerX - halfAgentBase, centerX + halfAgentBase, centerX},
                new double[]{centerY + halfAgentHeight, centerY + halfAgentHeight, centerY - halfAgentHeight },3);
        gc.restore();
    }
}

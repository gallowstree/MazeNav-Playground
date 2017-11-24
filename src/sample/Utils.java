package sample;

import io.vavr.collection.List;
import io.vavr.collection.SortedSet;
import io.vavr.collection.TreeSet;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.transform.Rotate;

import java.util.HashMap;
import java.util.Map;

import static java.text.MessageFormat.format;
import static sample.Direction.E;
import static sample.Direction.N;
import static sample.Direction.S;

public class Utils {

    static Tile tile(Direction... d) {
        return new Tile(TreeSet.of(d));
    }

    static void debug(MappingStrategy.State s, List<MappingStrategy.BreadCrumb> bc) {
        String pad = "";
        System.out.println(pad + " <" + s + ", " + str(bc)+ ">");
    }

    static String str(SortedSet<Direction> canMoveTo) {
        return "[" + canMoveTo.toJavaStream()
                .map(Enum::toString)
                .reduce("", (a,b) -> a + (a.length() == 0 ? "" : ",") + b) + "]";
    }

    static String str(Vec2 p) {
        return format("({0},{1})", p.x, p.y);
    }

    static String str(List<MappingStrategy.BreadCrumb> bc) {
        return bc.map(Utils::str).toString();
    }

    static String str(MappingStrategy.BreadCrumb b) {
        return "<" + str(b.position) + ", " + str(b.successors.canMoveTo) + ">";
    }

    static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (Exception e) {

        }
    }

    public static void rotate(GraphicsContext gc, double angle, double px, double py) {
        Rotate r = new Rotate(angle, px, py);
        gc.setTransform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(), r.getTx(), r.getTy());
    }

    public static double degrees(Direction direction) {
        return direction == N ? 0 :
                direction == S ? 180 :
                        direction == E ? 90 : -90;
    }

    public static Map<Vec2, SortedSet<Direction>> wallsFromTiles(Tile[][] maze) {
        return new HashMap<>();
    }
}

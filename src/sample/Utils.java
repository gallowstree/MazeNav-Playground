package sample;

import io.vavr.collection.List;
import io.vavr.collection.SortedSet;
import io.vavr.collection.TreeSet;

import static java.text.MessageFormat.format;

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
}

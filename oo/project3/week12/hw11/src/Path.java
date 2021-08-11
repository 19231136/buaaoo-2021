import com.oocourse.spec3.main.Person;

public class Path implements Comparable<Path> {
    private int distance;
    private Person begin;
    private Person end;

    public Path(int distance,Person begin,Person end) {
        this.distance = distance;
        this.begin = begin;
        this.end = end;
    }

    public int getDistance() {
        return distance;
    }

    public Person getBegin() {
        return begin;
    }

    public Person getEnd() {
        return end;
    }

    @Override
    public int compareTo(Path o) {
        return Integer.compare(distance,o.getDistance());
    }
}

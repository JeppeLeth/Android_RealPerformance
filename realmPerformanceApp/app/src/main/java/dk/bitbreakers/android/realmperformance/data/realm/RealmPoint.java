package dk.bitbreakers.android.realmperformance.data.realm;

import dk.bitbreakers.android.realmperformance.data.Point;
import io.realm.RealmObject;

public class RealmPoint extends RealmObject implements Point {
    private int x;
    private double y;

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

}

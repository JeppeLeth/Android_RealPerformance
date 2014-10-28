package dk.bitbreakers.android.realmperformance.data.sqlite;

import dk.bitbreakers.android.realmperformance.data.Point;

public class SqlitePoint implements Point {
    private int x;
    private double y;

    public SqlitePoint() {
    }

    public SqlitePoint(int x, double y) {
        this.x = x;
        this.y = y;
    }

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
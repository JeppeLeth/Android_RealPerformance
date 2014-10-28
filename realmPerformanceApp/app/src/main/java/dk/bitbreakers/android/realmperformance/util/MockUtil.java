package dk.bitbreakers.android.realmperformance.util;

public class MockUtil {
    private MockUtil(){}

    public static double sinCurveCalc(int x) {
        return 40.*Math.sin((x/10.)/2);
    }
}

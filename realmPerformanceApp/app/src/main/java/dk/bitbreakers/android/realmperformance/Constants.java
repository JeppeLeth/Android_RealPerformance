package dk.bitbreakers.android.realmperformance;

public interface Constants {

    /**
     * Number of queries to test performance with
     * If you change this parameter, please make sure to clear the whole application on the device
     * before installing a new build.
     * In a later edition this could be setup to change dynamically by the users selection.
     */
    int TEST_QUERIES_COUNT      = 250_000;

    String URL_HOMEPAGE         = "http://realm.io/";
    String URL_GITHUB           = "https://github.com/realm";
    String URL_TWITTER          = "http://twitter.com/realm";
    String URL_STACKOVERFLOW    = "https://stackoverflow.com/questions/tagged/realm?sort=newest";
    String EMAIL_ADDRESS        = "info@realm.io";
}

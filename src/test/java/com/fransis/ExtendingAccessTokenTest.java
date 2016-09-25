package com.fransis;

import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Version;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Created by francisco on 9/24/16.
 */
public class ExtendingAccessTokenTest {


    private FacebookClient facebookClient;
    private static final String ACCESS_TOKEN = "EAAS9ifvTHMABAFNdj13gAFuJcKvczwnd3nZCxg3LMXEpR7fUreXlCT8386G6aSqAUcN4OkZC5XkCsOglvnG2Lf6tawG1YuB7JECI4gj6FA2GraZBGGFzOEuJ6LI2g8NXOmk7xemYJhB1ZBi2OYbFzKMZBNutC99LtNZCqKkMjKXQZDZD";
    private static final String MY_APP_ID = "1334300239928512";
    private static final String MY_APP_SECRET = "fd26d7bc50496527912c4abcee2bf172";


    @Before
    public void setUp() throws Exception {
        facebookClient = new DefaultFacebookClient( this.ACCESS_TOKEN, Version.VERSION_2_7);
    }

    @Test
    @Ignore
    public void extending_accessToken() throws Exception {

        FacebookClient.AccessToken accessToken =
                new DefaultFacebookClient().obtainExtendedAccessToken(MY_APP_ID,
                        MY_APP_SECRET, ACCESS_TOKEN);
        System.out.println("My extended access token: " + accessToken);

    }
}

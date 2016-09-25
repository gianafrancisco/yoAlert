package com.fransis;

import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Version;
import com.restfb.scope.ScopeBuilder;
import com.restfb.scope.UserDataPermissions;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by francisco on 9/24/16.
 */
public class FacebookLoginTest {


    private FacebookClient facebookClient;
    private static final String MY_APP_ID = "1334300239928512";

    @Before
    public void setUp() throws Exception {
        facebookClient = new DefaultFacebookClient(Version.VERSION_2_7);
    }

    @Test
    public void facebook_login() throws Exception {

        ScopeBuilder scopeBuilder = new ScopeBuilder();
        scopeBuilder.addPermission(UserDataPermissions.USER_MANAGED_GROUPS);

        String loginDialogUrlString = facebookClient.getLoginDialogUrl(MY_APP_ID, "http://localhost", scopeBuilder);
        System.out.println(loginDialogUrlString);

    }
}

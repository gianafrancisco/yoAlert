package com.fransis;

import com.fransis.email.Mailin;

import java.util.*;

/**
 * Created by francisco on 23/09/2016.
 */
public class EmailTest {
    public static void main(String ...arg){
        Mailin http = new Mailin("https://api.sendinblue.com/v2.0", "", 5000);   //Optional parameter: Timeout in MS
        String str = http.get_account();

        Map<String, Object> mapData = new HashMap<>();

        Map<String, String> to = new HashMap<>();
        to.put("gianafrancisco@gmail.com", "Francisco Giana");

        mapData.put("to", to);

        ArrayList<String> from = new ArrayList<>();
        from.add("berna@yomeanimoyvos.com");
        from.add("Alertas Yo Me animo!!");

        mapData.put("from", from);

        mapData.put("subject", "test sendinblu");

        mapData.put("html", "Test de correo YoMeAnimo Alerts!!!");

        str = http.send_email(mapData);

        System.out.println(str);
    }
}

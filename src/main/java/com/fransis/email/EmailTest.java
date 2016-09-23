package com.fransis.email;

import java.util.*;

/**
 * Created by francisco on 23/09/2016.
 */
public class EmailTest {
    public static void main(String ...arg){
        Mailin http = new Mailin("https://api.sendinblue.com/v2.0", "", 5000);   //Optional parameter: Timeout in MS
        String str = http.get_account();

        List<Object> data = new ArrayList<>();

        Map<String, Object> mapData = new HashMap<>();


        Map<String, String> to = new HashMap<>();
        to.put("gianafrancisco@gmail.com", "Francisco Giana");

        mapData.put("to", to);

        Map<String, Map<String, String>> mapTo = new HashMap<>();
        mapTo.put("to", to);
        data.add(mapTo);

        ArrayList<String> from = new ArrayList<>();
        from.add("berna@yomeanimoyvos.com");
        from.add("Alertas Yo Me animo!!");

        Map<String, List<String>> mapFrom = new HashMap<>();
        mapFrom.put("from", from);
        mapData.put("from", from);
        data.add(mapFrom);

        Map<String, String> subject = new HashMap<>();
        subject.put("subject", "test sendinblu");
        mapData.put("subject", "test sendinblu");
        data.add(subject);

        Map<String, String> html = new HashMap<>();
        html.put("html", "Test de correo YoMeAnimo Alerts!!!");
        mapData.put("html", "Test de correo YoMeAnimo Alerts!!!");
        data.add(html);

        str = http.send_email(mapData);

        System.out.println(str);
    }
}

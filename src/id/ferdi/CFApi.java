package id.ferdi;

import org.json.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Scanner;

public class CFApi {
    public enum REQUEST_TYPE { ZONE, DNSRecord }

    static private String endpoint = "https://api.cloudflare.com/client/v4/";
    static private String filters = "?page=1&per_page=20&order=status&direction=desc&match=all";
    static private String X_Auth_Email = "";
    static private String X_Auth_Key = "";
    static private String Content_Type = "application/json";

    private JSONArray allZones;

    private ArrayList<CFZone> zonesARList = new ArrayList<>();

    public CFApi( ) {
        CFApi.readConfig();
    }

    public void obtainZones() {

        allZones = CFApi.fetchAPI(REQUEST_TYPE.ZONE,"");

        for ( int i = 0; i < allZones.length(); i++ ) {
            JSONObject obj = allZones.getJSONObject(i);
            zonesARList.add(
                    new CFZone(
                            obj.getString("name"),
                            obj.getString("id")
                    )
            );
        }
    }



    static public JSONArray fetchAPI(REQUEST_TYPE request_type, String zone_id) {
        InputStream response = null;

        URLConnection connection = null;
        try {
            if( request_type == REQUEST_TYPE.ZONE )
                connection = new URL(endpoint + "zones" + filters).openConnection();
            else
                connection = new URL(endpoint + "zones/" + zone_id + "/dns_records" + filters).openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }

        connection.setRequestProperty("X-Auth-Email", X_Auth_Email);
        connection.setRequestProperty("X-Auth-Key", X_Auth_Key);
        connection.setRequestProperty("Content-Type", Content_Type);

        try {
            response = connection.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONObject zoneObj;
        try (Scanner scanner = new Scanner(response)) {
            String receiveData = scanner.useDelimiter("\\A").next();
            zoneObj = new JSONObject(receiveData);
        }

        return zoneObj.getJSONArray("result");
    }

    static private void readConfig() {
        Properties prop = new Properties();
        InputStream input = null;
        try {

            input = new FileInputStream("config.properties");

            // load a properties file
            prop.load(input);

            // get the property value and print it out
            X_Auth_Email = prop.getProperty("X-Auth-Email");
            X_Auth_Key = prop.getProperty("X-Auth-Key");

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }



}

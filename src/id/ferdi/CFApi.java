package id.ferdi;

import org.json.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.*;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Scanner;

public class CFApi {
    public enum REQUEST_TYPE { ZONE, DNSRecord }

    static private String endpoint = "https://api.cloudflare.com/client/v4/";
    static private String filters = "?page=1&per_page=40&order=status&direction=desc&match=all";
    static private String X_Auth_Email = "";
    static private String X_Auth_Key = "";
    static private String Content_Type = "application/json";

    private JSONArray allZones;

    private ArrayList<CFZone> zonesARList = new ArrayList<>();

    public CFApi( ) {
        CFApi.readConfig();
    }

    public void obtainZones() {

        allZones = CFApi.retrieveZones();

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



    static public JSONArray getRecords(String zone_id) {

        URL url = null;
        try {
                url = new URL(endpoint + "zones/" + zone_id + "/dns_records" + filters);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return CFApi.contactCF(url,"GET","");
    }

    //at the moment void, we don't need to manage response
    static public void updateRecord(String zoneID, String recordID, String data) {
        URL url = null;
        try {
            url = new URL(endpoint + "zones/" + zoneID + "/dns_records/" + recordID);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        JSONArray newObj = contactCF(url,"PUT", data);
    }

    static public JSONArray retrieveZones() {

        URL url = null;
        try {
            url = new URL(endpoint + "zones");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return CFApi.contactCF(url,"GET","");
    }

    static private JSONArray contactCF(URL url, String requestMethod,String data) {
        InputStream response = null;
        HttpURLConnection httpCon = null;
        try {
            httpCon = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        httpCon.setDoOutput(true);
        try {
            httpCon.setRequestMethod(requestMethod);
        } catch (ProtocolException e) {
            e.printStackTrace();
        }
        httpCon.setRequestProperty("X-Auth-Email",X_Auth_Email);
        httpCon.setRequestProperty("X-Auth-Key",X_Auth_Key);
        httpCon.setRequestProperty("Content-Type",Content_Type);

        if ( data != "" ) {
            OutputStreamWriter out = null;
            try {
                out = new OutputStreamWriter(
                        httpCon.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                out.write(data);
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //obtain response and prepare JSONArray for CloudFlare result
        try {
            response = httpCon.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONObject zoneObj;
        try (Scanner scanner = new Scanner(response)) {
            String receiveData = scanner.useDelimiter("\\A").next();
            zoneObj = new JSONObject(receiveData);
        }
        JSONArray objArray = null;
        try {
            objArray = zoneObj.getJSONArray("result");
        } catch (JSONException e) {
               //e.printStackTrace();
        }

        return objArray;
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

    public CFZone findZone(String name) {
        CFZone theDomain = null;
        for(CFZone domain : zonesARList) {
            if ( domain.equals(name) ) {
                theDomain = domain;
                break;
            }
        }
        return theDomain;
    }

}

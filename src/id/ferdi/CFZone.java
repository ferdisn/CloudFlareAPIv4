package id.ferdi;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class CFZone {
    private String domain = "";
    private String id = "";

    private ArrayList<CFZoneRecord> records = new ArrayList<>();

    private JSONArray allRecords;

    public CFZone(String vDomain, String vId) {
        domain = vDomain;
        id = vId;

        this.obtainRecords();
    }

    public void obtainRecords() {
        allRecords = CFApi.fetchAPI(CFApi.REQUEST_TYPE.DNSRecord,id);

        for ( int i = 0; i < allRecords.length(); i++ ) {
            JSONObject obj = allRecords.getJSONObject(i);
            records.add(
                    new CFZoneRecord(
                            obj.getString("id"),
                            obj.getString("type"),
                            obj.getString("name"),
                            obj.getString("content")
                    )
            );
        }
    }

}

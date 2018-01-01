package id.ferdi;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

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

    public CFZoneRecord findRecord(String name) {
        CFZoneRecord theRecord = null;
        for(CFZoneRecord record : records) {
            if ( record.equals(name) ) {
                theRecord = record;
                break;
            }
        }
        return theRecord;
    }

    public void obtainRecords() {
        allRecords = CFApi.getRecords(id);

        for ( int i = 0; i < allRecords.length(); i++ ) {
            JSONObject obj = allRecords.getJSONObject(i);
            records.add(
                    new CFZoneRecord(
                            id,
                            obj.getString("id"),
                            obj.getString("type"),
                            obj.getString("name"),
                            obj.getString("content")
                    )
            );
        }
    }

    public boolean equals(String vName) {
        if (Objects.equals(vName, domain))
            return true;
        else {
            return false;
        }

    }

}

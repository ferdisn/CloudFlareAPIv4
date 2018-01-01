package id.ferdi;

import java.util.Objects;

public class CFZoneRecord {
    private String zoneID;
    private String id;
    private String type;
    private String name;
    private String content;


    public CFZoneRecord(String vZoneID, String vId, String vType, String vName, String vContent) {
        zoneID = vZoneID; id = vId; type = vType; name = vName; content = vContent;
    }

    public void updateRecord(String vContent) {
        String data = "{\"type\":\""+ type +"\"," +
                "\"name\":\"" + name + "\"," +
                "\"content\":\"" + vContent + "\"," +
                "\"ttl\":120}";
        CFApi.updateRecord(zoneID,id,data);
    }

    public boolean equals(String vName) {
        if (Objects.equals(vName, name))
            return true;
        else {
            return false;
        }

    }




}

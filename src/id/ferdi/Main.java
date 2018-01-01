package id.ferdi;

public class Main {

    public static void main(String[] args) {

        CFApi api = new CFApi();

        //populate zones and records
        api.obtainZones();

        //alter existing record
        api.findZone("paragita.com").findRecord("_acme-challenge.imap.paragita.com").updateRecord("DeployTest");


        System.out.println("");

    }


}

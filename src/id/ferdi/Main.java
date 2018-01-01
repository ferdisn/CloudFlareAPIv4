package id.ferdi;

public class Main {

    public static void main(String[] args) {

        CFApi api = new CFApi();

        //populate zones and records
        api.obtainZones();

        //alter existing record
        api.findZone("didinofendra.net").findRecord("_acme-challenge.imap.didinofendra.net").updateRecord("tes2");



        System.out.println("");

    }


}

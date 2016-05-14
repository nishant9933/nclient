package com.example.fugro;

/**
 * Created by Sneha on 02-05-2016.
 */
public class HappeningItems {

    String titlename;
    String eventdurationfrom;
    String eventdurationto;
    String eventdescription;

    public HappeningItems(String titlename, String eventdurationfrom, String eventdurationto, String eventdescription) {

        this.titlename = titlename;
        this.eventdurationfrom = eventdurationfrom;
        this.eventdescription = eventdescription;
        this.eventdurationto = eventdurationto;
    }

    public String getEventTitle() {
        return titlename;
    }

    public void setEventTitle(String titlename) {
        this.titlename = titlename;
    }

    public String getEventdurationFrom() {
        return eventdurationfrom;
    }

    public void setEventdurationFrom(String eventdurationfrom) {
        this.eventdurationfrom = eventdurationfrom;
    }

    public String getEventto() {
        return eventdurationto;
    }

    public void setEventto(String eventdurationto) {
        this.eventdurationto = eventdurationto;
    }

    public String getEventdescription() {
        return eventdescription;
    }

    public void setEventdescription(String eventdescription) {
        this.eventdescription = eventdescription;
    }
}

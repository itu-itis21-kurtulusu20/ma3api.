package tr.gov.tubitak.uekae.esya.api.webservice.mssclient.wrapper;

import java.util.Calendar;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: ramazan.girgin
 * Date: 7/31/12
 * Time: 9:16 AM
 * To change this template use File | Settings | File Templates.
 */
public class ETransIdManager {
    public static String getNewTransId(){
        Calendar instance = Calendar.getInstance();
        Date time = instance.getTime();
        int year = time.getYear();
        int month = time.getMonth();
        int day = time.getDay();
        int hour = time.getHours();
        int minute = time.getMinutes();
        int second = time.getSeconds();
        return "_"+day+""+month+""+year+""+hour+""+minute+""+second;
    }
}

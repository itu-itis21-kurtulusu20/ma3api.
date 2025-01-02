package tr.gov.tubitak.uekae.esya.api.webservice.mssclient.wrapper;

import java.util.HashMap;
import java.util.Queue;

/**
 * Created with IntelliJ IDEA.
 * User: ramazan.girgin
 * Date: 7/31/12
 * Time: 9:23 AM
 * To change this template use File | Settings | File Templates.
 */
public class EMSSPHanderCertCache {
    int maxElementCount=10;

    public void setMaxElementCount(int maxElementCount) {
        this.maxElementCount = maxElementCount;
    }

    Queue<String> serialQueue;
    HashMap<String,byte[]> serialCertHash;
    static EMSSPHanderCertCache instance;

    public static synchronized EMSSPHanderCertCache getInstance(){
        if(instance == null){
            instance = new EMSSPHanderCertCache();
        }
        return instance;
    }

    public void clear(){
        serialQueue.clear();
        serialCertHash.clear();
    }

    public byte[] getCert(String serialNumber)
    {
        byte [] retCert=null;
        if(serialCertHash.containsKey(serialNumber)){
            retCert = serialCertHash.get(serialNumber);
        }
        return retCert;
    }

    public void addCert(String serialNumber,byte [] certValue){
        if(serialQueue.size()>maxElementCount)
        {
            String removeElement = serialQueue.poll();
            if(removeElement!=null){
                serialCertHash.remove(removeElement);
            }
        }
        serialCertHash.put(serialNumber,certValue);
        serialQueue.add(serialNumber);
    }

}

package tr.gov.tubitak.uekae.esya.api.asn.cvc;

import com.objsys.asn1j.runtime.Asn1BerDecodeBuffer;
import com.objsys.asn1j.runtime.Asn1Exception;
import com.objsys.asn1j.runtime.Asn1Tag;
import com.objsys.asn1j.runtime.IntHolder;
import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.cvc.HeaderList;
import tr.gov.tubitak.uekae.esya.asn.cvc.RsaPuK;

import java.io.IOException;
import java.util.Collection;
import java.util.LinkedHashMap;

/**
 * Created by IntelliJ IDEA.
 * User: bilen.ogretmen
 * Date: 5/9/11
 * Time: 4:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class EHeaderList extends BaseASNWrapper<HeaderList> {

    public EHeaderList(HeaderList aObject) {
        super(aObject);
    }

    //LinkedHashMap<byte[], Integer> _headerListInfo;
    public EHeaderList(byte[] aEncoded) throws ESYAException {
        super(aEncoded, new HeaderList());

    }

    public EHeaderList() {
        super(new HeaderList());
    }


    public void setHeaderList(byte[] aHeaderListValue) {
        getObject().value = aHeaderListValue;
    }

    public static EHeaderList fromValue(byte[] aHeaderListValue) throws ESYAException {
        EHeaderList headerList = new EHeaderList();
        headerList.setHeaderList(aHeaderListValue);
        return headerList;
    }

    public int getMessageLen() throws IOException, Asn1Exception {
        int totalMessageSize = 0;
        LinkedHashMap<Asn1Tag, Integer> headerListInfo = getListInfo();
        Collection<Integer> values = headerListInfo.values();
        for(Integer len: values)
        {
            totalMessageSize += len;
        }
        return totalMessageSize;
    }


    public LinkedHashMap<Asn1Tag, Integer> getListInfo() throws IOException, Asn1Exception {
        Asn1BerDecodeBuffer decodeBuffer = new Asn1BerDecodeBuffer(getObject().value);
        Asn1Tag tag = new Asn1Tag();
        IntHolder parsedTypeLen = new IntHolder();
        //
        LinkedHashMap<Asn1Tag, Integer> headerListInfo = new LinkedHashMap<Asn1Tag, Integer>();

        while (decodeBuffer.getByteCount() < getObject().value.length) {
            Asn1Tag parsedTag = new Asn1Tag();
            if (!decodeBuffer.matchTag(tag, parsedTag, parsedTypeLen)) {
                if (decodeBuffer.matchTag(parsedTag, null, parsedTypeLen)) {
                    if (parsedTag.equals(RsaPuK.TAG))
                        continue;
                    //Asn1BerEncodeBuffer encodeBuffer = new Asn1BerEncodeBuffer();
                    //encodeBuffer.encodeTag(parsedTag);
                    headerListInfo.put(parsedTag, parsedTypeLen.value);
                }
            }
        }
        return headerListInfo;
    }

    private int getAsn1TagLen(Asn1Tag aAsn1Tag) throws IOException, Asn1Exception {
        String a = aAsn1Tag.toString();
        int headerListSize = getObject().value.length;
        Asn1BerDecodeBuffer decodeBuffer = new Asn1BerDecodeBuffer(getObject().value);
        IntHolder typeLen = new IntHolder();

        while (decodeBuffer.getByteCount() < headerListSize) {

            if (!decodeBuffer.matchTag(aAsn1Tag, null, typeLen)) {
                decodeBuffer.readByte();
            } else {
                return typeLen.value;
            }

//            catch (Asn1Exception e) {
//                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//            } catch (IOException e) {
//                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//            }

        }
        return 0;
    }

     /*
    public static void main(String[] args) {
        try{
        byte[] headerList = readBytes("E:\\cvcTestCerts\\EHeaderList.bin");

        EHeaderList eHeaderList = new EHeaderList(headerList);
        LinkedHashMap<Asn1Tag, Integer> listInfo = eHeaderList.getListInfo();

        System.out.println(listInfo.toString());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static byte[] readBytes(String aFileName) {
        FileInputStream fin = null;
        byte[] data = null;
        try {
            fin = new FileInputStream(aFileName);
            int i = fin.available();
            data = new byte[i];
            fin.read(data);
            fin.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


        return data;
    }   */

}

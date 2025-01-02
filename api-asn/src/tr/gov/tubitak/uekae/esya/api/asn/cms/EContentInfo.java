package tr.gov.tubitak.uekae.esya.api.asn.cms;

import com.objsys.asn1j.runtime.Asn1Exception;
import com.objsys.asn1j.runtime.Asn1ObjectIdentifier;
import com.objsys.asn1j.runtime.Asn1OctetString;
import com.objsys.asn1j.runtime.Asn1OpenType;
import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.cms.ContentInfo;
import tr.gov.tubitak.uekae.esya.asn.util.UtilOpenType;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author ayetgin
 */
public class EContentInfo extends BaseASNWrapper<ContentInfo> {
    public EContentInfo(ContentInfo aObject) {
        super(aObject);
    }
    /**
   	 * Create EContentInfo
   	  * @param contentType
   	  * @param content
   	  */
    public EContentInfo(Asn1ObjectIdentifier contentType, Asn1OpenType content) {
        super(new ContentInfo(contentType, content));
    }
    /**
   	 * Create EContentInfo from byte array
   	  * @param aBytes byte[]
   	  */
    public EContentInfo(byte[] aBytes) throws ESYAException {
        super(aBytes, new ContentInfo());
    }

    public EContentInfo(InputStream inputStream) throws ESYAException {
        super(inputStream, new ContentInfo());
    }



    /**
	 * Returns content type of ContentInfo
	 * @return
	 */
    public Asn1ObjectIdentifier getContentType() {
        return mObject.contentType;
    }
    /**
   	 * Set content type
   	 * @param aContentType  Asn1ObjectIdentifier
   	 */
    public void setContentType(Asn1ObjectIdentifier aContentType) {
        mObject.contentType = aContentType;
    }
    /**
	 * Returns content value
	 * @return 
	 */
    public byte[] getContent() {
        return mObject.content.value;
    }
    /**
   	 * Set content value
   	 * @param aContent  byte[]
   	 */
    public void setContent(byte[] aContent) {
        mObject.content = new Asn1OpenType(aContent);
    }
    /**
	 * Returns byte array of ContentInfo's content 
	 * @return  byte[]
	 */
    public byte[] getContentStringByteValue() throws IOException, Asn1Exception {
        Asn1OctetString str = new Asn1OctetString();
        UtilOpenType.fromOpenType(mObject.content, str);
        return str.value;
    }
}

package tr.gov.tubitak.uekae.esya.api.asn.x509;

import com.objsys.asn1j.runtime.Asn1BigInteger;
import com.objsys.asn1j.runtime.Asn1DerDecodeBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.asn.util.UtilTime;
import tr.gov.tubitak.uekae.esya.asn.x509.CRLReason;
import tr.gov.tubitak.uekae.esya.asn.x509.Extension;
import tr.gov.tubitak.uekae.esya.asn.x509.TBSCertList_revokedCertificates_element;
import tr.gov.tubitak.uekae.esya.asn.x509._ImplicitValues;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Date;

/**
 * @author ahmety
 * date: Jan 26, 2010
 */
public class ERevokedCertificateElement extends BaseASNWrapper<TBSCertList_revokedCertificates_element> {

    protected static Logger logger = LoggerFactory.getLogger(ERevokedCertificateElement.class);

    public ERevokedCertificateElement(TBSCertList_revokedCertificates_element aElement)
    {
        super(aElement);
    }

    public ERevokedCertificateElement( BigInteger userCertificate, ETime revocationDate) {
        super(new TBSCertList_revokedCertificates_element(new Asn1BigInteger(userCertificate), revocationDate.getObject()));
    }

    public BigInteger getUserCertificate(){
        return mObject.userCertificate.value;
    }

    public Date getRevocationDate(){
        return UtilTime.timeToDate(mObject.revocationDate);
    }

    public EExtensions getCrlEntryExtensions(){
        return new EExtensions(mObject.crlEntryExtensions);
    }

    public void setCrlEntryExtensions(EExtensions crlEntryExtensions){
        mObject.crlEntryExtensions = crlEntryExtensions.getObject();
    }

    public int getCRLReason(){

        if (mObject.crlEntryExtensions==null){
            return CRLReason._UNSPECIFIED;
        }
        Extension[] extensions = mObject.crlEntryExtensions.elements;

        if(extensions == null || extensions.length==0)
        {
            return CRLReason._UNSPECIFIED;
        }

        for (Extension extension : extensions) {
            if (Arrays.equals(extension.extnID.value, _ImplicitValues.id_ce_cRLReasons))
            {
                byte[] value = extension.extnValue.value;
                Asn1DerDecodeBuffer dec = new Asn1DerDecodeBuffer(value);
                try {
                    return dec.decodeEnumValue(CRLReason.TAG, false, value.length);
                }catch (Exception aEx) {
                    logger.error("Error in ERevokedCertificateElement", aEx);
                    return CRLReason._UNSPECIFIED;
                }
            }
        }
        return CRLReason._UNSPECIFIED;
    }


}

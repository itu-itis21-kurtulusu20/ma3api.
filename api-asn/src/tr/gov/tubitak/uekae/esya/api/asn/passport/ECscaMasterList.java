package tr.gov.tubitak.uekae.esya.api.asn.passport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.passport.CscaMasterList;
import tr.gov.tubitak.uekae.esya.asn.passport.CscaMasterListVersion;
import tr.gov.tubitak.uekae.esya.asn.passport._SetOfCertificate;
import tr.gov.tubitak.uekae.esya.asn.x509.Certificate;

import java.util.Arrays;

/**
 * Created by orcun.ertugrul on 04-Oct-17.
 */
public class ECscaMasterList  extends BaseASNWrapper<CscaMasterList>
{

    private static final Logger logger = LoggerFactory.getLogger(ECscaMasterList.class);

    public ECscaMasterList(CscaMasterList aObject)
    {
        super(aObject);
    }

    public ECscaMasterList(byte[] aBytes)
            throws ESYAException
    {
        super(aBytes, new CscaMasterList());
    }


    public ECscaMasterList()
    {
        super(new CscaMasterList());
        mObject.version = new CscaMasterListVersion(0);
    }

    public ECertificate [] getCertificates()
    {
        Certificate [] certs = mObject.certList.elements;
        ECertificate [] eCerts = new ECertificate[certs.length];

        for(int i=0; i < certs.length; i++)
        {
            eCerts[i] = new ECertificate(certs[i]);
        }

        return eCerts;
    }


    public void setCertificates(ECertificate [] eCerts)
    {
        Certificate [] certs = new Certificate[eCerts.length];

        for(int i=0; i < certs.length; i++)
        {
            certs[i] = eCerts[i].getObject();
        }

        mObject.certList = new _SetOfCertificate();
        mObject.certList.elements = certs;
    }



}

package tr.gov.tubitak.uekae.esya.api.asn.x509;

import com.objsys.asn1j.runtime.Asn1BigInteger;
import com.objsys.asn1j.runtime.Asn1BitString;
import com.objsys.asn1j.runtime.Asn1DerEncodeBuffer;
import com.objsys.asn1j.runtime.Asn1Exception;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.util.UtilTime;
import tr.gov.tubitak.uekae.esya.asn.x509.CertificateList;
import tr.gov.tubitak.uekae.esya.asn.x509.GeneralName;
import tr.gov.tubitak.uekae.esya.asn.x509.TBSCertList_revokedCertificates_element;
import tr.gov.tubitak.uekae.esya.asn.x509._SeqOfTBSCertList_revokedCertificates_element;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.Calendar;

/**
 * @author ahmety
 *         date: Jan 28, 2010
 */
public class ECRL extends BaseASNWrapper<CertificateList>
{

    private static Logger logger = LoggerFactory.getLogger(ECRL.class);


    public ECRL(CertificateList aCertificateList)
    {
        super(aCertificateList);
    }

    public ECRL(byte[] aCRLArray) throws ESYAException
    {
        super(aCRLArray, new CertificateList());
    }

    public ECRL(File aFile) throws ESYAException, IOException
    {
        super(new FileInputStream(aFile), new CertificateList());
    }


    public ECRL(InputStream aCRLStream) throws ESYAException
    {
        super(aCRLStream, new CertificateList());
    }

    public ECRL(ETBSCertList aTbsCertList, EAlgorithmIdentifier aSignatureAlgorithm, byte[] aSignature)
    {

        super(new CertificateList(aTbsCertList.getObject(),
                                  aSignatureAlgorithm.getObject(),
                                  new Asn1BitString(aSignature.length << 3, aSignature)));

    }

    public EName getIssuer()
    {
        return new EName(mObject.tbsCertList.issuer);
    }

    public BigInteger getCRLNumber()
    {
        EExtension crlNumberExt = getCRLExtensions().getCRLNumber();

        if (crlNumberExt != null) {
            Asn1BigInteger crlNumber = new Asn1BigInteger();
            try {
                crlNumber.decode(crlNumberExt.getValueAsDecodeBuffer());
                return crlNumber.value;
            }
            catch (Exception aEx) {
                logger.error("Sil CRL number extension decode edilemedi", aEx);
            }
        }
        return null;
    }

    /**
     * @return CRL thisUpdate field
     */
    public Calendar getThisUpdate()
    {
        return UtilTime.timeToCalendar(mObject.tbsCertList.thisUpdate);
    }

    /**
     * @return CRL nextUpdate field
     */
    public Calendar getNextUpdate()
    {
            return UtilTime.timeToCalendar(mObject.tbsCertList.nextUpdate);
    }

    public EAlgorithmIdentifier getTBSSignatureAlgorithm()
    {
        return new EAlgorithmIdentifier(mObject.tbsCertList.signature);
    }

    public void setTBSSignatureAlgorithm(EAlgorithmIdentifier aAlgorithm)
    {
        mObject.tbsCertList.signature = aAlgorithm.getObject();
    }


    public ERevokedCertificateElement getRevokedCerticateElement(int aIndex)
    {
        return new ERevokedCertificateElement(
                mObject.tbsCertList.revokedCertificates.elements[aIndex]
        );
    }

    public int getRevokedCerticateElementCount()
    {
        if (mObject.tbsCertList.revokedCertificates == null)
            return 0;
        return mObject.tbsCertList.revokedCertificates.elements.length;
    }

    public int getSize()
    {
        _SeqOfTBSCertList_revokedCertificates_element seq = mObject.tbsCertList.revokedCertificates;
        return seq != null ? seq.getLength() : 0;
    }

    public boolean contains(ECertificate aCertificate)
    {
        return (indexOf(aCertificate) >= 0);
    }

    public int indexOf(ECertificate aCertificate)
    {
        TBSCertList_revokedCertificates_element[] elements = mObject.tbsCertList.revokedCertificates.elements;
        for (int i = 0; i < elements.length; i++) {
            TBSCertList_revokedCertificates_element element = elements[i];
            if (element.userCertificate.value.equals(aCertificate.getSerialNumber()))
                return i;
        }
        return -1;
    }

    public EExtensions getCRLExtensions()
    {
        return new EExtensions(mObject.tbsCertList.crlExtensions);
    }


    public ECertificateIssuer getCertificateIssuer(int aIndex)
    {
        ECertificateIssuer defaultCI = new ECertificateIssuer();
        GeneralName gn = new GeneralName();
        gn.set_directoryName(getIssuer().getObject());
        defaultCI.addElement(gn);

        if (!isIndirectCRL() || getRevokedCerticateElementCount() == 0)
            return defaultCI;

        for (int i = aIndex; i >= 0; i--) {
            ECertificateIssuer ci = getRevokedCerticateElement(i).getCrlEntryExtensions().getCertificateIssuer();
            if (ci != null)
                return ci;
        }
        return defaultCI;
    }

    public void setSignature(byte[] aSignature)
    {
        mObject.signature = new Asn1BitString(aSignature.length << 3, aSignature);
    }

    public byte[] getSignature()
    {
        return mObject.signature.value;
    }

    public void setSignatureAlgorithm(EAlgorithmIdentifier aAlgorithm)
    {
        mObject.signatureAlgorithm = aAlgorithm.getObject();
    }

    public EAlgorithmIdentifier getSignatureAlgorithm()
    {
        return new EAlgorithmIdentifier(mObject.signatureAlgorithm);
    }

    public ETBSCertList getTBSCertList()
    {
        return new ETBSCertList(mObject.tbsCertList);
    }

    public void setTBSCertList(ETBSCertList aTBSCertList)
    {
        mObject.tbsCertList = aTBSCertList.getObject();
    }

    public byte[] getTBSEncodedBytes()
    {
        byte[] imzalanan = null;
        Asn1DerEncodeBuffer encBuf = new Asn1DerEncodeBuffer();
        try {
            mObject.tbsCertList.encode(encBuf);
            imzalanan = encBuf.getMsgCopy();
            encBuf.reset();
        }
        catch (Asn1Exception aEx) {
            logger.error("SİL değeri alınırken hata oluştu.", aEx);
        }
        return imzalanan;
    }


    public boolean isIndirectCRL()
    {
        EIssuingDistributionPoint idp = getCRLExtensions().getIssuingDistributionPoint();

        /*if (idp == null || !idp.indirectCRL.value)
            return false;

        return true;*/

        return (idp != null && idp.isIndirectCRL());
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("issuer: ").append(getIssuer().stringValue()).append("\n");
        builder.append("no: ").append(getCRLNumber()).append("\n");
        builder.append("valid from ").append(getThisUpdate().getTime()).append(" to ").append(getNextUpdate().getTime()).append("\n");
        builder.append("contains ").append(getSize()).append(" revocation info.");
        return builder.toString();
    }
}


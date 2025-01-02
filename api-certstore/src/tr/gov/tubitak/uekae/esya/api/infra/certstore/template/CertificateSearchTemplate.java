package tr.gov.tubitak.uekae.esya.api.infra.certstore.template;


import com.objsys.asn1j.runtime.Asn1BigInteger;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EName;
import tr.gov.tubitak.uekae.esya.api.common.util.Base64;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.CertStoreException;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.CertStoreUtil;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.db.cekirdek.yardimci.OzetTipi;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.model.DepoDizin;
import tr.gov.tubitak.uekae.esya.asn.util.UtilName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Date;

public class CertificateSearchTemplate
{
    protected static Logger logger = LoggerFactory.getLogger(CertificateSearchTemplate.class);
    protected byte[] mValue = null;
    protected byte[] mHash = null;
    protected OzetTipi mHashType = null;
    protected byte[] mIssuer = null;
    // this date should be after "not before" value of certificate
    protected Date mStartDate = null;
    // this date should be before "not after" value of certificate
    protected Date mEndDate = null;
    protected byte[] mSubject = null;
    protected byte[] mSerialNumber = null;
    protected KeyUsageSearchTemplate mAnahtarKullanim = null;
    protected byte[] mSubjectKeyIdentifier = null;
    protected String mEPosta = null;
    protected String mX400Adress = null;
    protected Long mDizinNo = null;

    public void setValue(byte[] aValue)
    {
        mValue = aValue;
    }

    public void setHash(byte[] aHash)
    {
        mHash = aHash;
    }

    public void setHash(String aHash)
            throws CertStoreException
    {
        try
        {
            mHash = Base64.decode(aHash);
        }
        catch(Exception aEx)
        {
            throw new CertStoreException("Hash degeri base64 decode edilirken hata olustu.",aEx);
        }
    }

    public void setHashType(OzetTipi aHashType)
    {
        mHashType = aHashType;
    }


    public void setIssuer(byte[] aIssuer)
    {
        byte[] issuer = null;
        try
        {
            issuer = CertStoreUtil.normalizeName(UtilName.byte2Name(aIssuer));
        }
        catch(Exception aEx)
        {
            //TODO
            //throw new CertStoreException("Issuer degeri normalize edilirken hata olustu.",aEx);
            logger.warn("Warning in CertificateSearchTemplate", aEx);
        }
        mIssuer = issuer;
    }

    public void setIssuer(EName aIssuer)
    {
        mIssuer = CertStoreUtil.getNormalizeName(aIssuer.getObject());
    }

    public void setStartDate(java.util.Date aStartDate)
    {
        mStartDate = new Date(aStartDate.getTime());
    }

    public void setEndDate(java.util.Date aEndDate)
    {
        mEndDate = new Date(aEndDate.getTime());
    }


    public void setSubject(byte[] aSubject)
    {
        byte[] subject = null;
        try
        {
            subject = CertStoreUtil.normalizeName(UtilName.byte2Name(aSubject));
        }
        catch(Exception aEx)
        {
            //TODO
            //throw new CertStoreException("Subject degeri normalize edilirken hata olustu.",aEx);
            logger.warn("Warning in CertificateSearchTemplate", aEx);
        }

        mSubject = subject;
    }

    public void setSubject(EName aSubject)
    {
        mSubject = CertStoreUtil.getNormalizeName(aSubject.getObject());
    }

    public void setSerial(byte[] aSerialNumber)
    {
        mSerialNumber = aSerialNumber;
    }

    public void setSerial(Asn1BigInteger aSerialNumber)
    {
        mSerialNumber = aSerialNumber.value.toByteArray();
    }


    public void setAnahtarKullanimi(KeyUsageSearchTemplate aAnahtarKullanim)
    {
        mAnahtarKullanim = aAnahtarKullanim;
    }

    public void setSubjectKeyID(byte[] aSubjectKeyID)
    {
        mSubjectKeyIdentifier = aSubjectKeyID;
    }

    public void setEPosta(String aEPosta)
    {
        mEPosta = aEPosta;
    }

    public void setDizin(DepoDizin aDizin)
    {
        mDizinNo = aDizin.getDizinNo();
    }

    public void setDizin(Long aDizinNo)
    {
        mDizinNo = aDizinNo;
    }

    public void setX400Adress(String aX400Address)
    {
        mX400Adress = aX400Address;
    }

    public byte[] getValue()
    {
        return mValue;
    }

    public byte[] getHash()
    {
        return mHash;
    }

    public OzetTipi getHashType()
    {
        return mHashType;
    }

    public byte[] getSerialNumber()
    {
        return mSerialNumber;
    }

    public byte[] getIssuer()
    {
        return mIssuer;
    }

    public Date getStartDate()
    {
        return mStartDate;
    }

    public Date getEndDate()
    {
        return mEndDate;
    }

    public byte[] getSubject()
    {
        return mSubject;
    }

    public KeyUsageSearchTemplate getAnahtarKullanim()
    {
        return mAnahtarKullanim;
    }

    public String getEPosta()
    {
        return mEPosta;
    }

    public byte[] getSubjectKeyID()
    {
        return mSubjectKeyIdentifier;
    }

    public Long getDizinNo()
    {
        return mDizinNo;
    }

    public String getX400Address()
    {
        return mX400Adress;
    }

}

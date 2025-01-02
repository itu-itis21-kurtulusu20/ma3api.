package tr.gov.tubitak.uekae.esya.api.certificate.validation;

import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.PathValidationResult;

import java.io.Serializable;
import java.util.List;

/**
 * Stores validation result of a certificate path 
 */
public class PathValidationRecord implements Serializable
{
    private List<ECertificate> mPath;
    private int mErrorIndex;
    PathValidationResult mResultCode;
    //private int mResultCode;


    public PathValidationRecord()
    {
        mErrorIndex = -1;
        mResultCode = PathValidationResult.SUCCESS;
    }

    public PathValidationRecord(List<ECertificate> aPath, int aErrorIndex, PathValidationResult aResultCode)
    {
        mPath = aPath;
        mErrorIndex = aErrorIndex;
        mResultCode = aResultCode;
    }


    public List<ECertificate> getPath()
    {
        return mPath;
    }

    public int getErrorIndex()
    {
        return mErrorIndex;
    }

    public PathValidationResult getResultCode()
    {
        return mResultCode;
    }

    public void setPath(List<ECertificate> aPath)
    {
        mPath = aPath;
    }

    public void addCert(ECertificate aISN)
    {
        mPath.add(aISN);
    }

    public void setErrorIndex(int iErrorIndex)
    {
        mErrorIndex = iErrorIndex;
    }

    public void setResultCode(PathValidationResult iResultCode)
    {
        mResultCode = iResultCode;
    }

}

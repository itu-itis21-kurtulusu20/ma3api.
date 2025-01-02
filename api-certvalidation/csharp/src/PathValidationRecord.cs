using System;
using System.Collections.Generic;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.check;

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation
{
    /**
     * Stores validation result of a certificate path 
     */
    [Serializable]
    public class PathValidationRecord
    {
        private List<ECertificate> mPath;
        private int mErrorIndex;
        PathValidationResult mResultCode;
        //private int mResultCode;

        
        public PathValidationRecord() {
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
            mPath.Add(aISN);
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
}

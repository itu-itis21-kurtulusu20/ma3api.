using System;

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.revocation
{
    /**
     * <p>RevocationStatusInfo is the structure for storing the result of
     * Certificate Revocation Control. 
     *
     * @author IH
     */
    [Serializable]
    public class RevocationStatusInfo: StatusInfo
    {
        private RevocationStatus mRevocationStatus;
        private int mRevocationCheckPlace;
        private int mRevocationCause;
        private DateTime? mRevocationDate;

        public RevocationStatusInfo()
        {

        }

        public RevocationStatus getRevocationStatus()
        {
            return mRevocationStatus;
        }

        public void setRevocationStatus(RevocationStatus aRevocationStatus)
        {
            mRevocationStatus = aRevocationStatus;
        }

        public int getRevocationCheckPlace()
        {
            return mRevocationCheckPlace;
        }

        public void setRevocationCheckPlace(int iptalKontroluYeri)
        {
            mRevocationCheckPlace = iptalKontroluYeri;
        }

        public int getRevocationCause()
        {
            return mRevocationCause;
        }

        public void setRevocationCause(int iptalNedeni)
        {
            mRevocationCause = iptalNedeni;
        }

        public DateTime? getRevocationDate()
        {
            return mRevocationDate;
        }

        public void setRevocationDate(DateTime? iptalTarihi)
        {
            mRevocationDate = iptalTarihi;
        }
    }
}

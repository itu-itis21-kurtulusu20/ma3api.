package tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.revocation;

import tr.gov.tubitak.uekae.esya.api.certificate.validation.StatusInfo;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>RevocationStatusInfo is the structure for storing the result of
 * Certificate Revocation Control. 
 *
 * @author IH
 */
public class RevocationStatusInfo extends StatusInfo implements Serializable
{
    private RevocationStatus mRevocationStatus;
    private int mRevocationCheckPlace;
    private int mRevocationCause;
    private Date mRevocationDate;

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

    public Date getRevocationDate()
    {
        return mRevocationDate;
    }

    public void setRevocationDate(Date iptalTarihi)
    {
        mRevocationDate = iptalTarihi;
    }

}

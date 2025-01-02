package tr.gov.tubitak.uekae.esya.api.infra.certstore.model;

import java.sql.Date;

/**
 * Created by IntelliJ IDEA.
 * User: bilen.ogretmen
 * Date: 7/7/11
 * Time: 3:38 PM
 */
public class DepoSertifikaOcsps {

    private Long mOcspNo;
    private Long mSertifikaNo;
    private Date mThisUpdate;
    private Long mStatus;
    private Date mRevocationTime;
    private Long mRevocationReason;

    public Long getOcspNo() {
        return mOcspNo;
    }

    public void setOcspNo(Long aOcspNo) {
        mOcspNo = aOcspNo;
    }

    public Long getSertifikaNo() {
        return mSertifikaNo;
    }

    public void setSertifikaNo(Long aSertifikaNo) {
        mSertifikaNo = aSertifikaNo;
    }

    public Date getThisUpdate() {
        return mThisUpdate;
    }

    public void setThisUpdate(Date aThisUpdate) {
        mThisUpdate = aThisUpdate;
    }

    public Long getStatus() {
        return mStatus;
    }

    public void setStatus(Long aStatus) {
        mStatus = aStatus;
    }

    public Date getRevocationTime() {
        return mRevocationTime;
    }

    public void setRevocationTime(Date aRevocationTime) {
        mRevocationTime = aRevocationTime;
    }

    public Long getRevocationReason() {
        return mRevocationReason;
    }

    public void setRevocationReason(Long aRevocationReason) {
        mRevocationReason = aRevocationReason;
    }
}

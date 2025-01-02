package tr.gov.tubitak.uekae.esya.cmpapi.esya;

import tr.gov.tubitak.uekae.esya.api.asn.cmp.ECertStatus;
import tr.gov.tubitak.uekae.esya.cmpapi.base20.common.ICertificationAcceptanceStrategy;
import tr.gov.tubitak.uekae.esya.cmpapi.base20.common.ICertificationParam;
import tr.gov.tubitak.uekae.esya.cmpapi.base20.util.UtilCmp;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zeldal.ozdemir
 * Date: 1/18/11
 * Time: 4:25 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class BaseCertificationAcceptanceStrategy implements ICertificationAcceptanceStrategy {
    public boolean handleAllCertificatesTogether() {
        return true;
    }

    public List<ECertStatus> acceptCertificates(List<ICertificationParam> certificationResults) {
        acceptAllCertificates(certificationResults);
        return UtilCmp.createSuccesfullCertificationStatuses(certificationResults);
    }

    public abstract void acceptAllCertificates(List<ICertificationParam> certificationResults) ;


    public void rollbackCertificates(List<ICertificationParam> certificationResult) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}

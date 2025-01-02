package tr.gov.tubitak.uekae.esya.cmpapi.base20.content;

import com.objsys.asn1j.runtime.Asn1BigInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.cmp.EPKIFailureInfo;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECRLReason;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EExtension;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.util.bag.Pair;
import tr.gov.tubitak.uekae.esya.asn.cmp.PKIStatusInfo;
import tr.gov.tubitak.uekae.esya.asn.cmp.RevDetails;
import tr.gov.tubitak.uekae.esya.asn.crmf.CertId;
import tr.gov.tubitak.uekae.esya.asn.x509.Extension;
import tr.gov.tubitak.uekae.esya.asn.x509.Extensions;
import tr.gov.tubitak.uekae.esya.cmpapi.base20.common.CMPProtocolException;
import tr.gov.tubitak.uekae.esya.cmpapi.base20.common.IRevocationParam;

import java.util.Arrays;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zeldal.ozdemir
 * Date: Dec 9, 2010
 * Time: 4:14:54 PM
 * To change this template use File | Settings | File Templates.
 */

public class RevocationParamWithSerial implements IRevocationParam {
    private static final Logger logger = LoggerFactory.getLogger(RevocationParamWithSerial.class);

    Asn1BigInteger certSerial;
    private ECRLReason crlReason;
    private RevDetails revDetails;
    private PKIStatusInfo pkiStatusInfo;

    public RevocationParamWithSerial(Asn1BigInteger certSerial, ECRLReason crlReason) {
        this.certSerial = certSerial;
        this.crlReason = crlReason;
    }

    public void addSpecificOperations(RevDetails revDetails) {
        revDetails.certDetails.serialNumber = certSerial;
        if (crlReason != null)
            try {
                EExtension crlReasonExt = crlReason.toExtension(false);//UtilExtensions.extensionFromValue(_ImplicitValues.id_ce_cRLReasons, false, crlReason);
                if (revDetails.crlEntryDetails == null) {
                    revDetails.crlEntryDetails = new Extensions(new Extension[]{crlReasonExt.getObject()});
                } else {
                    List<Extension> extensions = Arrays.asList(revDetails.crlEntryDetails.elements);
                    extensions.add(crlReasonExt.getObject());
                    revDetails.crlEntryDetails = new Extensions((Extension[]) extensions.toArray());
                }
            } catch (ESYAException e) {
                throw new RuntimeException("Error while adding CRLReason to crlEntryDetails:"+e.getMessage(),e);
            }
    }

    public void setRevDetails(RevDetails revDetails) {
        this.revDetails = revDetails;
    }

    public RevDetails getRevDetails() {
        return revDetails;
    }

    public void extractResponse(Pair<CertId, PKIStatusInfo> revokedCerts) throws CMPProtocolException {
        CertId certId = revokedCerts.getObject1();
        PKIStatusInfo pkiStatusInfo = revokedCerts.getObject2();
        if( ! certId.serialNumber.equals(certSerial) )
            throw new CMPProtocolException(EPKIFailureInfo.badCertId,
                    "Unexpected CertID or order, expected:"+certSerial.value
                            +" Found:"+certId.serialNumber.value);
        if(pkiStatusInfo == null ){
            logger.warn("PKIStatusInfo is empty, assuming its accepted");
        } else
            this.pkiStatusInfo = revokedCerts.getObject2();
    }

    public PKIStatusInfo getPkiStatusInfo() {
        return pkiStatusInfo;
    }
}

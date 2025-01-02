package tr.gov.tubitak.uekae.esya.cmpapi.base.protection;

import com.objsys.asn1j.runtime.Asn1ObjectIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.cmp.EPBMParameter;
import tr.gov.tubitak.uekae.esya.api.asn.cmp.EPKIMessage;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.cmp.PKIMessage;
import tr.gov.tubitak.uekae.esya.asn.cmp._cmpValues;
import tr.gov.tubitak.uekae.esya.cmpapi.base.common.IConfigType;
import tr.gov.tubitak.uekae.esya.cmpapi.base.util.CMPPBMac;
import tr.gov.tubitak.uekae.esya.cmpapi.base.util.UtilCmp;

/**
 * Created by IntelliJ IDEA.
 * User: zeldal.ozdemir
 * Date: Dec 3, 2010
 * Time: 4:29:44 PM
 * To change this template use File | Settings | File Templates.
 */

public class ProtectionControllerWithHMac<T extends IConfigType> implements IProtectionController<T> {
    private static final Logger logger = LoggerFactory.getLogger(ProtectionControllerWithHMac.class);
    private static final Asn1ObjectIdentifier PROTECTIONOID = new Asn1ObjectIdentifier(_cmpValues.id_PasswordBasedMac);

    private IPBMParameterFinder pbmParameterFinder;

    public ProtectionControllerWithHMac(IPBMParameterFinder pbmParameterFinder) {
        this.pbmParameterFinder = pbmParameterFinder;
    }

    public boolean verifyProtection(EPKIMessage message) {
        try {
            verifyProtectionImpl(message.getObject());
        } catch (Exception e) {
            logger.error("Protection Failed:" + e.getMessage(),e);
            return false;
        }
        return true;
    }

    private void verifyProtectionImpl(PKIMessage message) throws ESYAException {
        logger.debug("Protection kontrolu yapiliyor");
        if ((message.header == null)
                || (message.header.protectionAlg == null)
                || (message.header.protectionAlg.algorithm == null)
                )
            throw new ESYAException("Protection algorithm is Empty!");

        if (!message.header.protectionAlg.algorithm.equals(PROTECTIONOID))
            throw new ESYAException("Protection algorithm must be " + PROTECTIONOID);


        EPBMParameter params = pbmParameterFinder.getPbmParameter(message);
        CMPPBMac pbmac = new CMPPBMac(params, pbmParameterFinder.getHMacCode());
        boolean result = pbmac.pbmacKontrol(UtilCmp.getSigningData(message.header, message.body), message.protection.value);
        if (result) {
            logger.info("Protection is successfully satisfied");
            return;
        } else {
            throw new ESYAException("Message Protection failed: Message altered.");
        }
    }
}

package tr.gov.tubitak.uekae.esya.cmpapi.base20.protection;

import com.objsys.asn1j.runtime.Asn1DerDecodeBuffer;
import tr.gov.tubitak.uekae.esya.api.asn.cmp.EPBMParameter;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException;
import tr.gov.tubitak.uekae.esya.asn.cmp.PBMParameter;
import tr.gov.tubitak.uekae.esya.asn.cmp.PKIMessage;
import tr.gov.tubitak.uekae.esya.cmpapi.base20.util.CMPPBMac;

/**
 * Created by IntelliJ IDEA.
 * User: zeldal.ozdemir
 * Date: Dec 3, 2010
 * Time: 11:20:36 AM
 * To change this template use File | Settings | File Templates.
 */

public class PBMParameterFinder implements IPBMParameterFinder {
    private PBMParameter incomingPbmParameter;
    private PBMParameter existingPbmParameter;
    private String HMacCode;

    public PBMParameterFinder(String hMacCode) {
        HMacCode = hMacCode;
    }

    public EPBMParameter getPbmParameter(PKIMessage message) {
      //  if (incomingPbmParameter == null) {
            incomingPbmParameter = new PBMParameter();
            try {
                Asn1DerDecodeBuffer decBuf = new Asn1DerDecodeBuffer(message.header.protectionAlg.parameters.value);
                incomingPbmParameter.decode(decBuf);
            } catch (Exception e) {
                throw new RuntimeException("No PBMParameter is presented in PKIMessage.header.protectionAlg.parameters");
            }
        //}
        return new EPBMParameter(incomingPbmParameter);
    }

    public EPBMParameter getPBMParameter() {
        if(existingPbmParameter == null)
            try {
                existingPbmParameter = new CMPPBMac("").getMParams();
            } catch (CryptoException e) {
                throw new RuntimeException("Error while creating CMPPBMac");
            }

        return new EPBMParameter(existingPbmParameter);
    }

    public String getHMacCode() {
        return HMacCode;
    }
}

package tr.gov.tubitak.uekae.esya.cmpapi.base.content;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.common.ESYARuntimeException;
import tr.gov.tubitak.uekae.esya.api.common.util.bag.Pair;
import tr.gov.tubitak.uekae.esya.asn.cmp.*;
import tr.gov.tubitak.uekae.esya.asn.crmf.*;
import tr.gov.tubitak.uekae.esya.asn.util.AsnIO;
import tr.gov.tubitak.uekae.esya.cmpapi.base.common.*;
import tr.gov.tubitak.uekae.esya.cmpapi.base.util.UtilCmp;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: zeldal.ozdemir
 * Date: Dec 7, 2010
 * Time: 4:40:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class RevocationBuilder {
    private static final Logger logger = LoggerFactory.getLogger(RevocationBuilder.class);

    private ProtocolType protocolType;
    private List<IRevocationParam> revocationParams = new ArrayList<IRevocationParam>();


    public RevocationBuilder(ProtocolType protocolType) {
        this.protocolType = protocolType;
        if (protocolType != ProtocolType.REVOCATIONPROTOCOL)
            throw new ESYARuntimeException("RevocationBuilder cannot be created for protocol:" + protocolType.getProtocolName());
    }

    public void addRevDetails(IRevocationParam revocationParam) {
        CertTemplate certTemplate = new CertTemplate(null, //version
                null, //serialnumber
                null, //signingAlg
                null, //issuer
                null, //validity
                null, //subject
                null, //publickey
                null, //issuerUID
                null, //subjectUID
                null //extensions
        );
        RevDetails revDetails = new RevDetails(certTemplate);

        revocationParam.addSpecificOperations(revDetails);
        revocationParam.setRevDetails(revDetails);
        revocationParams.add(revocationParam);
    }

    public RevDetails[] getRevDetailses() {
        int i = 0;
        RevDetails[] revDetailses = new RevDetails[revocationParams.size()];
        for (IRevocationParam revocationParam : revocationParams)
            revDetailses[i++] = revocationParam.getRevDetails();
        return revDetailses;
    }

    public PKIBody createRevReqBody(RevDetails[] revDetailses) {
        PKIMessageType.IRequestType reqType = protocolType.getReqType();
        if (!(reqType instanceof PKIMessageType.RevReqType))
            throw new ESYARuntimeException("RevReqBody cannot be created for protocol:" + protocolType.getProtocolName());

        RevReqContent revReqContent = new RevReqContent(revDetailses);
        return new PKIBody(reqType.getChoice(), revReqContent);
    }

    public List<IRevocationParam> extractResponse(PKIBody responseBody) throws CMPProtocolException {
        List<Pair<CertId, PKIStatusInfo>> revokedCerts = verifyRevRepMessage(responseBody);
        for (int i = 0; i < revokedCerts.size(); i++)
              revocationParams.get(i).extractResponse(revokedCerts.get(i));

        return revocationParams;
/*                    if (mRecResponse.keyPairHist != null) {
                CertifiedKeyPair[] liste = mRecResponse.keyPairHist.elements;
                sertifikalar = new Certificate[liste.length];
                for (int i = 0; i < liste.length; i++) {
                    if (liste[i].certOrEncCert == null) {
                        throw new ESYAException("Gelen cevapta " + i + ". sertifika boş");
                    } else if (liste[i].privateKey == null) {
                        throw new ESYAException("Gelen cevapta " + i + ". private key boş");
                    }
                    certificate = liste[i].certOrEncCert;
                    sertifikalar[i] = _getCertificate(certificate, null);
                }
            } else {
                throw new ESYAException("Gelen cevapta keyPairHist alanı yok");
            }*/


    }
     public List<Pair<CertId, PKIStatusInfo>> verifyRevRepMessage(PKIBody responseBody) throws CMPProtocolException {
         PKIMessageType.IResponseType resType = protocolType.getResType();
         if (!(resType instanceof PKIMessageType.RevResType))
             throw new ESYARuntimeException("RevRepBody cannot be verified for protocol:" + protocolType.getProtocolName());

         PKIMessageType pkiMessageType = PKIMessageType.getPKIMessageType(responseBody.getChoiceID());
         if (pkiMessageType != resType)
             throw new CMPProtocolException(PKIFailureInfo.incorrectData, "Invalid PKI Body, must be " + resType + ", But it is:" + pkiMessageType);
         RevRepContent revRepContent = (RevRepContent) responseBody.getElement();
         
         if(revRepContent.status.elements.length != revocationParams.size())
          throw new CMPProtocolException(PKIFailureInfo.incorrectData, "Invalid Number of Status in Response, Expected: " 
                  + revocationParams.size() + ", Found:" + revRepContent.revCerts.elements.length);

         List<Pair<CertId,PKIStatusInfo>> revokedCerts = new ArrayList<Pair<CertId, PKIStatusInfo>>();
         for (int i = 0; i < revRepContent.revCerts.elements.length; i++) {
             CertId certId = revRepContent.revCerts.elements[i];
             PKIStatusInfo statusInfo = revRepContent.status.elements[i];
             revokedCerts.add(new Pair<CertId, PKIStatusInfo>(certId, statusInfo));
         }
         return revokedCerts;
     }

}

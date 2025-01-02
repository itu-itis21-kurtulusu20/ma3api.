package tr.gov.tubitak.uekae.esya.api.asn.cmp;

import com.objsys.asn1j.runtime.Asn1BigInteger;
import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EGeneralName;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.util.bag.Triple;
import tr.gov.tubitak.uekae.esya.asn.cmp.PKIStatusInfo;
import tr.gov.tubitak.uekae.esya.asn.cmp.RevRepContent;
import tr.gov.tubitak.uekae.esya.asn.cmp.RevRepContent_revCerts;
import tr.gov.tubitak.uekae.esya.asn.cmp.RevRepContent_status;
import tr.gov.tubitak.uekae.esya.asn.crmf.CertId;

import java.math.BigInteger;

/**
 * User: zeldal.ozdemir
 * Date: 2/4/11
 * Time: 2:24 PM
 */
public class ERevRepContent extends BaseASNWrapper<RevRepContent>{
    public ERevRepContent(RevRepContent aObject) {
        super(aObject);
    }

    public ERevRepContent(byte[] aBytes) throws ESYAException {
        super(aBytes, new RevRepContent());
    }

    public ERevRepContent( Triple<EPKIStatusInfo, EGeneralName, BigInteger>[] revContents) {
        super(new RevRepContent());
        PKIStatusInfo[] pkiStatusInfos = new PKIStatusInfo[revContents.length];
        CertId[] certIds = new CertId[revContents.length];
        for (int i = 0; i < revContents.length; i++) {
            Triple<EPKIStatusInfo, EGeneralName, BigInteger> revContent = revContents[i];
            pkiStatusInfos[i] = revContent.getmNesne1().getObject();
            certIds[i] = new CertId(revContent.getmNesne2().getObject(), new Asn1BigInteger(revContent.getmNesne3()));
        }
        mObject.status = new RevRepContent_status(pkiStatusInfos);
        mObject.revCerts = new RevRepContent_revCerts(certIds);
    }
}

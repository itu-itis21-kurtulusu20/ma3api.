package tr.gov.tubitak.uekae.esya.cmpapi.base20.common;

import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.common.util.bag.Pair;
import tr.gov.tubitak.uekae.esya.asn.cmp.CertifiedKeyPair;
import tr.gov.tubitak.uekae.esya.asn.crmf.CertReqMsg;

import java.security.PrivateKey;
import java.util.List;

/**
 * <b>Author</b>    : zeldal.ozdemir <br>
 * <b>Project</b>   : MA3   <br>
 * <b>Copyright</b> : TUBITAK Copyright (c) 2006-2011 <br>
 * <b>Date</b>: Dec 7, 2010 - 4:30:26 PM <p>
 * <b>Description</b>: <br>
 * Parameters and extractors for KeyRacovery Based protocols
 */

public interface IKeyRecoveryParam {

    void addSpecificOperations(CertReqMsg certReqMsg);

    void setCertReq(CertReqMsg certReqMsg);

    CertReqMsg getCertReqMsg();

    List<Pair<ECertificate, PrivateKey>> extractResponse(CertifiedKeyPair[] certifiedKeyPairs) throws CMPProtocolException;

    List<Pair<ECertificate, PrivateKey>> getCertAndPrivKeys();
}

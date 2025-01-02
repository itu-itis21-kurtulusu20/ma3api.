package tr.gov.tubitak.uekae.esya.cmpapi.base20.common;

import tr.gov.tubitak.uekae.esya.api.asn.cmp.ECertResponse;
import tr.gov.tubitak.uekae.esya.api.asn.cmp.ECertStatus;
import tr.gov.tubitak.uekae.esya.api.asn.crmf.ECertReqMsg;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EName;
import tr.gov.tubitak.uekae.esya.asn.cmp.CertResponse;

/**
 * <b>Author</b>    : zeldal.ozdemir <br>
 * <b>Project</b>   : MA3   <br>
 * <b>Copyright</b> : TUBITAK Copyright (c) 2006-2011 <br>
 * <b>Date</b>: Nov 11, 2010 - 2:58:21 PM <p>
 * <b>Description</b>: <br>
 * Parameter and extractor signature for Certification based protocols.
 * it mey differs with Key Generation type or ConfigType(Esya-Mobil etc.)
 */

public interface ICertificationParam {
    
    EName getSender();

    void addSpecificOperations(ECertReqMsg certReqMsg) ;

    void setCertReq(ECertReqMsg certReqMsg);

    ECertReqMsg getCertReqMsg();

    void extractResponse(ECertResponse certResponse) throws CMPProtocolException;

    byte[] getCertificateEncoded();

    boolean isWaiting();

    boolean canBeAccept();

    void setAcceptanceStatus(ECertStatus acceptanceStatus);

    ECertStatus getAcceptanceStatus();

    long getCertReqId();

    void setHandled(boolean handled);

    ECertResponse getCertResponse();

    void setCertRep(ECertResponse certResponse);
}

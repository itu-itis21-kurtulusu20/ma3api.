package tr.gov.tubitak.uekae.esya.cmpapi.base20.common;

import tr.gov.tubitak.uekae.esya.api.asn.cmp.ECertResponse;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.asn.cmp.CertOrEncCert;

/**
 * <b>Author</b>    : zeldal.ozdemir <br>
 * <b>Project</b>   : MA3   <br>
 * <b>Copyright</b> : TUBITAK Copyright (c) 2006-2011 <br>
 * <b>Date</b>: Dec 7, 2010 - 9:55:24 AM <p>
 * <b>Description</b>: <br>
 * Parameter and extractor interface for PKCS10 based protocols.
 *
 * @see tr.gov.tubitak.uekae.esya.cmpapi.base20.BasePKCS10Protocol
 */

public interface IPKCS10Param {

    byte[] getPkcs10Request();

    ECertificate extractCertificate(CertOrEncCert certOrEncCert) throws CMPProtocolException;

    public ECertResponse getCertResponse();
    public void setCertResponse(ECertResponse eCertResponse);
}

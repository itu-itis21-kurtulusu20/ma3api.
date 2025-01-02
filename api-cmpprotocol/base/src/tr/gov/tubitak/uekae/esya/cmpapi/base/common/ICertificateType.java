package tr.gov.tubitak.uekae.esya.cmpapi.base.common;

import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.asn.cmp.CertOrEncCert;
import tr.gov.tubitak.uekae.esya.asn.crmf.CertReqMsg;

/**
 * <b>Author</b>    : zeldal.ozdemir <br>
 * <b>Project</b>   : MA3   <br>
 * <b>Copyright</b> : TUBITAK Copyright (c) 2006-2011 <br>
 * <b>Date</b>: Nov 11, 2010 - 3:00:16 PM <p>
 * <b>Description</b>: <br>
 * This template is used for Certification types according to Key Types.
 * since we need different operation for Encr or Sign Certificates, other types can be presented later.
 */

public interface ICertificateType {
    
    void addSpecificOperations(CertReqMsg certReqMsg) ;

    ECertificate extractCertificate(CertOrEncCert certOrEncCert) throws CMPProtocolException;
}

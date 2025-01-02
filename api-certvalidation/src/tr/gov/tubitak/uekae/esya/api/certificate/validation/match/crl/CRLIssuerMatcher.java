package tr.gov.tubitak.uekae.esya.api.certificate.validation.match.crl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.x509.*;

import java.util.Arrays;

/**
 * Matches a CRL and a certificate according to the Issuer fields of the crl and
 * the certificate. It also checks CDP extension of the certificate and matches
 * CRLIssuer of the CDP extension with Issuer field of the crl 
 */
public class CRLIssuerMatcher extends CRLMatcher {
    
    private static final Logger logger = LoggerFactory.getLogger(CRLIssuerMatcher.class);

    /**
     * Sertifika-SİL issuer eşleştirmesini yapar
     */
    protected boolean _matchCRL(ECertificate aCertificate, ECRL aCRL) {
        EName crlIssuer;
        ECRLDistributionPoints cdps = aCertificate.getExtensions().getCRLDistributionPoints();
        if (cdps != null) {
            logger.debug("Sertifikada CRL Distribution Point uzantısı var");
            for (int i = 0; i < cdps.getCRLDistributionPointCount(); i++) {
                ECRLDistributionPoint cdp = cdps.getCRLDistributionPoint(i);
                if (cdp.getCRLIssuer() != null) {

                    if (!aCRL.isIndirectCRL())    // crlIssuer alanı varsa indirect CRL olmalı
                        return false;            // RFC5280 4.2.1.13.  CRL Distribution Points Page 45

                    // CRLIssuer varsa CRL in issueri ile crlIssuer aynı olmalı
                    // ve Silde  IndirecCRL alani true set edilmiş bir IDP olmali
                    crlIssuer = cdp.getCRLIssuer();
                    return crlIssuer != null && cdp.getAddressType() == AddressType.DN && crlIssuer.equals(aCRL.getIssuer());
                }
            }
        }

        // CDP bulunamadı veya CDP de crlIssuer bulunamadı o zman SİL Issuer ile sertifika issuer aynı olmalı
        if (aCertificate.getIssuer().equals(aCRL.getIssuer())) {
            if (!aCertificate.isSelfIssued())
                return true;
            else {
                EAuthorityKeyIdentifier aki = aCRL.getCRLExtensions().getAuthorityKeyIdentifier();
                ESubjectKeyIdentifier ski = aCertificate.getExtensions().getSubjectKeyIdentifier();
                if (aki != null && ski != null && Arrays.equals(aki.getKeyIdentifier(), ski.getValue()))
                    return false; // kendisinin yayınladığı sil
                
                return true;
            }
        }

        return false;
    }

}

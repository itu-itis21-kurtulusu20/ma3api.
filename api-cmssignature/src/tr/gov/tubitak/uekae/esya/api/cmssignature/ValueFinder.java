package tr.gov.tubitak.uekae.esya.api.cmssignature;

import tr.gov.tubitak.uekae.esya.api.asn.cms.ECertificateValues;
import tr.gov.tubitak.uekae.esya.api.asn.cms.ECompleteCertificateReferences;
import tr.gov.tubitak.uekae.esya.api.asn.cms.ECompleteRevocationReferences;
import tr.gov.tubitak.uekae.esya.api.asn.cms.ERevocationValues;

/**
 * Created with IntelliJ IDEA.
 * User: suleyman.uslu
 * Date: 04.12.2012
 * Time: 13:28
 * To change this template use File | Settings | File Templates.
 */
public interface ValueFinder {

    public ECertificateValues findCertValues(ECompleteCertificateReferences aRefs)
            throws CMSSignatureException;

    public ERevocationValues findRevocationValues(ECompleteRevocationReferences aRefs)
            throws CMSSignatureException;
}

package tr.gov.tubitak.uekae.esya.cmpapi.base20.common;

import tr.gov.tubitak.uekae.esya.api.asn.cmp.ECertStatus;

import java.util.List;

/**
 * <b>Author</b>    : zeldal.ozdemir <br>
 * <b>Project</b>   : MA3   <br>
 * <b>Copyright</b> : TUBITAK Copyright (c) 2006-2011 <br>
 * <b>Date</b>: Dec 1, 2010 - 3:03:57 PM <p>
 * <b>Description</b>: <br>
 *     this strategy can be used by Certification Based Protocols. so User can check certificates before
 *     protocol finishes
 */

public interface ICertificationAcceptanceStrategy {

    public boolean handleAllCertificatesTogether();

    public List<ECertStatus> acceptCertificates(List<ICertificationParam> certificationResults);

    void rollbackCertificates(List<ICertificationParam> certificationResult);
}

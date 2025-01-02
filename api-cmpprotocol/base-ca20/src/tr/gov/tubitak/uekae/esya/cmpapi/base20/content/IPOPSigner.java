package tr.gov.tubitak.uekae.esya.cmpapi.base20.content;

import tr.gov.tubitak.uekae.esya.asn.crmf.CertRequest;
import tr.gov.tubitak.uekae.esya.asn.crmf.ProofOfPossession;
import tr.gov.tubitak.uekae.esya.cmpapi.base20.common.CMPProtocolException;

/**
 * <b>Author</b>    : zeldal.ozdemir <br>
 * <b>Project</b>   : MA3   <br>
 * <b>Copyright</b> : TUBITAK Copyright (c) 2006-2011 <br>
 * <b>Date</b>: Nov 8, 2010 - 9:10:21 AM <p>
 * <b>Description</b>: <br>
 *     POP (Proof of Possesion) Signer for Certification Request Messages
 *     (KeyGeneration on Client)
 */
public interface IPOPSigner
{
	ProofOfPossession popOlustur(CertRequest aIstek) throws CMPProtocolException;
}

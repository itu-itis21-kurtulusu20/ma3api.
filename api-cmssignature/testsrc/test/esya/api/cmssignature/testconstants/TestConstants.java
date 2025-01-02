package test.esya.api.cmssignature.testconstants;

import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.policy.ValidationPolicy;
import tr.gov.tubitak.uekae.esya.api.cmssignature.ISignable;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.crypto.BaseSigner;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.params.AlgorithmParams;
import tr.gov.tubitak.uekae.esya.api.infra.tsclient.TSSettings;

import java.io.FileNotFoundException;
import java.security.PrivateKey;

public interface TestConstants 
{
	BaseSigner getSignerInterface(SignatureAlg aAlg) throws Exception;

	BaseSigner getSignerInterface(SignatureAlg aAlg, AlgorithmParams algorithmParams) throws Exception;

	BaseSigner getSecondSignerInterface(SignatureAlg aAlg) throws Exception;

	BaseSigner getSecondSignerInterface(SignatureAlg aAlg, AlgorithmParams algorithmParams) throws Exception;

	BaseSigner getECSignerInterface(SignatureAlg aAlg) throws Exception;

	ECertificate getSignerCertificate() throws Exception;

	PrivateKey getSignerPrivateKey() throws Exception;
	
	ECertificate getSecondSignerCertificate() throws Exception;

	ECertificate getECSignerCertificate() throws Exception;

	ValidationPolicy getPolicy() throws ESYAException, FileNotFoundException;
	
	ValidationPolicy getPolicyCRL() throws ESYAException, FileNotFoundException;
	
	ValidationPolicy getPolicyOCSP() throws ESYAException, FileNotFoundException;
	
	ValidationPolicy getPolicyNoOCSPNoCRL() throws ESYAException, FileNotFoundException;
	
	TSSettings getTSSettings();
	
	String getDirectory();
	
	ISignable getSimpleContent();
	
	ISignable getHugeContent();
}

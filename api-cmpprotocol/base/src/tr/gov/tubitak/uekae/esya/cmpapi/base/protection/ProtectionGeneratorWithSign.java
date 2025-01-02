package tr.gov.tubitak.uekae.esya.cmpapi.base.protection;

import tr.gov.tubitak.uekae.esya.api.asn.cmp.EPKIMessage;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EAlgorithmIdentifier;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.crypto.BaseSigner;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg;
import tr.gov.tubitak.uekae.esya.cmpapi.base.common.IConfigType;
import tr.gov.tubitak.uekae.esya.cmpapi.base.util.UtilCmp;

import java.security.spec.AlgorithmParameterSpec;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: ZELDAL  @ UEKAE - TUBITAK
 * Date: 21.May.2010
 * Time: 08:52:18
 * Description:
 */

public class ProtectionGeneratorWithSign<T extends IConfigType> implements IProtectionGenerator<T> {

	private BaseSigner baseSigner;
	private List<ECertificate> extraCertsForProtection = new ArrayList<ECertificate>();


	public ProtectionGeneratorWithSign(BaseSigner baseSigner) {
		this.baseSigner = baseSigner;
	}

	public ProtectionGeneratorWithSign(BaseSigner baseSigner, ECertificate signerCertificate) {
		this.baseSigner = baseSigner;
		this.addExtraCertificate(signerCertificate);
	}

	public EAlgorithmIdentifier getProtectionAlg() {
		SignatureAlg alg = SignatureAlg.fromName(baseSigner.getSignatureAlgorithmStr());
		AlgorithmParameterSpec spec = baseSigner.getAlgorithmParameterSpec();
		try {
			return alg.toAlgorithmIdentifierFromSpec(spec);
		} catch (ESYAException e) {
			throw new RuntimeException("Algoritma'dan AlgorithmIdentifier  elde edilemedi. Spec: " + spec.getClass().getName());
		}
	}


	public void addProtection(EPKIMessage message) {
		byte[] imzalanacak;
		byte[] imzali;
		imzalanacak = UtilCmp.getSigningData(message.getHeader().getObject(), message.getBody().getObject());
		try {
			imzali = baseSigner.sign(imzalanacak);
		} catch (ESYAException ex) {
			throw new RuntimeException("PKI mesaji imzalanamadi", ex);
		}
		message.setSignatureValue(imzali);
		message.addExtraCerts(extraCertsForProtection);
	}


	public void addExtraCertificate(ECertificate certificate){
		this.extraCertsForProtection.add(certificate);
	}

}

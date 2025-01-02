package tr.gov.tubitak.uekae.esya.api.asn.pkcs1pkcs8;

import java.math.BigInteger;


import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.pkcs1pkcs8.RSAPrivateKey;

public class ERSAPrivateKey extends BaseASNWrapper<RSAPrivateKey>{
	
	public ERSAPrivateKey(byte[] aBytes) throws ESYAException {
        super(aBytes, new RSAPrivateKey());
    }

	public BigInteger getPrivateExponent() {
		return mObject.privateExponent.value;
	}

	public BigInteger getModulus() {
		return mObject.modulus.value;
	}

	public BigInteger getPublicExponent() {
		return mObject.publicExponent.value;
	}

	public BigInteger getPrimeP() {
		return mObject.prime1.value;
	}

	public BigInteger getPrimeQ() {
		return mObject.prime2.value;
	}

	public BigInteger getPrimeExponentP() {
		return mObject.exponent1.value;
	}

	public BigInteger getPrimeExponentQ() {
		return mObject.exponent2.value;
	}

	public BigInteger getCrtCoefficient() {
		return mObject.coefficient.value;
	}
}

package tr.gov.tubitak.uekae.esya.api.infra.mobile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.cms.ESignerIdentifier;
import tr.gov.tubitak.uekae.esya.api.asn.cms.ESigningCertificateV2;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.EUserException;
import tr.gov.tubitak.uekae.esya.api.common.bundle.GenelDil;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg;
import tr.gov.tubitak.uekae.esya.asn.cms.SigningCertificate;

import java.security.spec.AlgorithmParameterSpec;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MultiMobileSigner
{
    int signatureCount;

    MobileSigner signer;
    boolean cancel;

    List<MultiSignResult> result = null;

    Object signOnceLock = new Object();
    Object requestCountLock = new Object();
    int requestCount = 0;

    byte[][] toBeSignedBytesList;
    String[] informativeTextList;

    Logger logger = LoggerFactory.getLogger(MultiMobileSigner.class);

    public MultiMobileSigner(MobileSigner signer, int signatureCount)
    {
        this.signer = signer;
        this.signatureCount = signatureCount;
        this.cancel = false;
        this.toBeSignedBytesList = new byte[signatureCount][];
        this.informativeTextList = new String[signatureCount];
    }

	public int getSignatureCount() {
		return signatureCount;
	}

	public byte[] sign(byte[] bytes, String informativeText, int index) throws ESYAException
    {
        synchronized (requestCountLock)
        {
        	toBeSignedBytesList[index] = bytes;
            informativeTextList[index] = informativeText;
            requestCount++;
        }
        try
        {
            while (requestCount < signatureCount && cancel == false)
                Thread.currentThread().sleep(200);

            if(cancel == true)
                throw new ESYAException("User cancelled operation!");

            synchronized (signOnceLock)
            {
               if(result == null)
               {
            	   ArrayList<byte []> toBeSignedBytes = new ArrayList<byte []> (Arrays.asList(toBeSignedBytesList));
            	   ArrayList<String> informativeTexts = new ArrayList<String> (Arrays.asList(informativeTextList));
                   result = signer.sign(toBeSignedBytes, informativeTexts);
               }
            }

            MultiSignResult multiSignResult = result.get(index);

            Status status = multiSignResult.getStatus();
            if (!Status.REQUEST_OK.isStatusCodeEquals(status)) {
                // todo get transaction ID
                // logger.debug("Status is not OK: {}. TransID: {}", status, response.getTransId()); // replace: response -> multiSignResult
                logger.debug("Status is not OK: {}.", status);

                // if signature request is cancelled by user
                if (Status.USER_CANCEL.isStatusCodeEquals(status)) {
                    throw new EUserException(EUserException.USER_CANCELLED, GenelDil.mesaj(GenelDil.MOBIL_IMZA_KULLANICI_IPTALI));
                }
                // if transaction expired (or "timed out")
                else if (Status.EXPIRED_TRANSACTION.isStatusCodeEquals(status)) {
                    throw new EUserException(EUserException.TIMEOUT, GenelDil.mesaj(GenelDil.MOBIL_IMZA_ZAMAN_ASIMI));
                }
            }

           byte[] signature = multiSignResult.getSignature();

           if(signature == null) {
                  String errorMsg = "Signature is not available for :"+ multiSignResult.getInformativeText();
                  logger.error(errorMsg);
                  throw new InterruptedException(errorMsg);
           }
           else{
                  return signature;
          }

        }
        catch (InterruptedException ex)
        {
        	throw new ESYAException(ex);
        }
    }

    public void cancel()
    {
        this.cancel = true;
    }

    public MobileSigner getMobileSigner()
	{
		return signer;
	}

    public static class MultiMobileSignerForOne extends IMobileSigner
    {
        MultiMobileSigner multiMobileSigner;
        int index;

        public MultiMobileSignerForOne(MultiMobileSigner multiMobileSigner, String informativeText, int index)
        {
            super(informativeText);
            this.multiMobileSigner = multiMobileSigner;
            this.index = index;
        }


		@Override
		public AlgorithmParameterSpec getAlgorithmParameterSpec() {
			return multiMobileSigner.getMobileSigner().getAlgorithmParameterSpec();
		}

		@Override
		public String getSignatureAlgorithmStr() {
			return multiMobileSigner.getMobileSigner().getSignatureAlgorithmStr();
		}

        @Override
        public byte[] sign(byte[] bytes) throws ESYAException {
            DigestAlg digestAlg = SignatureAlg.fromName(getSignatureAlgorithmStr()).getDigestAlg();
            calculateFingerPrint(digestAlg, bytes);
            return multiMobileSigner.sign(bytes, informativeText, index);
        }

		@Override
		public ESignerIdentifier getSignerIdentifier() {
			return multiMobileSigner.getMobileSigner().getSignerIdentifier();
		}

		@Override
		public DigestAlg getDigestAlg() {
			return multiMobileSigner.getMobileSigner().getDigestAlg();
		}

		@Override
		public SigningCertificate getSigningCertAttr() {
			return multiMobileSigner.getMobileSigner().getSigningCertAttr();
		}

		@Override
		public ESigningCertificateV2 getSigningCertAttrv2() {
			return multiMobileSigner.getMobileSigner().getSigningCertAttrv2();
		}

		@Override
		public ECertificate getSigningCert() {
			return multiMobileSigner.getMobileSigner().getSigningCert();
		}
    }
}

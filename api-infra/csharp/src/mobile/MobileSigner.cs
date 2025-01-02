using System;
using System.Collections.Generic;
using tr.gov.tubitak.uekae.esya.api.asn.cms;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.common.crypto;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.common.util;
using tr.gov.tubitak.uekae.esya.api.crypto.util;
using tr.gov.tubitak.uekae.esya.asn.cms;

namespace tr.gov.tubitak.uekae.esya.api.infra.mobile
{
    public class MobileSigner : BaseSigner
    {
        readonly MSSPClientConnector mConnector; 
	    readonly UserIdentifier mUserIden; 
	    readonly ECertificate mSigningCert;
	    readonly String mInformativeText;
	    readonly String mSigningAlg;
        readonly IAlgorithmParameterSpec mParams;
        FingerPrintInfo fingerPrintInfo;


        public MobileSigner(MSSPClientConnector connector, UserIdentifier aUserIden, ECertificate aSigningCert,
			    String informativeText,	String aSigningAlg, IAlgorithmParameterSpec aParams) 
	    {
		    mConnector = connector;
		    mUserIden = aUserIden;
		    mSigningCert = aSigningCert;
		    mInformativeText = informativeText;
		    mSigningAlg = aSigningAlg;
            mParams = aParams;
            this.fingerPrintInfo = new FingerPrintInfo(this);
        }

        public MobileSigner(String informativeText)
        {
            this.mInformativeText = informativeText;
            this.fingerPrintInfo = new FingerPrintInfo(this);
        }

        protected void calculateFingerPrint(DigestAlg digestAlg, byte[] dataToBeSigned)
        {
            byte[] fingerPrintBytes = DigestUtil.digest(digestAlg, dataToBeSigned);
            String fingerPrint = StringUtil.ToString(fingerPrintBytes);
            fingerPrintInfo.setFingerPrint(fingerPrint);
        }

        public virtual byte[] sign(byte[] aData) 
	    {
            DigestAlg digestAlg = SignatureAlg.fromName(getSignatureAlgorithmStr()).getDigestAlg();
            calculateFingerPrint(digestAlg, aData);
            return mConnector.sign(aData, SigningMode.SIGNHASH, mUserIden,  
				    mSigningCert, mInformativeText, mSigningAlg, mParams);
	    }

        public FingerPrintInfo getFingerPrintInfo()
        {
            return fingerPrintInfo;
        }

        public virtual List<MultiSignResult> sign(List<byte[]> aData, List<String> informativeText)
        {
            return mConnector.sign(aData, SigningMode.SIGNHASH, mUserIden,
                    mSigningCert, informativeText, mSigningAlg, mParams);
        }

        public virtual ECertificate getSigningCert()
        {
            return mConnector.getSigningCert();
        }

        public virtual ESignerIdentifier getSignerIdentifier()
        {
            return mConnector.getSignerIdentifier();
        }
        public virtual SigningCertificate getSigningCertAttr()
        {
            return mConnector.getSigningCertAttr();
        }
        public virtual ESigningCertificateV2 getSigningCertAttrv2()
        {
            return mConnector.getSigningCertAttrv2();
        }
        public virtual DigestAlg getDigestAlg()
        {
            return mConnector.getDigestAlg();
        }
	    public virtual String getSignatureAlgorithmStr() 
	    {
		    return mSigningAlg;
	    }

        public virtual IAlgorithmParameterSpec getAlgorithmParameterSpec()
        {
            return mParams;
        }

        public virtual String getInformativeText()
        {
            return mInformativeText;
        }

        public MSSPClientConnector getmConnector()
        {
            return mConnector;
        }

        public UserIdentifier getmUserIden()
        {
            return mUserIden;
        }
    }
}

using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Reflection;
using Com.Objsys.Asn1.Runtime;
using log4net;
using tr.gov.tubitak.uekae.esya.api.asn.cms;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.cmssignature.attribute;
using tr.gov.tubitak.uekae.esya.api.cmssignature.signature;
using tr.gov.tubitak.uekae.esya.api.common.license;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.crypto.util;
using tr.gov.tubitak.uekae.esya.asn.cms;
using Attribute = tr.gov.tubitak.uekae.esya.asn.x509.Attribute;

namespace tr.gov.tubitak.uekae.esya.api.cmssignature.validation.check
{
    public abstract class BaseChecker : Checker
    {
        private Dictionary<String, Object> mParameters = new Dictionary<String, Object>();
        private ESignedData mSignedData;

        protected ILog logger = null;

        public BaseChecker()
        {
            logger = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);
            try
            {
                LV.getInstance().checkLicenceDates(LV.Products.CMSIMZA);
            }
            catch (LE ex)
            {
                throw new SystemException("Lisans kontrolu basarisiz. " + ex.Message);
            }
        }

        public bool check(Signer aSigner, CheckerResult aCheckerResult)
        {
            //mSignerCertificate = (ECertificate)getParameters()[EParameter.P_SIGNING_CERTIFICATE];
            Object mSignerCert = null;
            getParameters().TryGetValue(AllEParameters.P_SIGNING_CERTIFICATE, out mSignerCert);

            //mSignedData = (ESignedData)getParameters()[EParameter.P_SIGNED_DATA];
            mSignedData = null;
            Object temp = null;
            mParameters.TryGetValue(AllEParameters.P_SIGNED_DATA, out temp);
            mSignedData = (ESignedData)temp;

            return _check(aSigner, aCheckerResult);
        }

        public Dictionary<String, Object> getParameters()
        {
            return mParameters;
        }

        public virtual void setParameters(Dictionary<String, Object> aParameters)
        {
            mParameters = aParameters;
        }

        public ESignedData getSignedData()
        {
            return mSignedData;
        }

        protected Attribute _findAttribute(Attribute[] aAttrs, Asn1ObjectIdentifier aOID)
        {
            foreach (Attribute att in aAttrs)
            {
                if (att.type.Equals(aOID))
                    return att;
            }

            return null;
        }

        protected byte[] _getAttributeValue(Attribute aAttribute)
        {
            Asn1OpenType attr_deger = aAttribute.values.elements[0];
            Asn1DerEncodeBuffer encBuf = new Asn1DerEncodeBuffer();
            attr_deger.Encode(encBuf);
            return encBuf.MsgCopy;
        }

        protected bool _checkDigest(byte[] aHashValue, Stream aOriginalValue, DigestAlg aDigestAlg)
        {
            byte[] hash = DigestUtil.digestStream(aDigestAlg, aOriginalValue);
            return aHashValue.SequenceEqual<byte>(hash);
        }

        protected List<Attribute> _findAttrList(Attribute[] aAttrs, Asn1ObjectIdentifier aOID)
        {
            List<Attribute> attrList = new List<Attribute>();
            foreach (Attribute att in aAttrs)
            {
                if (att.type.Equals(aOID))
                    attrList.Add(att);
            }

            return attrList;
        }

        protected List<SignedData> _findTSAsSignedData(Attribute[] aAttrList, Asn1ObjectIdentifier aOID)
        {
            List<Attribute> attrList = _findAttrList(aAttrList, aOID);
            if (attrList.Count != 0)
            {
                return null;
            }

            List<SignedData> sdList = new List<SignedData>();
            foreach (Attribute attr in attrList)
            {
                ContentInfo ci = new ContentInfo();
                Asn1DerDecodeBuffer decBuf = new Asn1DerDecodeBuffer(_getAttributeValue(attr));
                ci.Decode(decBuf);
                SignedData sd = new SignedData();
                decBuf.Reset();
                decBuf = new Asn1DerDecodeBuffer(ci.content.mValue);
                sd.Decode(decBuf);
                sdList.Add(sd);
            }

            return sdList;

        }

        protected bool isSignatureTypeAboveEST(ESignatureType type)
        {
            if (type != ESignatureType.TYPE_BES && type != ESignatureType.TYPE_EPES && type != ESignatureType.TYPE_EST)
                return true;
            else
                return false;
        }

        protected abstract bool _check(Signer aSigner, CheckerResult aCheckerResult);

    }
}

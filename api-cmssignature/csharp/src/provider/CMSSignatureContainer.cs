using System;
using System.IO;
using System.Linq;
using System.Reflection;
using Com.Objsys.Asn1.Runtime;
using log4net;
using tr.gov.tubitak.uekae.esya.api.asn.cms;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.cmssignature.attribute;
using tr.gov.tubitak.uekae.esya.api.cmssignature.signature;
using tr.gov.tubitak.uekae.esya.api.common.util;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.signature;
using tr.gov.tubitak.uekae.esya.api.signature.impl;

namespace tr.gov.tubitak.uekae.esya.api.cmssignature.provider
{
    public class CMSSignatureContainer : AbstractSignatureContainer
    {
        private static readonly ILog logger = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);
        private BaseSignedData baseSignedData;
        private SDValidationData validationData;

        public CMSSignatureContainer()
        {
            baseSignedData = new BaseSignedData();
            validationData = new SDValidationData(baseSignedData.getSignedData());
        }

        public override Signature createSignature(ECertificate certificate)
        {
            CMSSignatureImpl signature = new CMSSignatureImpl(context,this, baseSignedData, null, null, certificate, validationData);
            rootSignatures.Add(signature);

            return signature;
        }

        // @Override
        public override void addExternalSignature(Signature signature)
        {
            if (!(signature is CMSSignatureImpl))
                throw new SignatureRuntimeException("Unknown CMS Signature impl!");
            base.addExternalSignature(signature);
            Signable content = getSignatures()[0].getContents()[0];

            ESignerInfo signerInfo = ((CMSSignatureImpl)signature).signer.getSignerInfo();
            //Check the correct document is signed.
            if (checkMessageDigestAttr(signerInfo, content))
            {
                baseSignedData.getSignedData().addSignerInfo(signerInfo);
                CMSSignatureUtil.addCerIfNotExist(baseSignedData.getSignedData(), signature.getSignerCertificate());
                CMSSignatureUtil.addDigestAlgIfNotExist(baseSignedData.getSignedData(), signerInfo.getDigestAlgorithm());
            }
            else
                throw new SignatureException("İmzalanan içerik aynı değil");
        }
        private bool checkMessageDigestAttr(ESignerInfo aSignerInfo, Signable content)
        {
            EAttribute attr = aSignerInfo.getSignedAttribute(MessageDigestAttr.OID)[0];
            Asn1OctetString octetS = new Asn1OctetString();
            try
            {
                Asn1DerDecodeBuffer decBuf = new Asn1DerDecodeBuffer(attr.getValue(0));
                octetS.Decode(decBuf);
            }
            catch (Exception tEx)
            {
                throw new CMSSignatureException("Mesaj özeti çözülemedi.", tEx);
            }

            DigestAlg digestAlg = DigestAlg.fromOID(aSignerInfo.getDigestAlgorithm().getAlgorithm().mValue);
            try
            {
                byte[] contentDigest = content.getDigest(digestAlg);
                return octetS.mValue.SequenceEqual(contentDigest);
            }
            catch (Exception e)
            {
                throw new SignatureException("İmzalanan dosya özeti hesaplanamadı.", e);
            }
        }

        public override void detachSignature(Signature signature)
        {
            try
            {
                CMSSignatureImpl cmsSignature = (CMSSignatureImpl)signature;
                cmsSignature.signer.remove();
                getSignatures().Remove(signature);
            }
            catch (Exception x)
            {
                throw new SignatureException("Cant extract signature form container ", x);
            }
        }

        public override SignatureFormat getSignatureFormat()
        {
            return SignatureFormat.CAdES;
        }

        public override bool isSignatureContainer(Stream stream)
        {
            try
            {
                return BaseSignedData.isSigned(stream);
            }
            catch (Exception x)
            {
                logger.Error("Cant read stream for signature type detection", x);
                throw new SignatureException(x);
            }
        }

        public override void read(Stream stream)
        {
            try
            {
                byte[] bytes = StreamUtil.readAll(stream);
                baseSignedData = new BaseSignedData(bytes);
                baseSignedData.getSignerList();
                validationData = new SDValidationData(baseSignedData.getSignedData(),context);
                foreach (Signer s in baseSignedData.getSignerList())
                {
                    CMSSignatureImpl signature = new CMSSignatureImpl(context,this, baseSignedData, s, null, s.getSignerCertificate(), validationData);
                    rootSignatures.Add(signature);
                }
            }
            catch (Exception x)
            {
                throw new SignatureException(x);
            }

        }

        public override void write(Stream stream)
        {
            try
            {
                stream.Write(baseSignedData.getEncoded(), 0, baseSignedData.getEncoded().Length);
                // stream.flush();
            }
            catch (Exception x)
            {
                throw new SignatureException(x);
            }
        }

        public override Object getUnderlyingObject()
        {
            return baseSignedData;
        }
    }
}

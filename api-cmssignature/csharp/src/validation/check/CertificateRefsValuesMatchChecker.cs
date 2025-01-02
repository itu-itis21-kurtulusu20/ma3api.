using System;
using System.Collections.Generic;
using System.Linq;
using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.api.asn.cms;
using tr.gov.tubitak.uekae.esya.api.cmssignature.attribute;
using tr.gov.tubitak.uekae.esya.api.cmssignature.bundle;
using tr.gov.tubitak.uekae.esya.api.cmssignature.signature;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.crypto.util;
using tr.gov.tubitak.uekae.esya.asn.cms;
using tr.gov.tubitak.uekae.esya.asn.x509;

namespace tr.gov.tubitak.uekae.esya.api.cmssignature.validation.check
{
    /**
 * Checks if certificate references and certificate values match with each other
 * @author aslihan.kubilay
 *
 */
    public class CertificateRefsValuesMatchChecker : BaseChecker
    {
        private readonly Dictionary<int, Dictionary<DigestAlg, byte[]>> mOzetTablo = new Dictionary<int, Dictionary<DigestAlg, byte[]>>();
        private readonly Dictionary<int, byte[]> mEncodedCertsMap = new Dictionary<int, byte[]>();

        //@Override
        protected override bool _check(Signer aSigner, CheckerResult aCheckerResult)
        {
            //aCheckerResult.setCheckerName("Certificate References Values Match Checker");
            aCheckerResult.setCheckerName(Msg.getMsg(Msg.CERTIFICATE_REFERENCES_VALUES_MATCH_CHECKER), typeof(CertificateRefsValuesMatchChecker));

            Object searchRevData = false;
            getParameters().TryGetValue(AllEParameters.P_FORCE_STRICT_REFERENCE_USE, out searchRevData);
            if (!true.Equals(searchRevData))
            {
                aCheckerResult.addMessage(new ValidationMessage(Msg.getMsg(Msg.CertificateRefsValuesMatchChecker_SUCCESSFUL)));
                aCheckerResult.setResultStatus(Types.CheckerResult_Status.SUCCESS);
                return true;
            }

            List<EAttribute> refsAttrs = aSigner.getUnsignedAttribute(AttributeOIDs.id_aa_ets_certificateRefs);

            if (refsAttrs.Count == 0)
            {
                aCheckerResult.addMessage(new ValidationMessage(Msg.getMsg(Msg.NO_COMPLETE_CERTIFICATE_REFERENCES_IN_SIGNEDDATA)));
                aCheckerResult.setResultStatus(Types.CheckerResult_Status.NOTFOUND);
                return false;
            }

            EAttribute refsAttr = refsAttrs[0];
            ECompleteCertificateReferences certRefs = null;
            try
            {
                certRefs = new ECompleteCertificateReferences(refsAttr.getValue(0));
            }
            catch (Exception aEx)
            {
                aCheckerResult.addMessage(new ValidationMessage(Msg.getMsg(Msg.COMPLETE_CERTIFICATE_REFERENCES_DECODE_ERROR)));
                return false;
            }


            List<EAttribute> valuesAttrs = aSigner.getUnsignedAttribute(AttributeOIDs.id_aa_ets_certValues);

            if (valuesAttrs.Count == 0)
            {
                aCheckerResult.addMessage(new ValidationMessage(Msg.getMsg(Msg.NO_CERTIFICATE_VALUES_ATTRIBUTE_IN_SIGNEDDATA)));
                aCheckerResult.setResultStatus(Types.CheckerResult_Status.NOTFOUND);
                return false;
            }

            EAttribute valuesAttr = valuesAttrs[0];

            ECertificateValues values = null;
            try
            {
                values = new ECertificateValues(valuesAttr.getValue(0));
            }
            catch (Exception aEx)
            {
                aCheckerResult.addMessage(new ValidationMessage(Msg.getMsg(Msg.CERTIFICATE_VALUES_ATTRIBUTE_DECODE_ERROR)));
                return false;
            }


            bool sonuc = _eslestir(certRefs, values);
            if (!sonuc)
            {
                aCheckerResult.addMessage(new ValidationMessage(Msg.getMsg(Msg.CertificateRefsValuesMatchChecker_UNSUCCESSFUL)));
                return false;
            }

            aCheckerResult.addMessage(new ValidationMessage(Msg.getMsg(Msg.CertificateRefsValuesMatchChecker_SUCCESSFUL)));
            aCheckerResult.setResultStatus(Types.CheckerResult_Status.SUCCESS);
            return true;


        }


        private bool _eslestir(ECompleteCertificateReferences aRefs, ECertificateValues aValues)
        {
            OtherCertID[] refs = aRefs.getObject().elements;
            Certificate[] values = aValues.getObject().elements;
            if (refs.Length != values.Length)
            {
                return false;
            }

            foreach (OtherCertID certID in refs)
            {
                try
                {
                    if (_eslesenSertifikaBul(certID, values) == false)
                        return false;
                }
                catch (Exception aEx)
                {
                    return false;
                }
            }

            return true;
        }

        //TODO yeni asn classlarini kullan
        private bool _eslesenSertifikaBul(OtherCertID aCertID, Certificate[] aValues)
        {
            Asn1OctetString ozet = null;
            DigestAlg alg = null;
            if (aCertID.otherCertHash.ChoiceID == OtherHash._SHA1HASH)
            {
                ozet = (Asn1OctetString)aCertID.otherCertHash.GetElement();
                alg = DigestAlg.SHA1;
            }
            else
            {
                OtherHashAlgAndValue other = (OtherHashAlgAndValue)aCertID.otherCertHash.GetElement();
                alg = DigestAlg.fromOID(other.hashAlgorithm.algorithm.mValue);
                ozet = other.hashValue;
            }

            for (int i = 0; i < aValues.Length; i++)
            {
                byte[] encoded = null;
                mEncodedCertsMap.TryGetValue(i, out encoded);
                if (encoded == null)
                {
                    encoded = _encodeCertificate(aValues[i]);
                    mEncodedCertsMap[i] = encoded;
                }

                Dictionary<DigestAlg, byte[]> map = null;
                mOzetTablo.TryGetValue(i, out map);
                if (map == null)
                {

                    byte[] cerOzet = DigestUtil.digest(alg, encoded);
                    Dictionary<DigestAlg, byte[]> yenimap = new Dictionary<DigestAlg, byte[]>();
                    yenimap[alg] = cerOzet;
                    mOzetTablo[i] = yenimap;
                    if (ozet.mValue.SequenceEqual<byte>(cerOzet))
                        return true;
                }
                else
                {
                    byte[] cerOzet = null;
                    map.TryGetValue(alg, out cerOzet);
                    if (cerOzet == null)
                    {
                        cerOzet = DigestUtil.digest(alg, encoded);
                        map[alg] = cerOzet;
                        if (ozet.mValue.SequenceEqual<byte>(cerOzet))
                            return true;
                    }
                    else
                    {
                        if (ozet.mValue.SequenceEqual<byte>(cerOzet))
                            return true;
                    }

                }
            }

            return false;

        }

        private byte[] _encodeCertificate(Certificate aCer)
        {
            Asn1DerEncodeBuffer encBuf = new Asn1DerEncodeBuffer();
            aCer.Encode(encBuf);
            return encBuf.MsgCopy;
        }
    }
}

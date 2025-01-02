using System;
using System.Collections.Generic;
using System.Linq;
using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.api.asn.cms;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.cmssignature.attribute;
using tr.gov.tubitak.uekae.esya.api.cmssignature.bundle;
using tr.gov.tubitak.uekae.esya.api.cmssignature.signature;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.crypto.util;
using tr.gov.tubitak.uekae.esya.asn.cms;
using tr.gov.tubitak.uekae.esya.asn.ocsp;
using tr.gov.tubitak.uekae.esya.asn.util;
using tr.gov.tubitak.uekae.esya.asn.x509;

namespace tr.gov.tubitak.uekae.esya.api.cmssignature.validation.check
{
    /**
 * Checks if revocation references and revocation values match each other
 * @author aslihan.kubilay
 *
 */
    public class RevocationRefsValuesMatchChecker : BaseChecker
    {
        /*
        private Dictionary<int, Dictionary<DigestAlg, byte[]>> mCrlHashTable = new Dictionary<int, Dictionary<DigestAlg, byte[]>>();
        private Dictionary<int, byte[]> mEncodedCrlsMap = new Dictionary<int, byte[]>();

        private Dictionary<int, Dictionary<DigestAlg, byte[]>> mOCSPHashTable = new Dictionary<int, Dictionary<DigestAlg, byte[]>>();
        private Dictionary<int, byte[]> mEncodedOcspMap = new Dictionary<int, byte[]>();
        */

        //@Override
        protected override bool _check(Signer aSigner, CheckerResult aCheckerResult)
        {
            aCheckerResult.setCheckerName(Msg.getMsg(Msg.REVOCATION_REFERENCES_AND_VALUES_MATCH_CHECKER), typeof(RevocationRefsValuesMatchChecker));
            
            Object searchRevData = false;
            getParameters().TryGetValue(AllEParameters.P_FORCE_STRICT_REFERENCE_USE, out searchRevData);
            if (!true.Equals(searchRevData))
            {
                aCheckerResult.addMessage(new ValidationMessage(Msg.getMsg(Msg.REVOCATION_REFS_VALUES_MATCH_SUCCESSFUL)));
                aCheckerResult.setResultStatus(Types.CheckerResult_Status.SUCCESS);
                return true;
            }

            List<EAttribute> refsAttrs = aSigner.getUnsignedAttribute(AttributeOIDs.id_aa_ets_revocationRefs);

            if (refsAttrs.Count == 0)
            {
                aCheckerResult.addMessage(new ValidationMessage(Msg.getMsg(Msg.REVOCATION_REFERENCES_ATTRIBUTE_NOT_FOUND)));
                aCheckerResult.setResultStatus(Types.CheckerResult_Status.NOTFOUND);
                return false;
            }

            EAttribute refsAttr = refsAttrs[0];

            ECompleteRevocationReferences revRefs = null;
            try
            {
                revRefs = new ECompleteRevocationReferences(refsAttr.getValue(0));
            }
            catch (Exception aEx)
            {
                aCheckerResult.addMessage(new ValidationMessage(Msg.getMsg(Msg.REVOCATION_REFERENCES_ATTRIBUTE_DECODE_ERROR)));
                return false;
            }


            List<EAttribute> valuesAttrs = aSigner.getUnsignedAttribute(AttributeOIDs.id_aa_ets_revocationValues);

            if (valuesAttrs.Count == 0)
            {
                aCheckerResult.addMessage(new ValidationMessage(Msg.getMsg(Msg.REVOCATION_VALUES_ATTRIBUTE_NOT_FOUND)));
                aCheckerResult.setResultStatus(Types.CheckerResult_Status.NOTFOUND);
                return false;
            }

            EAttribute valuesAttr = valuesAttrs[0];

            ERevocationValues values = null;
            try
            {
                values = new ERevocationValues(valuesAttr.getValue(0));
            }
            catch (Exception aEx)
            {
                aCheckerResult.addMessage(new ValidationMessage(Msg.getMsg(Msg.REVOCATION_VALUES_DECODE_ERROR)));
                return false;
            }
            
            if (revRefs.getRefCount() != values.getBasicOCSPResponseCount() + values.getCRLCount())
            {
                aCheckerResult.addMessage(new ValidationMessage(Msg.getMsg(Msg.REVOCATION_REFS_VALUES_MATCH_UNSUCCESSFUL)));
                return false;
            }
            if (!_eslestir(revRefs, values))
            {
                aCheckerResult.addMessage(new ValidationMessage(Msg.getMsg(Msg.REVOCATION_REFS_VALUES_MATCH_UNSUCCESSFUL)));
                return false;
            }

            aCheckerResult.addMessage(new ValidationMessage(Msg.getMsg(Msg.REVOCATION_REFS_VALUES_MATCH_SUCCESSFUL)));
            aCheckerResult.setResultStatus(Types.CheckerResult_Status.SUCCESS);
            return true;
        }

        //TODO yeni asn classlari ile yap
        private bool _eslestir(ECompleteRevocationReferences aRefs, ERevocationValues aValues)
        {
            CrlOcspRef[] refs = aRefs.getObject().elements;
            List<CRLListID> crlRefs = new List<CRLListID>();
            List<OcspListID> ocspRefs = new List<OcspListID>();

            foreach (CrlOcspRef ref_ in refs)
            {
                if (ref_.crlids != null && ref_.crlids.crls.elements.Length != 0)
                    crlRefs.Add(ref_.crlids);

                if (ref_.ocspids != null && ref_.ocspids.ocspResponses.elements.Length != 0)
                    ocspRefs.Add(ref_.ocspids);
            }

            bool crlSonuc = true;
            if (aValues.getObject().crlVals != null)
            {
                CertificateList[] crls = aValues.getObject().crlVals.elements;
                crlSonuc = _crlEslestir(crlRefs, crls);
            }

            bool ocspSonuc = true;
            if (aValues.getObject().ocspVals != null)
            {
                BasicOCSPResponse[] ocsps = aValues.getObject().ocspVals.elements;
                ocspSonuc = _ocspEslestir(ocspRefs, ocsps);
            }

            return crlSonuc && ocspSonuc;

        }

        private bool _crlEslestir(List<CRLListID> crlRefs, CertificateList[] aCRLs)
        {
            foreach (CRLListID crlLID in crlRefs)
            {
                CrlValidatedID[] crlVIDList = crlLID.crls.elements;
                foreach (CrlValidatedID vid in crlVIDList)
                {
                    OtherHash hash = vid.crlHash;
                    CrlIdentifier id = vid.crlIdentifier;
                    try
                    {
                        if (!_crlkontrol(hash, id, aCRLs))
                            return false;
                    }
                    catch (Exception aEx)
                    {
                        return false;
                    }
                }
            }
            return true;
        }

        private bool _ocspEslestir(List<OcspListID> aOcspRefs, BasicOCSPResponse[] aOCSPs)
        {
            foreach (OcspListID ocspLID in aOcspRefs)
            {
                OcspResponsesID[] ocspRIDList = ocspLID.ocspResponses.elements;
                foreach (OcspResponsesID orid in ocspRIDList)
                {
                    OtherHash hash = orid.ocspRepHash;
                    OcspIdentifier id = orid.ocspIdentifier;
                    try
                    {
                        if (!_ocspkontrol(hash, id, aOCSPs))
                            return false;
                    }
                    catch (Exception aEx)
                    {
                        return false;
                    }
                }
            }
            return true;
        }

        private bool _crlkontrol(OtherHash aHash, CrlIdentifier aId, CertificateList[] aCRLs)
        {
            Asn1OctetString ozet = null;
            DigestAlg alg = null;
            if (aHash.ChoiceID == OtherHash._SHA1HASH)
            {
                ozet = (Asn1OctetString)aHash.GetElement();
                alg = DigestAlg.SHA1;
            }
            else
            {
                OtherHashAlgAndValue other = (OtherHashAlgAndValue)aHash.GetElement();
                alg = DigestAlg.fromOID(other.hashAlgorithm.algorithm.mValue);
                ozet = other.hashValue;
            }

            String issuedTime = null;
            Name crlIssuer = null;
            BigInteger crlNumber = null;
            if (aId != null)
            {
                issuedTime = aId.crlIssuedTime.mValue;
                crlIssuer = aId.crlissuer;

                if (aId.crlNumber != null)
                    crlNumber = aId.crlNumber.mValue;
            }

            for (int i = 0; i < aCRLs.Length; i++)
            {
                if (aId != null)
                {
                    String thisUpdate = ((Asn1UTCTime)aCRLs[i].tbsCertList.thisUpdate.GetElement()).mValue;//TODO burda generalizedtime da olabilir
                    if (!issuedTime.Equals(thisUpdate)) continue;
                    Name issuer = aCRLs[i].tbsCertList.issuer;
                    if (!UtilEsitlikler.esitMi(crlIssuer, issuer)) continue;
                    if (crlNumber != null)
                    {
                        BigInteger cn = new ECRL(aCRLs[i]).getCRLNumber();
                        if (cn == null || !crlNumber.Equals(cn)) continue;
                    }
                }

                byte[] encoded = _encodeCRL(aCRLs[i]);

                byte[] crlOzet = DigestUtil.digest(alg, encoded);
                if (ozet.mValue.SequenceEqual(crlOzet))
                    return true;

            }

            return false;

        }

        private bool _ocspkontrol(OtherHash aHash, OcspIdentifier aId, BasicOCSPResponse[] aOCSPs)
        {
            Asn1OctetString ozet = null;
            DigestAlg alg = null;
            if (aHash.ChoiceID == OtherHash._SHA1HASH)
            {
                ozet = (Asn1OctetString)aHash.GetElement();
                alg = DigestAlg.SHA1;
            }
            else
            {
                OtherHashAlgAndValue other = (OtherHashAlgAndValue)aHash.GetElement();
                alg = DigestAlg.fromOID(other.hashAlgorithm.algorithm.mValue);
                ozet = other.hashValue;
            }

            String producedAt = aId.producedAt.mValue;
            ResponderID rid = aId.ocspResponderID;

            for (int i = 0; i < aOCSPs.Length; i++)
            {
                String time = aOCSPs[i].tbsResponseData.producedAt.mValue;
                if (!time.Equals(producedAt)) continue;

                ResponderID id = aOCSPs[i].tbsResponseData.responderID;
                if (!UtilEsitlikler.esitMi(rid, id)) continue;

                byte[] encoded = _encodeOCSP(aOCSPs[i]);
                byte[] ocspOzet = DigestUtil.digest(alg, encoded);
                if (ozet.mValue.SequenceEqual(ocspOzet))
                    return true;

            }

            return false;

        }

        private byte[] _encodeCRL(CertificateList aCRL)
        {
            Asn1DerEncodeBuffer encBuf = new Asn1DerEncodeBuffer();
            aCRL.Encode(encBuf);
            return encBuf.MsgCopy;
        }

        private byte[] _encodeOCSP(BasicOCSPResponse aOCSP)
        {
            Asn1DerEncodeBuffer encBuf = new Asn1DerEncodeBuffer();
            aOCSP.Encode(encBuf);
            return encBuf.MsgCopy;
        }

    }
}

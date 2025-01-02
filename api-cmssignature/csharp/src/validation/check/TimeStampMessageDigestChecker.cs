using System;
using System.Collections.Generic;
using System.IO;
using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.api.asn.cms;
using tr.gov.tubitak.uekae.esya.api.asn.pkixtsp;
using tr.gov.tubitak.uekae.esya.api.cmssignature.attribute;
using tr.gov.tubitak.uekae.esya.api.cmssignature.bundle;
using tr.gov.tubitak.uekae.esya.api.cmssignature.signature;
using tr.gov.tubitak.uekae.esya.api.common;
using tr.gov.tubitak.uekae.esya.api.common.tools;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.crypto.util;
using tr.gov.tubitak.uekae.esya.asn.cms;
using tr.gov.tubitak.uekae.esya.asn.pkixtsp;
using tr.gov.tubitak.uekae.esya.asn.util;
using tr.gov.tubitak.uekae.esya.asn.x509;
using Attribute = tr.gov.tubitak.uekae.esya.asn.x509.Attribute;

namespace tr.gov.tubitak.uekae.esya.api.cmssignature.validation.check
{
/**
 * Checks whether the messageimprint field in timestamp mathes with the one calculated from signature
 * @author aslihan.kubilay
 *
 */
    public class TimeStampMessageDigestChecker : BaseChecker
    {
        private readonly Types.TS_Type? mType = null;
        private readonly ESignedData mSignedData = null;

        public TimeStampMessageDigestChecker(Types.TS_Type aTSType, ESignedData aSignedData)
        {
            mType = aTSType;
            mSignedData = aSignedData;
        }



        //@Override
        protected override bool _check(Signer aSigner, CheckerResult aCheckerResult)
        {
            aCheckerResult.setCheckerName(Msg.getMsg(Msg.TIMESTAMP_MESSAGE_DIGEST_CHECKER), typeof(TimeStampMessageDigestChecker));
            if (!mSignedData.getEncapsulatedContentInfo().getContentType().Equals(AttributeOIDs.id_ct_TSTInfo))
            {
                aCheckerResult.addMessage(new ValidationMessage("TimeStamp Attribute'unun content type i TSTInfo degil."));
                return false;
            }

            ETSTInfo tstInfo = null;
            try
            {
                tstInfo = new ETSTInfo(mSignedData.getEncapsulatedContentInfo().getContent());
            }
            catch (Exception aEx)
            {
                aCheckerResult.addMessage(new ValidationMessage(Msg.getMsg(Msg.TS_MESSAGE_DIGEST_CHECKER_DECODE_ERROR), aEx));
                return false;
            }

            try
            {
                byte[] tsHash = tstInfo.getHashedMessage();
                byte[] preCalculatedHash = null;
                if (getParameters().ContainsKey(AllEParameters.P_PRE_CALCULATED_TIMESTAMP_HASH))
                {
                    preCalculatedHash = (byte[])getParameters()[AllEParameters.P_PRE_CALCULATED_TIMESTAMP_HASH];
                }

                if (!UtilEsitlikler.esitMi(tsHash, preCalculatedHash))
                {
                    AlgorithmIdentifier tsHashAlg = tstInfo.getHashAlgorithm().getObject();
                    DigestAlg digestAlg = DigestAlg.fromOID(tsHashAlg.algorithm.mValue);
                    using (Stream hashInput = _getHashInput(aSigner.getSignerInfo().getObject(), digestAlg))
                    {
                        if (hashInput == null)
                        {
                            aCheckerResult.addMessage(
                                new ValidationMessage(Msg.getMsg(Msg.TS_MESSAGE_DIGEST_CHECKER_DIGEST_CALCULATION_ERROR)));
                            return false;
                        }
                        //using (MemoryStream memoryStream = new MemoryStream(hashInput))
                        //{
                        if (!_checkDigest(tsHash, hashInput, digestAlg))
                        {
                            //aCheckerResult.addMessage(new ValidationMessage("TimeStamp Attribute'unda ZamanDamgasi ozet degeri ile imzadan hesaplanan ozet degeri ayni degil"));
                            //return false;
                            if (mType != Types.TS_Type.ESA)
                            {
                                aCheckerResult.addMessage(
                                    new ValidationMessage(
                                        Msg.getMsg(Msg.TS_MESSAGE_DIGEST_CHECKER_UNSUCCESSFUL)));
                                return false;
                            }
                            //because of inconvenience between annex-k and chapter 6 in ETSI TS 101 733 V1.8.1 (2009-11).
                            //they defined different data to be hashed for ESA
                            else
                            {
                                using (
                                    Stream hashInputDefinedInAnnexK =
                                        _getHashInputDefinedInAnnexK(aSigner.getSignerInfo().getObject()))
                                {
                                    //using (MemoryStream mStream = new MemoryStream(hashInput))
                                    //{
                                    if (
                                        !_checkDigest(tsHash, hashInputDefinedInAnnexK,
                                                      digestAlg))
                                    {
                                        aCheckerResult.addMessage(
                                            new ValidationMessage(
                                                Msg.getMsg(Msg.TS_MESSAGE_DIGEST_CHECKER_UNSUCCESSFUL)));
                                        return false;
                                    }
                                    //}
                                }
                            }
                        }
                        //}
                    }
                }
            }

            catch (Exception aEx)
            {
                aCheckerResult.addMessage(new ValidationMessage(Msg.getMsg(Msg.TS_MESSAGE_DIGEST_CHECKER_DIGEST_CALCULATION_ERROR), aEx));
                return false;
            }

            aCheckerResult.setResultStatus(Types.CheckerResult_Status.SUCCESS);
            aCheckerResult.addMessage(new ValidationMessage(Msg.getMsg(Msg.TS_MESSAGE_DIGEST_CHECKER_SUCCESSFUL)));
            return true;
        }

        private Stream _getHashInputDefinedInAnnexK(SignerInfo aSignerInfo)
        {
            Stream hashInput;
            Object contentInfoBytes = null;
            getParameters().TryGetValue(AllEParameters.P_CONTENT_INFO_BYTES, out contentInfoBytes);
            hashInput = _esaZDIcerikBulForAnnexK(contentInfoBytes as byte[], aSignerInfo);
            return hashInput;
        }

        private Stream _getHashInput(SignerInfo aSignerInfo, DigestAlg aDigestAlg)
        {

            Stream hashInput = null;


            if (mType == Types.TS_Type.CONTENT)
            {
                hashInput = _contentZDIcerikBul();
            }

            //signature-time-stamp Attribute Definition(etsi ts 101 733 V1.7.4)
            //The value of the messageImprint field within TimeStampToken shall be a hash of the value of the signature
            //field within SignerInfo for the signedData being time-stamped.
            if (mType == Types.TS_Type.EST)
            {
                hashInput = new MemoryStream(aSignerInfo.signature.mValue);
            }
            //CAdES-C-time-stamp Attribute Definition(etsi ts 101 733 V1.7.4)
            //The value of the messageImprint field within TimeStampToken shall be a hash of the concatenated values (without the
            //type or length encoding for that value) of the following data objects:
            //• OCTETSTRING of the SignatureValue field within SignerInfo;
            //• signature-time-stamp, or a time-mark operated by a Time-Marking Authority;
            //• complete-certificate-references attribute; and
            //• complete-revocation-references attribute.
            else if (mType == Types.TS_Type.ESC)
            {
                hashInput = new MemoryStream(_escZDIcerikBul(aSignerInfo));
            }
            //time-stamped-certs-crls-references Attribute Definition(etsi ts 101 733 V1.7.4)
            //The value of the messageImprint field within the TimeStampToken shall be a hash of the concatenated values
            //(without the type or length encoding for that value) of the following data objects, as present in the ES with Complete
            //validation data (CAdES-C):
            //• complete-certificate-references attribute; and
            //• complete-revocation-references attribute.
            else if (mType == Types.TS_Type.ES_REFS)
            {
                hashInput = new MemoryStream(_esrefsZDIcerikBul(aSignerInfo));
            }
            //archive-time-stamp Attribute Definition(etsi ts 101 733 V1.7.4)
            //The value of the messageImprint field within TimeStampToken shall be a hash of the concatenation of:
            //• the encapContentInfo element of the SignedData sequence;
            //• any external content being protected by the signature, if the eContent element of the encapContentInfo is omitted;
            //• the Certificates and crls elements of the SignedData sequence, when present; and
            //• all data elements in the SignerInfo sequence including all signed and unsigned attributes.
            else if (mType == Types.TS_Type.ESA)
            {
                ESignedData sd = (ESignedData)getParameters()[AllEParameters.P_SIGNED_DATA];
                object contentInfoBytes = null;
                getParameters().TryGetValue(AllEParameters.P_CONTENT_INFO_BYTES, out contentInfoBytes);
                hashInput = _esaZDIcerikBul(sd.getObject(), (byte[])contentInfoBytes, aSignerInfo);

            }
            //archive-time-stamp v3 Attribute Definition(etsi ts 101 733 V2.2.1)
            /*
            The input for the archive-time-stamp-v3’s message imprint computation shall be the concatenation (in the
            order shown by the list below) of the signed data hash (see bullet 2 below) and certain fields in their binary encoded
            form without any modification and including the tag, length and value octets:
            1) The SignedData.encapContentInfo.eContentType.
            2) The octets representing the hash of the signed data. The hash is computed on the same content that was used
            for computing the hash value that is encapsulated within the message-digest signed attribute of the
            CAdES signature being archive-time-stamped. The hash algorithm applied shall be the same as the hash
            algorithm used for computing the archive time-stamp’s message imprint. The inclusion of the hash algorithm
            in the SignedData.digestAlgorithms set is recommended.
            3) Fields version, sid, digestAlgorithm, signedAttrs, signatureAlgorithm, and
            signature within the SignedData.signerInfos’s item corresponding to the signature being archive
            time-stamped, in their order of appearance.
            4) A single instance of ATSHashIndex type (created as specified in clause 6.4.2).
            */
            else if (mType == Types.TS_Type.ESAv3)
            {
                ESignedData sd = (ESignedData)getParameters()[AllEParameters.P_SIGNED_DATA];
                hashInput = _esaZDv3IcerikBul(sd.getObject(), aSignerInfo, aDigestAlg);
            }

            return hashInput;
        }

        private Stream _contentZDIcerikBul()
        {
            ESignedData sd = ((ESignedData)getParameters()[AllEParameters.P_SIGNED_DATA]);
            byte[] contentvalue = sd.getEncapsulatedContentInfo().getContent();
            if (contentvalue != null)
                return new MemoryStream(contentvalue);
            else
            {
                ISignable externalContent = ((ISignable)getParameters()[AllEParameters.P_EXTERNAL_CONTENT]); //((ISignable)getParameters().get(AllEParameters.P_EXTERNAL_CONTENT));
                if (externalContent != null)
                {
                    try
                    {
                        return externalContent.getAsInputStream();
                    }
                    catch (Exception aEx)
                    {
                        return null;
                    }
                }

                return null;
            }
        }

        private byte[] _escZDIcerikBul(SignerInfo aSignerInfo)
        {
            byte[] temp1 = aSignerInfo.signature.mValue;
            byte[] temp2 = null;
            byte[] temp3 = null;
            byte[] temp4 = null;


            Attribute[] unsignedAttrs = aSignerInfo.unsignedAttrs.elements;
            Attribute timestamp = null;
            Attribute certrefs = null;
            Attribute revrefs = null;

            foreach (Attribute attr in unsignedAttrs)
            {
                if (attr.type.Equals(AttributeOIDs.id_aa_signatureTimeStampToken))
                    timestamp = attr;
                else if (attr.type.Equals(AttributeOIDs.id_aa_ets_certificateRefs))
                    certrefs = attr;
                else if (attr.type.Equals(AttributeOIDs.id_aa_ets_revocationRefs))
                    revrefs = attr;
            }
            try
            {
                Asn1DerEncodeBuffer encBuf = new Asn1DerEncodeBuffer();
                timestamp.Encode(encBuf, false);
                temp2 = encBuf.MsgCopy;
                encBuf.Reset();
                certrefs.Encode(encBuf, false);
                temp3 = encBuf.MsgCopy;
                encBuf.Reset();
                revrefs.Encode(encBuf, false);
                temp4 = encBuf.MsgCopy;
            }
            catch (Exception aEx)
            {
                return null;
            }

            if (temp1 == null || temp2 == null || temp3 == null || temp4 == null)
            {
                return null;
            }

            byte[] icerikTemp12 = _byteArrBirlestir(temp1, temp2);


            byte[] icerikTemp123 = _byteArrBirlestir(icerikTemp12, temp3);
            byte[] icerikTemp1234 = _byteArrBirlestir(icerikTemp123, temp4);

            return icerikTemp1234;
        }

        private byte[] _esrefsZDIcerikBul(SignerInfo aSignerInfo)
        {
            Attribute[] unsignedAttrs = aSignerInfo.unsignedAttrs.elements;

            Attribute certrefs = null;
            Attribute revrefs = null;

            byte[] temp1 = null;
            byte[] temp2 = null;

            foreach (Attribute attr in unsignedAttrs)
            {
                if (attr.type.Equals(AttributeOIDs.id_aa_ets_certificateRefs))
                    certrefs = attr;
                else if (attr.type.Equals(AttributeOIDs.id_aa_ets_revocationRefs))
                    revrefs = attr;
            }

            try
            {
                Asn1DerEncodeBuffer encBuf = new Asn1DerEncodeBuffer();
                certrefs.Encode(encBuf, false);
                temp1 = encBuf.MsgCopy;

                encBuf.Reset();
                revrefs.Encode(encBuf, false);
                temp2 = encBuf.MsgCopy;

            }
            catch (Asn1Exception aEx)
            {
                return null;
            }

            if (temp1 == null || temp2 == null)
            {
                return null;
            }

            byte[] icerikTemp12 = _byteArrBirlestir(temp1, temp2);
            return icerikTemp12;
        }



        private Stream _esaZDIcerikBulForAnnexK(byte[] aContentInfoBytes, SignerInfo aSignerInfo)
        {
            //List<byte[]> dataToBeHashed = new List<byte[]>();
            CombinedInputStream cis = new CombinedInputStream();

            byte[] signedDataBytes = _signedDataBytesAl(aContentInfoBytes);

            Asn1BerDecodeBuffer buffer = new Asn1BerDecodeBuffer(signedDataBytes);

            CMSVersion version;
            DigestAlgorithmIdentifiers digestAlgorithms;
            EncapsulatedContentInfo encapContentInfo;
            CertificateSet certificates;  // optional
            RevocationInfoChoices crls;  // optional
            int startIndex = 0;
            int endIndex = 0;

            try
            {
                int llen = matchTag(buffer, Asn1Tag.SEQUENCE);

                // decode SEQUENCE
                Asn1BerDecodeContext _context = new Asn1BerDecodeContext(buffer, llen);

                IntHolder elemLen = new IntHolder();

                // decode version
                if (_context.MatchElemTag(Asn1Tag.UNIV, Asn1Tag.PRIM, 2, elemLen, false))
                {
                    version = new CMSVersion();
                    version.Decode(buffer, true, elemLen.mValue);
                }
                else throw new Asn1MissingRequiredException(buffer);

                // decode digestAlgorithms
                if (_context.MatchElemTag(Asn1Tag.UNIV, Asn1Tag.CONS, 17, elemLen, false))
                {
                    digestAlgorithms = new DigestAlgorithmIdentifiers();
                    digestAlgorithms.Decode(buffer, true, elemLen.mValue);
                }
                else throw new Asn1MissingRequiredException(buffer);

                startIndex = buffer.ByteCount;
                // decode encapContentInfo
                if (_context.MatchElemTag(Asn1Tag.UNIV, Asn1Tag.CONS, 16, elemLen, false))
                {
                    encapContentInfo = new EncapsulatedContentInfo();
                    encapContentInfo.Decode(buffer, true, elemLen.mValue);
                }
                else throw new Asn1MissingRequiredException(buffer);
                endIndex = buffer.ByteCount;

                byte[] newArray = null;
                if (encapContentInfo.eContent != null)
                {/*
                    newArray = new byte[endIndex - startIndex];
                    Array.Copy(signedDataBytes, startIndex, newArray, 0, endIndex - startIndex);
                    dataToBeHashed.Add(newArray);
                    */
                    cis.addInputStream(new MemoryStream(signedDataBytes, startIndex, endIndex - startIndex));

                }
                else
                {
                    ISignable externalContent = null;
                    try
                    {
                        externalContent = ((ISignable)getParameters()[AllEParameters.P_EXTERNAL_CONTENT]);
                    }
                    catch (Exception e)
                    {
                        externalContent = null;
                    }

                    if (externalContent == null)
                        return null;
                    else
                    {
                        cis.addInputStream(externalContent.getAsInputStream());
                    }

                }

                startIndex = buffer.ByteCount;
                // decode certificates
                if (_context.MatchElemTag(Asn1Tag.CTXT, Asn1Tag.CONS, 0, elemLen, true))
                {
                    certificates = new CertificateSet();
                    certificates.Decode(buffer, false, elemLen.mValue);
                    if (elemLen.mValue == Asn1Status.INDEFLEN)
                    {
                        matchTag(buffer, Asn1Tag.EOC);
                    }
                }
                endIndex = buffer.ByteCount;

                //newArray = new byte[endIndex - startIndex];
                //Array.Copy(signedDataBytes, startIndex, newArray, 0, endIndex - startIndex);
                //dataToBeHashed.Add(Arrays.copyOfRange(signedDataBytes, startIndex, endIndex));
                //dataToBeHashed.Add(newArray);
                cis.addInputStream(new MemoryStream(signedDataBytes, startIndex, endIndex - startIndex));


                startIndex = buffer.ByteCount;
                // decode crls
                if (_context.MatchElemTag(Asn1Tag.CTXT, Asn1Tag.CONS, 1, elemLen, true))
                {
                    crls = new RevocationInfoChoices();
                    crls.Decode(buffer, false, elemLen.mValue);
                    if (elemLen.mValue == Asn1Status.INDEFLEN)
                    {
                        matchTag(buffer, Asn1Tag.EOC);
                    }
                }
                endIndex = buffer.ByteCount;

                //newArray = new byte[endIndex - startIndex];
                //Array.Copy(signedDataBytes, startIndex, newArray, 0, endIndex - startIndex);
                //dataToBeHashed.Add(Arrays.copyOfRange(signedDataBytes, startIndex, endIndex));
                //dataToBeHashed.Add(newArray);
                cis.addInputStream(new MemoryStream(signedDataBytes, startIndex, endIndex - startIndex));

                //decode SignerInfos
                {
                    int llen2 = matchTag(buffer, Asn1Tag.SET);

                    // decode SEQUENCE OF or SET OF

                    Asn1BerDecodeContext _context2 =
                        new Asn1BerDecodeContext(buffer, llen2);
                    SignerInfo element;
                    int elemLen2 = 0;

                    while (!_context2.Expired())
                    {
                        element = new SignerInfo();
                        startIndex = buffer.ByteCount;
                        element.Decode(buffer, true, elemLen2);
                        endIndex = buffer.ByteCount;
                        //ESignerInfo aESigner = new ESignerInfo(element);
                        ESignerInfo signer = new ESignerInfo(aSignerInfo);
                        if (signer.getSignerIdentifier().Equals(signer.getSignerIdentifier()))
                        {
                            newArray = new byte[endIndex - startIndex];
                            Array.Copy(signedDataBytes, startIndex, newArray, 0, endIndex - startIndex);
                            _addSignerInfoFieldsForAnnexK(newArray, cis);
                            continue;
                        }

                    }
                }

            }
            catch (Exception E)
            {
                throw new CMSSignatureException("Can not get the data to be hashed");
            }

            //byte[] data = concatenateArrays(dataToBeHashed.ToArray());

            //return data;
            return cis;
        }

        private byte[] concatenateArrays(byte[][] array)
        {
            int totalLen = 0;
            for (int i = 0; i < array.Length; i++)
                totalLen += array[i].Length;

            byte[] allArray = new byte[totalLen];
            int index = 0;

            for (int i = 0; i < array.Length; i++)
            {
                Array.Copy(array[i], 0, allArray, index, array[i].Length);
                index += array[i].Length;
            }

            return allArray;
        }



        private void _addSignerInfoFieldsForAnnexK(byte[] signedDataBytes,
                CombinedInputStream cis)
        {
            TSTInfo mainTS = _tstInfoBul(mSignedData.getObject());

            Asn1DerDecodeBuffer buffer = new Asn1DerDecodeBuffer(signedDataBytes);

            int startIndex ;
            int endIndex;

            CMSVersion version;
            SignerIdentifier sid;
            AlgorithmIdentifier digestAlgorithm;
            SignedAttributes signedAttrs;  // optional
            AlgorithmIdentifier signatureAlgorithm;
            Asn1OctetString signature;

            int llen = matchTag(buffer, Asn1Tag.SEQUENCE);

            // decode SEQUENCE

            Asn1BerDecodeContext _context = new Asn1BerDecodeContext(buffer, llen);

            IntHolder elemLen = new IntHolder();

            // decode version
            startIndex = buffer.ByteCount;
            if (_context.MatchElemTag(Asn1Tag.UNIV, Asn1Tag.PRIM, 2, elemLen, false))
            {
                version = new CMSVersion();
                version.Decode(buffer, true, elemLen.mValue);
            }
            else throw new Asn1MissingRequiredException(buffer);
            endIndex = buffer.ByteCount;

            //byte[] newArray = new byte[endIndex - startIndex];
            //Array.Copy(signedDataBytes, startIndex, newArray, 0, endIndex - startIndex);
            //dataToBeHashed.Add(Arrays.copyOfRange(signedDataBytes, startIndex, endIndex));
            //dataToBeHashed.Add(newArray);
            cis.addInputStream(new MemoryStream(signedDataBytes, startIndex, endIndex - startIndex));


            // decode sid
            startIndex = buffer.ByteCount;
            if (!_context.Expired())
            {
                Asn1Tag tag = buffer.PeekTag();
                if (tag.Equals(Asn1Tag.UNIV, Asn1Tag.CONS, 16) ||
                        tag.Equals(Asn1Tag.CTXT, Asn1Tag.PRIM, 0))
                {
                    sid = new SignerIdentifier();
                    sid.Decode(buffer, true, elemLen.mValue);
                }
                else throw new Asn1MissingRequiredException(buffer);
            }
            else throw new Asn1MissingRequiredException(buffer);
            endIndex = buffer.ByteCount;

            //newArray = new byte[endIndex - startIndex];
            //Array.Copy(signedDataBytes, startIndex, newArray, 0, endIndex - startIndex);
            //dataToBeHashed.Add(Arrays.copyOfRange(signedDataBytes, startIndex, endIndex));
            //dataToBeHashed.Add(newArray);
            cis.addInputStream(new MemoryStream(signedDataBytes, startIndex, endIndex - startIndex));


            // decode digestAlgorithm
            startIndex = buffer.ByteCount;
            if (_context.MatchElemTag(Asn1Tag.UNIV, Asn1Tag.CONS, 16, elemLen, false))
            {
                digestAlgorithm = new AlgorithmIdentifier();
                digestAlgorithm.Decode(buffer, true, elemLen.mValue);
            }
            else throw new Asn1MissingRequiredException(buffer);
            endIndex = buffer.ByteCount;

            //newArray = new byte[endIndex - startIndex];
            //Array.Copy(signedDataBytes, startIndex, newArray, 0, endIndex - startIndex);
            //dataToBeHashed.Add(Arrays.copyOfRange(signedDataBytes, startIndex, endIndex));
            //dataToBeHashed.Add(newArray);
            cis.addInputStream(new MemoryStream(signedDataBytes, startIndex, endIndex - startIndex));


            // decode signedAttrs
            startIndex = buffer.ByteCount;
            if (_context.MatchElemTag(Asn1Tag.CTXT, Asn1Tag.CONS, 0, elemLen, true))
            {
                signedAttrs = new SignedAttributes();
                signedAttrs.Decode(buffer, false, elemLen.mValue);
                if (elemLen.mValue == Asn1Status.INDEFLEN)
                {
                    matchTag(buffer, Asn1Tag.EOC);
                }
            }
            endIndex = buffer.ByteCount;

            //newArray = new byte[endIndex - startIndex];
            //Array.Copy(signedDataBytes, startIndex, newArray, 0, endIndex - startIndex);
            //dataToBeHashed.Add(Arrays.copyOfRange(signedDataBytes, startIndex, endIndex));
            //dataToBeHashed.Add(newArray);
            cis.addInputStream(new MemoryStream(signedDataBytes, startIndex, endIndex - startIndex));


            // decode signatureAlgorithm
            startIndex = buffer.ByteCount;
            if (_context.MatchElemTag(Asn1Tag.UNIV, Asn1Tag.CONS, 16, elemLen, false))
            {
                signatureAlgorithm = new AlgorithmIdentifier();
                signatureAlgorithm.Decode(buffer, true, elemLen.mValue);
            }
            else throw new Asn1MissingRequiredException(buffer);
            endIndex = buffer.ByteCount;

            //newArray = new byte[endIndex - startIndex];
            //Array.Copy(signedDataBytes, startIndex, newArray, 0, endIndex - startIndex);
            //dataToBeHashed.Add(Arrays.copyOfRange(signedDataBytes, startIndex, endIndex));
            //dataToBeHashed.Add(newArray);
            //dataToBeHashed.Add(Arrays.copyOfRange(signedDataBytes, startIndex, endIndex));

            cis.addInputStream(new MemoryStream(signedDataBytes, startIndex, endIndex - startIndex));

            // decode signature
            startIndex = buffer.ByteCount;
            if (_context.MatchElemTag(Asn1Tag.UNIV, Asn1Tag.PRIM, 4, elemLen, false))
            {
                signature = new Asn1OctetString();
                signature.Decode(buffer, true, elemLen.mValue);
            }
            else throw new Asn1MissingRequiredException(buffer);
            endIndex = buffer.ByteCount;


            //newArray = new byte[endIndex - startIndex];
            //Array.Copy(signedDataBytes, startIndex, newArray, 0, endIndex - startIndex);
            //dataToBeHashed.Add(Arrays.copyOfRange(signedDataBytes, startIndex, endIndex));
            //dataToBeHashed.Add(newArray);
            cis.addInputStream(new MemoryStream(signedDataBytes, startIndex, endIndex - startIndex));

            // decode unsignedAttrs
            if (_context.MatchElemTag(Asn1Tag.CTXT, Asn1Tag.CONS, 1, elemLen, true))
            {

                int llen2 = elemLen.mValue;
                // decode SEQUENCE OF or SET OF

                Asn1BerDecodeContext _context2 = new Asn1BerDecodeContext(buffer, llen2);
                Attribute element;
                int elemLen2 = 0;

                while (!_context2.Expired())
                {
                    element = new Attribute();
                    startIndex = buffer.ByteCount;
                    element.Decode(buffer, true, elemLen2);
                    endIndex = buffer.ByteCount;
                    if (element.type.Equals(AttributeOIDs.id_aa_ets_archiveTimestampV2))
                    {
                        TSTInfo tstinfo = _tstInfoBul(element);
                        DateTime tstInfoTime = new DateTime(tstinfo.genTime.Year, tstinfo.genTime.Month, tstinfo.genTime.Day, tstinfo.genTime.Hour, tstinfo.genTime.Minute, tstinfo.genTime.Second, tstinfo.genTime.UTC ? DateTimeKind.Utc : DateTimeKind.Local);
                        DateTime mainTSTime = new DateTime(mainTS.genTime.Year, mainTS.genTime.Month, mainTS.genTime.Day, mainTS.genTime.Hour, mainTS.genTime.Minute, mainTS.genTime.Second, mainTS.genTime.UTC ? DateTimeKind.Utc : DateTimeKind.Local);
                        //if (tstinfo.genTime.CompareTo(mainTS.genTime) < 0)
                        if (tstInfoTime < mainTSTime)
                        {
                            //newArray = new byte[endIndex - startIndex];
                            //Array.Copy(signedDataBytes, startIndex, newArray, 0, endIndex - startIndex);
                            //dataToBeHashed.Add(Arrays.copyOfRange(signedDataBytes, startIndex, endIndex));
                            //dataToBeHashed.Add(newArray);
                            cis.addInputStream(new MemoryStream(signedDataBytes, startIndex, endIndex - startIndex));

                        }
                    }
                    else
                    {
                        //newArray = new byte[endIndex - startIndex];
                        //Array.Copy(signedDataBytes, startIndex, newArray, 0, endIndex - startIndex);
                        //dataToBeHashed.Add(Arrays.copyOfRange(signedDataBytes, startIndex, endIndex));
                        //dataToBeHashed.Add(newArray);
                        cis.addInputStream(new MemoryStream(signedDataBytes, startIndex, endIndex - startIndex));
                    }
                }


                if (llen2 == Asn1Status.INDEFLEN)
                {
                    matchTag(buffer, Asn1Tag.EOC);
                }


                if (elemLen.mValue == Asn1Status.INDEFLEN)
                {
                    matchTag(buffer, Asn1Tag.EOC);
                }
            }

            if (!_context.Expired())
            {
                Asn1Tag _tag = buffer.PeekTag();
                if (_tag.Equals(Asn1Tag.UNIV, Asn1Tag.PRIM, 2) ||
                        _tag.Equals(Asn1Tag.UNIV, Asn1Tag.CONS, 16) ||
                        _tag.Equals(Asn1Tag.CTXT, Asn1Tag.CONS, 0) ||  //asn
                        _tag.Equals(Asn1Tag.UNIV, Asn1Tag.PRIM, 4) ||
                        _tag.Equals(Asn1Tag.CTXT, Asn1Tag.CONS, 1))
                    throw new Asn1SeqOrderException();

            }

            if (llen == Asn1Status.INDEFLEN)
            {
                matchTag(buffer, Asn1Tag.EOC);
            }

        }


        private Stream _esaZDIcerikBul(SignedData aSignedData, byte[] aContentInfoBytes, SignerInfo aSignerInfo)
        {
            //Get signeddata from ContentInfo
            byte[] signedDataBytes = _signedDataBytesAl(aContentInfoBytes);

            //• the encapContentInfo element of the SignedData sequence
            byte[] encapsulatedCI = _encapsulatedContentEncodedAl(aSignedData);
            // byte[] encapsulatedCI = _encapsulatedContentEncodedAl(signedDataBytes);

            //• any external content being protected by the signature, if the eContent element of the encapContentInfo is omitted 
            ISignable externalContent = null;
            if (aSignedData.encapContentInfo.eContent == null)
            {
                //externalContent = (byte[])getParameters()[AllEParameters.P_EXTERNAL_CONTENT];
                try
                {
                    externalContent = ((ISignable)getParameters()[AllEParameters.P_EXTERNAL_CONTENT]);
                }
                catch (Exception e)
                {
                    externalContent = null;
                }
                if (externalContent == null)
                    return null;
            }

            //• the Certificates and crls elements of the SignedData sequence, when present;
            byte[] certs = _certificatesEncodedAl(aSignedData.certificates);

            byte[] crls = _crlsEncodedAl(aSignedData.crls);

            //• all data elements in the SignerInfo sequence including all signed and unsigned attributes.
            byte[] signerInfo = _signerInfoEncodedAl(aSignerInfo);

            if (encapsulatedCI == null || signerInfo == null)
                return null;

            CombinedInputStream cis = new CombinedInputStream();
            byte[] allContent = null;
            if (externalContent != null)
            {
                cis.addInputStream(new MemoryStream(encapsulatedCI));
                try
                {
                    cis.addInputStream(externalContent.getAsInputStream());
                }
                catch (IOException e)
                {
                    throw new CMSSignatureException(e.Message, e);
                }

            }
            else
            {
                cis.addInputStream(new MemoryStream(encapsulatedCI));

            }


            byte[] icerikTemp123 = null;
            if (certs != null && crls != null)
            {
                //byte[] icerikTemp12 = _byteArrBirlestir(allContent, certs);
                //icerikTemp123 = _byteArrBirlestir(icerikTemp12, crls);
                cis.addInputStream(new MemoryStream(certs));
                cis.addInputStream(new MemoryStream(crls));

            }
            else if (certs == null && crls != null)
            {
                //icerikTemp123 = _byteArrBirlestir(allContent, crls);
                cis.addInputStream(new MemoryStream(crls));
            }
            else if (certs != null && crls == null)
            {
                //icerikTemp123 = _byteArrBirlestir(allContent, certs);
                cis.addInputStream(new MemoryStream(certs));
            }

            //byte[] icerikTemp1234 = _byteArrBirlestir(icerikTemp123, signerInfo);
            //return icerikTemp1234;
            cis.addInputStream(new MemoryStream(signerInfo));

            return cis;

        }
        private Stream _esaZDv3IcerikBul(SignedData aSignedData, SignerInfo aSignerInfo, DigestAlg aDigestAlg)
        {
            //• the encapContentInfo content type element of the SignedData sequence
            byte[] encapsulatedCIContentType = _encapsulatedContentInfoContentTypeEncodedAl(aSignedData);

            //• any external content being protected by the signature, if the eContent element of the encapContentInfo is omitted 
            ISignable externalContent = null;
            if (aSignedData.encapContentInfo.eContent == null)
            {
                try
                {
                    externalContent = ((ISignable)getParameters()[AllEParameters.P_EXTERNAL_CONTENT]);
                }
                catch (Exception e)
                {
                    externalContent = null;
                }
                if (externalContent == null)
                    return null;
            }

            // Fields version, sid, digestAlgorithm, signedAttrs, signatureAlgorithm, and
            //signature within the SignedData.signerInfos’s item corresponding to the signature being archive
            //time-stamped, in their order of appearance.
            byte[] signerInfo = _signerInfoItemsEncodedAl(aSignerInfo);

            byte[] atsHashIndex = _atsHashIndexEncodedAl();

            if (encapsulatedCIContentType == null || signerInfo == null)
                return null;

            CombinedInputStream cis = new CombinedInputStream();

            cis.addInputStream(new MemoryStream(encapsulatedCIContentType));
            if (externalContent != null)
            {
                try
                {
                    cis.addInputStream(new MemoryStream(externalContent.getMessageDigest(aDigestAlg)));
                }
                catch (Exception e)
                {
                    throw new ESYARuntimeException("Error while digesting external content", e);
                }
            }
            else
            {
                try
                {
                    cis.addInputStream(new MemoryStream(DigestUtil.digest(aDigestAlg, aSignedData.encapContentInfo.eContent.mValue)));
                }
                catch (Exception e)
                {
                    throw new ESYARuntimeException("Error while digesting content value", e);
                }
            }

            cis.addInputStream(new MemoryStream(signerInfo));
            cis.addInputStream(new MemoryStream(atsHashIndex));
            return cis;
        }
        private byte[] _signedDataBytesAl(byte[] contentInfoBytes)
        {
            try
            {

                Asn1BerDecodeBuffer buffer = new Asn1BerDecodeBuffer(contentInfoBytes);

                int llen = matchTag(buffer, Asn1Tag.SEQUENCE);
                Asn1BerDecodeContext _context = new Asn1BerDecodeContext(buffer, llen);
                IntHolder elemLen = new IntHolder();

                if (_context.MatchElemTag(Asn1Tag.UNIV, Asn1Tag.PRIM, 6, elemLen, false))
                {
                    Asn1ObjectIdentifier contentType = new Asn1ObjectIdentifier();
                    contentType.Decode(buffer, true, elemLen.mValue);
                }
                else throw new Asn1MissingRequiredException(buffer);

                if (_context.MatchElemTag(Asn1Tag.CTXT, Asn1Tag.CONS, 0, elemLen, true))
                {
                    Asn1OpenType content = new Asn1OpenType();
                    content.Decode(buffer, true, 0);
                    if (elemLen.mValue == Asn1Status.INDEFLEN)
                    {
                        matchTag(buffer, Asn1Tag.EOC);
                    }
                    return content.mValue;
                }
                else throw new Asn1MissingRequiredException(buffer);
            }
            catch (Exception e)
            {
                return null;
            }
        }

        private byte[] _encapsulatedContentEncodedAl(SignedData aSignedData)
        {
            Asn1DerEncodeBuffer buffer = new Asn1DerEncodeBuffer();
            try
            {
                aSignedData.encapContentInfo.Encode(buffer);
            }
            catch (Exception x)
            {
                throw new ESYARuntimeException("Cant ENCODE encapsulatedCI for timestamp");
            }
            return buffer.MsgCopy;
        }

        private byte[] _encapsulatedContentInfoContentTypeEncodedAl(SignedData aSignedData)
        {
            Asn1DerEncodeBuffer buffer = new Asn1DerEncodeBuffer();
            try
            {
                aSignedData.encapContentInfo.eContentType.Encode(buffer);
            }
            catch (Exception x)
            {
                throw new ESYARuntimeException("Cant ENCODE encapsulatedCI for timestamp");
            }
            return buffer.MsgCopy;
        }

        private byte[] _atsHashIndexEncodedAl()
        {
            Asn1DerEncodeBuffer buffer = new Asn1DerEncodeBuffer();
            try
            {
                EAttribute atsHashIndexAttr = mSignedData.getSignerInfo(0).getUnsignedAttribute(AttributeOIDs.id_aa_ATSHashIndex)[0];
                EATSHashIndex atsHashIndex = new EATSHashIndex(atsHashIndexAttr.getValue(0));
                atsHashIndex.getObject().Encode(buffer);
            }
            catch (Exception aEx)
            {
                throw new ESYARuntimeException("Error while getting ats-hash-index attribute", aEx);
            }
            return buffer.MsgCopy;
        }
        /*
        private byte[] _encapsulatedContentEncodedAl(byte[] aSignedDataBytes)
        {
            try
            {
                Asn1BerDecodeBuffer buffer = new Asn1BerDecodeBuffer(aSignedDataBytes);

                int llen = matchTag(buffer, Asn1Tag.SEQUENCE);


                // decode SEQUENCE

                Asn1BerDecodeContext _context =
                    new Asn1BerDecodeContext(buffer, llen);

                IntHolder elemLen = new IntHolder();

                // decode version

                if (_context.MatchElemTag(Asn1Tag.UNIV, Asn1Tag.PRIM, 2, elemLen, false))
                {
                    CMSVersion version = new CMSVersion();
                    version.Decode(buffer, true, elemLen.mValue);
                }
                else throw new Asn1MissingRequiredException(buffer);

                // decode digestAlgorithms

                if (_context.MatchElemTag(Asn1Tag.UNIV, Asn1Tag.CONS, 17, elemLen, false))
                {
                    DigestAlgorithmIdentifiers digestAlgorithms = new DigestAlgorithmIdentifiers();
                    digestAlgorithms.Decode(buffer, true, elemLen.mValue);
                }
                else throw new Asn1MissingRequiredException(buffer);

                // decode encapContentInfo

                int start = buffer.ByteCount;

                if (_context.MatchElemTag(Asn1Tag.UNIV, Asn1Tag.CONS, 16, elemLen, false))
                {
                    EncapsulatedContentInfo encapContentInfo = new EncapsulatedContentInfo();
                    encapContentInfo.Decode(buffer, true, elemLen.mValue);
                }
                else throw new Asn1MissingRequiredException(buffer);

                int end = buffer.ByteCount;

                //returns the encapsulated content info
                byte[] encContent = new byte[end - start];
                Array.Copy(aSignedDataBytes, start, encContent, 0, end - start);
                return encContent;
            }
            catch (Exception e)
            {
                return null;
            }

        }
        */

        protected int matchTag(Asn1BerDecodeBuffer paramAsn1BerDecodeBuffer, Asn1Tag paramAsn1Tag)
        {
            return matchTag(paramAsn1BerDecodeBuffer, paramAsn1Tag.mClass, paramAsn1Tag.mForm, paramAsn1Tag.mIDCode);
        }

        protected int matchTag(Asn1BerDecodeBuffer paramAsn1BerDecodeBuffer, short paramShort1, short paramShort2, int paramInt)
        {
            IntHolder localIntHolder = new IntHolder();

            Asn1Tag localAsn1Tag = new Asn1Tag();

            if (paramAsn1BerDecodeBuffer.MatchTag(paramShort1, paramShort2, paramInt, localAsn1Tag, localIntHolder))
            {
                return localIntHolder.mValue;
            }

            throw new Asn1TagMatchFailedException(paramAsn1BerDecodeBuffer, new Asn1Tag(paramShort1, paramShort2, paramInt), localAsn1Tag);
        }



        private byte[] _certificatesEncodedAl(CertificateSet aCerts)
        {
            try
            {
                Asn1DerEncodeBuffer encBuf = new Asn1DerEncodeBuffer();
                int len = aCerts.Encode(encBuf, false);
                encBuf.EncodeTagAndLength(Asn1Tag.CTXT, Asn1Tag.CONS, 0, len);
                return encBuf.MsgCopy;
            }
            catch (Exception ex)
            {
                return null;
            }
        }

        private byte[] _crlsEncodedAl(RevocationInfoChoices aCRLs)
        {
            try
            {
                if (aCRLs == null)
                    return null;
                Asn1DerEncodeBuffer encBuf = new Asn1DerEncodeBuffer();
                int len = aCRLs.Encode(encBuf, false);
                encBuf.EncodeTagAndLength(Asn1Tag.CTXT, Asn1Tag.CONS, 1, len);
                return encBuf.MsgCopy;
            }
            catch (Exception ex)
            {
                return null;
            }
        }

        private byte[] _signerInfoEncodedAl(SignerInfo aSignerInfo)
        {
            /**
             * archive-time-stamp Attribute Definition(etsi ts 101 733 V1.7.4)
             * The ArchiveTimeStamp will be added as an unsigned attribute in the SignerInfo sequence. For the validation of
             * one ArchiveTimeStamp, the data elements of the SignerInfo must be concatenated, excluding all later
             * ArchivTimeStampToken attributes.
             */
            try
            {
                TSTInfo mainTS = _tstInfoBul(mSignedData.getObject());

                Attribute[] attrList = aSignerInfo.unsignedAttrs.elements;
                List<Attribute> yeniAttrList = new List<Attribute>();

                foreach (Attribute attr in attrList)
                {
                    if (!attr.type.Equals(AttributeOIDs.id_aa_ets_archiveTimestamp)
                        && !attr.type.Equals(AttributeOIDs.id_aa_ets_archiveTimestampV2)
                        && !attr.type.Equals(AttributeOIDs.id_aa_ets_archiveTimestampV3))
                        yeniAttrList.Add(attr);
                    else
                    {
                        TSTInfo tstinfo = _tstInfoBul(attr);
                        if (tstinfo.genTime.Fraction.Length > 3)
                        {
                            tstinfo.genTime.Fraction = tstinfo.genTime.Fraction.Substring(0, 3);
                        }
                        if (mainTS.genTime.Fraction.Length > 3)
                        {
                            mainTS.genTime.Fraction = mainTS.genTime.Fraction.Substring(0, 3);
                        }
                        // DateTime tstInfoGenTime = new DateTime(tstinfo.genTime.Year, tstinfo.genTime.Month, tstinfo.genTime.Day, tstinfo.genTime.Hour, tstinfo.genTime.Minute, tstinfo.genTime.Second, tstinfo.genTime.UTC ? DateTimeKind.Utc : DateTimeKind.Local);
                        // DateTime mainTsGenTime = new DateTime(mainTS.genTime.Year, mainTS.genTime.Month, mainTS.genTime.Day, mainTS.genTime.Hour, mainTS.genTime.Minute, mainTS.genTime.Second, mainTS.genTime.UTC ? DateTimeKind.Utc : DateTimeKind.Local);
                        if (tstinfo.genTime.GetTime() < mainTS.genTime.GetTime()) //if (tstInfoGenTime < mainTsGenTime)
                            yeniAttrList.Add(attr);
                    }
                }

                aSignerInfo.unsignedAttrs.elements = yeniAttrList.ToArray();

                Asn1DerEncodeBuffer encBuf = new Asn1DerEncodeBuffer();
                aSignerInfo.Encode(encBuf, false);
                aSignerInfo.unsignedAttrs.elements = attrList;

                return encBuf.MsgCopy;
            }
            catch (Exception aEx)
            {
                logger.Fatal("Encapsulated content info okuanmadi" + aEx);
                return null;
            }
        }
        private byte[] _signerInfoItemsEncodedAl(SignerInfo aSignerInfo)
        {
            try
            {
                Asn1DerEncodeBuffer encBuf = new Asn1DerEncodeBuffer();
                int len;

                // encode signature
                len = aSignerInfo.signature.Encode(encBuf, true);

                // encode signatureAlgorithm
                len = aSignerInfo.signatureAlgorithm.Encode(encBuf, true);

                // encode signedAttrs
                if (aSignerInfo.signedAttrs != null)
                {
                    len = aSignerInfo.signedAttrs.Encode(encBuf, false);
                    len += encBuf.EncodeTagAndLength(Asn1Tag.CTXT, Asn1Tag.CONS, 0, len);
                }

                // encode digestAlgorithm
                len = aSignerInfo.digestAlgorithm.Encode(encBuf, true);

                // encode sid
                len = aSignerInfo.sid.Encode(encBuf, true);

                // encode version
                len = aSignerInfo.version.Encode(encBuf, true);

                return encBuf.MsgCopy;
            }
            catch (Exception aEx)
            {
                logger.Error("Encapsulated content info okuanmadi" + aEx.Message);
                return null;
            }
        }
        private byte[] _byteArrBirlestir(byte[] b1, byte[] b2)
        {
            byte[] b3 = new byte[b1.Length + b2.Length];
            Array.Copy(b1, 0, b3, 0, b1.Length);
            Array.Copy(b2, 0, b3, b1.Length, b2.Length);
            return b3;
        }


        private TSTInfo _tstInfoBul(Attribute aAttr)
        {
            ContentInfo ci = new ContentInfo();
            Asn1DerDecodeBuffer decBuf = new Asn1DerDecodeBuffer(_getAttributeValue(aAttr));
            ci.Decode(decBuf);
            SignedData sd = new SignedData();
            decBuf.Reset();
            decBuf = new Asn1DerDecodeBuffer(ci.content.mValue);
            sd.Decode(decBuf);
            return _tstInfoBul(sd);
        }

        private TSTInfo _tstInfoBul(SignedData aSD)
        {
            TSTInfo tstInfo = new TSTInfo();
            Asn1DerDecodeBuffer decBuf = new Asn1DerDecodeBuffer(aSD.encapContentInfo.eContent.mValue);
            tstInfo.Decode(decBuf);
            return tstInfo;
        }
    }
}

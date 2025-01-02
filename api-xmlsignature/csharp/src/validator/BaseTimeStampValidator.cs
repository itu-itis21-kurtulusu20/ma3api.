using System;
using System.Collections.Generic;
using log4net;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.common.util;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.timestamp;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.vrefs;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.util;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.validator
{
    using Logger = ILog;


    /// <summary>
    /// <p>Base class for BES TimeStamp validator objects.
    /// </summary>
    /// <seealso cref= tr.gov.tubitak.uekae.esya.api.xmlsignature.validator.IndividualDataObjectsTimeStampValidator </seealso>
    /// <seealso cref= tr.gov.tubitak.uekae.esya.api.xmlsignature.validator.AllDataObjectsTimeStampValidator
    /// 
    /// @author ahmety
    /// date: Oct 9, 2009 </seealso>
    public abstract class BaseTimeStampValidator : Validator
    {
        public static readonly int MILLIS_IN_SECONDS = 1000;
        //	public abstract tr.gov.tubitak.uekae.esya.api.xmlsignature.ValidationResult validate(tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignature aSignature, ECertificate aCertificate);
        private static readonly Logger logger = LogManager.GetLogger(typeof (BaseTimeStampValidator));
        internal abstract string TSNodeName { get; }

        #region Validator Members

        public virtual ValidationResult validate(XMLSignature aSignature, ECertificate aCertificate)
        {
            List<XAdESTimeStamp> tsNodes = getTimeStampNodes(aSignature);
            // if no such thing exists return null = no validation
            if (tsNodes == null || tsNodes.Count < 1)
            {
                return null;
            }

            var result = new ValidationResult(GetType());

            foreach (XAdESTimeStamp tsNode in tsNodes)
            {
                logger.Debug("Validate " + tsNode.LocalName + " for signature " + aSignature.Id);
                byte[] bytesForTimeStamp = tsNode.getContentForTimeStamp(aSignature);

                // todo? : xml timestampler su an degerlendirilmiyor!
                if (tsNode.EncapsulatedTimeStampCount < 1)
                {
                    return new ValidationResult(ValidationResultType.INVALID,
                                                I18n.translate("validation.check.timestamp", TSNodeName),
                                                I18n.translate("validation.timestamp.cantFind", TSNodeName),
                                                null, GetType());
                }

                //For each time-stamp token encapsulated by the property
                for (int index = 0; index < tsNode.EncapsulatedTimeStampCount; index++)
                {
                    EncapsulatedTimeStamp ets = tsNode.getEncapsulatedTimeStamp(index);
                    try
                    {
                        /* todo
                        Verify the signature present within the time-stamp token. Rules
                        for acceptance of the validity of the signature within the
                        time-stamp, involving trust decisions, are out of the scope of
                        the present document.
                        */


                        // check if it is Time Stamp !
                        if (!ets.TimeStamp)
                        {
                            return new ValidationResult(ValidationResultType.INVALID,
                                                        I18n.translate("validation.check.timestamp", TSNodeName),
                                                        I18n.translate("validation.timestamp.invalidFormat", TSNodeName, index),
                                                        null, GetType());
                        }


                        /*
                        compute the digest of the resulting octet stream using
                        the algorithm indicated in the time-stamp token and
                        check if it is the same as the digest present there.
                        */
                        byte[] hash = KriptoUtil.digest(bytesForTimeStamp, ets.DigestAlgorithm);

                        if (!ArrayUtil.Equals(hash, ets.DigestValue))
                        {
                            return new ValidationResult(ValidationResultType.INVALID,
                                                        I18n.translate("validation.check.timestamp", TSNodeName),
                                                        I18n.translate("validation.timestamp.invalidDigest", TSNodeName, index),
                                                        null, GetType());
                        }

                        /*
                        Check for coherence in the value of the times indicated in the
                        time-stamp tokens. All the time instants have to be previous to
                        the times indicated within the time-stamp tokens enclosed within
                        all the rest of time-stamp container properties except
                        IndividualDataObjectsTimeStamp.
                        */
                        bool coherent = checkCoherence(result, ets.Time, ets, index, tsNode, aSignature, aCertificate);
                        if (!coherent)
                        {
                            result.setStatus(ValidationResultType.INVALID,
                                             I18n.translate("validation.check.timestamp", TSNodeName),
                                             I18n.translate("validation.timestamp.incoherent"), null);
                            return result;
                        }

                        //if (aSignature.Context.ValidateCertificates)
                        //{
                        ValidationResult vri = verifySignature(aSignature, tsNode, ets);
                        result.addItem(vri);
                        if (vri.getType() != ValidationResultType.VALID)
                        {
                            result.setStatus(ValidationResultType.INVALID,
                                             I18n.translate("validation.check.timestamp", TSNodeName),
                                             I18n.translate("validation.timestamp.error", TSNodeName, index), null);
                            return vri;
                        }
                        //}

                        index++;
                    }
                    catch (Exception x)
                    {
                        throw new XMLSignatureException(x, "validation.timestamp.error", TSNodeName, index);
                    }
                }
            }

            result.setStatus(ValidationResultType.VALID,
                             I18n.translate("validation.check.timestamp", TSNodeName),
                             I18n.translate("validation.timestamp.valid", TSNodeName), null);
            return result;
        }

        public virtual string Name
        {
            get { return GetType().Name; }
        }

        #endregion

        internal abstract List<XAdESTimeStamp> getTimeStampNodes(XMLSignature aSignature);

        protected internal virtual ValidationResult verifySignature(XMLSignature signature,
                                                                    XAdESTimeStamp aXAdESTimeStamp,
                                                                    EncapsulatedTimeStamp aTimeStamp)
        {
            var result = new ValidationResult(GetType());

            try
            {
                XAdESTimeStamp next = getNextTimeStamp(aXAdESTimeStamp, signature);
                var validator = new EncapsulatedTimeStampValidator(signature, aXAdESTimeStamp);
                DateTime? validationDate;
                if (next == null)
                    validationDate = null;
                else
                    validationDate = next.getEncapsulatedTimeStamp(0).Time;

                return validator.verify(aTimeStamp, validationDate);
            }
            catch (Exception exc)
            {
                logger.Error("Cant verify Timestamp signature", exc);
                result.setStatus(ValidationResultType.INCOMPLETE,
                                 I18n.translate("validation.check.timestamp", TSNodeName),
                                 I18n.translate("validation.timestamp.signature.verificationError"), exc.Message);
                return result;
            }
        }

        /**
        * Each timestamp is validated at next timestamp time.
        */
        internal abstract XAdESTimeStamp getNextTimeStamp(XAdESTimeStamp timestamp, XMLSignature signature);

        /// <summary>
        /// Compare current timestamp to other time instants. Note that by design,
        /// time stamp validators only check time instants supposed to be before
        /// checked timestamp
        /// </summary>
        /// <param name="aResult">               check result will be added here </param>
        /// <param name="aTime">                 current timestamps time </param>
        /// <param name="aTimeStampToCheck">     current timestamp </param>
        /// <param name="aTimeStampIndex">       current timestamps order in container timestamp node </param>
        /// <param name="aContainerTimeStamp">   container node </param>
        /// <param name="aSignature">            container signature </param>
        /// <param name="aCertificate">          signer certificate </param>
        /// <returns> validity info about timesatmp time </returns>
        /// <exception cref="XMLSignatureException"> if unexpected  problems arise  </exception>
        internal abstract bool checkCoherence(ValidationResult aResult, DateTime aTime,
                                              EncapsulatedTimeStamp aTimeStampToCheck, int aTimeStampIndex,
                                              XAdESTimeStamp aContainerTimeStamp, XMLSignature aSignature,
                                              ECertificate aCertificate);

        // utility methods for overriders
        protected internal virtual bool checkTimestampVsSigningTime(DateTime aTime, int aTSIndex,
                                                                    XMLSignature aSignature, ValidationResult aResult)
        {
            SignedSignatureProperties ssp = aSignature.QualifyingProperties.SignedSignatureProperties;
            int signingTimeTolerance = aSignature.Context.Config.ValidationConfig.getSignatureTimeToleranceInSeconds();
      
            if (ssp != null && ssp.SigningTime != null)
            {              
                    long stsTimeInLong = aTime.Ticks / 10000;
                    long signingTimeInLong = ssp.SigningTime.Value.Ticks / 10000;
                    int signingTimeToleranceInMilliSeconds = signingTimeTolerance * MILLIS_IN_SECONDS;
                    long signingTimeFlex = signingTimeInLong - signingTimeToleranceInMilliSeconds;

                    if (stsTimeInLong < signingTimeFlex)
                    {
                        aResult.addItem(new ValidationResult(ValidationResultType.INVALID,
                            I18n.translate("validation.check.timestamp", TSNodeName),
                            I18n.translate("validation.timestamp.cantBeBeforeSigningTime",
                                TSNodeName, aTSIndex, aTime, ssp.SigningTime.Value), null, GetType()));
                        return false;
                    }                  
            }
            // since this methods return only warning, return value is always true
            return true;
        }


        protected internal virtual bool checkTimestampVsSigningCertificate(DateTime aTime, int aTSIndex,
                                                                           ECertificate aCertificate,
                                                                           ValidationResult aResult)
        {
            DateTime certStartDate = aCertificate.getNotBefore().Value;
            DateTime certEndDate = aCertificate.getNotAfter().Value;

            if (aTime > certEndDate || aTime < certStartDate)
            {
                aResult.addItem(new ValidationResult(ValidationResultType.INVALID,
                                                     I18n.translate("validation.check.timestamp", TSNodeName),
                                                     I18n.translate("validation.timestamp.notWithinCertificatePeriod",
                                                                    TSNodeName, aTSIndex, aTime),
                                                     null, GetType()));
                return false;
            }
            return true;
        }

        protected internal virtual bool checkTimestampVsDataObjectTimestamps(DateTime aTime, int aTSIndex,
                                                                             XMLSignature aSignature,
                                                                             ValidationResult aResult)
        {
            bool valid = true;

            SignedDataObjectProperties sdop = aSignature.QualifyingProperties.SignedDataObjectProperties;
            if (sdop == null)
            {
                return valid;
            }

            // all data object timestamps
            for (int i = 0; i < sdop.AllDataObjectsTimeStampCount; i++)
            {
                AllDataObjectsTimeStamp previousNode = sdop.getAllDataObjectsTimeStamp(i);
                for (int j = 0; j < previousNode.EncapsulatedTimeStampCount; j++)
                {
                    EncapsulatedTimeStamp previousTS = previousNode.getEncapsulatedTimeStamp(j);
                    valid &= checkTimestampVsPreviousTimestamp(aTime, aTSIndex, previousTS, previousNode, aResult);
                }
            }
            // individual data object timestamps
            for (int i = 0; i < sdop.IndividualDataObjectsTimeStampCount; i++)
            {
                IndividualDataObjectsTimeStamp previousNode = sdop.getIndividualDataObjectsTimeStamp(i);
                for (int j = 0; j < previousNode.EncapsulatedTimeStampCount; j++)
                {
                    EncapsulatedTimeStamp previousTS = previousNode.getEncapsulatedTimeStamp(j);
                    valid &= checkTimestampVsPreviousTimestamp(aTime, aTSIndex, previousTS, previousNode, aResult);
                }
            }
            return valid;
        }

        protected internal virtual bool checkTimestampVsRefTimestamps(DateTime aTime, int aTSIndex,
                                                                      XMLSignature aSignature, ValidationResult aResult)
        {
            bool valid = true;
            UnsignedSignatureProperties usp = aSignature.QualifyingProperties.UnsignedSignatureProperties;
            if (usp == null)
            {
                return valid;
            }

            // all data object timestamps
            for (int i = 0; i < usp.RefsOnlyTimeStampCount; i++)
            {
                RefsOnlyTimeStamp previousNode = usp.getRefsOnlyTimeStamp(i);
                for (int j = 0; j < previousNode.EncapsulatedTimeStampCount; j++)
                {
                    EncapsulatedTimeStamp previousTS = previousNode.getEncapsulatedTimeStamp(j);
                    valid &= checkTimestampVsPreviousTimestamp(aTime, aTSIndex, previousTS, previousNode, aResult);
                }
            }
            // individual data object timestamps
            for (int i = 0; i < usp.SigAndRefsTimeStampCount; i++)
            {
                SigAndRefsTimeStamp previousNode = usp.getSigAndRefsTimeStamp(i);
                for (int j = 0; j < previousNode.EncapsulatedTimeStampCount; j++)
                {
                    EncapsulatedTimeStamp previousTS = previousNode.getEncapsulatedTimeStamp(j);
                    valid &= checkTimestampVsPreviousTimestamp(aTime, aTSIndex, previousTS, previousNode, aResult);
                }
            }
            return valid;
        }

        protected internal virtual bool checkTimestampVsSignatureTimestamp(DateTime aTime, int aTSIndex,
                                                                           XMLSignature aSignature,
                                                                           ValidationResult aResult)
        {
            bool valid = true;
            UnsignedSignatureProperties usp = aSignature.QualifyingProperties.UnsignedSignatureProperties;
            if (usp == null)
            {
                return valid;
            }

            for (int i = 0; i < usp.SignatureTimeStampCount; i++)
            {
                SignatureTimeStamp previousNode = usp.getSignatureTimeStamp(i);
                for (int j = 0; j < previousNode.EncapsulatedTimeStampCount; j++)
                {
                    EncapsulatedTimeStamp previousTS = previousNode.getEncapsulatedTimeStamp(j);
                    valid &= checkTimestampVsPreviousTimestamp(aTime, aTSIndex, previousTS, previousNode, aResult);
                }
            }
            return valid;
        }


        protected internal virtual bool checkTimestampVsPreviousTimestamp(DateTime aTime, int aTSIndex,
                                                                          EncapsulatedTimeStamp aPreviousTS,
                                                                          XAdESTimeStamp aPreviousNode,
                                                                          ValidationResult aResult)
        {
            DateTime previousTSTime;
            try
            {
                previousTSTime = aPreviousTS.Time;
            }
            catch (Exception x)
            {
                aResult.addItem(new ValidationResult(ValidationResultType.INVALID,
                                                     I18n.translate("validation.check.timestamp", TSNodeName),
                                                     // todo i18n
                                                     x.Message + " : " + aPreviousNode.LocalName,
                                                     null, GetType()));
                return false;
            }

            if (aTime < previousTSTime)
            {
                aResult.addItem(new ValidationResult(ValidationResultType.INVALID,
                                                     I18n.translate("validation.check.timestamp", TSNodeName),
                                                     I18n.translate("validation.timestamp.cantBeBeforeTimestamp",
                                                                    TSNodeName, aTSIndex, aTime, aPreviousNode.LocalName,
                                                                    previousTSTime),
                                                     null, GetType()));
                return false;
            }
            return true;
        }
    }
}
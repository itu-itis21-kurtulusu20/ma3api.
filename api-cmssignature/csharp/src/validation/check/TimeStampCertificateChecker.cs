using System;
using System.Collections.Generic;
using tr.gov.tubitak.uekae.esya.api.asn.cms;
using tr.gov.tubitak.uekae.esya.api.asn.pkixtsp;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.certificate.validation;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.policy;
using tr.gov.tubitak.uekae.esya.api.cmssignature.attribute;
using tr.gov.tubitak.uekae.esya.api.cmssignature.bundle;
using tr.gov.tubitak.uekae.esya.api.cmssignature.signature;
using tr.gov.tubitak.uekae.esya.api.common;
using tr.gov.tubitak.uekae.esya.api.signature.certval;

namespace tr.gov.tubitak.uekae.esya.api.cmssignature.validation.check
{
    /**
 * Checks the certificate of timestamp server
 * @author aslihan.kubilay
 *
 */
    public class TimeStampCertificateChecker : BaseChecker
    {
        private readonly ESignedData mSignedData = null;
        private bool closeFinders = false;

        public TimeStampCertificateChecker(ESignedData aSignedData)
        {
            mSignedData = aSignedData;
        }


        //@SuppressWarnings("unchecked")
        //@Override
        protected override bool _check(Signer aSigner, CheckerResult aCheckerResult)
        {
            aCheckerResult.setCheckerName(Msg.getMsg(Msg.TIMESTAMP_CERTIFICATE_CHECKER), typeof(TimeStampCertificateChecker));
            DateTime? tsTime = null;
            DateTime? nextTSTime = null;
            
            try
            {
                //get time of the timestamp
                ETSTInfo tstInfo = new ETSTInfo(mSignedData.getEncapsulatedContentInfo().getContent());
                tsTime = tstInfo.getTime();

                List<DateTime?> timeStampTimes = getTimeStampTimes(aSigner.getSignerInfo());
                int index = timeStampTimes.IndexOf(tsTime);
                if (timeStampTimes.Count > index + 1)
                {
                    //TODO aslında burada ats-hash indexe göre sertifika eklenmeli ve çıkarılmalı. bknz ->ATSHashIndexCollector
                    nextTSTime = timeStampTimes[index + 1];

                    if (isSignatureTypeAboveEST(aSigner.getType()))
                    {
                        Object searchRevData = false;
                        getParameters().TryGetValue(AllEParameters.P_FORCE_STRICT_REFERENCE_USE, out searchRevData);
                        if (!true.Equals(searchRevData))
                            getParameters().TryGetValue(AllEParameters.P_VALIDATION_WITHOUT_FINDERS, out searchRevData);
                        //P_FORCE_STRICT_REFERENCE_USE parametresi set edilmediyse(null ise) bulucular acılacak.
                        if (true.Equals(searchRevData))
                            closeFinders = true;
                        else
                            closeFinders = false;
                    }
                }
                else
                {
                    //signer is EST, one of its parent ESA
                    if (getParameters().ContainsKey(AllEParameters.P_PARENT_ESA_TIME))
                    {
                        Object parentESATimeObj = null;
                        getParameters().TryGetValue(AllEParameters.P_PARENT_ESA_TIME, out parentESATimeObj);
                        if (parentESATimeObj != null){
                            DateTime parentESATime = (DateTime)parentESATimeObj;
                            if (parentESATime.CompareTo(tsTime) > 0) //parentESATime her zaman sonra olmuyor mu?check kalksın?
                                nextTSTime = parentESATime;
                        }
                    }
                    else
                        nextTSTime = DateTime.UtcNow.ToLocalTime();
                }

            }
            catch (Exception aEx)
            {
                aCheckerResult.addMessage(new ValidationMessage("Error while getting time from timestamp"));
                return false;
            }


            //get certificate of the timestamp
            ECertificate cer = mSignedData.getSignerInfo(0).getSignerCertificate(mSignedData.getCertificates());

            if (cer == null)
            {
                ESignerIdentifier signerIdentifier = mSignedData.getSignerInfo(0).getSignerIdentifier();
                cer = findCertFromFinders(signerIdentifier, aCheckerResult);
            }
                

            if (cer == null)
            {
                aCheckerResult.addMessage(new ValidationMessage(Msg.getMsg(Msg.TS_CERTIFICATE_NOT_FOUND)));
                return false;
            }
            else if (!cer.isTimeStampingCertificate())
            {
                aCheckerResult.addMessage(new ValidationMessage(Msg.getMsg(Msg.TS_CERTIFICATE_NOT_QUALIFIED)));
                return false;
            }

            CertificateChecker certificateChecker = new CertificateChecker();
            certificateChecker.setParameters(getParameters());

            bool valid = certificateChecker.checkCertificateAtTime(cer, aCheckerResult, nextTSTime, true, closeFinders);
            if (!valid){
                aCheckerResult.removeMessages();
                valid = certificateChecker.checkCertificateAtTime(cer, aCheckerResult, tsTime, true, closeFinders);
            }
            return valid;

        }

        private List<DateTime?> getTimeStampTimes(ESignerInfo aSignerInfo)
        {
            List<DateTime?> tsTimes = new List<DateTime?>();
            List<EAttribute> tsAttributes;


            tsAttributes = aSignerInfo.getUnsignedAttribute(ContentTimeStampAttr.OID);
            addTimesOfTSs(tsAttributes, tsTimes);

            tsAttributes = aSignerInfo.getUnsignedAttribute(SignatureTimeStampAttr.OID);
            addTimesOfTSs(tsAttributes, tsTimes);

            tsAttributes = aSignerInfo.getUnsignedAttribute(CAdES_C_TimeStampAttr.OID);
            addTimesOfTSs(tsAttributes, tsTimes);

            tsAttributes = aSignerInfo.getUnsignedAttribute(TimeStampedCertsCrlsAttr.OID);
            addTimesOfTSs(tsAttributes, tsTimes);

            tsAttributes = aSignerInfo.getUnsignedAttribute(AttributeOIDs.id_aa_ets_archiveTimestampV2);
            addTimesOfTSs(tsAttributes, tsTimes);

            tsAttributes = aSignerInfo.getUnsignedAttribute(AttributeOIDs.id_aa_ets_archiveTimestamp);
            addTimesOfTSs(tsAttributes, tsTimes);

            tsAttributes = aSignerInfo.getUnsignedAttribute(AttributeOIDs.id_aa_ets_archiveTimestampV3);
            addTimesOfTSs(tsAttributes, tsTimes);

            tsTimes.Sort();

            return tsTimes;
        }

        private void addTimesOfTSs(List<EAttribute> tsAttributes,List<DateTime?> tsTimes)
	    {
		    foreach (EAttribute attr in tsAttributes) 
		    {
			    tsTimes.Add(SignatureTimeStampAttr.toTime(attr));
		    }
	    }

        private ECertificate findCertFromFinders(ESignerIdentifier aSignerIdentifier, CheckerResult aCheckerResult)
        {
            ECertificate cer = null;
            try
            {
                CertificateSearchCriteria searchCriteria = null;
                if (aSignerIdentifier.getIssuerAndSerialNumber() != null)
                {
                    searchCriteria = new CertificateSearchCriteria(aSignerIdentifier.getIssuerAndSerialNumber().getIssuer().stringValue(),
                            aSignerIdentifier.getIssuerAndSerialNumber().getSerialNumber());
                }
                else if (aSignerIdentifier.getSubjectKeyIdentifier() != null)
                {
                    searchCriteria = new CertificateSearchCriteria(aSignerIdentifier.getSubjectKeyIdentifier());
                }

                if (searchCriteria == null)
                    cer = null;
                else
                {
                    ValidationInfoResolver vir = new ValidationInfoResolver();

                    List<ECertificate> certs = (List<ECertificate>)getParameters()[AllEParameters.P_ALL_CERTIFICATES];
                    vir.addCertificates(certs);

                    cer = vir.resolve(searchCriteria)[0];
                }
            }
            catch (InvalidCastException aEx)
            {
                aCheckerResult.addMessage(new ValidationMessage(Msg.getMsg(Msg._0_WRONG_PARAMETER_TYPE_1_, new String[] { "P_POLICY_FILE", "ValidationPolicy" })));
            }
            catch (ESYAException e)
            {
                cer = null;
            }

            return cer;
        }

        public void setCloseFinders(bool closeFinders)
        {
            this.closeFinders = closeFinders;
        }
  }
}

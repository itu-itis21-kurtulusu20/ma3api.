using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using tr.gov.tubitak.uekae.esya.asn.x509;
using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.asn.util;

namespace tr.gov.tubitak.uekae.esya.api.asn.x509
{
    public class ETBSCertificate : BaseASNWrapper<TBSCertificate>
    {
        public ETBSCertificate():base(new TBSCertificate())
        {
            //super(new TBSCertificate());
            mObject.validity = new Validity();
        }

        public ETBSCertificate(long aVersion, BigInteger aSerialNumber,
                               EAlgorithmIdentifier aSignatureAlg, EName aIssuer,
                               DateTime? aNotBefore, DateTime? aNotAfter,
                               EName aSubject, ESubjectPublicKeyInfo aSubjectPublicKeyInfo,
                               byte[] aIssuerUniqueId, byte[] aSubjectUniqueId,
                               EExtensions aExtensions):this()
        {
            //this();
            setVersion(aVersion);
            setSerialNumber(aSerialNumber);
            setSignature(aSignatureAlg);
            setIssuer(aIssuer);
            setNotBefore(aNotBefore);
            setNotAfter(aNotAfter);
            setSubject(aSubject);
            setSubjectPublicKeyInfo(aSubjectPublicKeyInfo);
            setIssuerUniqueID(aIssuerUniqueId);
            setSubjectUniqueID(aSubjectUniqueId);
            setExtensions(aExtensions);
        }

        public ETBSCertificate(TBSCertificate aTBSCertificate)
            :base(aTBSCertificate)
        {
            //super(aTBSCertificate);
        }

        public long getVersion()
        {
            return mObject.version.mValue;
        }

        public void setVersion(long aVersion)
        {
            mObject.version.mValue = aVersion;
        }

        public BigInteger getSerialNumber()
        {
            return mObject.serialNumber.mValue;
        }

        public void setSerialNumber(BigInteger aSerialNumber)
        {
            mObject.serialNumber = new Asn1BigInteger(aSerialNumber);
        }

        public EAlgorithmIdentifier getSignatureAlgorithm()
        {
            return new EAlgorithmIdentifier(mObject.signature);
        }

        public void setSignature(EAlgorithmIdentifier aSignature)
        {
            mObject.signature = aSignature.getObject();
        }

        public EName getIssuer()
        {
            return new EName(mObject.issuer);
        }

        public void setIssuer(EName aIssuer)
        {
            mObject.issuer = aIssuer.getObject();
        }

        public DateTime? getNotBefore()
        {
            return UtilTime.timeToDate(mObject.validity.notBefore);
        }

        public void setNotBefore(DateTime? aNotBefore)
        {
            mObject.validity.notBefore = UtilTime.DateTimeToTimeFor3280(aNotBefore);
        }

        public DateTime? getNotAfter()
        {
            return UtilTime.timeToDate(mObject.validity.notAfter);
        }

        public void setNotAfter(DateTime? aNotAfter)
        {
            mObject.validity.notAfter = UtilTime.DateTimeToTimeFor3280(aNotAfter);
        }

        public EName getSubject()
        {
            return new EName(mObject.subject);
        }

        public void setSubject(EName aSubject)
        {
            mObject.subject = aSubject.getObject();
        }

        public ESubjectPublicKeyInfo getSubjectPublicKeyInfo()
        {
            return new ESubjectPublicKeyInfo(mObject.subjectPublicKeyInfo);
        }

        public void setSubjectPublicKeyInfo(ESubjectPublicKeyInfo aSubjectPublicKeyInfo)
        {
            mObject.subjectPublicKeyInfo = aSubjectPublicKeyInfo.getObject();
        }

        public byte[] getIssuerUniqueID()
        {
            if (mObject.issuerUniqueID == null)
                return null;
            return mObject.issuerUniqueID.mValue;
        }

        public void setIssuerUniqueID(byte[] aIssuerUniqueID)
        {
            mObject.issuerUniqueID = (aIssuerUniqueID == null) ? null : new Asn1BitString(aIssuerUniqueID.Length << 3, aIssuerUniqueID);
        }

        public byte[] getSubjectUniqueID()
        {
            if (mObject.subjectUniqueID == null)
                return null;
            return mObject.subjectUniqueID.mValue;
        }

        public void setSubjectUniqueID(byte[] aSubjectUniqueID)
        {
            mObject.subjectUniqueID = (aSubjectUniqueID == null) ? null : new Asn1BitString(aSubjectUniqueID.Length << 3, aSubjectUniqueID);
        }

        public EExtensions getExtensions()
        {
            if (mObject.extensions == null)
                return null;
            return new EExtensions(mObject.extensions);
        }

        public void setExtensions(EExtensions aExtensions)
        {
            mObject.extensions = (aExtensions == null) ? null : aExtensions.getObject();
        }
    }
}

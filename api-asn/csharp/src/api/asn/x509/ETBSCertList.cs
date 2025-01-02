using System;
using tr.gov.tubitak.uekae.esya.asn.util;
using tr.gov.tubitak.uekae.esya.asn.x509;
using Version = tr.gov.tubitak.uekae.esya.asn.x509.Version;
namespace tr.gov.tubitak.uekae.esya.api.asn.x509
{
    public class ETBSCertList : BaseASNWrapper<TBSCertList>
    {
        public ETBSCertList()
            : base(new TBSCertList())
        {
        }

        public ETBSCertList(TBSCertList aObject)
            : base(aObject)
        {
        }

        public ETBSCertList(long aVersion,
                           EAlgorithmIdentifier aSignatureAlg, EName aIssuer,
                           DateTime? aThisUpdate, DateTime? aNextUpdate,
                           ERevokedCertificateElement[] aRevokedCerts,
                           EExtensions aExtensions)
            : base(
                new TBSCertList(aVersion,
                aSignatureAlg.getObject(),
                aIssuer.getObject(),
                UtilTime.DateTimeToTimeFor3280(aThisUpdate),
                UtilTime.DateTimeToTimeFor3280(aNextUpdate),
                new _SeqOfTBSCertList_revokedCertificates_element(unwrapArray<TBSCertList_revokedCertificates_element, ERevokedCertificateElement>(aRevokedCerts)),
                aExtensions.getObject())
                )
        {
        }

        public EVersion getVersion()
        {
            if (mObject.version == null)
                return null;
            else
                return new EVersion(mObject.version.mValue);
        }

        public void setVersion(long aVersion)
        {
            mObject.version = new Version(aVersion);
        }

        public EAlgorithmIdentifier getSignatureAlgorithm()
        {
            return new EAlgorithmIdentifier(mObject.signature);
        }

        public void setSignatureAlgorithm(EAlgorithmIdentifier aSignature)
        {
            mObject.signature = aSignature.getObject();
        }

        public EName getIssuer()
        {
            return new EName(mObject.issuer);
        }

        public void setIssuer(EName aName)
        {
            mObject.issuer = aName.getObject();
        }

        public ETime thisUpdate()
        {
            return new ETime(mObject.thisUpdate);
        }

        public void setThisUpdate(DateTime? aThisUpdate)
        {
            mObject.thisUpdate = UtilTime.DateTimeToTimeFor3280(aThisUpdate);
        }

        public ETime nextUpdate()
        {
            if (mObject.nextUpdate == null)
                return null;

            return new ETime(mObject.nextUpdate);
        }

        public void setNextUpdate(DateTime? aNextUpdate)
        {
            if (aNextUpdate == null)
                mObject.nextUpdate = null;

            mObject.nextUpdate = UtilTime.DateTimeToTimeFor3280(aNextUpdate);
        }

        public ERevokedCertificateElement[] getRevokedCertificates()
        {
            return wrapArray<ERevokedCertificateElement, TBSCertList_revokedCertificates_element>(mObject.revokedCertificates.elements, typeof(ERevokedCertificateElement));
        }

        public void setRevokedCertificates(ERevokedCertificateElement[] aElements)
        {
            if (aElements == null)
                mObject.revokedCertificates = null;
            else
                mObject.revokedCertificates = new _SeqOfTBSCertList_revokedCertificates_element(unwrapArray<TBSCertList_revokedCertificates_element, ERevokedCertificateElement>(aElements));
        }

        public EExtensions getCrlExtensions()
        {
            if (mObject.crlExtensions == null)
                return null;
            return new EExtensions(mObject.crlExtensions);
        }

        public void setCrlExtensions(EExtensions aExtensions)
        {
            if (aExtensions == null)
                mObject.crlExtensions = null;
            else
                mObject.crlExtensions = aExtensions.getObject();
        }
    }
}

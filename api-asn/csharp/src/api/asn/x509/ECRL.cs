using System;
using System.IO;
using System.Reflection;
using System.Text;
using Com.Objsys.Asn1.Runtime;
using log4net;
using tr.gov.tubitak.uekae.esya.asn.x509;
using tr.gov.tubitak.uekae.esya.asn.util;
namespace tr.gov.tubitak.uekae.esya.api.asn.x509
{
    public class ECRL : BaseASNWrapper<CertificateList>
    {
        private static ILog logger = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);
        //protected CertificateList mCertificateList;

        public ECRL(CertificateList aCertificateList)
            : base(aCertificateList)
        {
        }

        public ECRL(byte[] aCRLArray)
            : base(aCRLArray, new CertificateList())
        {
        }

        public ECRL(FileInfo aFile)
            : base(new FileStream(aFile.FullName, FileMode.Open, FileAccess.Read), new CertificateList())
        {
        }

        public ECRL(Stream aCRLStream)
            : base(aCRLStream, new CertificateList())
        {
        }
        public ECRL(ETBSCertList aTbsCertList, EAlgorithmIdentifier aSignatureAlgorithm, byte[] aSignature)
            : base(new CertificateList(aTbsCertList.getObject(),
                                      aSignatureAlgorithm.getObject(),
                                      new Asn1BitString(aSignature.Length << 3, aSignature)))
        {
        }

        public EName getIssuer()
        {
            return new EName(mObject.tbsCertList.issuer);
        }

        public /*System.Numerics.*/BigInteger getCRLNumber()
        {
            EExtension crlNumberExt = getCRLExtensions().getCRLNumber();

            if (crlNumberExt != null)
            {
                Asn1BigInteger crlNumber = new Asn1BigInteger();
                try
                {
                    crlNumber.Decode(crlNumberExt.getValueAsDecodeBuffer());
                    //return new System.Numerics.BigInteger(crlNumber.mValue.GetData());
                    return crlNumber.mValue;
                    //return crlNumber.mValue;
                }
                catch (Exception aEx)
                {
                    logger.Error("Sil CRL number extension decode edilemedi: " + aEx.Message);
                }
            }
            return null;
        }
        /**
         * @return CRL thisUpdate field
         */
        public DateTime? getThisUpdate()
        {
            return UtilTime.timeToDate(mObject.tbsCertList.thisUpdate);            
        }

        /**
         * @return CRL nextUpdate field
         */
        public DateTime? getNextUpdate()
        {
            return UtilTime.timeToDate(mObject.tbsCertList.nextUpdate);
        }

        public EAlgorithmIdentifier getTBSSignatureAlgorithm()
        {
            return new EAlgorithmIdentifier(mObject.tbsCertList.signature);
        }

        public void setTBSSignatureAlgorithm(EAlgorithmIdentifier aAlgorithm)
        {
            mObject.tbsCertList.signature = aAlgorithm.getObject();
        }

        public ERevokedCertificateElement getRevokedCerticateElement(int aIndex)
        {
            return new ERevokedCertificateElement(
                    mObject.tbsCertList.revokedCertificates.elements[aIndex]
            );
        }

        public int getRevokedCerticateElementCount()
        {
            if (mObject.tbsCertList.revokedCertificates == null)
                return 0;
            return mObject.tbsCertList.revokedCertificates.elements.Length;
        }

        public int getSize()
        {
            _SeqOfTBSCertList_revokedCertificates_element seq = mObject.tbsCertList.revokedCertificates;
            return seq != null ? seq.getLength() : 0;
        }

        public bool contains(ECertificate aCertificate)
        {
            return (indexOf(aCertificate) >= 0);
        }

        public int indexOf(ECertificate aCertificate)
        {
            TBSCertList_revokedCertificates_element[] elements = mObject.tbsCertList.revokedCertificates.elements;
            for (int i = 0; i < elements.Length; i++)
            {
                TBSCertList_revokedCertificates_element element = elements[i];
                if (element.userCertificate.mValue.Equals(aCertificate.getSerialNumber()))
                    return i;
            }
            return -1;
        }

        public EExtensions getCRLExtensions()
        {
            return new EExtensions(mObject.tbsCertList.crlExtensions);
        }


        public ECertificateIssuer getCertificateIssuer(int aIndex)
        {
            ECertificateIssuer defaultCI = new ECertificateIssuer();
            GeneralName gn = new GeneralName();
            gn.Set_directoryName(getIssuer().getObject());
            defaultCI.addElement(gn);

            if (!isIndirectCRL() || getRevokedCerticateElementCount() == 0)
                return defaultCI;

            for (int i = aIndex; i >= 0; i--)
            {
                ECertificateIssuer ci = getRevokedCerticateElement(i).getCrlEntryExtensions().getCertificateIssuer();
                if (ci != null)
                    return ci;
            }
            return defaultCI;
        }

        public void setSignature(byte[] aSignature)
        {
            mObject.signature = new Asn1BitString(aSignature.Length << 3, aSignature);
        }

        public byte[] getSignature()
        {
            return mObject.signature.mValue;
        }

        public void setSignatureAlgorithm(EAlgorithmIdentifier aAlgorithm)
        {
            mObject.signatureAlgorithm = aAlgorithm.getObject();
        }

        public EAlgorithmIdentifier getSignatureAlgorithm()
        {
            return new EAlgorithmIdentifier(mObject.signatureAlgorithm);
        }

        public ETBSCertList getTBSCertList()
        {
            return new ETBSCertList(mObject.tbsCertList);
        }

        public void setTBSCertList(ETBSCertList aTBSCertList)
        {
            mObject.tbsCertList = aTBSCertList.getObject();
        }

        public byte[] getTBSEncodedBytes()
        {
            byte[] imzalanan = null;
            Asn1DerEncodeBuffer encBuf = new Asn1DerEncodeBuffer();
            try
            {
                mObject.tbsCertList.Encode(encBuf);
                imzalanan = encBuf.MsgCopy;
                encBuf.Reset();
            }
            catch (Asn1Exception aEx)
            {
                //logger.error("SİL değeri alınırken hata oluştu.", aEx);
                Console.Error.WriteLine("SIL degeri alinirken hata olustu.", aEx);
            }
            return imzalanan;
        }


        public bool isIndirectCRL()
        {
            EIssuingDistributionPoint idp = getCRLExtensions().getIssuingDistributionPoint();

            /*if (idp == null || !idp.indirectCRL.value)
                return false;

            return true;*/

            return (idp != null && idp.isIndirectCRL());
        }

        //@Override
        public override String ToString()
        {
            StringBuilder builder = new StringBuilder();
            builder.Append("\n----- CRL -----\n")
            .Append("Issuer: ").Append(getIssuer().stringValue()).Append("\n")
            .Append("no: ").Append(getCRLNumber()).Append("\n")
            .Append("valid from ").Append(getThisUpdate().Value.ToLocalTime())
            .Append(" to ").Append(getNextUpdate().Value.ToLocalTime()).Append("\n")
            .Append("contains ").Append(getSize()).Append(" revocation info.");
            return builder.ToString();
        }

    }
}

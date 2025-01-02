using System;
using System.Reflection;
using Com.Objsys.Asn1.Runtime;
using log4net;
using tr.gov.tubitak.uekae.esya.api.asn.pkixtsp;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.common;
using tr.gov.tubitak.uekae.esya.api.common.license;
using tr.gov.tubitak.uekae.esya.asn.cms;
using tr.gov.tubitak.uekae.esya.asn.pkixtsp;
using tr.gov.tubitak.uekae.esya.asn.x509;
using tr.gov.tubitak.uekae.esya.api.asn.cms;

namespace tr.gov.tubitak.uekae.esya.api.infra.tsclient
{
    /**
     * Zaman Damgası Cevabı içerisindeki imzalı veriyi içeren sınıftır.
     * @author muratk
     *
     */
    public class TSCms
    {
        //private static readonly ILog logger = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);
        private readonly ETSTInfo mTstInfo;

        private readonly ESignedData mSignedData;

        /**
	 * Imzali veri yapisini olustur.
	 * @param aCevap Zaman Damgasi cevabi	
	 */
        public TSCms(ETimeStampResponse aCevap)
        {
            if (aCevap == null || aCevap.getContentInfo() == null)
                throw new ESYAException("Imzali veri yapisi null");

            mSignedData = new ESignedData(aCevap.getContentInfo().getContent());
            mTstInfo = new ETSTInfo(mSignedData.getEncapsulatedContentInfo().getContent());

        }


        /**
         * Imzalı veri yapısını oluştur.
         * @param aCevap Zaman Damgası Cevabı         
         */
        public TSCms(byte[] aCevap)
        {
            ETimeStampResponse response = new ETimeStampResponse(aCevap);

            if (aCevap == null || response.getContentInfo() == null)
                throw new ESYAException("Imzali veri yapisi null");

            mSignedData = new ESignedData(response.getContentInfo().getContent());
            mTstInfo = new ETSTInfo(mSignedData.getEncapsulatedContentInfo().getContent());
        }
               

        /**
         * Imzalı veri yapısını oluştur.
         * @param aSignedData Zaman Damgası cevabın içindeki SignedData yapısı
         * @throws com.objsys.asn1j.runtime.Asn1Exception
         * @throws java.io.IOException
         */
        public TSCms(ESignedData aSignedData)
        {
            mSignedData = aSignedData;
            mTstInfo = new ETSTInfo(aSignedData.getEncapsulatedContentInfo().getContent());
        }

        //private void _decode(ContentInfo aCevap)
        //{
        //    if (aCevap == null || aCevap.content == null)
        //        throw new ESYAException("Imzali veri yapisi null");

        //    Asn1DerEncodeBuffer encBuf = new Asn1DerEncodeBuffer();
        //    aCevap.content.Encode(encBuf);

        //    Asn1DerDecodeBuffer decBuf = new Asn1DerDecodeBuffer(encBuf.MsgCopy);
        //    mSignedData.Decode(decBuf);

        //    decBuf = new Asn1DerDecodeBuffer(mSignedData.encapContentInfo.eContent.mValue);

        //    mTstInfo.getObject().Decode(decBuf);
        //}

        ///**
        // * Imzalı verinin byte[] halini verir
        // * @return imzalı veri
        // */
        //public byte[] encode()
        //{
        //    Asn1DerEncodeBuffer encBuf = null;
        //    try
        //    {
        //        encBuf = new Asn1DerEncodeBuffer();
        //        mSignedData.Encode(encBuf);
        //    }
        //    catch (Asn1Exception e)
        //    {
        //        //Buraya gelmesi mumkun degil. Cunku zaten constructor'da parse ediliyor.
        //    }
        //    return encBuf.MsgCopy;
        //}

        /**
         * TSTInfo veri yapısını verir
         * @return ZDTstInfo
         */
        public ETSTInfo getTSTInfo()
        {
            return mTstInfo;
        }


        /**
         * Zaman damgalanan verinin özetini verir
         * @return damgalanan veri özeti
         */
        public byte[] getTimestampedHashedMessage()
        {
            return mTstInfo.getHashedMessage();
        }

        /**
         * Zaman Damgası cevabı imzacısını verir.
         * @return imzacı
         */
        public ESignedData getSignedData()
        {
            return mSignedData;
        }

        ///**
        // * Zaman Damgasi cevabini imzalayan sertifikayi doner.
        // * @return
        // */
        //public ECertificate getTSCertificate()
        //{
        //    if (mSignedData.certificates == null || mSignedData.certificates.elements == null)
        //        return null;

        //    if (mSignedData.certificates.elements[0].ChoiceID == CertificateChoices._CERTIFICATE)
        //        return new ECertificate((Certificate)mSignedData.certificates.elements[0].GetElement());

        //    return null;
        //}
    }
}

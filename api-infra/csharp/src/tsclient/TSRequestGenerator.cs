using System;
using System.Reflection;
using Com.Objsys.Asn1.Runtime;
using log4net;
using tr.gov.tubitak.uekae.esya.api.common;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.crypto.parameters;
using tr.gov.tubitak.uekae.esya.api.crypto.util;
using tr.gov.tubitak.uekae.esya.asn.algorithms;
using tr.gov.tubitak.uekae.esya.asn.pkixtsp;
using tr.gov.tubitak.uekae.esya.asn.x509;
namespace tr.gov.tubitak.uekae.esya.api.infra.tsclient
{
    /*public */class TSRequestGenerator
    {
        private static readonly TimeStampReq_version VERSION = new TimeStampReq_version(TimeStampReq_version.v1);

        private static readonly Asn1ObjectIdentifier REQPOLICY = new Asn1ObjectIdentifier(_pkixtspValues.id_ts_policy);

        //private static readonly AlgorithmIdentifier HASHALG = new AlgorithmIdentifier(_algorithmsValues.sha_1);

        private static readonly ILog LOGGER = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);

        private readonly byte[] mToBeStamped;

        //private int mKullaniciNo;

        //private char[] mParola;

        private String mKimlikBilgisi;

        protected TimeStampReq mTsRequest = null;

        private readonly TSSettings mTsSettings;

        public TSRequestGenerator()
        {
        }

        //internal TSRequestGenerator(byte[] aDamgalanacak, int aKullaniciNo, char[] aParola)
        //{
        //    mToBeStamped = aDamgalanacak;
        //    mKullaniciNo = aKullaniciNo;
        //    mParola = aParola;
        //}
        internal TSRequestGenerator(byte[] toBeStamped, TSSettings aTsSettings)
        {
            this.mToBeStamped = toBeStamped;
            this.mTsSettings = aTsSettings;
        }


        internal byte[] generate()
        {

            MessageImprint messageImprint;
            Asn1BigInteger nonce;
            Asn1Boolean certReq = new Asn1Boolean(true);
            Asn1DerEncodeBuffer buf = new Asn1DerEncodeBuffer();

            // Nonce'u set et.
            nonce = _nonceUret();

            try
            {
                // MessageImprint'i oluştur
                messageImprint = new MessageImprint(mTsSettings.DigestAlg.toAlgorithmIdentifier().getObject(), mToBeStamped);
                // Identity bilgisini oluştur
                if (mTsSettings.isUseIdentity())
                    mKimlikBilgisi = _kimlikBilgisiOlustur(mTsSettings, mToBeStamped);
                // TimeStampReq objesini oluştur
                mTsRequest = new TimeStampReq(VERSION, messageImprint, REQPOLICY, nonce, certReq, null);

                mTsRequest.Encode(buf);

            }
            catch (Exception ex)
            {
                LOGGER.Error("Zaman Damgasi istegi olusturulamadi", ex);
                throw new ESYAException("Zaman Damgasi istegi olusturulamadi", ex);
            }

            return buf.MsgCopy;
        }

        internal String getKimlikBilgisi()
        {
            return mKimlikBilgisi;
        }

        /**
         * Random bir sayı üreten metodtur.
         * 
         * @return Asn1Integer tipinde nonce
         */
        private Asn1BigInteger _nonceUret()
        {
            byte[] nonce = new byte[20];

            RandomUtil.generateRandom(nonce);

            BigInteger bigInteger = new BigInteger();
            bigInteger.SetData(nonce);
            return new Asn1BigInteger(bigInteger);
            //return new Asn1BigInteger(new BigInteger(nonce, 1));    //todo BigInteger pozitif olarak kabul edilecek, emin miyiz??
        }


        internal String _kimlikBilgisiOlustur(TSSettings tsSettings, byte[] aOzet)
        {
            ESYAReqEx passInfo;

            Asn1DerEncodeBuffer buffer = new Asn1DerEncodeBuffer();

            PBEKeySpec pbeKeySpec = new PBEKeySpec(tsSettings.getCustomerPassword().ToCharArray());

            byte[] IV = new byte[16];

            RandomUtil.generateRandom(IV);

            byte[] encMI = _sifreliOzetiAl(pbeKeySpec, IV, aOzet);

            passInfo = new ESYAReqEx(new Asn1Integer(tsSettings.getCustomerID()),
                    new Asn1OctetString(pbeKeySpec.getSalt()),
                    new Asn1Integer(pbeKeySpec.getIterationCount()),
                    new Asn1OctetString(IV),
                    new Asn1OctetString(encMI));

            passInfo.Encode(buffer);
            BigInteger kimlikBilgisi = new BigInteger();
            kimlikBilgisi.SetData(buffer.MsgCopy);
            return kimlikBilgisi.ToString(16);            
            //return Base64.encode(buffer.getMsgCopy());
        }

        /**
         * Hashi PBE ile şifreler
         * 
         * @param aPBEKeySpec  PBE parametreleri
         * @param aHashMsg   açık hash
         * @return şifrelenmiş hash
         * @throws tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException
         */
        private byte[] _sifreliOzetiAl(PBEKeySpec aPBEKeySpec, byte[] aIV, byte[] aHashMsg)
        {            
            // aes 256 icin key length set et...
            aPBEKeySpec.setKeyLength(KeyUtil.getKeyLength(PBEAlg.PBE_AES256_SHA256.getCipherAlg()) / 8);
            byte[] encrypted = CipherUtil.encrypt(PBEAlg.PBE_AES256_SHA256, new ParamsWithIV(aIV), aPBEKeySpec, aHashMsg);
            //byte[] decrypted = CipherUtil.decrypt(PBEAlg.PBE_AES256_SHA256, new ParamsWithIV(aIV), aPBEKeySpec, encrypted);
            return encrypted;           
        }

        internal TimeStampReq getTSRequest()
        {
            return mTsRequest;
        }
    }
}

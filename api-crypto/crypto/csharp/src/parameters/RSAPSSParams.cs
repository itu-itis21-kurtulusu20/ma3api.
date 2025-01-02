using System;
using tr.gov.tubitak.uekae.esya.api.asn.algorithms;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.crypto.exceptions;

namespace tr.gov.tubitak.uekae.esya.api.crypto.parameters
{
    public class RSAPSSParams : IAlgorithmParams
    {
        protected const int TRAILER_DEFAULT = 0x01;

        protected DigestAlg mDigestAlg;
        protected MGF mMGF;
        protected int mSaltLength;
        protected int mTrailerField;

        public static readonly RSAPSSParams DEFAULT = new RSAPSSParams();

        public RSAPSSParams()
        {
            mDigestAlg = DigestAlg.SHA256;
            mMGF = MGF.MGF1;
            mSaltLength = mDigestAlg.getDigestLength();
            mTrailerField = TRAILER_DEFAULT;
        }

        public RSAPSSParams(DigestAlg aDigestAlg)
        {
            init(aDigestAlg, MGF.MGF1, aDigestAlg.getDigestLength(), TRAILER_DEFAULT);
        }

        public RSAPSSParams(DigestAlg aDigestAlg, MGF aMGF, int aSaltLength, int aTrailerField)
        {
            init(aDigestAlg, aMGF, aSaltLength, aTrailerField);
        }



        protected void init(DigestAlg aDigestAlg, MGF aMGF, int aSaltLength, int aTrailerField)
        {
            mDigestAlg = aDigestAlg;
            mMGF = aMGF;
            mSaltLength = aSaltLength;
            mTrailerField = aTrailerField;
        }

        public DigestAlg getDigestAlg()
        {
            return mDigestAlg;
        }

        public void setDigestAlg(DigestAlg aDigestAlg)
        {
            mDigestAlg = aDigestAlg;
        }

        public MGF getMGF()
        {
            return mMGF;
        }

        public void setMGF(MGF aMGF)
        {
            mMGF = aMGF;
        }

        public int getSaltLength()
        {
            return mSaltLength;
        }

        public void setSaltLength(int aSaltLength)
        {
            mSaltLength = aSaltLength;
        }

        public int getTrailerField()
        {
            return mTrailerField;
        }

        public void setTrailerField(int aTrailerField)
        {
            mTrailerField = aTrailerField;
        }


        public byte[] getEncoded()
        {
            try
            {
                ERSASSA_PSS_params pssParams = new ERSASSA_PSS_params(mDigestAlg.toAlgorithmIdentifier(),mMGF.getOID(),mDigestAlg.toAlgorithmIdentifier(), mSaltLength, mTrailerField);
                return pssParams.getEncoded();
            }
            catch (Exception x)
            {
                throw new CryptoException(x.Message, x);
            }

        }

        public override bool Equals(object obj)
        {
            if (!(obj is RSAPSSParams))
                return false;

            RSAPSSParams paramsObj = (RSAPSSParams)obj;

            if (paramsObj.getDigestAlg().getName().Equals(mDigestAlg.getName())
                && paramsObj.getMGF().getName().Equals(mMGF.getName())
                && paramsObj.getSaltLength() == mSaltLength
                && paramsObj.getTrailerField() == mTrailerField)
                return true;
            else
                return false;
        }
   }
}


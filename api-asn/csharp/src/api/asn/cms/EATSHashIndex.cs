using System;
using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.asn.algorithms;
using tr.gov.tubitak.uekae.esya.asn.cms;
using tr.gov.tubitak.uekae.esya.asn.x509;

namespace tr.gov.tubitak.uekae.esya.api.asn.cms
{
    public class EATSHashIndex : BaseASNWrapper<MyATSHashIndex>
    {
        public EATSHashIndex(MyATSHashIndex aObject)
            : base(aObject)
        {
        }

        public EATSHashIndex(byte[] aBytes)
            : base(aBytes, new MyATSHashIndex(aBytes))
        {
        }

        public EAlgorithmIdentifier gethashIndAlgorithm()
        {
            if (mObject.hashIndAlgorithm != null && mObject.hashIndAlgorithm.algorithm != null)
                return new EAlgorithmIdentifier(mObject.hashIndAlgorithm);
            else
                return new EAlgorithmIdentifier(new AlgorithmIdentifier(
                        _algorithmsValues.id_sha256));
        }

        public void sethashIndAlgorithm(EAlgorithmIdentifier digestAlg)
        {
            mObject.hashIndAlgorithm = digestAlg.getObject();
        }

        public Asn1OctetString[] getCertificatesHashIndex()
        {
            return mObject.certificatesHashIndex.elements;
        }

        public Asn1OctetString[] getCrlsHashIndex()
        {
            return mObject.crlsHashIndex.elements;
        }

        public Asn1OctetString[] getUnsignedAttrsHashIndex()
        {
            return mObject.unsignedAttrsHashIndex.elements;
        }
    }

    public class MyATSHashIndex : ATSHashIndex
    {
        private readonly byte[] bytes = null;
        public MyATSHashIndex()
            : base()
        {
        }
        public MyATSHashIndex(byte[] aBytes)
            : base()
        {
            bytes = aBytes;
        }
        public override void Decode
           (Asn1BerDecodeBuffer buffer, bool explicitTagging, int implicitLength)
        {
            try
            {
                base.Decode(buffer, explicitTagging, implicitLength);
            }
            catch (Exception ex)
            {
                buffer = new Asn1BerDecodeBuffer(bytes);
                int llen = (explicitTagging)
                               ? MatchTag(buffer, Asn1Tag.SEQUENCE)
                               : implicitLength;

                Init();

                // decode SEQUENCE

                Asn1BerDecodeContext _context =new Asn1BerDecodeContext(buffer, llen);

                IntHolder elemLen = new IntHolder();

                base.hashIndAlgorithm = null;
                // decode certificatesHashIndex

                if (_context.MatchElemTag(Asn1Tag.UNIV, Asn1Tag.CONS, 16, elemLen, false))
                {
                    certificatesHashIndex = new ATSHashIndex_certificatesHashIndex();
                    certificatesHashIndex.Decode(buffer, true, elemLen.mValue);
                }
                else throw new Asn1MissingRequiredException(buffer);

                // decode crlsHashIndex

                if (_context.MatchElemTag(Asn1Tag.UNIV, Asn1Tag.CONS, 16, elemLen, false))
                {
                    crlsHashIndex = new ATSHashIndex_crlsHashIndex();
                    crlsHashIndex.Decode(buffer, true, elemLen.mValue);
                }
                else throw new Asn1MissingRequiredException(buffer);

                // decode unsignedAttrsHashIndex

                if (_context.MatchElemTag(Asn1Tag.UNIV, Asn1Tag.CONS, 16, elemLen, false))
                {
                    unsignedAttrsHashIndex = new ATSHashIndex_unsignedAttrsHashIndex();
                    unsignedAttrsHashIndex.Decode(buffer, true, elemLen.mValue);
                }
                else throw new Asn1MissingRequiredException(buffer);
            }
        }
    }
	
}

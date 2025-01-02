using Com.Objsys.Asn1.Runtime;
using System.IO;
using System.Linq;
using tr.gov.tubitak.uekae.esya.api.asn.exception;
using tr.gov.tubitak.uekae.esya.asn.cms;


namespace tr.gov.tubitak.uekae.esya.api.asn.cms
{
    public class EContentInfoWithSignedData : Asn1Type
    {
        Asn1ObjectIdentifier contentType;
        ESignedData signedData;

        public EContentInfoWithSignedData(Stream stream)
        {
            bool explicitTagging = true;
            int implicitLength = 0;

            Asn1DerDecodeBuffer buffer = new Asn1DerDecodeBuffer(stream);

            // decode SEQUENCE
            int llen = (explicitTagging) ? MatchTag(buffer, Asn1Tag.SEQUENCE) : implicitLength;

            
            Asn1BerDecodeContext _context = new Asn1BerDecodeContext(buffer, llen);

            IntHolder elemLen = new IntHolder();

            // decode contentType

            if (_context.MatchElemTag(Asn1Tag.UNIV, Asn1Tag.PRIM, 6, elemLen, false))
            {
                contentType = new Asn1ObjectIdentifier();
                contentType.Decode(buffer, true, elemLen.mValue);
            }
            else throw new Asn1MissingRequiredException(buffer);

            if (!contentType.mValue.SequenceEqual(_cmsValues.id_signedData))
                throw new InvalidContentTypeException("Content type is not a signed data. Its oid is " + contentType);

            // decode content

            if (_context.MatchElemTag(Asn1Tag.CTXT, Asn1Tag.CONS, 0, elemLen, true))
            {
                SignedData signedData = new SignedData();
                signedData.Decode(buffer);
                this.signedData = new ESignedData(signedData);
            }
            else throw new Asn1MissingRequiredException(buffer);

            if (explicitTagging && llen == Asn1Status.INDEFLEN)
            {
                MatchTag(buffer, Asn1Tag.EOC);
            }
        }

        public ESignedData getSignedData()
        {
            return this.signedData;
        }

    }
}

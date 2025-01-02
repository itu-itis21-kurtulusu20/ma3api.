package tr.gov.tubitak.uekae.esya.api.asn.cms;

import com.objsys.asn1j.runtime.*;
import tr.gov.tubitak.uekae.esya.api.asn.exception.InvalidContentTypeException;
import tr.gov.tubitak.uekae.esya.asn.cms.SignedData;
import tr.gov.tubitak.uekae.esya.asn.cms._cmsValues;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class EContentInfoWithSignedData extends Asn1Type  {

    Asn1ObjectIdentifier contentType;
    ESignedData signedData;

    public EContentInfoWithSignedData(InputStream inputStream) throws InvalidContentTypeException, IOException {
        boolean explicit = true;
        int implicitLength = 0;

        Asn1DerDecodeBuffer buffer = new Asn1DerDecodeBuffer(inputStream);

        int llen = (explicit) ?
                matchTag (buffer, Asn1Tag.SEQUENCE) : implicitLength;

        // decode SEQUENCE

        Asn1BerDecodeContext _context =
                new Asn1BerDecodeContext (buffer, llen);

        IntHolder elemLen = new IntHolder();

        // decode contentType

        if (_context.matchElemTag (Asn1Tag.UNIV, Asn1Tag.PRIM, 6, elemLen, false)) {
            contentType = new Asn1ObjectIdentifier();
            contentType.decode (buffer, true, elemLen.value);
        }
        else throw new Asn1MissingRequiredException (buffer, "contentType");

        if(!Arrays.equals(contentType.value, _cmsValues.id_signedData))
            throw new InvalidContentTypeException("Content type is not a signed data. Its oid is " + Arrays.toString(contentType.value));

        // decode content

        if (_context.matchElemTag (Asn1Tag.CTXT, Asn1Tag.CONS, 0, elemLen, true)) {
            SignedData signedData = new SignedData();
            signedData.decode(buffer);
            this.signedData = new ESignedData(signedData);
        }
        else throw new Asn1MissingRequiredException (buffer);

        if (!_context.expired()) {
            Asn1Tag _tag = buffer.peekTag ();
            if (_tag.equals (Asn1Tag.UNIV, Asn1Tag.PRIM, 6))
                throw new Asn1SeqOrderException ();

        }
        if (explicit && llen == Asn1Status.INDEFLEN) {
            matchTag(buffer, Asn1Tag.EOC);
        }
    }

    public ESignedData getSignedData(){
        return this.signedData;
    }

}

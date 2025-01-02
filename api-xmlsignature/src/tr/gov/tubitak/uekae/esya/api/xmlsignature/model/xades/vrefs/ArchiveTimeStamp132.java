package tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.vrefs;

import org.w3c.dom.Element;
import tr.gov.tubitak.uekae.esya.api.signature.attribute.TimestampType;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignature;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.document.Document;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.BaseElement;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.Reference;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.SignedInfo;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.XMLObject;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.UnsignedSignatureProperties;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.UnsignedSignaturePropertyElement;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.util.XmlUtil;

import java.io.ByteArrayOutputStream;
import java.util.List;

/**
 * Deprecated in 1.4.1.
 *
 * Only for backwards compatibility. Newly created signatures, should not use this one.
 *
 * @author ahmety
 * date: Mar 28, 2011
 */
public class ArchiveTimeStamp132 extends ArchiveTimeStamp implements UnsignedSignaturePropertyElement
{

    public ArchiveTimeStamp132(Element aElement, Context aContext) throws XMLSignatureException
    {
        super(aElement, aContext);
    }

    @Override
    public byte[] getContentForTimeStamp(XMLSignature aSignature) throws XMLSignatureException
    {
        try {
            // init
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            SignedInfo signedInfo = aSignature.getSignedInfo();

            // add all references resolved
            for (int i=0; i<signedInfo.getReferenceCount(); i++){
                Reference ref = signedInfo.getReference(i);
                Document doc = ref.getTransformedDocument(mCanonicalizationMethod);
                baos.write(doc.getBytes());
            }

            // signedInfo, signatureValue, keyInfo
            //baos.write(signedInfo.getCanonicalizedBytes());
            baos.write(XmlUtil.outputDOM(signedInfo.getElement(), mCanonicalizationMethod));
            baos.write(XmlUtil.outputDOM(aSignature.getSignatureValueElement(), mCanonicalizationMethod));
            if (aSignature.getKeyInfo()!=null)
            baos.write(XmlUtil.outputDOM(aSignature.getKeyInfo().getElement(), mCanonicalizationMethod));

            // Take the unsigned signature properties that appear before the
            // current xadesv141:ArchiveTimeStamp in the order they appear,
            // canonicalize each one and concatenate each
            UnsignedSignatureProperties usp = aSignature.getQualifyingProperties().getUnsignedSignatureProperties();
            List<UnsignedSignaturePropertyElement> unsignedProperties = usp.getProperties();
            for (UnsignedSignaturePropertyElement unsignedProperty : unsignedProperties) {
                if (unsignedProperty.equals(this)){
                    break;
                }
                baos.write(XmlUtil.outputDOM(((BaseElement)unsignedProperty).getElement(), mCanonicalizationMethod));
            }

            // Take any ds:Object element in the signature that is not referenced
            // by any ds:Reference within ds:SignedInfo, except that one containing
            // the QualifyingProperties element
            XMLObject qpObject = aSignature.getQualifyingPropertiesObject();
            for (int j=0; j<aSignature.getObjectCount(); j++){
                XMLObject object = aSignature.getObject(j);
                if (!object.equals(qpObject) && !isObjectReferenced(object, signedInfo)){
                    baos.write(XmlUtil.outputDOM(object.getElement(), mCanonicalizationMethod));
                }
            }

            return baos.toByteArray();
        } catch (Exception x){
            throw new XMLSignatureException(x, "errors.cantDigest", getLocalName());
        }
    }

    private boolean isObjectReferenced(XMLObject aObject, SignedInfo aSignedinfo){
        String objectId = aObject.getId();
        if (objectId==null || objectId.length()==0)
            return false;

        for (int i=0; i<aSignedinfo.getReferenceCount(); i++){
            Reference ref = aSignedinfo.getReference(i);
            String uri = ref.getURI();
            if (uri.charAt(0) == '#' && uri.length()>1){
                if (uri.substring(1).equals(objectId)){
                    return true;
                }
            }
        }
        return false;
    }

    public TimestampType getType() {
        return TimestampType.ARCHIVE_TIMESTAMP;
    }


    @Override
    public String getLocalName()
    {
        return Constants.TAGX_ARCHIVETIMESTAMP;  
    }

    @Override
    public String getNamespace()
    {
        return Constants.NS_XADES_1_3_2;
    }
}


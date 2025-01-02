package tr.gov.tubitak.uekae.esya.api.xmlsignature.validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.ValidationResult;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.ValidationResultType;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignature;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.Reference;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.SignedInfo;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.XMLObject;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.DataObjectFormat;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.QualifyingProperties;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.SignedDataObjectProperties;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.SignedProperties;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.util.I18n;


/**
 * <h3>G.2.2.8 Checking DataObjectFormat</h3>
 *
 * <p>The verifier should check that the <code>ObjectReference</code> element
 * actually references one <code>ds:Reference</code> element from the signature.
 *
 * <p>In addition, should this property refer to a <code>ds:Reference</code>
 * that in turn refers to a <code>ds:Object</code>, the verifier should
 * check the values of attributes <code>MimeType</code> and
 * <code>Encoding</code> as indicated below:
 *
 * <ul>
 * <li>If any of the aforementioned attributes is present in both
 * <code>xades:DataObjectFormat</code> and in <code>ds:Object</code> elements,
 * the verifier should check that their values are equal.
 * <li>If any of the aforementioned attributes is present in
 * <code>xades:DataObjectFormat</code> but NOT in <code>ds:Object</code>,
 * the verifier should act as if this attribute was present within the
 * <code>ds:Object</code> with the same value.
 * </ul>
 *
 * <p>Additional rules governing the acceptance of the XAdES signature as
 * valid or not in the view of the contents of this property are out of the
 * scope of the present document.
 *
 * @author ahmety
 * date: Oct 1, 2009
 */
public class DataObjectFormatValidator implements Validator
{
    private static Logger logger = LoggerFactory.getLogger(DataObjectFormatValidator.class);

    public ValidationResult validate(XMLSignature aSignature, ECertificate certificate) throws XMLSignatureException
    {
        QualifyingProperties qp = aSignature.getQualifyingProperties();
        if (qp==null)
            return null;

        SignedProperties sp = qp.getSignedProperties();
        if (sp==null)
            return null;

        // data object properties
        SignedDataObjectProperties sdop = sp.getSignedDataObjectProperties();
        if (sdop==null)
            return null;

        for (int k=0; k<sdop.getDataObjectFormatCount(); k++)
        {
            DataObjectFormat dof = sdop.getDataObjectFormat(k);
            String ref = dof.getObjectReference();
            String referenceId = ref.substring(1);
            Reference found = null;
            SignedInfo si = aSignature.getSignedInfo();
            for (int i=0; i < si.getReferenceCount(); i++){
                Reference reference = si.getReference(i);
                if (reference.getId()!=null && reference.getId().equals(referenceId)){
                    found = reference;
                    break;
                }
            }
            if (found==null)
            {
                String failMessage = I18n.translate("validation.dataObjectFormat.noReference");
                logger.warn(failMessage);
                return new ValidationResult(ValidationResultType.INVALID,
                                            I18n.translate("validation.check.dataObjectFormat"),
                                            failMessage,
                                            null, getClass());
            }

            //Document doc = Resolver.resolve(found, aSignature.getContext());

            // if object check mime
            if (found.getURI().startsWith("#")){
                String id = found.getURI().substring(1);
                for (int n=0; n<aSignature.getObjectCount(); n++){
                    XMLObject obj = aSignature.getObject(n);
                    if (id.equals(obj.getId())){
                        logger.debug("Referenced data belongs to object ");
                        String mime = obj.getMIMEType();
                        String expectedMime = dof.getMIMEType();
                        if (mime!=null && !mime.equalsIgnoreCase(expectedMime))
                        {
                            String failMessage = I18n.translate("validation.dataObjectFormat.mimeMismatch", expectedMime, mime);
                            logger.warn(failMessage);
                            return new ValidationResult(ValidationResultType.INVALID,
                                                        failMessage,
                                                        I18n.translate("validation.check.dataObjectFormat"),
                                                        null, getClass());
                        }

                        String encoding = obj.getEncoding();
                        String expectedEncoding = dof.getEncoding();
                        if (encoding!=null && !encoding.equalsIgnoreCase(expectedEncoding))
                        {
                            String failMessage = I18n.translate("validation.dataObjectFormat.encodingMismatch", expectedEncoding, encoding);
                            logger.warn(failMessage);
                            return new ValidationResult(ValidationResultType.INVALID, failMessage,
                                                        I18n.translate("validation.check.dataObjectFormat"),
                                                        null, getClass());
                        }
                    }
                }
            }

            return new ValidationResult(ValidationResultType.VALID,
                                        I18n.translate("validation.check.dataObjectFormat"),
                                        I18n.translate("validation.dataObjectFormat.valid"),
                                        null, getClass());
        }

        //DataObjectFormat property not found
        return null;
    }

    public String getName()
    {
        return getClass().getSimpleName();
    }
    
}

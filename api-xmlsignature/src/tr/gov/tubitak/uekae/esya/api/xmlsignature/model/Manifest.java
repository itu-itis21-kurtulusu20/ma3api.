package tr.gov.tubitak.uekae.esya.api.xmlsignature.model;

import org.w3c.dom.Element;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.DigestMethod;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.util.IdGenerator;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>The <code>Manifest</code> element provides a list of
 * <code>Reference</code>s. The difference from the list in
 * <code>SignedInfo</code> is that it is application defined which, if any, of
 * the digests are actually checked against the objects referenced and what to
 * do if the object is inaccessible or the digest compare fails. If a
 * <code>Manifest</code> is pointed to from <code>SignedInfo</code>, the digest
 * over the <code>Manifest</code> itself will be checked by the core signature
 * validation behavior. The digests within such a <code>Manifest</code> are
 * checked at the application's discretion. If a <code>Manifest</code> is
 * referenced from another <code>Manifest</code>, even the overall digest of
 * this two level deep <code>Manifest</code> might not be checked.</p>
 *
 * <p>The following schema fragment specifies the expected content contained
 * within this class.
 *
 * <pre>
 * &lt;complexType name="ManifestType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{http://www.w3.org/2000/09/xmldsig#}Reference" maxOccurs="unbounded"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="Id" type="{http://www.w3.org/2001/XMLSchema}ID" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * @author ahmety
 * date: Jun 12, 2009
 */
public class Manifest extends BaseElement
{

    private List<Reference> mReferences = new ArrayList<Reference>(1);


    public Manifest(Context aBaglam)
    {
        super(aBaglam);
        addLineBreak();
    }

    /**
     *  Construct Manifest from existing
     *  @param aElement xml element
     *  @param aContext according to context
     *  @throws XMLSignatureException when structure is invalid or can not be
     *      resolved appropriately
     */
    public Manifest(Element aElement, Context aContext)
            throws XMLSignatureException
    {
        super(aElement, aContext);

        Element[] refElements = selectChildren(Constants.NS_XMLDSIG, Constants.TAG_REFERENCE);

        if (refElements.length == 0) {
            // En az bir reference olmali.
            throw new XMLSignatureException("xml.WrongContent",
                                       Constants.TAG_REFERENCE,
                                       Constants.TAG_MANIFEST);
        }

        this.mReferences = new ArrayList<Reference>(refElements.length);
        for (Element refElement : refElements) {
            Reference ref = new Reference(refElement, aContext);
            mReferences.add(ref);
        }
    }

    public Reference getReference(int index)
    {
        if (index >= 0 && index < mReferences.size())
            return mReferences.get(index);
        return null;
    }

    public Reference getReferenceByURI(String aURI)
    {
        for (Reference ref : mReferences){
            if (ref.getURI().equals(aURI))
                return ref;
        }
        return null;
    }

    public String addReference(String aDocumentURI,
                              Transforms aTransforms,
                              DigestMethod aDigestMethod,
                              String aType)
            throws XMLSignatureException
    {
        Reference reference = new Reference(mContext);

        String referenceId = mContext.getIdGenerator().uret(IdGenerator.TYPE_REFERENCE);
        reference.setId(referenceId);

        if (aTransforms!=null){
            reference.setTransforms(aTransforms);
        }

        DigestMethod digestAlg = (aDigestMethod == null) ? mContext.getConfig().getAlgorithmsConfig().getDigestMethod() : aDigestMethod;
        reference.setDigestMethod(digestAlg);

        reference.setURI(aDocumentURI);

        if (aType != null)
            reference.setType(aType);

        addReference(reference);

        return referenceId;
    }

    public void addReference(Reference aReference)
    {
        mElement.appendChild(aReference.getElement());
        addLineBreak();

        mReferences.add(aReference);
    }

    public int getReferenceCount()
    {
        return mReferences.size();
    }

    // base element
    public String getNamespace()
    {
        return Constants.NS_XMLDSIG;
    }

    public String getLocalName()
    {
        return Constants.TAG_MANIFEST;
    }
}

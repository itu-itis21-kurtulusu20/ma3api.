package tr.gov.tubitak.uekae.esya.api.xmlsignature.model;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import tr.gov.tubitak.uekae.esya.api.common.ESYARuntimeException;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.C14nMethod;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
import static tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants.*;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.document.Document;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.document.StreamingDocument;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.document.DOMDocument;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.transforms.TransformEngine;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants.*;

/**
 * <p>The optional <code>Transforms</code> element contains an ordered list of
 * <code>Transform</code> elements; these describe how the signer obtained the
 * data object that was digested. The output of each <code>Transform</code>
 * serves as input to the next <code>Transform</code>. The input to the first
 * <code>Transform</code> is the result of dereferencing the <code>URI</code>
 * attribute of the <code>Reference</code> element. The output from the last
 * <code>Transform</code> is the input for the <code>DigestMethod</code>
 * algorithm. When transforms are applied the signer is not signing the native
 * (original) document but the resulting (transformed) document.
 *
 * <p>The following schema fragment specifies the expected content contained
 * within this class.
 *
 * <pre>
 * &lt;complexType name="TransformsType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{http://www.w3.org/2000/09/xmldsig#}Transform" maxOccurs="unbounded"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 * @author ahmety
 * date: Jun 11, 2009
 */
public class Transforms extends BaseElement
{
    List<Transform> mTransforms = new ArrayList<Transform>();

    public Transforms(Context aBaglam)
    {
        super(aBaglam);
        addLineBreak();
    }

    /**
     *  Construct KeyInfo from existing
     *  @param aElement xml element
     *  @param aContext according to context
     *  @throws XMLSignatureException when structure is invalid or can not be
     *      resolved appropriately
     */
    public Transforms(Element aElement, Context aContext)
            throws XMLSignatureException
    {
        super(aElement, aContext);

        Element[] transforms = selectChildren(NS_XMLDSIG, TAG_TRANSFORM);

        if (transforms==null || transforms.length==0)
        {
            // En az 1 transform olmali.
            throw new XMLSignatureException("xml.WrongContent", TAG_TRANSFORM, TAG_TRANSFORMS);
        }

        for (Element transform : transforms) {
            mTransforms.add(new Transform(transform, aContext));
        }

    }

    /**
     * Add user defined transform step.
     *
     * @param aTransform user defined transform
     */
    public void addTransform(Transform aTransform)
    {
        mElement.appendChild(aTransform.getElement());
        addLineBreak();

        mTransforms.add(aTransform);
    }

    /**
     * Return the i<sup>th</sup> <code>{@link Transform}</code>.
     * Valid <code>i</code> values are 0 to
     *  <code>{@link #getTransformCount()}-1</code>.
     *
     * @param aIndex index of {@link Transform} to return
     * @return transform at the aIndex
     */
    public Transform getTransform(int aIndex){
        if (aIndex>=0 && aIndex<mTransforms.size()){
            return mTransforms.get(aIndex);
        }
        return null;
    }

    /**
     * @return total number of <code>{@link Transform}</code>s
     */
    public int getTransformCount(){
        return mTransforms.size();
    }

    /**
     * Applies all included <code>Transform</code>s to input and returns the
     * result of these transformations.
     *
     * @param aSource object to be transformed
     * @return Document containing transformed data,
     *      if aSource is null, returned data will be null.
     * @throws XMLSignatureException if anything goes wrong.
     */
    public Document apply(Document aSource)
        throws XMLSignatureException
    {
        if (aSource==null)
            return null;

        Object result = aSource;
        for (int i =0; i<getTransformCount(); i++){
            Transform t = getTransform(i);
            result =  TransformEngine.transform(result, t.getAlgorithm(), t.getParameters(), t.getElement(), mContext.getBaseURIStr());
        }

        Document resultingDoc;
        if (result instanceof Document){
            resultingDoc = (Document)result;
        }
        else {
            resultingDoc = generateDocument(result, aSource.getURI(), aSource.getMIMEType(), aSource.getEncoding());
        }
        return resultingDoc;
    }

    private Document generateDocument(Object transformed, String aURI, String aMIMEType, String aEncoding)
    {
        if (transformed instanceof NodeList)
        {
            return new DOMDocument((NodeList)transformed, aURI, aMIMEType, aEncoding);
        }
        else if (transformed instanceof InputStream){
            return new StreamingDocument((InputStream)transformed, aURI, aMIMEType, aEncoding);
        }

        throw new ESYARuntimeException("Unknown transformation result.");
    }

    /**
     * @return if list of transforms contains any <code>C14nTransform</code>
     */
    public boolean hasC14nTransform(){
        for (int i =0; i<getTransformCount(); i++){
            Transform t = getTransform(i);
            if (C14nMethod.isSupported(t.getAlgorithm()))
                return true;
        }
        return false;
    }

    // base element
    public String getLocalName()
    {
        return TAG_TRANSFORMS;
    }
}

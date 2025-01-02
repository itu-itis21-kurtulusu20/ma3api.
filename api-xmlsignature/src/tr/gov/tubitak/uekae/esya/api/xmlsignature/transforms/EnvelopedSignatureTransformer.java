package tr.gov.tubitak.uekae.esya.api.xmlsignature.transforms;

import tr.gov.tubitak.uekae.esya.api.xmlsignature.DataType;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.TransformType;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.UnsupportedOperationException;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.util.XmlUtil;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.core.xml.NodeListImpl;

import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;

import org.w3c.dom.*;

/**
 *
 * <p>An enveloped signature transform <strong><em>T</em></strong>
 *  removes the whole <code>Signature</code> element containing
 *  <strong><em>T</em></strong> from the digest calculation of the
 *  <code>Reference</code> element containing
 *  <strong><em>T</em></strong>. The entire string of characters used
 *  by an XML processor to match the <code>Signature</code> with the
 *  XML production <code>element</code> is removed. The output of the
 *  transform is equivalent to the output that would result from
 *  replacing <strong><em>T</em></strong> with an XPath transform
 *  containing the following <code>XPath</code> parameter
 *  element:</p>
 *
 *  <pre class="xml-example">
 *   {@literal <XPath xmlns:dsig="&amp;dsig;">}
 *   count(ancestor-or-self::dsig:Signature |
 *   here()/ancestor::dsig:Signature[1]) {@literal >}
 *   count(ancestor-or-self::dsig:Signature){@literal </XPath>}
 * </pre>
 *  <p>The input and output requirements of this transform are
 *  identical to those of the XPath transform, but may only be
 *  applied to a node-set from its parent XML document. Note that it
 *  is not necessary to use an XPath expression evaluator to create
 *  this transform. However, this transform MUST produce output in
 *  exactly the same manner as the XPath transform parameterized by
 *  the XPath expression above.</p>
 *
 * @author ahmety
 * date: Jul 8, 2009
 */
public class EnvelopedSignatureTransformer implements Transformer
{

    private static final List<DataType> TYPES = Arrays.asList(DataType.NODESET);


    public List<DataType> expectedDataTypes()
    {
        return TYPES;
    }

    public DataType returnType()
    {
        return DataType.NODESET;
    }

    public boolean acceptsAlgorithm(String aAlgorithmURI)
    {
        return TransformType.ENVELOPED.getUrl().equals(aAlgorithmURI);
    }

    public Object transform(Object aObject, String aAlgorithmURI,
                            Object[] aParams, Element aTransformElement, String aBaseURI)
            throws XMLSignatureException
    {
        if (aObject instanceof NodeList){
            return makeTransform((NodeList)aObject, aTransformElement);
        }
        throw new UnsupportedOperationException("transform.unsupported",
                                               aObject);
    }

    private NodeList makeTransform(NodeList aNodeList,Element aTransformElement)
            throws XMLSignatureException
    {
        List<Node > cleanList = new ArrayList<Node>();

        Node signature = searchSignatureElement(aTransformElement);

        for (int i=0; i<aNodeList.getLength();i++){
            Node current = aNodeList.item(i);
            if (!XmlUtil.isDescendantOrSelf(signature, current)){
                cleanList.add(current);
            }
        }

        return new NodeListImpl(cleanList);
    }

    private static Node searchSignatureElement(Node signatureElement)
            throws XMLSignatureException
    {
        boolean found = false;

        while (true) {
            if ((signatureElement == null) || (signatureElement.getNodeType() == Node.DOCUMENT_NODE)) {
                break;
            }
            Element el = (Element) signatureElement;
            if (el.getNamespaceURI().equals(Constants.NS_XMLDSIG)
                && el.getLocalName().equals(Constants.TAG_SIGNATURE))
            {
                found = true;
                break;
            }

            signatureElement = signatureElement.getParentNode();
        }

        if (!found) {
            throw new XMLSignatureException("transform.enveloped.cantFindSignature");
        }
        return signatureElement;
    }

}

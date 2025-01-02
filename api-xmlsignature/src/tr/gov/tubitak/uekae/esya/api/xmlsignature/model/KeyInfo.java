package tr.gov.tubitak.uekae.esya.api.xmlsignature.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException;
import tr.gov.tubitak.uekae.esya.api.crypto.util.KeyUtil;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.c14n.core.utils.I18n;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.keyinfo.KeyInfoElement;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.keyinfo.KeyName;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.keyinfo.KeyValue;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.keyinfo.MgmtData;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.keyinfo.PGPData;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.keyinfo.RetrievalMethod;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.keyinfo.SPKIData;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.keyinfo.X509Data;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.keyinfo.x509.X509Certificate;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.keyinfo.x509.X509DataElement;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.util.XmlUtil;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

import static tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants.TAG_KEYINFO;
import static tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants.TAG_KEYNAME;
import static tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants.TAG_KEYVALUE;
import static tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants.TAG_MGMTDATA;
import static tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants.TAG_PGPDATA;
import static tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants.TAG_RETRIEVALMETHOD;
import static tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants.TAG_SPKIDATA;
import static tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants.TAG_X509DATA;

/**
 * <p>KeyInfo is an optional element that enables the recipient(s) to obtain the
 * key needed to validate the signature.  KeyInfo may contain keys, names,
 * certificates and other public key management information, such as in-band key
 * distribution or key agreement data. This specification defines a few simple
 * types but applications may extend those types or all together replace them
 * with their own key identification and exchange semantics using the XML
 * namespace facility. [XML-ns] However, questions of trust of such key
 * information (e.g., its authenticity or  strength) are out of scope of this
 * specification and left to the application.
 * <p>
 * <p>If KeyInfo is omitted, the recipient is expected to be able to identify
 * the key based on application context. Multiple declarations within KeyInfo
 * refer to the same key. While applications may define and use any mechanism
 * they choose through inclusion of elements from a different namespace,
 * compliant versions MUST implement KeyValue (section 4.4.2) and SHOULD
 * implement RetrievalMethod (section 4.4.3).
 * <p>
 * <p>The schema/DTD specifications of many of KeyInfo's children (e.g., PGPData,
 * SPKIData, X509Data) permit their content to be extended/complemented with
 * elements from another namespace. This may be done only if it is safe to
 * ignore these extension elements while claiming support for the types defined
 * in this specification. Otherwise, external elements, including alternative
 * structures to those defined by this specification, MUST be a child of
 * KeyInfo. For example, should a complete XML-PGP standard be defined, its root
 * element MUST be a child of KeyInfo. (Of course, new structures from external
 * namespaces can incorporate elements from the &amp;dsig; namespace via features of
 * the type definition language. For instance, they can create a DTD that mixes
 * their own and dsig qualified elements, or a schema that permits, includes,
 * imports, or derives new types based on &amp;dsig; elements.)
 * <p>
 * <p>The following schema fragment specifies the expected content contained
 * within this class.
 * <p>
 * <pre>
 * &lt;complexType name="KeyInfoType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;choice maxOccurs="unbounded"&gt;
 *         &lt;element ref="{http://www.w3.org/2000/09/xmldsig#}KeyName"/&gt;
 *         &lt;element ref="{http://www.w3.org/2000/09/xmldsig#}KeyValue"/&gt;
 *         &lt;element ref="{http://www.w3.org/2000/09/xmldsig#}RetrievalMethod"/&gt;
 *         &lt;element ref="{http://www.w3.org/2000/09/xmldsig#}X509Data"/&gt;
 *         &lt;element ref="{http://www.w3.org/2000/09/xmldsig#}PGPData"/&gt;
 *         &lt;element ref="{http://www.w3.org/2000/09/xmldsig#}SPKIData"/&gt;
 *         &lt;element ref="{http://www.w3.org/2000/09/xmldsig#}MgmtData"/&gt;
 *         &lt;any processContents='lax' namespace='##other'/&gt;
 *       &lt;/choice&gt;
 *       &lt;attribute name="Id" type="{http://www.w3.org/2001/XMLSchema}ID" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 * @author ahmety
 *         date: Jun 10, 2009
 */
public class KeyInfo extends BaseElement
{
    private static Logger logger = LoggerFactory.getLogger(KeyInfo.class);

    // KeyName, KeyValue, RetrievalMethod, X509Data, PGPData, SPKIData, MgmtData
    private List<KeyInfoElement> mIcerik = new ArrayList<KeyInfoElement>(1);

    private boolean mInited = false;

    public KeyInfo(Context aBaglam)
    {
        super(aBaglam);

        addLineBreak();
    }

    /**
     * Construct KeyInfo from existing
     *
     * @param aElement xml element
     * @param aContext according to context
     * @throws XMLSignatureException when structure is invalid or can not be
     *                               resolved appropriately
     */
    public KeyInfo(Element aElement, Context aContext)
            throws XMLSignatureException
    {
        super(aElement, aContext);
    }

    // delay construction of conntent because some children may reference a not
    // yet constructed conted via RetriecalMethod!

    private void init() throws XMLSignatureException
    {
        mInited = true;
        List<Element> children = XmlUtil.selectChildElements(mElement);
        for (Element element : children) {
            KeyInfoElement kie = resolve(element, mContext);
            if (kie != null) {
                if (kie instanceof RetrievalMethod) {
                    RetrievalMethod rm = (RetrievalMethod) kie;
                    kie = rm.resolve();
                }
                mIcerik.add(kie);
                if (logger.isDebugEnabled()) {
                    logger.debug("Key info element: " + kie);
                }
            }
        }
    }


    public static KeyInfoElement resolve(Element aElement, Context aBaglam)
            throws XMLSignatureException
    {
        String name = aElement.getLocalName();

        if (name.equals(TAG_KEYNAME)) {
            return new KeyName(aElement, aBaglam);
        }
        else if (name.equals(TAG_KEYVALUE)) {
            return new KeyValue(aElement, aBaglam);
        }
        else if (name.equals(TAG_RETRIEVALMETHOD)) {
            return new RetrievalMethod(aElement, aBaglam);
        }
        else if (name.equals(TAG_X509DATA)) {
            return new X509Data(aElement, aBaglam);
        }
        else if (name.equals(TAG_PGPDATA)) {
            return new PGPData(aElement, aBaglam);
        }
        else if (name.equals(TAG_SPKIDATA)) {
            return new SPKIData(aElement, aBaglam);
        }
        else if (name.equals(TAG_MGMTDATA)) {
            return new MgmtData(aElement, aBaglam);
        }
        else {
            //throw new XMLSignatureException("Unknown KeyInfoElement:" + name);
            // todo unknown permitted element
        }
        return null;
    }

    public int getElementCount() throws XMLSignatureException
    {
        if (!mInited)
            init();
        return mIcerik.size();
    }

    public KeyInfoElement get(int aIndex) throws XMLSignatureException
    {
        if (!mInited)
            init();
        return mIcerik.get(aIndex);
    }

    public void add(KeyInfoElement aElement)
    {
        mIcerik.add(aElement);
        mElement.appendChild(((BaseElement) aElement).getElement());
        addLineBreak();
    }

    public ECertificate resolveCertificate() throws XMLSignatureException
    {
        for (int i = 0; i < getElementCount(); i++) {
            KeyInfoElement o = get(i);

            try {
                if (o instanceof X509Data) {
                    X509Data data = (X509Data) o;
                    for (int j = 0; j < data.getElementCount(); j++) {
                        X509DataElement xde = data.get(j);
                        if (xde instanceof X509Certificate) {
                            X509Certificate cert = (X509Certificate) xde;
                            return new ECertificate(cert.getCertificateBytes());
                        }
                    }
                }
                else if (o instanceof RetrievalMethod) {
                    // retrieval metod raxX509 a map eder
                    return new ECertificate(((RetrievalMethod) o).getRawX509());
                }
            }
            catch (Exception e) {
                throw new XMLSignatureException("core.invalidKeyInfo", e);
            }

        }
        return null;
    }

    public PublicKey resolvePublicKey() throws XMLSignatureException
    {
        ECertificate certificate = resolveCertificate();
        if (certificate != null) {
            try {
                return KeyUtil.decodePublicKey(
                                        certificate.getSubjectPublicKeyInfo());
            }
            catch (CryptoException c) {
                throw new XMLSignatureException(c, "errors.cantDecode", certificate.getSubjectPublicKeyInfo(), I18n.translate("publicKey"));
            }
        }

        for (int i = 0; i < getElementCount(); i++) {
            KeyInfoElement o = get(i);

            if (o instanceof KeyValue) {
                KeyValue kv = (KeyValue) o;
                return kv.getValue().getPublicKey();
            }
            // todo more key info types
        }
        return null;
    }

    // baseElement metodlari

    public String getLocalName()
    {
        return TAG_KEYINFO;
    }

}

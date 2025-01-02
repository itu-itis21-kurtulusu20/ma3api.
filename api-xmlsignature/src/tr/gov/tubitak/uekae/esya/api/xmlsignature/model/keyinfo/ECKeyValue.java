package tr.gov.tubitak.uekae.esya.api.xmlsignature.model.keyinfo;

import tr.gov.tubitak.uekae.esya.api.common.util.OIDUtil;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.BaseElement;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.util.XmlUtil;

import static tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants.*;

import java.security.PublicKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import gnu.crypto.key.ecdsa.ECDSAPublicKey;
import gnu.crypto.sig.ecdsa.ecmath.curve.ECGNUPoint;
import gnu.crypto.sig.ecdsa.ecmath.curve.ECDomainParameter;
import gnu.crypto.sig.ecdsa.ecmath.curve.ECPointFp;

/**
 * @author ahmety
 * date: Aug 25, 2009
 */
public class ECKeyValue extends BaseElement implements KeyValueElement
{

    private static Logger logger = LoggerFactory.getLogger(ECKeyValue.class);

    private String mOID;
    private ECDSAPublicKey mPublicKey;

    private Element mPublicKeyElement, mNamedCurveElement;

    public ECKeyValue(Context aContext, ECDSAPublicKey aPublicKey)
            throws XMLSignatureException
    {
        super(aContext);

        addLineBreak();

        mPublicKey = aPublicKey;
        byte[] bytes =mPublicKey.getMQ().toOctetString(ECGNUPoint.UNCOMPRESSED);
        int[] oid = mPublicKey.getMParameters().getmParamOID();

        mPublicKeyElement = insertElement(NS_XMLDSIG_11, TAG_PUBLICKEY);
        XmlUtil.setBase64EncodedText(mPublicKeyElement, bytes);

        mNamedCurveElement = insertTextElement(NS_XMLDSIG_11, TAG_NAMEDCURVE, OIDUtil.toURN(oid));

    }

    /**
     * Construct KeyValueElement from existing
     * @param aElement xml element
     * @param aContext according to context
     * @throws tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException
     *          when structure is invalid or can not be
     *          resolved appropriately
     */
    public ECKeyValue(Element aElement, Context aContext)
            throws XMLSignatureException
    {
        super(aElement, aContext);
        // todo
        mNamedCurveElement = selectChildElement(NS_XMLDSIG_11, TAG_NAMEDCURVE);

        if (mNamedCurveElement!=null){
            mOID = XmlUtil.getText(mNamedCurveElement).trim();
            
        }

        mPublicKeyElement = selectChildElement(NS_XMLDSIG_11, TAG_PUBLICKEY);
        if (mPublicKeyElement!=null){
            byte[] publicKeyBytes = XmlUtil.getBase64DecodedText(mPublicKeyElement);
            try {
                int[] oid = OIDUtil.fromURN(mOID);
                ECDomainParameter ecdparam = ECDomainParameter.getInstance(oid);
                ECGNUPoint point = new ECPointFp(ecdparam.getMCurve(), publicKeyBytes);

                mPublicKey = new ECDSAPublicKey(ecdparam, point);

            } catch (Exception x){
                logger.error("Problem in public key generation", x);
                throw new XMLSignatureException(x, "core.cantGeneratePublicKey", "ECDSA");
            }
        }

    }

    public PublicKey getPublicKey() throws XMLSignatureException
    {
        return mPublicKey;
    }

    public String getOID()
    {
        return mOID;
    }

    @Override
    public String getNamespace()
    {
        return NS_XMLDSIG_11;
    }

    public String getLocalName()
    {
        return TAG_ECKEYVALUE;
    }
}

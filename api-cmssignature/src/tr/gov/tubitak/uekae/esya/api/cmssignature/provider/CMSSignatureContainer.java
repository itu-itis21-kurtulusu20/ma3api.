package tr.gov.tubitak.uekae.esya.api.cmssignature.provider;

import com.objsys.asn1j.runtime.Asn1DerDecodeBuffer;
import com.objsys.asn1j.runtime.Asn1OctetString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.cms.EAttribute;
import tr.gov.tubitak.uekae.esya.api.asn.cms.ESignerInfo;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.cmssignature.CMSSignatureException;
import tr.gov.tubitak.uekae.esya.api.cmssignature.CMSSignatureUtil;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.MessageDigestAttr;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.BaseSignedData;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.Signer;
import tr.gov.tubitak.uekae.esya.api.common.util.StreamUtil;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
import tr.gov.tubitak.uekae.esya.api.signature.*;
import tr.gov.tubitak.uekae.esya.api.signature.impl.AbstractSignatureContainer;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

/**
 * @author ayetgin
 */
public class CMSSignatureContainer extends AbstractSignatureContainer
{
    private static Logger logger = LoggerFactory.getLogger(CMSSignatureContainer.class);

    private BaseSignedData baseSignedData;
    private SDValidationData validationData;

    public CMSSignatureContainer()
    {
        baseSignedData = new BaseSignedData();
        validationData = new SDValidationData(baseSignedData.getSignedData());
    }

    public Signature createSignature(ECertificate certificate)
    {
        CMSSignatureImpl signature = new CMSSignatureImpl(context, this, baseSignedData, null, null, certificate, validationData);
        rootSignatures.add(signature);

        return signature;
    }

    @Override
    public void addExternalSignature(Signature signature) throws SignatureException
    {
        if (!(signature instanceof CMSSignatureImpl))
            throw new SignatureRuntimeException("Unknown CMS Signature impl!"+signature.getClass());
        super.addExternalSignature(signature);
        Signable content = getSignatures().get(0).getContents().get(0);

        ESignerInfo signerInfo = ((CMSSignatureImpl) signature).signer.getSignerInfo();
        //Check the correct document is signed.
        if(checkMessageDigestAttr(signerInfo, content))
        {
            baseSignedData.getSignedData().addSignerInfo(signerInfo);
            CMSSignatureUtil.addCerIfNotExist(baseSignedData.getSignedData(), signature.getSignerCertificate());
            CMSSignatureUtil.addDigestAlgIfNotExist(baseSignedData.getSignedData(), signerInfo.getDigestAlgorithm());
        }
        else
            throw new SignatureException("İmzalanan içerik aynı değil");
    }
    private boolean checkMessageDigestAttr(ESignerInfo aSignerInfo,	Signable content) throws SignatureException
    {
        EAttribute attr = aSignerInfo.getSignedAttribute(MessageDigestAttr.OID).get(0);
        Asn1OctetString octetS = new Asn1OctetString();
        try
        {
            Asn1DerDecodeBuffer decBuf = new Asn1DerDecodeBuffer(attr.getValue(0));
            octetS.decode(decBuf);
        }
        catch(Exception tEx)
        {
            throw new CMSSignatureException("Mesaj özeti çözülemedi.", tEx);
        }

        DigestAlg digestAlg = DigestAlg.fromOID(aSignerInfo.getDigestAlgorithm().getAlgorithm().value);
        try
        {
            byte [] contentDigest = content.getDigest(digestAlg);
            return Arrays.equals(octetS.value, contentDigest);
        }
        catch (Exception e)
        {
            throw new SignatureException("İmzalanan dosya özeti hesaplanamadı.", e);
        }
    }

    public void detachSignature(Signature signature) throws SignatureException
    {
        try {
            CMSSignatureImpl cmsSignature = (CMSSignatureImpl)signature;
            cmsSignature.signer.remove();
            getSignatures().remove(signature);
        } catch (Exception x){
            throw new SignatureException("Cant extract signature form container "+ signature.getClass(), x);
        }
    }

    public SignatureFormat getSignatureFormat()
    {
        return SignatureFormat.CAdES;
    }

    public boolean isSignatureContainer(InputStream stream) throws SignatureException
    {
        try {
            return BaseSignedData.isSigned(stream);
        }
        catch (Exception x){
            logger.error("Cant read stream for signature type detection", x);
            throw new SignatureException(x);
        }
    }

    public void read(InputStream stream) throws SignatureException
    {
        try {
            byte[] bytes = StreamUtil.readAll(stream);
            baseSignedData = new BaseSignedData(bytes);
            baseSignedData.getSignerList();
            validationData = new SDValidationData(baseSignedData.getSignedData(),context);
            for (Signer s : baseSignedData.getSignerList()){
                CMSSignatureImpl signature = new CMSSignatureImpl(context, this, baseSignedData, s, null, s.getSignerCertificate(), validationData);
                rootSignatures.add(signature);
            }
        } catch (Exception x){
            throw new SignatureException(x);
        }

    }

    public void write(OutputStream stream) throws SignatureException
    {
        try {
            stream.write(baseSignedData.getEncoded());
            stream.flush();
        } catch (Exception x){
            throw new SignatureException(x);
        }
    }

    public Object getUnderlyingObject()
    {
        return baseSignedData;
    }
}

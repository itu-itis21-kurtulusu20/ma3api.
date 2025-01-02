package dev.esya.api.cmssignature.downgrade;

import test.esya.api.cmssignature.CMSSignatureTest;
import tr.gov.tubitak.uekae.esya.api.asn.cms.EAttribute;
import tr.gov.tubitak.uekae.esya.api.asn.cms.EUnsignedAttributes;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.ArchiveTimeStampAttr;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.ArchiveTimeStampV2Attr;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.EParameters;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.BaseSignedData;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.ESignatureType;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.Signer;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg;

import java.util.HashMap;
import java.util.List;

public class SignatureDowngrade extends CMSSignatureTest {

    public void testESAtoXLONG() throws Exception{

        BaseSignedData bs = new BaseSignedData();
        bs.addContent(getSimpleContent());

        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put(EParameters.P_TSS_INFO, getTSSettings());
        params.put(EParameters.P_CERT_VALIDATION_POLICY, getPolicy());

        bs.addSigner(ESignatureType.TYPE_BES, getSignerCertificate(), getSignerInterface(SignatureAlg.RSA_SHA256), null, params);

        bs.getSignerList().get(0).convert(ESignatureType.TYPE_ESA, params);

        byte[] esaSignature = bs.getEncoded();

        bs = new BaseSignedData(esaSignature);

        Signer signer = bs.getSignerList().get(0);

        EUnsignedAttributes unsignedAttributes1 = signer.getSignerInfo().getUnsignedAttributes();

        int attrCount1 = unsignedAttributes1.getAttributeCount();

        List<EAttribute> archiveAttributes = signer.getUnsignedAttribute(ArchiveTimeStampAttr.OID);
        archiveAttributes.addAll(signer.getUnsignedAttribute(ArchiveTimeStampV2Attr.OID));

        for (EAttribute archiveAttribute:archiveAttributes ) {
            signer.removeUnSignedAttribute(archiveAttribute);
        }

        EUnsignedAttributes unsignedAttributes2 = signer.getSignerInfo().getUnsignedAttributes();
        int attrCount2 = unsignedAttributes2.getAttributeCount();

        byte[] xlongSignature = bs.getEncoded();

        bs = new BaseSignedData(xlongSignature);

        ESignatureType type = bs.getSignerList().get(0).getType();

        System.out.println(type);


    }


    /*public void testESAtoXLONG2() throws Exception{

        BaseSignedData bs = new BaseSignedData();
        bs.addContent(getSimpleContent());

        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put(EParameters.P_TSS_INFO, getTSSettings());
        params.put(EParameters.P_CERT_VALIDATION_POLICY, getPolicy());

        bs.addSigner(ESignatureType.TYPE_ESXLong, getSignerCertificate(), getSignerInterface(SignatureAlg.RSA_SHA256), null, params);

        bs.getSignerList().get(0).convert(ESignatureType.TYPE_ESA, params);

        byte[] esaSignature = bs.getEncoded();

        bs = new BaseSignedData(esaSignature);

        Signer signer = bs.getSignerList().get(0);

        EUnsignedAttributes unsignedAttributes1 = signer.getSignerInfo().getUnsignedAttributes();

        int attrCount1 = unsignedAttributes1.getAttributeCount();

        List<EAttribute> archiveAttributes = signer.getUnsignedAttribute(ArchiveTimeStampAttr.OID);
        archiveAttributes.addAll(signer.getUnsignedAttribute(ArchiveTimeStampV2Attr.OID));



        for (EAttribute archiveAttribute:archiveAttributes ) {
            signer.removeUnSignedAttribute(archiveAttribute);
        }

        EUnsignedAttributes unsignedAttributes2 = signer.getSignerInfo().getUnsignedAttributes();
        int attrCount2 = unsignedAttributes1.getAttributeCount();

        byte[] xlongSignature = bs.getEncoded();

        bs = new BaseSignedData(xlongSignature);

        ESignatureType type = bs.getSignerList().get(0).getType();

        System.out.println(type);


    }*/



}
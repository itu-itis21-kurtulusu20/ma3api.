package tr.gov.tubitak.uekae.esya.cmpapi.base.protection;

import com.objsys.asn1j.runtime.Asn1DerDecodeBuffer;
import com.objsys.asn1j.runtime.Asn1ObjectIdentifier;
import com.objsys.asn1j.runtime.Asn1OpenType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.cmp.EPBMParameter;
import tr.gov.tubitak.uekae.esya.api.asn.cmp.EPKIMessage;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EAlgorithmIdentifier;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException;
import tr.gov.tubitak.uekae.esya.asn.cmp._cmpValues;
import tr.gov.tubitak.uekae.esya.cmpapi.base.common.IConfigType;
import tr.gov.tubitak.uekae.esya.cmpapi.base.util.CMPPBMac;
import tr.gov.tubitak.uekae.esya.cmpapi.base.util.UtilCmp;

/**
 * Author: ZELDAL  @ UEKAE - TUBITAK
 * Date: 10.May.2010
 * Time: 11:17:46
 * Description:
 */

public class ProtectionGeneratorWithHMac<T extends IConfigType> implements IProtectionGenerator<T> {
    private static final Logger logger = LoggerFactory.getLogger(ProtectionGeneratorWithHMac.class);

    private IPBMParameterFinder pbmParameterFinder;

    public ProtectionGeneratorWithHMac(IPBMParameterFinder pbmParameterFinder) {
        this.pbmParameterFinder = pbmParameterFinder;
    }



    public EAlgorithmIdentifier getProtectionAlg() {
        EPBMParameter pbm = pbmParameterFinder.getPBMParameter();
//        byte[] encoded = null;
//        Asn1DerEncodeBuffer encBuf = new Asn1DerEncodeBuffer();

//        try {
//            pbm.encode(encBuf);
//        } catch (Asn1Exception aEx) {
        if(pbm.getEncoded() == null )
            throw new RuntimeException("PBMParameter Null encoded geldi");
//        }
//        encoded = encBuf.getMsgCopy();
        Asn1DerDecodeBuffer decBuf = new Asn1DerDecodeBuffer(pbm.getEncoded());

        Asn1OpenType param = new Asn1OpenType();
        try {
            param.decode(decBuf);
        } catch (Exception aEx) {
            throw new RuntimeException("PBMParameter decode edilirken hata oluştu", aEx);
        }

        return new EAlgorithmIdentifier(new Asn1ObjectIdentifier( _cmpValues.id_PasswordBasedMac), param);
    }


    public void addProtection(EPKIMessage message) {
        byte[] imzalanacak;
        byte[] imzali;

        imzalanacak = UtilCmp.getSigningData(message.getHeader().getObject(), message.getBody().getObject());

        try {
            CMPPBMac pbmac = new CMPPBMac(pbmParameterFinder.getPBMParameter(), pbmParameterFinder.getHMacCode());
            imzali = pbmac.pbmacHesapla(imzalanacak);
        } catch (CryptoException ex) {
            throw new RuntimeException("PKI mesaji imzalanamadi", ex);
        }
        message.setSignatureValue(imzali);
    }



}
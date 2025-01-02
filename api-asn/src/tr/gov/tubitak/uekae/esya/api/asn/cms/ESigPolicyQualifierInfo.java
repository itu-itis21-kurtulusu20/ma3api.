package tr.gov.tubitak.uekae.esya.api.asn.cms;

import com.objsys.asn1j.runtime.*;
import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.common.ESYARuntimeException;
import tr.gov.tubitak.uekae.esya.api.common.OID;
import tr.gov.tubitak.uekae.esya.asn.cms.SigPolicyQualifierInfo;
import tr.gov.tubitak.uekae.esya.asn.util.AsnIO;

/**
 * @author ayetgin
 */
public class ESigPolicyQualifierInfo extends BaseASNWrapper<SigPolicyQualifierInfo>
{
    public ESigPolicyQualifierInfo(SigPolicyQualifierInfo aObject)
    {
        super(aObject);
    }

    public ESigPolicyQualifierInfo(int[] oid, Asn1Type openType)
    {
        super(new SigPolicyQualifierInfo());
        mObject.sigPolicyQualifierId = new Asn1ObjectIdentifier(oid);
        Asn1DerEncodeBuffer encBuf = new Asn1DerEncodeBuffer();
        try {
            openType.encode(encBuf);
        } catch (Asn1Exception ex) {

            throw new ESYARuntimeException("Cant convert to open type : "+openType, ex);
        }
        mObject.qualifier = new Asn1OpenType(encBuf.getMsgCopy());
    }

    public OID getObjectIdentifier(){
        return new OID(mObject.sigPolicyQualifierId.value);
    }

    public void decodeQualifier(Asn1Type object){
        try {
            Asn1DerDecodeBuffer decBuf = new Asn1DerDecodeBuffer(mObject.qualifier.value);
            object = AsnIO.derOku(object, decBuf);
        } catch (Exception ex) {
            throw new ESYARuntimeException("Cant convert to open type : "+object, ex);
        }
    }

}

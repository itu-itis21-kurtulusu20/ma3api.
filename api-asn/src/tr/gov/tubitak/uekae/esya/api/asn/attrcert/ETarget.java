package tr.gov.tubitak.uekae.esya.api.asn.attrcert;

import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EGeneralName;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.attrcert.Target;
import tr.gov.tubitak.uekae.esya.asn.x509.GeneralName;

/**
 * <b>Author</b>    : zeldal.ozdemir <br>
 * <b>Project</b>   : MA3   <br>
 * <b>Copyright</b> : TUBITAK Copyright (c) 2006-2011 <br>
 * <b>Date</b>: 4/13/11 - 2:59 PM <p>
 * <b>Description</b>: <br>
 */
public class ETarget extends BaseASNWrapper<Target>{

    public final static byte TYPE_NAME = Target._TARGETNAME;
    public final static byte TYPE_GROUP = Target._TARGETGROUP;
    public final static byte TYPE_CERT = Target._TARGETCERT;


    public ETarget(byte[] aBytes) throws ESYAException {
        super(aBytes, new Target());
    }

    public ETarget(Target aObject) throws ESYAException {
        super(aObject);
    }

    public EGeneralName getAsTargetName(){
        if(mObject.getChoiceID() == Target._TARGETNAME)
            return new EGeneralName((GeneralName) mObject.getElement());
        else
            return null;
    }

    public EGeneralName getAsTargetGroupName(){
        if(mObject.getChoiceID() == Target._TARGETGROUP)
            return new EGeneralName((GeneralName) mObject.getElement());
        else
            return null;
    }

    public int getType(){
        return getObject().getChoiceID();
    }
}

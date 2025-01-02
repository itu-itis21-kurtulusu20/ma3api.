package tr.gov.tubitak.uekae.esya.api.asn.esya;

import com.objsys.asn1j.runtime.Asn1ValueParseException;
import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EExtension;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ExtensionType;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.esya.*;


public class EESYAServiceConfigType extends BaseASNWrapper<ESYAServiceConfigType> implements ExtensionType {


    public EESYAServiceConfigType(byte[] aBytes) throws ESYAException {
        super(aBytes, new ESYAServiceConfigType());
    }

    public EESYAServiceConfigType(ESYAServiceConfigType aObject) {
        super(aObject);
    }

    public ESYAServiceConfigType_configType getConfigType(){
        if(mObject==null){
            return ESYAServiceConfigType_configType.undefined_();
        }
        return mObject.configType;
    }

    public EExtension toExtension(boolean aCritic) throws ESYAException {
        return new EExtension(EESYAOID.oid_cmpServiceConfigType, aCritic, this);
    }

}

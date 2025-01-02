package tr.gov.tubitak.uekae.esya.api.asn.attrcert;

import com.objsys.asn1j.runtime.Asn1OpenType;
import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.util.bag.Pair;
import tr.gov.tubitak.uekae.esya.asn.attrcert.SecurityCategory;

/**
 <b>Author</b>    : zeldal.ozdemir <br>
 <b>Project</b>   : MA3   <br>
 <b>Copyright</b> : TUBITAK Copyright (c) 2006-2011 <br>
 <b>Date</b>: 10/31/11 - 4:36 PM <p>
 <b>Description</b>: <br>
 */
public class ESecurityCategory extends BaseASNWrapper<SecurityCategory>{
    public ESecurityCategory(SecurityCategory aObject) {
        super(aObject);
    }

    public ESecurityCategory(byte[] aBytes) throws ESYAException {
        super(aBytes, new SecurityCategory());
    }

    public ESecurityCategory(int[] type, byte[] value) {
        super(new SecurityCategory(type,new Asn1OpenType(value)));
    }

    public Pair<int[],byte[]> getAsTypeValuePair(){
        return new Pair<int[], byte[]>(mObject.type.value,mObject.value.value);
    }
}

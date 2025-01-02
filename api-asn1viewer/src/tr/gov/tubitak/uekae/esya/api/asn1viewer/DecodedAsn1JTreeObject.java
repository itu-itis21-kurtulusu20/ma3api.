package tr.gov.tubitak.uekae.esya.api.asn1viewer;

import com.objsys.asn1j.runtime.Asn1Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by orcun.ertugrul on 06-Oct-17.
 */
public class DecodedAsn1JTreeObject {

    protected static Logger logger = LoggerFactory.getLogger(DecodedAsn1JTreeObject.class);
    Asn1ObjectContainer mAsn1Obj;
    String mFieldName;

    public DecodedAsn1JTreeObject(Asn1ObjectContainer asn1Obj, String fieldName)
    {
        mAsn1Obj = asn1Obj;
        mFieldName = fieldName;
    }

    public String getTypeName()
    {
        return mAsn1Obj.mObject.getClass().getSimpleName();
    }

    public Asn1Type getAsn1Obj()
    {
        return mAsn1Obj.mObject;
    }

    public Asn1ObjectContainer getAsn1ObjectContainer()
    {
        return mAsn1Obj;
    }


    @Override
    public String toString()
    {
        try
        {
            return mFieldName;
        }
        catch(Exception ex) {
            logger.warn("Warning in DecodedAsn1JTreeObject", ex);
            return ex.toString();
        }
    }
}

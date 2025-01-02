package tr.gov.tubitak.uekae.esya.api.asn.cmp;

import com.objsys.asn1j.runtime.Asn1Integer;
import com.objsys.asn1j.runtime.Asn1UTF8String;
import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.cmp.PKIFreeText;
import tr.gov.tubitak.uekae.esya.asn.cmp.PollRepContent_element;
import tr.gov.tubitak.uekae.esya.asn.cmp.SuppLangTagsValue;

/**
 * <b>Author</b>    : zeldal.ozdemir <br>
 * <b>Project</b>   : MA3   <br>
 * <b>Copyright</b> : TUBITAK Copyright (c) 2006-2013 <br>
 * <b>Date</b>: 5/24/13 - 4:39 PM <p>
 * <b>Description</b>: <br>
 */
public class ESuppLangTagsValue extends BaseASNWrapper<SuppLangTagsValue> {
    public ESuppLangTagsValue(SuppLangTagsValue aObject) {
        super(aObject);
    }

    public ESuppLangTagsValue(byte[] aBytes) throws ESYAException {
        super(aBytes, new SuppLangTagsValue());
    }

    public ESuppLangTagsValue(String langTag) throws ESYAException {
        super(new SuppLangTagsValue(new Asn1UTF8String[]{new Asn1UTF8String(langTag)}));
    }

    void setLangTag(String langTag){
        if(mObject == null)
        {
            mObject = new SuppLangTagsValue();
        }
        if(mObject.elements == null){
            mObject.elements = new Asn1UTF8String[1];
        }
        mObject.elements[0]=new Asn1UTF8String(langTag);
    }

    String getLangugeTag(){
        String retTag = null;
        for (Asn1UTF8String element : mObject.elements) {
            return element.value;
        }
        return retTag;
    }
}

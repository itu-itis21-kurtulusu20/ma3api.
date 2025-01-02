package tr.gov.tubitak.uekae.esya.api.asn.cms.envelope;

import com.objsys.asn1j.runtime.Asn1Type;
import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.cms.RecipientInfo;
import tr.gov.tubitak.uekae.esya.asn.cms.RecipientInfos;

/**
 * Created by sura.emanet on 27.06.2018.
 */

public abstract  class EnvelopeASNWrapper<T extends Asn1Type> extends BaseASNWrapper<T> implements IEnvelopeData{

    public EnvelopeASNWrapper(T aObject) {
        super(aObject);
    }

    public EnvelopeASNWrapper(byte[] aBytes, T aObject) throws ESYAException{
        super(aBytes, aObject);
    }

    public RecipientInfo getRecipientInfo(int aIndex){

        if(getRecipientInfos() == null)
            return null;
        return getRecipientInfos().elements[aIndex];
    }

    public int getRecipientInfoCount(){
        if (getRecipientInfos()==null || getRecipientInfos().elements == null)
            return 0;
        return getRecipientInfos().elements.length;
    }

    public void addRecipientInfo(RecipientInfo aRecipientInfo){
        RecipientInfos recipientInfos = getRecipientInfos();
        if (recipientInfos == null)
        {
            recipientInfos = new RecipientInfos();
            setRecipientInfos(recipientInfos);

            recipientInfos.elements = new RecipientInfo[0];
        }

        recipientInfos.elements = extendArray(recipientInfos.elements, aRecipientInfo);
    }

}

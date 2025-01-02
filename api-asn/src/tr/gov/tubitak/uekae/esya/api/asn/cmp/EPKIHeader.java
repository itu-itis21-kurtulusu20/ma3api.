package tr.gov.tubitak.uekae.esya.api.asn.cmp;

import com.objsys.asn1j.runtime.Asn1GeneralizedTime;
import com.objsys.asn1j.runtime.Asn1ObjectIdentifier;
import com.objsys.asn1j.runtime.Asn1OctetString;
import com.objsys.asn1j.runtime.Asn1Type;
import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EAlgorithmIdentifier;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EGeneralName;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.ESYARuntimeException;
import tr.gov.tubitak.uekae.esya.asn.cmp.PKIHeader;
import tr.gov.tubitak.uekae.esya.asn.cmp.PKIHeader_generalInfo;
import tr.gov.tubitak.uekae.esya.asn.cmp.PKIHeader_pvno;

/**
 * User: zeldal.ozdemir
 * Date: 1/31/11
 * Time: 1:57 PM
 */
public class EPKIHeader extends BaseASNWrapper<PKIHeader> {
    public final static int CMP1999 = PKIHeader_pvno.cmp1999;
    public final static int CMP2000 = PKIHeader_pvno.cmp2000;

    public EPKIHeader(byte[] aBytes) throws ESYAException {
        super(aBytes, new PKIHeader());
    }

    public EPKIHeader(PKIHeader aObject) {
        super(aObject);
    }

    public EPKIHeader(int pvno, EGeneralName sender, EGeneralName recipient) {
        super(new PKIHeader(pvno, sender.getObject(), recipient.getObject()));
    }

    public void setHeaderPVNo(int pvno){
        mObject.pvno = new PKIHeader_pvno(pvno);
    }

    public int getHeaderPVNo(){
        return (int) mObject.pvno.value;
    }

    public EGeneralName getSender() {
        return new EGeneralName(mObject.sender);
    }

    public void setSender(EGeneralName sender) {
        this.mObject.sender = sender.getObject();
    }

    public EGeneralName getRecipient() {
        return new EGeneralName(mObject.recipient);
    }

    public void setRecipient(EGeneralName recipient) {
        this.mObject.recipient = recipient.getObject();
    }

    public Asn1GeneralizedTime getMessageTime() {
        return this.mObject.messageTime;
    }

    public void setMessageTime(Asn1GeneralizedTime messageTime) {
        this.mObject.messageTime = messageTime;
    }

    public EAlgorithmIdentifier getProtectionAlg() {
        return new EAlgorithmIdentifier(mObject.protectionAlg);
    }

    public void setProtectionAlg(EAlgorithmIdentifier protectionAlg) {
        this.mObject.protectionAlg = protectionAlg.getObject();
    }

    public Asn1OctetString getSenderKID() {
        return this.mObject.senderKID;
    }

    public void setSenderKID(Asn1OctetString senderKID) {
        this.mObject.senderKID = senderKID;
    }

    public Asn1OctetString getRecipKID() {
        return this.mObject.recipKID;
    }

    public void setRecipKID(Asn1OctetString recipKID) {
        this.mObject.recipKID = recipKID;
    }

    public Asn1OctetString getTransactionID() {
        return this.mObject.transactionID;
    }

    public void setTransactionID(Asn1OctetString transactionID) {
        this.mObject.transactionID = transactionID;
    }

    public Asn1OctetString getSenderNonce() {
        return this.mObject.senderNonce;
    }

    public void setSenderNonce(Asn1OctetString senderNonce) {
        this.mObject.senderNonce = senderNonce;
    }

    public Asn1OctetString getRecipNonce() {
        return this.mObject.recipNonce;
    }

    public void setRecipNonce(Asn1OctetString recipNonce) {
        this.mObject.recipNonce = recipNonce;
    }

    public boolean isProtectionAlgorithmEquals(Asn1ObjectIdentifier objectIdentifier){
        if(mObject == null || mObject.protectionAlg == null)
            return false;
        return new EAlgorithmIdentifier(mObject.protectionAlg).isAlgorithmEquals(objectIdentifier);
    }

    public EInfoTypeAndValue[] getGeneralInfos(){
        if(mObject == null || mObject.generalInfo == null )
            return null;
        return wrapArray(mObject.generalInfo.elements,EInfoTypeAndValue.class);
    }

    public void addToGeneralInfo(EInfoTypeAndValue infoTypeAndValue){
        if(mObject == null)
            throw new ESYARuntimeException("Fix") ;
        if(mObject.generalInfo == null || mObject.generalInfo.elements == null)
            mObject.generalInfo = new PKIHeader_generalInfo();
        mObject.generalInfo.elements = extendArray(mObject.generalInfo.elements,infoTypeAndValue.getObject());
    }

    public <T extends Asn1Type> T getGeneralInfo(Asn1ObjectIdentifier infoOid, Class<T> aClass) throws ESYAException {
        EInfoTypeAndValue[] generalInfos = getGeneralInfos();
        if(generalInfos == null)
            return null;
        for (EInfoTypeAndValue generalInfo : generalInfos) {
            if(generalInfo.getObject().infoType.equals(infoOid)){
                return generalInfo.getGeneralInfo(aClass);
            }
        }
        return null;
    }
}

package tr.gov.tubitak.uekae.esya.api.asn.cmp;

import com.objsys.asn1j.runtime.Asn1BigInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.asn.crmf.ECertReqMsg;
import tr.gov.tubitak.uekae.esya.api.asn.esya.EESYACAControlRepMsg;
import tr.gov.tubitak.uekae.esya.api.asn.esya.EESYACAControlReqMsg;
import tr.gov.tubitak.uekae.esya.api.asn.esya.EESYAOID;
import tr.gov.tubitak.uekae.esya.api.asn.pkcs10.ECertificationRequest;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EGeneralName;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.util.bag.Triple;
import tr.gov.tubitak.uekae.esya.asn.cmp.CertConfirmContent;
import tr.gov.tubitak.uekae.esya.asn.cmp.CertRepMessage;
import tr.gov.tubitak.uekae.esya.asn.cmp.CertResponse;
import tr.gov.tubitak.uekae.esya.asn.cmp.ErrorMsgContent;
import tr.gov.tubitak.uekae.esya.asn.cmp.GenMsgContent;
import tr.gov.tubitak.uekae.esya.asn.cmp.GenRepContent;
import tr.gov.tubitak.uekae.esya.asn.cmp.InfoTypeAndValue;
import tr.gov.tubitak.uekae.esya.asn.cmp.KeyRecRepContent;
import tr.gov.tubitak.uekae.esya.asn.cmp.PKIBody;
import tr.gov.tubitak.uekae.esya.asn.cmp.PKIStatusInfo;
import tr.gov.tubitak.uekae.esya.asn.cmp.PollRepContent;
import tr.gov.tubitak.uekae.esya.asn.cmp.PollReqContent;
import tr.gov.tubitak.uekae.esya.asn.cmp.RevRepContent;
import tr.gov.tubitak.uekae.esya.asn.cmp.RevRepContent_revCerts;
import tr.gov.tubitak.uekae.esya.asn.cmp.RevRepContent_status;
import tr.gov.tubitak.uekae.esya.asn.cmp.RevReqContent;
import tr.gov.tubitak.uekae.esya.asn.cmp.SuppLangTagsValue;
import tr.gov.tubitak.uekae.esya.asn.cmp._SeqOfCertResponse;
import tr.gov.tubitak.uekae.esya.asn.crmf.CertId;
import tr.gov.tubitak.uekae.esya.asn.crmf.CertReqMessages;
import tr.gov.tubitak.uekae.esya.asn.crmf.CertReqMsg;
import tr.gov.tubitak.uekae.esya.asn.esya.ESYACAControlRepMsg;
import tr.gov.tubitak.uekae.esya.asn.esya.ESYACAControlReqMsg;
import tr.gov.tubitak.uekae.esya.asn.pkcs10.CertificationRequest;

import java.math.BigInteger;
import java.util.List;

/**
 * User: zeldal.ozdemir
 * Date: 1/31/11
 * Time: 1:57 PM
 */
public class EPKIBody extends BaseASNWrapper<PKIBody> {

    protected static Logger logger = LoggerFactory.getLogger(EPKIBody.class);
    public final static byte IR = PKIBody._IR;
    public final static byte IP = PKIBody._IP;
    public final static byte CR = PKIBody._CR;
    public final static byte CP = PKIBody._CP;
    public final static byte P10CR = PKIBody._P10CR;
    public final static byte KUR = PKIBody._KUR;
    public final static byte KUP = PKIBody._KUP;
    public final static byte KRR = PKIBody._KRR;
    public final static byte KRP = PKIBody._KRP;
    public final static byte RR = PKIBody._RR;
    public final static byte RP = PKIBody._RP;
    public final static byte PKICONF = PKIBody._PKICONF;
    public final static byte NESTED = PKIBody._NESTED;

    public final static byte GENM = PKIBody._GENM;
    public final static byte GENP = PKIBody._GENP;

    public final static byte ERROR = PKIBody._ERROR;
    public final static byte CERTCONF = PKIBody._CERTCONF;
    public final static byte CVCREQ = PKIBody._CVCREQ;


    public EPKIBody() {
        super(new PKIBody());
    }

    public EPKIBody(byte[] aBytes) throws ESYAException {
        super(aBytes, new PKIBody());
    }

    public EPKIBody(PKIBody aObject) {
        super(aObject);
    }

    public int getChoiceID() {
        return mObject.getChoiceID();
    }

    public EESYACAControlReqMsg getCAControlReqMsg(){
        if(mObject == null || mObject.getElement() == null )
            return null;
        GenMsgContent genMsgContent = (GenMsgContent) mObject.getElement();
        InfoTypeAndValue[] infoTypeAndValues = genMsgContent.elements;
        for (InfoTypeAndValue infoTypeAndValue : infoTypeAndValues) {
            EInfoTypeAndValue eInfoTypeAndValue = new EInfoTypeAndValue(infoTypeAndValue);
            if(eInfoTypeAndValue.getObject().infoType.equals(EESYAOID.oid_cmpCAControlReqMsg)){
                try {
                    return new EESYACAControlReqMsg(eInfoTypeAndValue.getObject().infoValue.value);
                } catch (ESYAException e) {
                   logger.warn("Warning in EPKIBody", e);
                }
            }
        }
        return null;
    }

    public void setCAControlReqMessage(EESYACAControlReqMsg eesyacaControlReqMsg){
        ESYACAControlReqMsg object = eesyacaControlReqMsg.getObject();
        try {
            EInfoTypeAndValue eInfoTypeAndValue = new EInfoTypeAndValue(EESYAOID.oid_cmpCAControlReqMsg,object);
            InfoTypeAndValue[] recs = new InfoTypeAndValue[1];
            recs[0]=eInfoTypeAndValue.getObject();
            mObject.set_genm(new GenMsgContent(recs));
        } catch (ESYAException e) {
            logger.error("Error in EPKIBody", e);
        }
    }

    public EESYACAControlRepMsg getCAControlRepMsg(){
        if(mObject == null || mObject.getElement() == null )
            return null;
        GenRepContent genRepContent = (GenRepContent) mObject.getElement();
        InfoTypeAndValue[] infoTypeAndValues = genRepContent.elements;
        for (InfoTypeAndValue infoTypeAndValue : infoTypeAndValues) {
            EInfoTypeAndValue eInfoTypeAndValue = new EInfoTypeAndValue(infoTypeAndValue);
            if(eInfoTypeAndValue.getObject().infoType.equals(EESYAOID.oid_cmpCAControlRepMsg)){
                try {
                    return new EESYACAControlRepMsg(eInfoTypeAndValue.getObject().infoValue.value);
                } catch (ESYAException e) {
                    logger.error("Error in EPKIBody", e);
                }
            }
        }
        return null;
    }

    public void setCAControlRepMessage(EESYACAControlRepMsg eesyacaControlRepMsg){
        ESYACAControlRepMsg object = eesyacaControlRepMsg.getObject();
        try {
            EInfoTypeAndValue eInfoTypeAndValue = new EInfoTypeAndValue(EESYAOID.oid_cmpCAControlRepMsg,object);
            InfoTypeAndValue[] recs = new InfoTypeAndValue[1];
            recs[0]=eInfoTypeAndValue.getObject();
            mObject.set_genp(new GenRepContent(recs));
        } catch (ESYAException e) {
            logger.error("Error in EPKIBody", e);
        }
    }

    public ESuppLangTagsValue getSuppLangTagsValueReqeust(){
        if(mObject == null || mObject.getElement() == null )
            return null;
        GenMsgContent genReqContent = (GenMsgContent) mObject.getElement();
        InfoTypeAndValue[] infoTypeAndValues = genReqContent.elements;
        for (InfoTypeAndValue infoTypeAndValue : infoTypeAndValues) {
            EInfoTypeAndValue eInfoTypeAndValue = new EInfoTypeAndValue(infoTypeAndValue);
            if(eInfoTypeAndValue.getObject().infoType.equals(ECmpValues.oid_SuppLangTags.getObject())){
                try {
                    return new ESuppLangTagsValue(eInfoTypeAndValue.getObject().infoValue.value);
                } catch (ESYAException e) {
                    logger.error("Error in EPKIBody", e);
                }
            }
        }
        return null;
    }

    public void setSuppLangTagsValueRequest(ESuppLangTagsValue eSuppLangTagsValue){
        SuppLangTagsValue object = eSuppLangTagsValue.getObject();
        try {
            EInfoTypeAndValue eInfoTypeAndValue = new EInfoTypeAndValue(ECmpValues.oid_SuppLangTags.getObject(),object);
            InfoTypeAndValue[] recs = new InfoTypeAndValue[1];
            recs[0]=eInfoTypeAndValue.getObject();
            mObject.set_genm(new GenMsgContent(recs));
        } catch (ESYAException e) {
            logger.error("Error in EPKIBody", e);
        }
    }

    public ESuppLangTagsValue getSuppLangTagsValueResponse(){
        if(mObject == null || mObject.getElement() == null )
            return null;
        GenRepContent object = (GenRepContent) mObject.getElement();
        InfoTypeAndValue[] infoTypeAndValues = object.elements;
        for (InfoTypeAndValue infoTypeAndValue : infoTypeAndValues) {
            EInfoTypeAndValue eInfoTypeAndValue = new EInfoTypeAndValue(infoTypeAndValue);
            if(eInfoTypeAndValue.getObject().infoType.equals(ECmpValues.oid_SuppLangTags.getObject())){
                try {
                    return new ESuppLangTagsValue(eInfoTypeAndValue.getObject().infoValue.value);
                } catch (ESYAException e) {
                    logger.error("Error in EPKIBody", e);
                }
            }
        }
        return null;
    }

    public void setSuppLangTagsValueResponse(ESuppLangTagsValue eSuppLangTagsValue){
        SuppLangTagsValue object = eSuppLangTagsValue.getObject();
        try {
            EInfoTypeAndValue eInfoTypeAndValue = new EInfoTypeAndValue(ECmpValues.oid_SuppLangTags.getObject(),object);
            InfoTypeAndValue[] recs = new InfoTypeAndValue[1];
            recs[0]=eInfoTypeAndValue.getObject();
            mObject.set_genp(new GenRepContent(recs));
        } catch (ESYAException e) {
            logger.error("Error in EPKIBody", e);
        }
    }






    /*public void addToGeneralInfo(EInfoTypeAndValue infoTypeAndValue){
        if(mObject == null)
            throw new RuntimeException("Fix") ;
        if(mObject.generalInfo == null || mObject.generalInfo.elements == null)
            mObject.generalInfo = new PKIHeader_generalInfo();
        mObject.generalInfo.elements = extendArray(mObject.generalInfo.elements,infoTypeAndValue.getObject());
    }*/

    public ECertificationRequest getCertificationRequest() {
        if (mObject.getElement() instanceof CertificationRequest)
            return new ECertificationRequest((CertificationRequest) mObject.getElement());
        else
            return null;
    }

    public ECertReqMsg[] getCertReqMsgs() {
        if (mObject.getElement() instanceof CertReqMessages)
            return BaseASNWrapper.wrapArray(((CertReqMessages) mObject.getElement()).elements, ECertReqMsg.class);
        else
            return null;
    }

    public ECertResponse[] getCertRepMessage() {
        if (mObject.getElement() instanceof CertRepMessage)
            return BaseASNWrapper.wrapArray(((CertRepMessage) mObject.getElement()).response.elements, ECertResponse.class);
        else
            return null;
    }

    public ERevDetails[] getRevDetails() {
        if (mObject.getElement() instanceof RevReqContent)
            return BaseASNWrapper.wrapArray(((RevReqContent) mObject.getElement()).elements, ERevDetails.class);
        else
            return null;
    }

    public EPollReq[] getPollReqs() {
        if (mObject.getElement() instanceof PollReqContent)
            return BaseASNWrapper.wrapArray(((PollReqContent) mObject.getElement()).elements, EPollReq.class);
        else
            return null;
    }

    public EPollRep[] getPollReps() {
        if (mObject.getElement() instanceof PollRepContent)
            return BaseASNWrapper.wrapArray(((PollRepContent) mObject.getElement()).elements, EPollRep.class);
        else
            return null;
    }

    public EKeyRecRepContent getKeyRecRepContent() {
        if (mObject.getElement() instanceof KeyRecRepContent)
            return new EKeyRecRepContent((KeyRecRepContent) mObject.getElement());
        else
            return null;
    }

    public ECertStatus[] getCertConfirmStatuses() {
        if (mObject.getElement() instanceof CertConfirmContent)
            return BaseASNWrapper.wrapArray( ((CertConfirmContent) mObject.getElement()).elements, ECertStatus.class);
        else
            return null;
    }

    public void setIP(ECertResponse[] certResponses) {
        CertResponse[] responses = BaseASNWrapper.unwrapArray(certResponses);
        mObject.set_ip(new CertRepMessage(new _SeqOfCertResponse(responses)));
    }

    public void setCP(ECertResponse[] certResponses) {
        CertResponse[] responses = BaseASNWrapper.unwrapArray(certResponses);
        mObject.set_cp(new CertRepMessage(new _SeqOfCertResponse(responses)));
    }

    public void setKUP(ECertResponse[] certResponses) {
        CertResponse[] responses = BaseASNWrapper.unwrapArray(certResponses);
        mObject.set_kup(new CertRepMessage(new _SeqOfCertResponse(responses)));
    }

    public void setCertConf(ECertStatus[] certStatuses) {
        mObject.set_certConf(new CertConfirmContent(BaseASNWrapper.unwrapArray(certStatuses)));
    }

    public void setKRP(EKeyRecRepContent repContent) {
        mObject.set_krp(repContent.getObject());
    }


    public void setGenMsgContent(GenMsgContent genMsgContent) {
        mObject.set_genm(genMsgContent);
    }

    public void setGenRepContent(GenRepContent genRepContent) {
        mObject.set_genp(genRepContent);
    }

    public void setError(ErrorMsgContent errorMsgContent) {
        mObject.set_error(errorMsgContent);
    }

    public void setPollReq(EPollReq[] reqContents) {
        mObject.set_pollReq(new PollReqContent(BaseASNWrapper.unwrapArray(reqContents)));
    }

    public void setPollRep(EPollRep[] repContents) {
        mObject.set_pollRep(new PollRepContent(BaseASNWrapper.unwrapArray(repContents)));
    }

    public void setRP(List<Triple<EPKIStatusInfo, EGeneralName, BigInteger>> revContents) {
        PKIStatusInfo[] pkiStatusInfos = new PKIStatusInfo[revContents.size()];
        CertId[] certIds = new CertId[revContents.size()];
        for (int i = 0; i < revContents.size(); i++) {
            Triple<EPKIStatusInfo, EGeneralName, BigInteger> revContent = revContents.get(i);
            pkiStatusInfos[i] = revContent.getmNesne1().getObject();
            certIds[i] = new CertId(revContent.getmNesne2().getObject(), new Asn1BigInteger(revContent.getmNesne3()));
        }

        RevRepContent repContent = new RevRepContent();
        repContent.status = new RevRepContent_status(pkiStatusInfos);
        repContent.revCerts = new RevRepContent_revCerts(certIds);
        mObject.set_rp(repContent);
    }

    public void setPkiConf() {
        mObject.set_pkiconf();
    }

    public boolean isCertificationRequest() {
        return mObject.getElement() instanceof CertificationRequest;
    }

    public boolean isCertReqMessages() {
        return mObject.getElement() instanceof CertReqMessages;
    }

    public boolean isCertRepMessage() {
        return mObject.getElement() instanceof CertRepMessage;
    }


    public void setCertReqMessages(byte choice, CertReqMsg[] certReqMessages) {
        mObject.setElement(choice,new CertReqMessages(certReqMessages));
    }
}

package tr.gov.tubitak.uekae.esya.api.asn.x509;

import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.asn.util.UtilTime;
import tr.gov.tubitak.uekae.esya.asn.x509.*;

import java.util.Calendar;

/**
 * @author ayetgin
 */
public class ETBSCertList extends BaseASNWrapper<TBSCertList>
{
    public ETBSCertList() {
        super(new TBSCertList());
    }

    public ETBSCertList(TBSCertList aObject)
    {
        super(aObject);
    }

    public ETBSCertList(long aVersion,
                       EAlgorithmIdentifier aSignatureAlg, EName aIssuer,
                       Calendar aThisUpdate, Calendar aNextUpdate,
                       ERevokedCertificateElement[] aRevokedCerts,
                       EExtensions aExtensions){
        super(
                new TBSCertList(aVersion,
                                aSignatureAlg.getObject(),
                                aIssuer.getObject(),
                                UtilTime.calendarToTimeFor3280(aThisUpdate),
                                UtilTime.calendarToTimeFor3280(aNextUpdate),
                                new _SeqOfTBSCertList_revokedCertificates_element(unwrapArray(aRevokedCerts)),
                                aExtensions.getObject())
        );
    }

    public EVersion getVersion(){
        if(mObject.version == null)
            return null;
        else
            return new EVersion(mObject.version.value);
    }

    public void setVersion(long aVersion){
        mObject.version= new Version(aVersion);
    }


    public void setVersion(EVersion aVersion){
        mObject.version= aVersion.getObject();
    }

    public EAlgorithmIdentifier getSignatureAlgorithm(){
        return new EAlgorithmIdentifier(mObject.signature);
    }

    public void setSignatureAlgorithm(EAlgorithmIdentifier aSignature){
        mObject.signature = aSignature.getObject();
    }

    public EName getIssuer(){
        return new EName(mObject.issuer);
    }

    public void setIssuer(EName aName){
        mObject.issuer = aName.getObject();
    }

    public ETime thisUpdate(){
        return new ETime(mObject.thisUpdate);
    }

    public void setThisUpdate(Calendar aThisUpdate){
        mObject.thisUpdate = UtilTime.calendarToTimeFor3280(aThisUpdate);
    }

    public ETime nextUpdate(){
        if (mObject.nextUpdate==null)
            return null;

        return new ETime(mObject.nextUpdate);
    }

    public void setNextUpdate(Calendar aNextUpdate){
        if (aNextUpdate==null)
            mObject.nextUpdate = null;

        mObject.nextUpdate = UtilTime.calendarToTimeFor3280(aNextUpdate);
    }

    public ERevokedCertificateElement[] getRevokedCertificates(){
        return wrapArray(mObject.revokedCertificates.elements, ERevokedCertificateElement.class);
    }

    public void setRevokedCertificates(ERevokedCertificateElement[] aElements){
        if (aElements==null)
            mObject.revokedCertificates = null;
        else
            mObject.revokedCertificates = new _SeqOfTBSCertList_revokedCertificates_element(unwrapArray(aElements));
    }

    public EExtensions getCrlExtensions(){
        if (mObject.crlExtensions==null)
            return null;
        return new EExtensions(mObject.crlExtensions);
    }

    public void setCrlExtensions(EExtensions aExtensions){
        if (aExtensions==null)
            mObject.crlExtensions = null;
        else
            mObject.crlExtensions = aExtensions.getObject();
    }


}

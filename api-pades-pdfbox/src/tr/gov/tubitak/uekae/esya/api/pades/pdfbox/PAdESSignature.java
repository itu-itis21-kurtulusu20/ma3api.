package tr.gov.tubitak.uekae.esya.api.pades.pdfbox;


import com.objsys.asn1j.runtime.Asn1ObjectIdentifier;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.cos.COSString;
import org.apache.pdfbox.pdmodel.interactive.digitalsignature.PDSignature;
import org.apache.pdfbox.pdmodel.interactive.digitalsignature.SignatureOptions;
import org.apache.pdfbox.util.DateConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.cms.EAttribute;
import tr.gov.tubitak.uekae.esya.api.asn.cms.EContentInfo;
import tr.gov.tubitak.uekae.esya.api.asn.ocsp.EBasicOCSPResponse;
import tr.gov.tubitak.uekae.esya.api.asn.ocsp.EOCSPResponse;
import tr.gov.tubitak.uekae.esya.api.asn.pkixtsp.ETSTInfo;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.cmssignature.CMSSignatureException;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.AllEParameters;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.CommitmentType;
import tr.gov.tubitak.uekae.esya.api.cmssignature.provider.CMSSigProviderUtil;
import tr.gov.tubitak.uekae.esya.api.cmssignature.provider.CMSSignatureImpl;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.BaseSignedData;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.check.CertificateChecker;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.check.CheckerResult;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.ESYARuntimeException;
import tr.gov.tubitak.uekae.esya.api.common.crypto.BaseSigner;
import tr.gov.tubitak.uekae.esya.api.common.util.StringUtil;
import tr.gov.tubitak.uekae.esya.api.crypto.DTBSRetrieverSigner;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.Algorithm;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException;
import tr.gov.tubitak.uekae.esya.api.crypto.util.DigestUtil;
import tr.gov.tubitak.uekae.esya.api.infra.mobile.MobileDTBSRetrieverSigner;
import tr.gov.tubitak.uekae.esya.api.infra.mobile.MobileSigner;
import tr.gov.tubitak.uekae.esya.api.pades.pdfbox.util.PDFBoxUtil;
import tr.gov.tubitak.uekae.esya.api.pades.pdfbox.validation.ByteRange;
import tr.gov.tubitak.uekae.esya.api.pades.pdfbox.validation.ByteRangeCollection;
import tr.gov.tubitak.uekae.esya.api.pades.pdfbox.validation.PadesSignatureValidator;
import tr.gov.tubitak.uekae.esya.api.signature.*;
import tr.gov.tubitak.uekae.esya.api.signature.attribute.*;
import tr.gov.tubitak.uekae.esya.api.signature.certval.ValidationInfoResolver;
import tr.gov.tubitak.uekae.esya.api.signature.impl.TimestampInfoImp;
import tr.gov.tubitak.uekae.esya.asn.cms.ContentInfo;
import tr.gov.tubitak.uekae.esya.asn.cms._cmsValues;

import java.io.*;
import java.security.spec.AlgorithmParameterSpec;
import java.util.*;
import java.util.List;

public class PAdESSignature implements Signature {

    private static Logger logger = LoggerFactory.getLogger(PAdESSignature.class);

    protected String pdfFieldName;
    protected int index;

    protected PAdESContainer container;
    protected PDSignature pdSignature;
    protected ECertificate certificate;


    private Signature internalSignature;

    protected Calendar signingTime;

    protected boolean timestamp;
    private ETSTInfo tstInfo;

    private VisibleSignature visibleSignature;

    public PAdESSignature(PAdESContainer container, PDSignature pdSignature, ECertificate certificate, int index) {
        this.pdfFieldName = "Signature " + (index + 1);
        this.container = container;
        this.pdSignature = pdSignature;
        this.certificate = certificate;
        this.index = index;

        SignatureContainer cadesContainer = SignatureFactory.createContainer(SignatureFormat.CAdES, container.getContext());
        internalSignature = cadesContainer.createSignature(certificate);

        this.timestamp = false;
    }

    public PDSignature getPdSignature() {
        return pdSignature;
    }


    public PAdESSignature(String name, PAdESContainer container, Signature internalSignature, int index, boolean isTimestamp) {
        this.pdfFieldName = name;
        this.container = container;
        this.internalSignature  =  internalSignature;
        this.index = index;

        try {
            this.pdSignature = container.document.getSignatureDictionaries().get(index);
        } catch (Exception x) {
            throw new SignatureRuntimeException(x);
        }

        this.timestamp = isTimestamp;

        logger.debug("Signing time : {}", internalSignature.getSigningTime());
        if (timestamp){
            try {
                tstInfo = new ETSTInfo(((BaseSignedData)internalSignature.getContainer().getUnderlyingObject()).getSignedData().getEncapsulatedContentInfo().getContent());
                logger.debug("Timestamp time : {}", tstInfo.getTime().getTime());
            } catch (Exception x){
                throw new SignatureRuntimeException(x);
            }
        }
    }

    public void setVisibleSignature(VisibleSignature visibleSignature) {
        this.visibleSignature = visibleSignature;
    }

    public Signature getInternalSignature()
    {
        return internalSignature;
    }

    public void setSigningTime(Calendar aTime) {
        signingTime = aTime;
    }

    public Calendar getSigningTime() {
        Calendar stime = pdSignature.getSignDate();
        if (stime !=null) {
            return stime;
        }
        return signingTime;
    }

    public void setSignaturePolicy(SignaturePolicyIdentifier policyId) {
        internalSignature.setSignaturePolicy(policyId);
    }

    public SignaturePolicyIdentifier getSignaturePolicy() {
        return internalSignature.getSignaturePolicy();
    }

    public void setCommitmentType(CommitmentType commitmentType) throws CMSSignatureException {
        ((CMSSignatureImpl) internalSignature).setCommitmentType(commitmentType);
    }

    public List<EAttribute> getCommitmentTypeAttributes() {
        return ((CMSSignatureImpl) internalSignature).getCommitmentTypeAttributes();
    }

    public List<TimestampInfo> getTimestampInfo(TimestampType type) {
        try {
            List<TimestampInfo> all = new ArrayList<TimestampInfo>();

            if(type == TimestampType.SIGNATURE_TIMESTAMP ){
                //CMS yapısı içinde zaman damgası.
                List<TimestampInfo> ti = ((Signature)getUnderlyingObject()).getTimestampInfo(TimestampType.SIGNATURE_TIMESTAMP);
                if (ti!=null && ti.size()>0)
                    all.addAll(ti);

                //İmza sonrasında yer alan imzalar zaman damgası imzası olabilir.
                List<Signature> signatures = container.getSignatures();
                for (int i= index + 1; i < signatures.size(); i++) {
                    PAdESSignature ps = (PAdESSignature) signatures.get(i);
                    if (ps.isTimestamp()) {
                        BaseSignedData bsd = (BaseSignedData) ((Signature) ps.getUnderlyingObject()).getUnderlyingObject();
                        all.add(new TimestampInfoImp(TimestampType.SIGNATURE_TIMESTAMP, bsd.getSignedData(), ps.getTstInfo()));
                    }
                }
            }
            //Arşiv imza zaman damgası olması için doğrulama verilerini de imzalanması lazım. Bunun burada karar verilmesi zor olduğundan en sondaki
            //zaman damgaları alındı. Son kullanıcı imzasından sonraki zaman damgası imzası imza zaman damgası sonrakiler arşiv zaman damgası olarak ele
            //alındı.
            else if(type == TimestampType.ARCHIVE_TIMESTAMP){
                List<Signature> signatures = container.getSignatures();
                for(int i = signatures.size() - 1; i >= index; i--){
                    PAdESSignature ps = (PAdESSignature) signatures.get(i);
                    if (ps.isTimestamp()) {
                        BaseSignedData bsd = (BaseSignedData) ((Signature) ps.getUnderlyingObject()).getUnderlyingObject();
                        all.add(new TimestampInfoImp(TimestampType.SIGNATURE_TIMESTAMP, bsd.getSignedData(), ps.getTstInfo()));
                    } else {
                        //Kullanıcı imzası sonraki imza, imza zaman damgası olduğu için çıkartıldı.
                        if(all.size() > 0)
                            all.remove(all.size() - 1);

                        //Eklemeye sondan başlandığı için ters döndürülür.
                        if(all.size() > 0)
                            Collections.reverse(all);

                        break;
                    }
                }
            }
            return all;
        }
        catch (Exception x) {
            throw new SignatureRuntimeException(x);
        }
    }

    public List<TimestampInfo> getAllTimestampInfos() {
        List<TimestampInfo> all = new ArrayList<TimestampInfo>();
        all.addAll(getTimestampInfo(TimestampType.SIGNATURE_TIMESTAMP));
        return all;
    }

    public CertValidationReferences getCertValidationReferences() {
        // no reference
        return null;
    }

    public CertValidationValues getCertValidationValues() {
        CertValidationValues cvv = null;
        DocSecurityStore dss = container.getDocumentSecurityStore();
        if (dss!=null){
            List<EBasicOCSPResponse> basicOCSPResponses = new ArrayList<EBasicOCSPResponse>();
            for (EOCSPResponse r : dss.getOCSPResponses())
                basicOCSPResponses.add(r.getBasicOCSPResponse());

            cvv = new CertValidationValues(dss.getCertificates(), dss.getCRLs(), basicOCSPResponses);
        }
        return cvv;
    }

    public Signature createCounterSignature(ECertificate certificate) throws SignatureException {
        throw new NotSupportedException();
    }

    public List<Signature> getCounterSignatures() {
        return Collections.emptyList();
    }

    public void detachFromParent() throws SignatureException {
        throw new NotSupportedException();
    }

    public void addContent(Signable aData, boolean includeContent) throws SignatureException {
        if (includeContent)
            throw new NotSupportedException("Cannot include content");
        internalSignature.addContent(aData, false);
    }

    public List<Signable> getContents() throws SignatureException {
        return  internalSignature.getContents();
    }

    public Algorithm getSignatureAlg() {
        return internalSignature.getSignatureAlg();
    }

    public byte [] initSign(SignatureAlg signatureAlg, AlgorithmParameterSpec algorithmParams) throws SignatureException {
        DTBSRetrieverSigner dtbsRetrieverSigner = new DTBSRetrieverSigner(signatureAlg, algorithmParams);
        sign(dtbsRetrieverSigner);
        return dtbsRetrieverSigner.getDtbs();
    }

    public byte[] initSignForMobile(MobileSigner mobileSigner) throws SignatureException {
        MobileDTBSRetrieverSigner dtbsRetrieverSigner = new MobileDTBSRetrieverSigner(mobileSigner.getmConnector(), mobileSigner.getmUserIden(), mobileSigner.getSigningCert(), mobileSigner.getInformativeText(), mobileSigner.getSignatureAlgorithmStr(), mobileSigner.getAlgorithmParameterSpec());
        sign(dtbsRetrieverSigner);
        return dtbsRetrieverSigner.getDtbs();
    }

    public void sign(BaseSigner cryptoSigner) throws SignatureException {

        if(signingTime != null)
            pdSignature.setSignDate(this.signingTime);

        try {
            if (visibleSignature != null) {

                InputStream visualSignatureTemplate = PDFBoxUtil.createVisualSignatureTemplate(container.document, visibleSignature);

                SignatureOptions signatureOptions = new SignatureOptions();
                signatureOptions.setPage(visibleSignature.getSignaturePanel().getPageNum()-1);
                signatureOptions.setVisualSignature(visualSignatureTemplate);

                PdfBoxSignatureInterface signatureInterface = new PdfBoxSignatureInterface(internalSignature, cryptoSigner, this.container.getContext());
                container.document.addSignature(pdSignature, signatureInterface, signatureOptions);
                container.updatePDF();

                signatureOptions.close();

            } else {

                PdfBoxSignatureInterface signatureInterface = new PdfBoxSignatureInterface(internalSignature, cryptoSigner, this.container.getContext());
                container.document.addSignature(pdSignature, signatureInterface);
                container.updatePDF();
            }
        } catch (IOException ex)
        {
                throw new SignatureException("Error while signing", ex);
        }
    }

    public void upgrade(SignatureType type) throws SignatureException
    {
        PAdESUpgrader pAdESUpgrader = new PAdESUpgrader(container, this);
        SignatureType currentType = getSignatureType();
        pAdESUpgrader.upgrade(currentType, type);
    }

    public SignatureValidationResult verify() throws SignatureException {

        PadesSignatureValidator validator = new PadesSignatureValidator();
        return validator.verify(this);
    }

    public void addArchiveTimestamp() throws SignatureException {
        upgrade(SignatureType.ES_A);
    }

    public SignatureType getSignatureType() {
        try
        {
            boolean hasTS = false;
            boolean hasVD = false;
            boolean hasTSThatCoversVD = false;

            Calendar tsTime = null;

            //İmza içerisindeki zaman damgaları kontrol edilir.
            List<TimestampInfo> cmsTSI = this.internalSignature.getTimestampInfo(TimestampType.SIGNATURE_TIMESTAMP);
            if (cmsTSI.size() > 0) {
                hasTS = true;

                EContentInfo ci = new EContentInfo(new ContentInfo());
                ci.setContentType(new Asn1ObjectIdentifier(_cmsValues.id_signedData));
                ci.setContent(cmsTSI.get(0).getSignedData().getEncoded());

                tsTime = cmsTSI.get(0).getTSTInfo().getTime();
            } else {
                //Sonraki imzaların içindeki zaman damgaları da kontrol edilebilir.

                //Document Time Stampt'ler kontrol edilir.
                List<Signature> signatures = container.getSignatures();
                for(int i=index+1; i < signatures.size(); i++){
                    PAdESSignature postSignature = (PAdESSignature)signatures.get(i);
                    if(postSignature.isTimestamp() == true) {
                        tsTime = postSignature.getTimeStampTime();
                        hasTS = true;
                        break;
                    }
                }
            }

            if (hasTS == true) {
                DocSecurityStore dss = container.getDocumentSecurityStore();
                if(dss != null) {
                    CertValidationValues allDssValidationValues = new CertValidationValues(
                            dss.getCertificates(),
                            dss.getCRLs(),
                            EOCSPResponse.getEBasicOCSPResponseArrayList(dss.getOCSPResponses()));


                    PAdESSignature latestTS = getLatestDocumentTimeStamp();
                    if(latestTS != null){
                        ByteRangeCollection tsSignRange = latestTS.getSignatureInputByteRange();
                        CertValidationValues validationInfo = dss.filterByRange(container, tsSignRange);

                        if (validateCertificate(validationInfo, tsTime)) {
                            hasVD = true;
                            hasTSThatCoversVD = true;
                        }else if (validateCertificate(allDssValidationValues, tsTime)) {
                            hasVD = true;
                        }
                    }else if (validateCertificate(allDssValidationValues, tsTime)) {
                        hasVD = true;
                    }
                }
            }

            if (hasTSThatCoversVD == true)
                return SignatureType.ES_A;
            if (hasVD && hasTS)
                return SignatureType.ES_XL;
            else if (hasTS == true)
                return SignatureType.ES_T;
            else
                return SignatureType.ES_BES;

        }  catch (Exception e) {
            throw new ESYARuntimeException("Signature type can not be defined!", e);
        }
    }

    private boolean validateCertificate(CertValidationValues validationValues, Calendar tsTime) {
        CheckerResult result = new CheckerResult();

        ValidationInfoResolver validationInfoResolver = new ValidationInfoResolver();
        validationInfoResolver.addCertificates(validationValues.getCertificates());
        validationInfoResolver.addCRLs(validationValues.getCrls());
        ArrayList<EOCSPResponse> ocspResponseArrayList = EOCSPResponse.getEOCSPResponseArrayList(validationValues.getOcspResponses());
        validationInfoResolver.addOCSPResponses(ocspResponseArrayList);

        Map<String, Object> params = CMSSigProviderUtil.toSignatureParameters(container.getContext());
        params.put(AllEParameters.P_VALIDATION_INFO_RESOLVER, validationInfoResolver);

        CertificateChecker certChecker = new CertificateChecker();
        certChecker.setParameters(params);

        return certChecker.checkCertificateAtTime(getSignerCertificate(), result, tsTime, true, true, true);
    }

    private ByteRangeCollection getSignatureInputByteRange() throws IOException {

        PDSignature signatureDictionary = container.document.getSignatureDictionaries().get(index);
        int[] signedBytes = signatureDictionary.getByteRange();

        ByteRangeCollection byteRangeCollection = new ByteRangeCollection();
        for(int i = 0; i < signedBytes.length; i = i + 2){
            ByteRange aRange = new ByteRange(signedBytes[i], signedBytes[i+1]);
            byteRangeCollection.add(aRange);
        }

        return byteRangeCollection;
    }

    private PAdESSignature getLatestDocumentTimeStamp(){
        List<Signature> signatures = container.getSignatures();
        for (int i = signatures.size() - 1; i > index; i--) {
            PAdESSignature signature = (PAdESSignature) signatures.get(i);
            if(signature.isTimestamp())
                return signature;
        }
        return null;
    }


    public SignatureFormat getSignatureFormat() {
        return SignatureFormat.PAdES;
    }


    public ECertificate getSignerCertificate(){
        if (certificate!=null)
            return certificate;
        return  internalSignature.getSignerCertificate();
    }

    public SignatureContainer getContainer(){
        return container;
    }

    public String getPdfFieldName() {
        return pdfFieldName;
    }

    ETSTInfo getTstInfo(){
        return tstInfo;
    }

    public Object getUnderlyingObject() {
        return internalSignature;
    }

    public boolean isTimestamp() {
        return timestamp;
    }

    public boolean isLastSignature(){
        return container.getSignatures().size()-1 == index;
    }

    public boolean canBeUpgradable(){
        if(isLastSignature())
            return true;

        //Hepsi Zaman Damgalı Olmalı.
        List<Signature> signatures = container.getSignatures();
        for(int i = index + 1; i < signatures.size(); i++){
            PAdESSignature aSignature = (PAdESSignature)signatures.get(i);
            if(!aSignature.isTimestamp())
                return false;
        }
        return true;
    }

    public Calendar getSigningTimeAttrFromM() {

        COSDictionary dict = pdSignature.getCOSObject();
        COSString str = (COSString)dict.getDictionaryObject(COSName.M);
        if(str != null){
           return DateConverter.toCalendar(str);
        }
        return null;
    }

    public Calendar getSigningTimeAttrFromCMS() {

        return internalSignature.getSigningTime();
    }

    public Calendar  getDocTimeStampTime() {

        if (timestamp){
            return tstInfo.getTime();
        }

        if(!isLastSignature()){
            return ((PAdESSignature)container.getSignatures().get(index+1)).getDocTimeStampTime();
        }
        return null;
    }

    public Calendar getTimeStampTime(){

        if (timestamp){
            return tstInfo.getTime();
        }

        List<TimestampInfo> stsList = internalSignature.getTimestampInfo(TimestampType.SIGNATURE_TIMESTAMP);

        if (stsList!=null && stsList.size()>0){
            TimestampInfo tsi = stsList.get(0);
            return tsi.getTSTInfo().getTime();
        }

        if(!isLastSignature()){
            return ((PAdESSignature)container.getSignatures().get(index+1)).getTimeStampTime();
        }

        return null;
    }

    public Calendar  getSigningTimeAttrTime() {
        Calendar time = getSigningTimeAttrFromM();
        if(time == null){
            time = getSigningTimeAttrFromCMS();
        }
        return time;
    }

    public int getIndex(){
        return index;
    }

    public String toString() {
        return pdfFieldName;
    }

    public boolean hasVRI() throws SignatureException {
        try {

            final PDSignature pdSignature = container.document.getSignatureDictionaries().get(index);
            COSDictionary sigDict = pdSignature.getCOSObject();

            String subFilter = sigDict.getCOSName(COSName.SUB_FILTER).getName();
            COSString contents = (COSString)sigDict.getDictionaryObject(COSName.CONTENTS);

            byte[] bytes = contents.getBytes();

            if("ETSI.RFC3161".equals(subFilter)){
                EContentInfo info = new EContentInfo(bytes);
                bytes = info.getEncoded();
            }

            byte[] digestBytes = DigestUtil.digest(DigestAlg.SHA1, bytes);

            String digestHex = StringUtil.toHexString(digestBytes);

            COSBase dssElement = container.document.getDocumentCatalog().getCOSObject().getDictionaryObject("DSS");

            boolean result = false;
            if(dssElement instanceof COSDictionary) {

                COSDictionary dss = (COSDictionary) dssElement;

                COSBase vriElement = dss.getDictionaryObject("VRI");
                if(vriElement instanceof COSDictionary) {
                    COSDictionary vri = (COSDictionary) vriElement;

                    result = vri.containsKey(digestHex);
                }
            }

            return result;
        }
        catch (IOException ex){
            throw new SignatureException(ex);
        } catch (CryptoException ex) {
            throw new SignatureException(ex);
        } catch (ESYAException ex) {
            throw new SignatureException(ex);
        }
    }

}

package tr.gov.tubitak.uekae.esya.api.pades.pdfbox;

import org.apache.pdfbox.cos.*;
import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.asn.ocsp.EOCSPResponse;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECRL;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.common.util.StringUtil;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException;
import tr.gov.tubitak.uekae.esya.api.crypto.util.DigestUtil;
import tr.gov.tubitak.uekae.esya.api.pades.pdfbox.util.PAdESUtil;
import tr.gov.tubitak.uekae.esya.api.pades.pdfbox.validation.ByteRange;
import tr.gov.tubitak.uekae.esya.api.pades.pdfbox.validation.ByteRangeCollection;
import tr.gov.tubitak.uekae.esya.api.signature.SignatureRuntimeException;
import tr.gov.tubitak.uekae.esya.api.signature.attribute.CertValidationValues;

import java.io.ByteArrayInputStream;
import java.util.*;

import static tr.gov.tubitak.uekae.esya.api.pades.pdfbox.util.PDFBoxUtil.*;
import static tr.gov.tubitak.uekae.esya.api.pades.pdfbox.util.PDFBoxUtil.createCOSArray;

public class DocSecurityStore {

    private COSDictionary dss;
    private List<ECertificate> certificates;
    private List<ECRL> crls;
    private List<EOCSPResponse> ocspResponses;
    private Map<String, List<COSObject>> referenceMap;

    public DocSecurityStore(COSDictionary dss) {
        this.dss = dss;
        referenceMap = new HashMap<String, List<COSObject>>();
    }

    public List<ECertificate> getCertificates()  {
        if (certificates != null)
            return certificates;

        certificates = new ArrayList<ECertificate>();

        if (dss == null)
            return certificates;

        COSBase certsElement = dss.getDictionaryObject("Certs");
        if(certsElement == null)
            return certificates;

        if(certsElement instanceof COSArray){
            COSArray certArray = (COSArray) certsElement;

            for(int i =0; i<certArray.size(); i++) {
                try {
                    COSStream stream = (COSStream)certArray.getObject(i);
                    ECertificate certificate = new ECertificate(new ByteArrayInputStream(getStreamBytes(stream)));
                    certificates.add(certificate);

                    if(certArray.get(i) instanceof COSObject)
                      addToReferenceMap(certificate, (COSObject)certArray.get(i));

                }catch (Exception x){
                    throw new SignatureRuntimeException(x);
                }
            }
        }
        return certificates;
    }

    public void updateCertificates(List<ECertificate> certs)  {

        if(certs != null && !certs.isEmpty()) {

            COSBase certsElement = dss.getDictionaryObject("Certs");
            COSArray certArray;

            if (certsElement != null && certsElement instanceof COSArray) {

                certArray = (COSArray) certsElement;

                List<ECertificate> certificatesFoundInDss = getCertificates();
                List<ECertificate> certsNeedToBeAddedToValidationRecords = PAdESUtil.findDifferentOnes(certificatesFoundInDss, certs);
                certArray.addAll(createCOSArray(certsNeedToBeAddedToValidationRecords));

            } else
                certArray = createCOSArray(certs);

            dss.setItem("Certs", certArray);
        }
    }

    public List<ECRL> getCRLs()  {
        if (crls!=null)
            return crls;
        crls = new ArrayList<ECRL>();
        if(dss == null)
            return crls;

        COSBase crlElement = dss.getDictionaryObject("CRLs");
        if(crlElement == null)
            return crls;

        if(crlElement instanceof COSArray){
            COSArray crlArray = (COSArray) crlElement;

            for(int i =0; i<crlArray.size(); i++) {
                try {
                    COSStream stream = (COSStream)crlArray.getObject(i);
                    ECRL crl = new ECRL(new ByteArrayInputStream(getStreamBytes(stream)));
                    crls.add(crl);

                    if(crlArray.get(i) instanceof COSObject)
                       addToReferenceMap(crl, (COSObject)crlArray.get(i));
                }
                catch (Exception e){
                    throw new SignatureRuntimeException(e);
                }
            }
        }
        return crls;
    }

    public void updateCrls(List<ECRL> crls)  {

        if(crls != null && !crls.isEmpty()) {

            COSBase crlsElement = dss.getDictionaryObject("CRLs");

            COSArray crlArray;
            if (crlsElement != null && crlsElement instanceof COSArray) {

                crlArray = (COSArray) crlsElement;
                List<ECRL> crlsFoundInDss = getCRLs();

                List<ECRL> crlsNeedToBeAddedToValidationRecords = PAdESUtil.findDifferentOnes(crlsFoundInDss, crls);
                crlArray.addAll(createCOSArray(crlsNeedToBeAddedToValidationRecords));

            } else
                crlArray = createCOSArray(crls);

            dss.setItem("CRLs", crlArray);
        }
    }

    public List<EOCSPResponse> getOCSPResponses()  {
        if(ocspResponses != null)
            return ocspResponses;
        ocspResponses = new ArrayList<EOCSPResponse>();
        if(dss == null)
            return ocspResponses;

        COSBase ocspElement = dss.getDictionaryObject("OCSPs");
        if(ocspElement == null)
            return ocspResponses;

        if(ocspElement instanceof COSArray){
            COSArray ocspArray = (COSArray) ocspElement;

            for(int i =0; i<ocspArray.size(); i++) {
                try {
                    COSStream stream = (COSStream) ocspArray.getObject(i);
                    byte[] bytes = getStreamBytes(stream);
                    EOCSPResponse ocspResponse = new EOCSPResponse(bytes);
                    if (ocspResponse.getResponseStatus() == 0) {
                        ocspResponses.add(ocspResponse);

                        if(ocspArray.get(i) instanceof COSObject)
                           addToReferenceMap(ocspResponse, (COSObject) ocspArray.get(i));
                    }
                }catch (Exception x){
                    throw new SignatureRuntimeException(x);
                }
            }
        }
        return ocspResponses;
    }

    public void updateOCSPs(List<EOCSPResponse> ocsps)  {

        if(ocsps != null && ocsps.size() > 0) {

            COSBase ocspElement = dss.getDictionaryObject("OCSPs");

            COSArray ocspArray;
            if (ocspElement != null && ocspElement instanceof COSArray) {

                ocspArray = (COSArray) ocspElement;
                List<EOCSPResponse> ocspResponsesFoundInDss = getOCSPResponses();

                List<EOCSPResponse> ocspsNeedToBeAddedToValidationRecords = PAdESUtil.findDifferentOnes(ocspResponsesFoundInDss, ocsps);
                ocspArray.addAll(createCOSArray(ocspsNeedToBeAddedToValidationRecords));

            } else
                ocspArray = createCOSArray(ocsps);

            dss.setItem("OCSPs", ocspArray);
        }
    }

    public void updateVRIRecords(COSDictionary vriRecords){

        if(vriRecords != null && vriRecords.size() > 0){

            COSBase vriElement = dss.getDictionaryObject("VRI");
            COSDictionary vriArray;

            if (vriElement != null && vriElement instanceof COSDictionary) {
                vriArray = (COSDictionary) vriElement;

                vriRecords = updateObjectNumbersInVri(vriRecords);
                vriArray.addAll(vriRecords);
            }
            else {
                vriRecords = updateObjectNumbersInVri(vriRecords);
                vriArray = vriRecords;
            }

            vriArray.setNeedToBeUpdated(true);
            dss.setItem("VRI", vriArray);
        }
    }

    private COSDictionary updateObjectNumbersInVri(COSDictionary vriRecords) {

        if(vriRecords != null && vriRecords.size() > 0){

            Iterator<COSBase> vriRecordIterator = vriRecords.getValues().iterator();
            while(vriRecordIterator.hasNext()){
                COSBase vriRecord = vriRecordIterator.next();

                if (((COSDictionary) vriRecord).getDictionaryObject(COSName.CERT) != null) {
                    COSBase certElement = ((COSDictionary) vriRecord).getDictionaryObject(COSName.CERT).getCOSObject();

                    if (certElement != null && certElement instanceof COSArray) {
                        COSArray certArray = (COSArray) certElement;

                        updateObjectNumbersOfCertsInVRI(certArray);
                    }
                }

                if (((COSDictionary) vriRecord).getDictionaryObject("CRL") != null) {

                    COSBase crlElement = ((COSDictionary) vriRecord).getDictionaryObject("CRL").getCOSObject();
                    if (crlElement != null && crlElement instanceof COSArray) {
                        COSArray crlArray = (COSArray) crlElement;

                        updateObjectNumbersOfCrlsInVRI(crlArray);

                    }
                }

                if (((COSDictionary) vriRecord).getDictionaryObject("OCSP") != null) {

                    COSBase ocspElement = ((COSDictionary) vriRecord).getDictionaryObject("OCSP").getCOSObject();
                    if (ocspElement != null && ocspElement instanceof COSArray) {
                        COSArray ocspArray = (COSArray) ocspElement;

                        updateObjectNumbersOfOcspsInVRI(ocspArray);

                    }
                }
            }
        }
        return vriRecords;

    }

    private void updateObjectNumbersOfOcspsInVRI(COSArray ocspArray) {

        EOCSPResponse ocspInVri = null;
        for (int j = 0; j < ocspArray.size(); j++) {
            try {
                byte[] ocspBytes = getStreamBytes((COSStream) ocspArray.getObject(j));
                ocspInVri = new EOCSPResponse(ocspBytes);

                //check whether ocsp is found in DSS
                COSBase ocspInDSSElement = dss.getDictionaryObject("OCSPs");
                COSArray ocspInDSSArray;

                if (ocspInDSSElement != null && ocspInDSSElement instanceof COSArray) {
                    ocspInDSSArray = (COSArray) ocspInDSSElement;

                    for (int i = 0; i < ocspInDSSArray.size(); i++) {
                        try {
                            byte[] ocspInDSSBytes = getStreamBytes((COSStream) ocspInDSSArray.getObject(i));
                            EOCSPResponse ocspInDSS = new EOCSPResponse(ocspInDSSBytes);

                            if (ocspInVri.equals(ocspInDSS)) {
                                ocspArray.set(j, ocspInDSSArray.getObject(i));
                            }
                        } catch (Exception x) {
                            throw new SignatureRuntimeException(x);
                        }
                    }
                }
            } catch (Exception x) {
                throw new SignatureRuntimeException(x);
            }
        }
    }

    private void updateObjectNumbersOfCrlsInVRI(COSArray crlArray) {

        ECRL crlInVri = null;
        for (int j = 0; j < crlArray.size(); j++) {
            try {
                byte[] crlBytes = getStreamBytes((COSStream) crlArray.getObject(j));
                crlInVri = new ECRL(crlBytes);

                //check whether crl is found in DSS
                COSBase crlInDSSElement = dss.getDictionaryObject("CRLs");
                COSArray crlInDSSArray;

                if (crlInDSSElement != null && crlInDSSElement instanceof COSArray) {
                    crlInDSSArray = (COSArray) crlInDSSElement;

                    for (int i = 0; i < crlInDSSArray.size(); i++) {
                        try {

                            byte[] crlInDSSBytes = getStreamBytes((COSStream) crlInDSSArray.getObject(i));
                            ECRL crlInDSS = new ECRL(crlInDSSBytes);

                            if (crlInVri.equals(crlInDSS)) {
                                crlArray.set(j, crlInDSSArray.getObject(i));
                            }
                        } catch (Exception x) {
                            throw new SignatureRuntimeException(x);
                        }
                    }
                }
            } catch (Exception x) {
                throw new SignatureRuntimeException(x);
            }
        }
    }


    private void updateObjectNumbersOfCertsInVRI(COSArray certArray) {

        ECertificate certificateInVri = null;
        for (int j = 0; j < certArray.size(); j++) {
            try {
                byte[] certBytes = getStreamBytes((COSStream) certArray.getObject(j));
                certificateInVri = new ECertificate(certBytes);

                //check whether certificate is found in DSS
                COSBase certificateInDSSElement = dss.getDictionaryObject("Certs");
                COSArray certInDSSArray;

                if (certificateInDSSElement != null && certificateInDSSElement instanceof COSArray) {
                    certInDSSArray = (COSArray) certificateInDSSElement;

                    for (int i = 0; i < certInDSSArray.size(); i++) {
                        try {

                            byte[] certificateInDSSBytes = getStreamBytes((COSStream) certInDSSArray.getObject(i));
                            ECertificate certificateInDSS = new ECertificate(certificateInDSSBytes);

                            if (certificateInVri.equals(certificateInDSS)) {
                                certArray.set(j, certInDSSArray.getObject(i));
                            }
                        } catch (Exception x) {
                            throw new SignatureRuntimeException(x);
                        }
                    }
                }
            } catch (Exception x) {
                throw new SignatureRuntimeException(x);
            }
        }
    }

    public COSDictionary getDss() {
        return dss;
    }

    public CertValidationValues filterByRange(PAdESContainer container, ByteRangeCollection range) throws CryptoException {

        CertValidationValues validationValues = new CertValidationValues();

        List<ECertificate> certs = getCertificates();
        for(ECertificate cert : certs){
            List<COSObject> referenceList = getFromReferenceList(cert);
            if(isThereAObjectInRangeCollection(container, referenceList, range))
                validationValues.addCertificate(cert);
        }

        List<ECRL> crls = getCRLs();
        for(ECRL crl : crls){
            List<COSObject> referenceList = getFromReferenceList(crl);
            if(isThereAObjectInRangeCollection(container, referenceList, range))
                validationValues.addCRL(crl);
        }

        List<EOCSPResponse> ocspResponses = getOCSPResponses();
        for(EOCSPResponse ocsp : ocspResponses){
            List<COSObject> referenceList = getFromReferenceList(ocsp);
            if(isThereAObjectInRangeCollection(container, referenceList, range))
                validationValues.addOCSPResponse(ocsp.getBasicOCSPResponse());
        }

        return validationValues;
    }

    protected List<COSObject> getFromReferenceList(BaseASNWrapper asnObj) throws CryptoException {
        String digestOfObject = StringUtil.toHexString(DigestUtil.digest(DigestAlg.SHA1, asnObj.getEncoded()));
        return referenceMap.get(digestOfObject);
    }

    private boolean isThereAObjectInRangeCollection(PAdESContainer container, List<COSObject> referenceList, ByteRangeCollection range) {
        
        for(COSObject aObj : referenceList){
            Long xrefAddress = container.document.getDocument().getXrefTable().get(new COSObjectKey(aObj.getObjectNumber(), aObj.getGenerationNumber()));
            COSStream aCOSObj = (COSStream) aObj.getObject();

            ByteRange objRange = new ByteRange(xrefAddress, aCOSObj.getLength());
            if(range.isCoverGivenRange(objRange))
                return true;
        }
        return false;
    }

    private void addToReferenceMap(BaseASNWrapper asn1Obj, COSObject pdfObject) throws CryptoException {
        String digestOfObject = StringUtil.toHexString(DigestUtil.digest(DigestAlg.SHA1, asn1Obj.getEncoded()));
        List<COSObject> pdfObjects = referenceMap.get(digestOfObject);

        if(pdfObjects == null){
            pdfObjects = new ArrayList<COSObject>();
            pdfObjects.add(pdfObject);
            referenceMap.put(digestOfObject, pdfObjects);
        } else {
            pdfObjects.add(pdfObject);
        }
    }
}

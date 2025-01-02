package tr.gov.tubitak.uekae.esya.api.pades.pdfbox;

import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.interactive.digitalsignature.PDSignature;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.cmssignature.CMSSignatureException;
import tr.gov.tubitak.uekae.esya.api.cmssignature.CMSSignatureUtil;
import tr.gov.tubitak.uekae.esya.api.cmssignature.bundle.CMSSignatureI18n;
import tr.gov.tubitak.uekae.esya.api.cmssignature.bundle.E_KEYS;
import tr.gov.tubitak.uekae.esya.api.cmssignature.provider.CMSSignatureImpl;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.BaseSignedData;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.Signer;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.check.CheckerResult;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.check.CryptoChecker;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.util.StreamUtil;
import tr.gov.tubitak.uekae.esya.api.common.util.StringUtil;
import tr.gov.tubitak.uekae.esya.api.crypto.IDTBSRetrieverSigner;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.util.ECAlgorithmUtil;
import tr.gov.tubitak.uekae.esya.api.signature.*;
import tr.gov.tubitak.uekae.esya.api.signature.impl.AbstractSignatureContainer;
import tr.gov.tubitak.uekae.esya.api.signature.impl.SignableBytes;

import java.io.*;
import java.util.Arrays;
import java.util.List;

public class PAdESContainer extends AbstractSignatureContainer {

    PDDocument document;

    File tempFile;

    private static final int SIGNED_HEADER_SIZE=5;
    private static final byte[] SIGNED_HEADER={0x25,0x50,0x044,0x46,0x2D};

    public PAdESContainer(){

    }

    @Override
    public void detachSignature(Signature signature) throws SignatureException {
        throw new NotSupportedException();
    }

    @Override
    public Signature createSignature(ECertificate certificate) {

        PDSignature signature = new PDSignature();

        signature.setFilter(PDSignature.FILTER_ADOBE_PPKLITE);
        signature.setSubFilter(PDSignature.SUBFILTER_ETSI_CADES_DETACHED);

        PAdESSignature padesSignature = new PAdESSignature(this, signature, certificate, rootSignatures.size());
        rootSignatures.add(padesSignature);
        return padesSignature;
    }

    @Override
    public SignatureFormat getSignatureFormat() {
        return SignatureFormat.PAdES;
    }

    @Override
    public boolean isSignatureContainer(InputStream stream) throws SignatureException {
            try {
                byte [] buffer = new byte[SIGNED_HEADER_SIZE];
                int readSize = stream.read(buffer,0,SIGNED_HEADER_SIZE);

                int signedHeaderIndex=0;
                for (byte bufferByte : buffer) {
                    if(signedHeaderIndex==SIGNED_HEADER.length-1){
                        return true;
                    }
                    byte headerByte = SIGNED_HEADER[signedHeaderIndex];
                    if(bufferByte != headerByte)
                    {
                        return false;
                    }
                    signedHeaderIndex++;
                }
            } catch (Exception x){
                throw new SignatureException(x);
            }
            return false;
    }

    @Override
    public List<Signature> getSignatures() {
        return rootSignatures;
    }

    @Override
    public void read(InputStream stream) throws SignatureException {
        try
        {
            byte[] pdfFileBytes = StreamUtil.readAll(stream);
            document = PDDocument.load(pdfFileBytes);
            List<PDSignature> pdSignatures = document.getSignatureDictionaries();

            rootSignatures.clear();
            int index = 0;

            for(PDSignature pdSignature : pdSignatures)
            {
                String subFilter = pdSignature.getCOSObject().getCOSName(COSName.SUB_FILTER).getName();

                boolean isCades = PDSignature.SUBFILTER_ETSI_CADES_DETACHED.getName().equals(subFilter);
                boolean isTsp = "ETSI.RFC3161".equals(subFilter);

                byte [] cadesBytes = pdSignature.getContents(pdfFileBytes);
                byte[] signedContent = pdSignature.getSignedContent(pdfFileBytes);

                Context copyContext = context.clone();
                copyContext.setData(new SignableBytes(signedContent, null, null));
                SignatureContainer cadesContainer = SignatureFactory.readContainer(SignatureFormat.CAdES, new ByteArrayInputStream(cadesBytes), copyContext);

                pdSignature.setName("Signature " + (index + 1));
                PAdESSignature signature = new PAdESSignature(pdSignature.getName(), this, cadesContainer.getSignatures().get(0), index, isTsp);

                rootSignatures.add(signature);

                index++;
            }
        }
        catch (IOException e)
        {
            throw new SignatureException("Can not read pdf file. ", e);
        }
    }

    @Override
    public void write(OutputStream stream) throws SignatureException {
        try
        {
            if(tempFile == null || !tempFile.exists()){
                document.saveIncremental(stream);
                close();
            }
            else{
                StreamUtil.copy(new FileInputStream(tempFile), stream);
            }
        }
        catch (IOException e)
        {
            throw new SignatureException("Can not save pdf file.", e);
        }
    }

    @Override
    public Object getUnderlyingObject() {
        return this;
    }

    public void updatePDF() throws SignatureException
    {
        try
        {
            File tempFile = getNewTempFile();
            FileOutputStream outputStream = new FileOutputStream(tempFile);

            document.saveIncremental(outputStream);
            outputStream.close();
            document.close();

            FileInputStream inputStream = new FileInputStream(tempFile);
            this.read(inputStream);
        }
        catch (IOException e)
        {
            throw new SignatureException("Can not save temp PDF! " + e.getMessage(), e);
        } finally {
            tempFile.delete();
            this.tempFile = null;
        }
    }

    public File getNewTempFile() throws SignatureException {

        try
        {
            if(tempFile != null && tempFile.exists())
                tempFile.delete();

            tempFile = File.createTempFile("ma3api-pades", null);
        }
        catch (IOException ex)
        {
            throw new SignatureException("Can not create temp PDF!", ex);
        }

        return tempFile;
    }

    public DocSecurityStore getDocumentSecurityStore() {

        PDDocumentCatalog catalog = document.getDocumentCatalog();
        COSBase dssElement = catalog.getCOSObject().getDictionaryObject("DSS");

        if(dssElement instanceof COSDictionary){
            COSDictionary dss = (COSDictionary) dssElement;
            return new DocSecurityStore(dss);
        }

        return null;
    }

    public Signature getLatestSignerSignature(){

        for(int i = rootSignatures.size() - 1; i >= 0; i--){
            PAdESSignature padesSignature = (PAdESSignature) rootSignatures.get(i);
            if(!padesSignature.isTimestamp())
                return padesSignature;
        }

        return null;
    }

    public void finishAddingSignerForMobile(byte[] signature, ECertificate eCertificate) throws SignatureException, ESYAException {
        byte[] tempSignature = IDTBSRetrieverSigner.getTempSignatureBytes();
        List<Signature> signatures = getSignatures();

        boolean valid = false;
        boolean unFinishedSignerFound = false;
        for (Signature aSignature : signatures) {
            PAdESSignature padesSignature = (PAdESSignature) aSignature;

            BaseSignedData baseSignedData = (BaseSignedData) ((CMSSignatureImpl) padesSignature.getUnderlyingObject()).getUnderlyingObject();

            CMSSignatureUtil.addCerIfNotExist(baseSignedData.getSignedData(),eCertificate);

            Signer aSigner = baseSignedData.getSignerList().get(0);

            byte[] signatureOfSigner = aSigner.getSignerInfo().getSignature();
            if (Arrays.equals(signatureOfSigner, tempSignature)) {
                unFinishedSignerFound = true;
                aSigner.getSignerInfo().setSignature(signature);

                CryptoChecker cryptoChecker = new CryptoChecker();
                valid = cryptoChecker.check(aSigner, new CheckerResult());
                if (valid) {
                    SignatureAlg signatureAlg = aSigner.getSignatureAlg().getObject1();
                    ECAlgorithmUtil.checkDigestAlgForECCAlgorithm(eCertificate, signatureAlg);

                    if (context instanceof PAdESContext && ((PAdESContext) context).isSignWithTimestamp())
                        ((CMSSignatureImpl) padesSignature.getUnderlyingObject()).upgrade(SignatureType.ES_T);

                    byte[] cadesBytes = baseSignedData.getEncoded();
                    PDSignature pdSignature = padesSignature.getPdSignature();
                    updateSignature(pdSignature, cadesBytes);
                    break;
                } else
                    aSigner.getSignerInfo().setSignature(tempSignature);
            }
        }

        if (unFinishedSignerFound == false)
            throw new CMSSignatureException(CMSSignatureI18n.getMsg(E_KEYS.NO_UNFINISHED_SIGNATURE));

        if (valid == false)
            throw new CMSSignatureException(CMSSignatureI18n.getMsg(E_KEYS.NOT_VALID_SIGNATURE_VALUE_FOR_UNFINISHED_SIGNATURE));
    }

    public void  finishSign(byte [] signature) throws SignatureException {
        byte[] tempSignature = IDTBSRetrieverSigner.getTempSignatureBytes();
        List<Signature> signatures = getSignatures();

        boolean valid = false;
        boolean unFinishedSignerFound = false;
        for(Signature aSignature : signatures){
            PAdESSignature padesSignature = (PAdESSignature) aSignature;

            BaseSignedData baseSignedData = (BaseSignedData) ((CMSSignatureImpl) padesSignature.getUnderlyingObject()).getUnderlyingObject();

            Signer aSigner = baseSignedData.getSignerList().get(0);
            byte[] signatureOfSigner = aSigner.getSignerInfo().getSignature();
            if (Arrays.equals(signatureOfSigner, tempSignature)) {
                unFinishedSignerFound = true;
                aSigner.getSignerInfo().setSignature(signature);

                CryptoChecker cryptoChecker = new CryptoChecker();
                valid = cryptoChecker.check(aSigner, new CheckerResult());
                if(valid) {

                    if (context instanceof PAdESContext && ((PAdESContext) context).isSignWithTimestamp())
                        ((CMSSignatureImpl)padesSignature.getUnderlyingObject()).upgrade(SignatureType.ES_T);

                    byte [] cadesBytes = baseSignedData.getEncoded();
                    PDSignature pdSignature = padesSignature.getPdSignature();
                    updateSignature(pdSignature, cadesBytes);
                    break;
                }
                else
                    aSigner.getSignerInfo().setSignature(tempSignature);
            }
        }

        if(unFinishedSignerFound == false)
            throw new CMSSignatureException(CMSSignatureI18n.getMsg(E_KEYS.NO_UNFINISHED_SIGNATURE));

        if(valid == false)
            throw new CMSSignatureException(CMSSignatureI18n.getMsg(E_KEYS.NOT_VALID_SIGNATURE_VALUE_FOR_UNFINISHED_SIGNATURE));
    }

    private void updateSignature(PDSignature pdSignature, byte[] cadesBytes) throws SignatureException {
        try {
            File tempFile = getNewTempFile();
            FileOutputStream outputStream = new FileOutputStream(tempFile);

            document.saveIncremental(outputStream);
            outputStream.close();
            document.close();

            //begin index'i bir önceki dosyaya ait index. Yalnız saveIncremental yapıldığı için imza index'i değişmiyor.
            int[] byteRange = pdSignature.getByteRange();
            int begin = byteRange[0] + byteRange[1] + 1;
            int spaceLen = byteRange[2] - begin - 1;

            byte [] cadesHexBytes = StringUtil.toHexString(cadesBytes).getBytes("ASCII");

            if(cadesHexBytes.length > spaceLen )
                throw new SignatureException("Not enough space for signature! Space Len: " + spaceLen + " Demanded Len: " + cadesHexBytes.length );

            RandomAccessFile randomAccessFile = new RandomAccessFile(tempFile, "rw");
            randomAccessFile.seek(begin);
            randomAccessFile.write(cadesHexBytes);
            randomAccessFile.close();

            FileInputStream inputStream = new FileInputStream(tempFile);
            this.read(inputStream);
        }
        catch (IOException e)
        {
            throw new SignatureException("Can not save temp PDF! " + e.getMessage(), e);
        } finally {
            tempFile.delete();
            this.tempFile = null;
        }
    }

    @Override
    public void close() throws IOException {
        if (document != null)
            document.close();

        if (tempFile != null) {
            tempFile.delete();
            this.tempFile = null;
        }
    }

    @Override
    protected void finalize() throws Throwable {
        this.close();

        super.finalize();
    }
}

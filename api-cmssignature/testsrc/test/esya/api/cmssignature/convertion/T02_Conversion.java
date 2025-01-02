package test.esya.api.cmssignature.convertion;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import test.esya.api.cmssignature.CMSSignatureTest;
import test.esya.api.cmssignature.validation.ValidationUtil;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.EParameters;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.BaseSignedData;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.ESignatureType;
import tr.gov.tubitak.uekae.esya.asn.util.AsnIO;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RunWith(Parameterized.class)
public class T02_Conversion extends CMSSignatureTest {
    public static final String DIRECTORY = "T:\\api-cmssignature\\test-output\\java\\ma3\\conversion\\";

    @Parameterized.Parameters(name = "{0}-To-{2}")
    public static Collection<Object[]> data(){
        return Arrays.asList(new Object[][]{
                {"BES.p7s",ESignatureType.TYPE_EST, "EST.p7s"},
                {"EPES.p7s",ESignatureType.TYPE_EST, "EST-2.p7s"},
                {"EST.p7s",ESignatureType.TYPE_EST, "EST-3.p7s"},

                {"BES.p7s",ESignatureType.TYPE_ESC, "ESC.p7s"},
                {"EPES.p7s",ESignatureType.TYPE_ESC, "ESC-2.p7s"},
                {"EST.p7s",ESignatureType.TYPE_ESC, "ESC-3.p7s"},

                {"BES.p7s",ESignatureType.TYPE_ESX_Type1, "ESX1.p7s"},
                {"EPES.p7s",ESignatureType.TYPE_ESX_Type1, "ESX1-2.p7s"},
                {"EST.p7s",ESignatureType.TYPE_ESX_Type1, "ESX1-3.p7s"},
                {"ESC.p7s",ESignatureType.TYPE_ESX_Type1, "ESX1-4.p7s"},

                {"BES.p7s",ESignatureType.TYPE_ESX_Type2, "ESX2.p7s"},
                {"EPES.p7s",ESignatureType.TYPE_ESX_Type2, "ESX2-2.p7s"},
                {"EST.p7s",ESignatureType.TYPE_ESX_Type2, "ESX2-3.p7s"},
                {"ESC.p7s",ESignatureType.TYPE_ESX_Type2, "ESX2-4.p7s"},

                {"BES.p7s",ESignatureType.TYPE_ESXLong, "ESXLong.p7s"},
                {"EPES.p7s",ESignatureType.TYPE_ESXLong, "ESXLong-2.p7s"},
                {"EST.p7s",ESignatureType.TYPE_ESXLong, "ESXLong-3.p7s"},
                //{"ESC.p7s",ESignatureType.TYPE_ESXLong, "ESXLong-4.p7s"}, //ESC oluşturulurken kullanılan referanların kaydedilmesi ve conversion sırasında bulunması gerekiyor.


                {"BES.p7s",ESignatureType.TYPE_ESXLong_Type1, "ESXLongType1.p7s"},
                {"EPES.p7s",ESignatureType.TYPE_ESXLong_Type1, "ESXLongType1-2.p7s"},
                {"EST.p7s",ESignatureType.TYPE_ESXLong_Type1, "ESXLongType1-3.p7s"},
                //{"ESC.p7s",ESignatureType.TYPE_ESXLong_Type1, "ESXLongType1-4.p7s"}, //ESC oluşturulurken kullanılan referanların kaydedilmesi ve conversion sırasında bulunması gerekiyor.
                //{"ESX1.p7s",ESignatureType.TYPE_ESXLong_Type1, "ESXLongType1-5.p7s"}, //ESC oluşturulurken kullanılan referanların kaydedilmesi ve conversion sırasında bulunması gerekiyor.


                {"BES.p7s",ESignatureType.TYPE_ESXLong_Type2, "ESXLongType2.p7s"},
                {"EPES.p7s",ESignatureType.TYPE_ESXLong_Type2, "ESXLongType2-2.p7s"},
                {"EST.p7s",ESignatureType.TYPE_ESXLong_Type2, "ESXLongType2-3.p7s"},
                //{"ESC.p7s",ESignatureType.TYPE_ESXLong_Type1, "ESXLongType2-4.p7s"}, //ESC oluşturulurken kullanılan referanların kaydedilmesi ve conversion sırasında bulunması gerekiyor.
                //{"ESX2.p7s",ESignatureType.TYPE_ESXLong_Type1, "ESXLongType2-5.p7s"}, //ESC oluşturulurken kullanılan referanların kaydedilmesi ve conversion sırasında bulunması gerekiyor.


                {"BES.p7s",ESignatureType.TYPE_ESA, "ESA.p7s"},
                {"EPES.p7s",ESignatureType.TYPE_ESA, "ESA-2.p7s"},
                {"EST.p7s",ESignatureType.TYPE_ESA, "ESA-3.p7s"},
                //{"ESC.p7s",ESignatureType.TYPE_ESA, "ESA-4.p7s"}, // ↑ Referans Problemi
                //{"ESX1.p7s",ESignatureType.TYPE_ESA, "ESA-5.p7s"}, // ↑ Referans Problemi
                //{"ESX2.p7s",ESignatureType.TYPE_ESA, "ESA-6.p7s"}, // ↑ Referans Problemi
                {"ESXLong.p7s",ESignatureType.TYPE_ESA, "ESA-7.p7s"},
                {"ESA.p7s",ESignatureType.TYPE_ESA, "ESA-8.p7s"},
        });
    }

    String inputFile;
    ESignatureType outputType;
    String outputFile;

    public T02_Conversion(String inputFile, ESignatureType outputType, String outputFile){
        this.inputFile = inputFile;
        this.outputType = outputType;
        this.outputFile = outputFile;
    }

    @Test
    public void testConversion() throws Exception{
        conversion(inputFile, outputType, outputFile);
    }

    public void conversion(String inputFile, ESignatureType outputType, String outputFile)throws Exception{
        byte [] signatureFile = AsnIO.dosyadanOKU(DIRECTORY + inputFile);

        BaseSignedData bs = new BaseSignedData(signatureFile);

        Map<String,Object> parameters = new HashMap<String, Object>();

        //Time stamp will be needed for some conversion.
        parameters.put(EParameters.P_TSS_INFO, getTSSettings());

        parameters.put(EParameters.P_CERT_VALIDATION_POLICY, getPolicy());

        bs.getSignerList().get(0).convert(outputType, parameters);

        AsnIO.dosyayaz(bs.getEncoded(), DIRECTORY + outputFile);

        ValidationUtil.checkSignatureIsValid(bs.getEncoded(), null);
    }





}

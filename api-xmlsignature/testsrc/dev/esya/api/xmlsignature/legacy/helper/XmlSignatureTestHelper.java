package dev.esya.api.xmlsignature.legacy.helper;

import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.util.LicenseUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;

/**
 * Created with IntelliJ IDEA.
 * User: yavuz.kahveci
 * Date: 30.01.2013
 * Time: 10:13
 * To change this template use File | Settings | File Templates.
 */
public class XmlSignatureTestHelper {

    private String baseDir = null;
    private String rootDir = null;
    private static XmlSignatureTestHelper instance;

    public static XmlSignatureTestHelper getInstance()
    {
        if(instance == null)
        {
            instance = new XmlSignatureTestHelper();
        }
        return instance;
    }

    public XmlSignatureTestHelper()
    {
        URL root = XmlSignatureTestHelper.class.getResource("../../../../../../../../../../");
        String classPath = root.getPath();
        File binDir = new File(classPath);
        rootDir = binDir.getParentFile().getParent();
        baseDir = rootDir+"/docs/samples/signatures/";
    }

    public String getRootDir()
    {
        return rootDir;
    }

    public String getBaseDir()
    {
        return baseDir;
    }

    public void loadFreeLicense() throws IOException, ESYAException
    {
        String licenseFilePath = getRootDir() + "\\docs\\config\\lisans\\lisansFree.xml";
        FileInputStream fis = new FileInputStream(licenseFilePath);
        LicenseUtil.setLicenseXml(fis);
        fis.close();
    }

    public void loadTestLicense() throws IOException, ESYAException
    {
        String licenseFilePath = getRootDir() + "\\docs\\config\\lisans\\lisansTest.xml";
        FileInputStream fis = new FileInputStream(licenseFilePath);
        LicenseUtil.setLicenseXml(fis);
        fis.close();
    }

    public String getSignatureOutputDir()
    {
        String outputDirPath = baseDir;
        File outputDir = new File(outputDirPath);
        if(!outputDir.exists())
        {
            outputDir.mkdirs();
        }
        return outputDirPath;
    }



}

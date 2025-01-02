package dev.esya.api.xmlsignature.legacy.signerHelpers;

import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.util.LicenseUtil;
import dev.esya.api.xmlsignature.legacy.helper.XmlSignatureTestHelper;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Created with IntelliJ IDEA.
 * User: yavuz.kahveci
 * Date: 30.01.2013
 * Time: 14:07
 * To change this template use File | Settings | File Templates.
 */
public class UgTestParameterGetter extends BaseParameterGetter {
    @Override
    public void loadLicense()
    {
        try
        {
            String rootDir = XmlSignatureTestHelper.getInstance().getRootDir();
            String licenseFilePath = rootDir + "\\docs\\config\\lisans\\lisansFull.xml";
            LicenseUtil.setLicenseXml(new FileInputStream(licenseFilePath));
        }
        catch (ESYAException e)
        {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}

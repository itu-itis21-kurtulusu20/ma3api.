package dev.esya.api.pades.pdfbox.settings;

import tr.gov.tubitak.uekae.esya.api.pades.pdfbox.PAdESContext;
import tr.gov.tubitak.uekae.esya.api.signature.config.Config;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * @author ayetgin
 */
public class UGPAdESTestSettings implements PAdESTestSettings {

    protected static String BASE_DIR = "T:\\api-parent\\resources\\unit-test-resources\\plain-files\\";
    protected static String CONFIG_FILE = "T:\\api-parent\\resources\\ug\\config\\esya-signature-config.xml";


    public InputStream getPdfFile() throws Exception {

            return new FileInputStream(BASE_DIR + "sample.pdf");
    }

    public PAdESContext createContext()
    {
        PAdESContext c = new PAdESContext(new File(BASE_DIR).toURI());
        c.setConfig(new Config(CONFIG_FILE));
        return c;
    }
}



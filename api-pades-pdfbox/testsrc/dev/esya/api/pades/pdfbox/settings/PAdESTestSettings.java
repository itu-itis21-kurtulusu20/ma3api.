package dev.esya.api.pades.pdfbox.settings;

import tr.gov.tubitak.uekae.esya.api.pades.pdfbox.PAdESContext;

import java.io.InputStream;

/**
 * @author ayetgin
 */
public interface PAdESTestSettings
{

    PAdESContext createContext() throws Exception;

    InputStream getPdfFile() throws Exception;

}

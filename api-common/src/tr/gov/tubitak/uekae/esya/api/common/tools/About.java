/**
 *
 */
package tr.gov.tubitak.uekae.esya.api.common.tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.jar.Attributes;
import java.util.jar.JarFile;

/**
 * @author mss
 */
public class About {


    private static final Logger LOGCU = LoggerFactory.getLogger(About.class);


    private static final Map<String, ModuleInfo> KNOWNMODULES = new TreeMap<String, ModuleInfo>(new Comparator<String>() {
        public int compare(String o1, String o2) {
            return o1.compareToIgnoreCase(o2);
        }
    });

    public static void listAllInDefualtClassPath() {
        String classpath = System.getProperty("java.class.path");
        if(classpath == null || classpath.trim().equals("")){
            LOGCU.warn("java.class.path is empty");
            return;
        }
        String[] pathElements = classpath.split(";");
        listAllInClassPath(pathElements);
    }

    public static void listAllInClassPath(String[] pathElements) {
        listAllInClassPath(pathElements, false);
    }

    public static void listAllInClassPath(String[] pathElements, boolean filterSun) {
        for (String pathElement : pathElements) {
            if (filterSun && isSun(pathElement))
                return;
            addModuleInfoWithPathElement(pathElement);
        }
    }

    private static void addModuleInfoWithPathElement(String pathElement) {
        try {
            ModuleInfo moduleInfo = toModuleInfoWithPathElement(pathElement);
            if (moduleInfo != null)
                KNOWNMODULES.put(pathElement, moduleInfo);
        } catch (Exception e) {
            LOGCU.warn("Cannot Inspect Jar File: " + pathElement + " Error:" + e.getMessage(), e);
        }
    }


    public static ModuleInfo toModuleInfoWithPathElement(String pathElement) {
        try {
            File file = new File(pathElement);
            JarFile jarFile = new JarFile(file);
            Attributes manifest = jarFile.getManifest().getMainAttributes();
            String moduleName = manifest.getValue("Implementation-Title");
            if (moduleName == null || moduleName.trim().equals(""))
                moduleName = file.getName();
            return new ModuleInfo(moduleName, manifest);
        } catch (Exception e) {
            LOGCU.warn("Cannot Inspect Jar File: " + pathElement + " Error:" + e.getMessage(), e);
            return null;
        }
    }

    private static boolean isSun(String pathElement) {
        String javaHome = System.getProperty("java.home");
        if (javaHome == null || javaHome.equals(""))
            return false;
        return pathElement.contains(javaHome);
    }

    public static String getVersionInfo(Class aClass) {
        return getVersionInfo(aClass,"Test");
    }

    private static String getVersionInfo(Class aClass, String defaultVersion) {
        try {
            String convert = aClass.getPackage().getImplementationVersion();
            if (convert == null)
                return defaultVersion;
            return convert;
        } catch (Exception e) {
            LOGCU.warn("Cannot Inspect Module Class:" + aClass + " Error:" + e.getMessage(), e);
            return defaultVersion;
        }
    }


    private static String getModulInfo(String aModulName) {
        return KNOWNMODULES.get(aModulName).toString();
    }

    private static String getModulInfoAsHtml(String aModulName) {
        return KNOWNMODULES.get(aModulName).toStringAsHtml();
    }


    public static String getModuleInformations() {
        Iterator<String> enumeration = KNOWNMODULES.keySet().iterator();
        String modul, deger;
        String r = "";
        while (enumeration.hasNext()) {
            modul = enumeration.next();

            deger = getModulInfo(modul);
            r += deger + "\n";
        }
        return r;
    }

    public static String getModuleInformationsAsHtml() {
        String header = "\n<table border=0 width=700 >";
        String body = getModuleInformationBodyAsHtml();
        return header + body + "</table>\n";
    }

    public static String getModuleInformationBodyAsHtml() {
        String modul;
        String deger;
        String body = "";
        Iterator<String> enumeration = KNOWNMODULES.keySet().iterator();
        while (enumeration.hasNext()) {
            modul = enumeration.next();
            deger = getModulInfoAsHtml(modul);
            body += "<tr><td>\n" + deger + "</td></tr>" + "\n";
        }
        return body;
    }

    private static JEditorPane getAllModulInfosAsJEditorPane(String aBilgi) {
        JEditorPane mesaj = new JEditorPane();
        mesaj.setOpaque(false);

        mesaj.setContentType("text/html");

        mesaj.setPreferredSize(new Dimension(500, 300));
//          mesaj.setLineWrap(true);
//          mesaj.setRows( 50 );
//          mesaj.setColumns(45);
//          mesaj.setWrapStyleWord(true);
        mesaj.setText(aBilgi);
        mesaj.setEditable(false);

        return mesaj;
    }

    public static JEditorPane getAllModulInfosAsJEditorPane() {
        String bilgi = getModuleInformationsAsHtml();

        return getAllModulInfosAsJEditorPane(bilgi);
    }

    public static JEditorPane getAllModulInfosAsJEditorPaneWithAuthorityInfo(String caName) {
        caName = caName.replaceAll(",", "<br />");
        String bilgi = "<center><big><b>Certification Authority </b></big></center>" + "<br>" + caName + "<br>";
        bilgi += getModuleInformationsAsHtml();

        return getAllModulInfosAsJEditorPane(bilgi);
    }

    public static JEditorPane getAllModulInfosAsJEditorPaneWithAuthorityInfo(String caName, String productName, Class versionClass) {
        String version = getVersionInfo(versionClass, "Test");
        caName = caName.replaceAll(",", "<br />");
        String bilgi = "<center><big><b>"+productName+" "+version+"</b></big></center>" ;
        bilgi += "<big><i><b>Certification Authority </b></i></big>" + "<br>" + caName + "<br>";
        bilgi += getModuleInformationsAsHtml();

        return getAllModulInfosAsJEditorPane(bilgi);
    }

    public static void tumModulBilgileriniDialogdaGoster(JComponent aParent) {

        JScrollPane pane = new JScrollPane(getAllModulInfosAsJEditorPane());

        JOptionPane.showMessageDialog(aParent, pane, "About", JOptionPane.INFORMATION_MESSAGE, null);

    }


    public static class ModuleInfo {
        final String specTitle;
        final String specVendor;
        final String specVersion;

        final String impTitle;
        final String impVendor;
        final String impVersion;

        final String modulName;
/*
        ModuleInfo(String aModulName) {
            mModulName = aModulName;

            mSpecTitle = null;
            mSpecVendor = null;
            mSpecVersion = null;

            mImpTitle = null;
            mImpVendor = null;
            mImpVersion = null;
        }


        ModuleInfo(String aModulName, Class aClass) {
            mModulName = aModulName;

            mSpecTitle = aClass.getPackage().getSpecificationTitle();
            mSpecVendor = (aClass.getPackage().getSpecificationVendor());
            mSpecVersion = aClass.getPackage().getSpecificationVersion();

            mImpTitle = aClass.getPackage().getImplementationTitle();
            mImpVendor = (aClass.getPackage().getImplementationVendor());
            mImpVersion = (aClass.getPackage().getImplementationVersion());
        }*/

        ModuleInfo(String aModulName, Attributes attrib) {
            modulName = aModulName;

            specTitle = (attrib.getValue("Specification-Title"));
            specVendor = (attrib.getValue("Specification-Vendor"));
            specVersion = (attrib.getValue("Specification-Version"));

            impTitle = (attrib.getValue("Implementation-Title"));
            impVendor = (attrib.getValue("Implementation-Vendor"));
            impVersion = (attrib.getValue("Implementation-Version"));
        }


        public String getModulName() {
            return modulName;
        }

        public String toString() {
            String r = "Modul " + modulName;

            String bas = "\n      ";
            if (specVendor != null)
                r += bas + "Spec Producer : " + specVendor;
            if (specTitle != null)
                r += bas + "Spec : " + specTitle;
            if (specVersion != null)
                r += bas + "Spec Version : " + specVersion;

            if (impVendor != null)
                r += bas + "Application Producer : " + impVendor;
            if (impTitle != null)
                r += bas + "Application :" + impTitle;
            if (impVersion != null)
                r += bas + "Application Version : " + impVersion;

            return r;
        }

        public String toStringAsHtml() {
            String r;

            String bas = "\n<tr> <td width=30></td> <td width=150  ";
            String orta = "</b></td><td> ";
            String son = "</td></tr>";

            r = "<table border=0 width=\"100%\">";
            r += "<tr><td colspan=3   <i><b>" + modulName + "</b></i>  </td></tr>";

            if (specVendor != null)
                r += bas + "<b>" + "Spec Producer : " + orta + specVendor + son;
            if (specTitle != null)
                r += bas + "<b>" + "Spec : " + orta + specTitle + son;
            if (specVersion != null)
                r += bas + "<b>" + "Spec Version : " + orta + specVersion + son;

            if (impVendor != null)
                r += bas + "<b>" + "Application Producer : " + orta + impVendor + son;
            if (impTitle != null)
                r += bas + "<b>" + "Application : " + orta + impTitle + son;
            if (impVersion != null)
                r += bas + "<b>" + "Application Version : " + orta + impVersion + son;

            return r + "\n</table>";
        }

        public String getSpecTitle() {
            return specTitle;
        }

        public String getSpecVendor() {
            return specVendor;
        }

        public String getSpecVersion() {
            return specVersion;
        }

        public String getImpTitle() {
            return impTitle;
        }

        public String getImpVendor() {
            return impVendor;
        }

        public String getImpVersion() {
            return impVersion;
        }
    }
}



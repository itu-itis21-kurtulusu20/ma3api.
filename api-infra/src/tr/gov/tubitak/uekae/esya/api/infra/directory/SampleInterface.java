package tr.gov.tubitak.uekae.esya.api.infra.directory;


import tr.gov.tubitak.uekae.esya.api.common.util.LicenseUtil;

import java.io.FileInputStream;
import java.net.URI;
import java.util.List;

/**
 * <p>Title: MA3 Dizin islemleri</p>
 * <p>Description: Sadece ornek olmasi icin yazilmistir. fonksiyonel bir ozelligi yoktur.
 * Testlerde, denemelerde kullanilabilir. </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: TUBITAK/UEKAE</p>
 *
 * @author M. Serdar SORAN
 * @version 1.0
 */

public class SampleInterface
        implements DirectoryInfo {
    String mTip = DirectoryBase.ACTIVE_DIRECTORY;
    //String tip = DizinBase.NETSCAPE;

    public SampleInterface() {
        //Default tip olarak aktiv dir aliyor.
    }


    public SampleInterface(String aDir) {
        if (aDir.equals(DirectoryBase.ACTIVE_DIRECTORY)) {
            mTip = DirectoryBase.ACTIVE_DIRECTORY;
        } else {
            mTip = DirectoryBase.NETSCAPE;
        }
    }


    public URI getURI() {
        return null;
    }

    public String getIP() {
        if (mTip.equals(DirectoryBase.ACTIVE_DIRECTORY)) {
            return "192.168.0.106";
        } else {
            return "192.168.0.161";
        }
    }


    public int getPort() {
        return 389;
    }


    public String getUserName() {
        if (mTip.equals(DirectoryBase.ACTIVE_DIRECTORY)) {
            return "asyaadmin@asya.net";
        } else {
            return "cn=Directory Manager";
        }
    }


    public String getUserPassword() {
        if (mTip.equals(DirectoryBase.ACTIVE_DIRECTORY)) {
            return "123456";
        } else {
            return "12345678";
        }
    }


    public String getType()   //NOPMD
    {
        return mTip;
    }


    public static void main(String[] args) throws Exception {

        LicenseUtil.setLicenseXml(new FileInputStream("T:\\api-parent\\lisans\\lisans.xml"));


        StaticDirectoryInfo sdi = new StaticDirectoryInfo("10.203.52.5",
                389,
                DirectoryBase.ACTIVE_DIRECTORY,
                "esyasm@kermen.net",
                "123456"
        );

        String searchPoint = "OU=001,DC=kermen,DC=net";

        SearchDirectory search = new SearchDirectory(sdi, searchPoint);

        boolean tkaPresent = search.isTKAPresent("CN=Cenk Gümüş (U\\\\\\H TEKN.ASB.ÜÇVŞ.)(KKK),OU=001,DC=kermen,DC=net");

        String[][] listeler = search.getEntryListesiVeObjectcls(searchPoint);

        for(String[] liste : listeler) {

            for(String s : liste) {
                System.out.println(s);
            }

        }

        List<String> tkAsbyEmail = search.getTKAsbyEmail("t1@kermen.net");
        for (String s : tkAsbyEmail) {
            System.out.println(s);
        }

        System.out.println("#####################");

        List<String> tkAsbyEmail2 = search.getTKAsbyEmail("t/21@kermen.net");
        for (String s : tkAsbyEmail2) {
            System.out.println(s);
        }

        System.out.println("#####################");

        System.out.println("For searchPoint: " + searchPoint);
        String name = "CN=Cem Gümüş (U/H TEKN.ASB.ÜÇVŞ.)(KKK),OU=001,DC=kermen,DC=net";
        List<String> groups = search.getGroupsByCommonName(name);
        System.out.println("Groups for " + name);
        for (String group : groups) {
            System.out.println(group);
        }

        System.out.println("#####################");

        String group = "CN=TMK31,OU=Diger_Gruplar,DC=kermen,DC=net"; // groups.get(0)
        search = new SearchDirectory(sdi, group);
        System.out.println("For searchPoint: " + group);
        List<String> users = search.getUsersByGroup(group);
        System.out.println("Users for " + group);
        for (String user : users) {
            System.out.println(user);
        }

        System.out.println("#####################");

        System.out.println("For searchPoint: " + group);
        String groupName = search.getNameByGroup(group);
        System.out.println("Name for " + group);
        System.out.println(groupName);

        System.out.println("#####################");

        System.out.println("For searchPoint: " + group);
        String description = search.getDescriptionByGroup(group);
        System.out.println("Description for " + group);
        System.out.println(description);

        System.out.println("#####################");

        System.out.println("For searchPoint: " + group);
        String email = search.getEmailByGroup(group);
        System.out.println("Email for " + group);
        System.out.println(email);

        System.out.println("#####################");
    }
}
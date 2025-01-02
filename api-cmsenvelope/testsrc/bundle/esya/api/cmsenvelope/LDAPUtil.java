package bundle.esya.api.cmsenvelope;

import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EKeyUsage;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.infra.directory.DirectoryBase;
import tr.gov.tubitak.uekae.esya.api.infra.directory.DirectoryInfo;
import tr.gov.tubitak.uekae.esya.api.infra.directory.SearchDirectory;
import tr.gov.tubitak.uekae.esya.api.infra.directory.StaticDirectoryInfo;

import java.util.ArrayList;
import java.util.List;

public class LDAPUtil {

    public static ECertificate[] readEncCertificatesFromDirectory(String aEmail) throws ESYAException {

        String ip = ""; //Enter the ip address
        int port = -1; //Enter the port information
        String directoryType = DirectoryBase.ACTIVE_DIRECTORY;
        String user = ""; // Enter the user name
        String pass = ""; //Enter the password

        String searchPoint = "dc=ug, dc=net";

        DirectoryInfo di = new StaticDirectoryInfo(ip, port, directoryType, user, pass);
        SearchDirectory search = new SearchDirectory(di, searchPoint);

        if (search.isConnected() == false) {
            System.out.println("Dizine baglanamadi!");
            return null;
        }

        String tka = search.getTKAbyEmail(aEmail);
        Object[][] objs = search.getAttributes(tka, DirectoryBase.ATTR_KULLANICISERTIFIKASI);
        if (objs == null) {
            System.out.println("kullanici sertifikasi yok 1.");
            return null;
        }
        Object[] certs = objs[0];
        if (certs == null) {
            System.out.println("kullanici sertifikasi yok 2.");
            return null;
        }

        List<ECertificate> encCerts = new ArrayList<ECertificate>();
        for (int i = 0; i < certs.length; i++) {
            byte[] certBytes = (byte[]) certs[i];

            ECertificate cert = new ECertificate(certBytes);
            EKeyUsage keyUsage = cert.getExtensions().getKeyUsage();

            if (keyUsage.isDataEncipherment() || keyUsage.isKeyEncipherment()) {
                encCerts.add(cert);
            }
        }
        return encCerts.toArray(new ECertificate[0]);
    }
}

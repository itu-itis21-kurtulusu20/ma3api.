using System;
using System.Collections.Generic;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.infra.directory;

namespace tr.gov.tubitak.uekae.esya.api.envelope.example
{
    public class LDAPUtil
    {
        public static ECertificate[] readEncCertificatesFromDirectory(String aEmail)
        {
            String ip = "";//Enter the ip address
            int port = -1;//Enter the port information
            String directoryType = DirectoryBase.ACTIVE_DIRECTORY;
            String user = "";//Enter the user name
            String pass = "";//Enter the password

            String searchPoint = "dc=ug, dc=net";
            DirectoryInfo di = new StaticDirectoryInfo(ip, port, directoryType, user, pass);

            SearchDirectory search = new SearchDirectory(di, searchPoint);

            String tka = search.getTKAbyEmail(aEmail);
            Object[][] objs = search.getAttributes(tka, DirectoryBase.ATTR_KULLANICISERTIFIKASI);

            if (objs == null)
            {
                Console.WriteLine("kullanici sertifikasi yok 1.");
                return null;
            }
            Object[] certs = objs[0];
            if (certs == null)
            {
                Console.WriteLine("kullanici sertifikasi yok 2.");
                return null;
            }

            List<ECertificate> encCerts = new List<ECertificate>();
            for (int i = 0; i < certs.Length; i++)
            {
                byte[] certBytes = (byte[])certs[i];

                ECertificate cert = new ECertificate(certBytes);
                EKeyUsage keyUsage = cert.getExtensions().getKeyUsage();

                if (keyUsage.isDataEncipherment() || keyUsage.isKeyEncipherment())
                {
                    Console.WriteLine("sifreleme sertifikasidir");
                    encCerts.Add(cert);
                }
            }

            return encCerts.ToArray();
        }
    }
}

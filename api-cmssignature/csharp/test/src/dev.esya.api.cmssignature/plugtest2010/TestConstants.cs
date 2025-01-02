using System;
using System.Text;
using tr.gov.tubitak.uekae.esya.api.cmssignature.validation;
using tr.gov.tubitak.uekae.esya.asn.util;

namespace dev.esya.api.cmssignature.plugtests2010
{
    public class TestConstants
    {
        public static void writeXml(SignedDataValidationResult sdvr, String file)
        {
            if (sdvr.getSDStatus() == SignedData_Status.ALL_VALID)
            {
                String valid = "<Verified/>";

                AsnIO.dosyayaz(Encoding.ASCII.GetBytes(valid), file);
            }
            else
            {
                String failed = "<Failed/>";
                AsnIO.dosyayaz(Encoding.ASCII.GetBytes(failed), file);
            }
        }
    }
}
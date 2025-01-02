
using NUnit.Framework;
using test.esya.api.xmlsignature.validation;

namespace dev.esya.api.xmlsignature
{
    class ValidateExternalSignatures
    {
        [Test]
        public void validateInnova()
        {
            XMLValidationUtil.checkSignatureIsValid("D:\\Projeler\\API\\destek\\innova", "D:\\Projeler\\API\\destek\\innova\\0012019154010071.xml");
        }


        [Test]
        public void validateVakifBank()
        {
            XMLValidationUtil.checkSignatureIsValid("D:\\Projeler\\API\\Destek\\Vakifbank Xades - Ori", "D:\\Projeler\\API\\Destek\\Vakifbank Xades - Ori\\476DAEB4-BF89-442D-82DB-A0507DA35A45.xml");
        }
    }
}

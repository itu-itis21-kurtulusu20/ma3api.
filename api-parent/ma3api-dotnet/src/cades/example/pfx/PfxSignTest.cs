using System.Text;
using NUnit.Framework;
using System.Collections.Generic;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.cmssignature;
using tr.gov.tubitak.uekae.esya.api.cmssignature.attribute;
using tr.gov.tubitak.uekae.esya.api.cmssignature.validation;
using tr.gov.tubitak.uekae.esya.api.cades.example.validation;
using tr.gov.tubitak.uekae.esya.api.cmssignature.signature;
using tr.gov.tubitak.uekae.esya.api.signature.util;
using tr.gov.tubitak.uekae.esya.api.common.util;

namespace tr.gov.tubitak.uekae.esya.api.cades.example.pfx
{
    public class PfxSignTest : CadesSampleBase
    {
        [Test]
        public void testBESSign()
        {
            BaseSignedData baseSignedData = new BaseSignedData();

            ISignable content = new SignableByteArray(ASCIIEncoding.ASCII.GetBytes("test"));
            baseSignedData.addContent(content);

            Dictionary<string, object> params_ = new Dictionary<string, object>();
            //if the user does not want certificate validation at generating signature,he can add 
            //P_VALIDATE_CERTIFICATE_BEFORE_SIGNING parameter with its value set to false
            params_[EParameters.P_VALIDATE_CERTIFICATE_BEFORE_SIGNING] = false;

            //necessary for certificate validation.By default,certificate validation is done 
            params_[EParameters.P_CERT_VALIDATION_POLICY] = getPolicy();

            //By default, QC statement is checked,and signature wont be created if it is not a 
            //qualified certificate. 

            PfxSigner signer = getPfxSigner();
            ECertificate cert = signer.getSignersCertificate();

            baseSignedData.addSigner(ESignatureType.TYPE_BES, cert, signer, null, params_);
            byte[] signedDocument = baseSignedData.getEncoded();

            //write the contentinfo to file
            FileUtil.writeBytes(getTestDataFolder() + @"\BESpfx.p7s", signedDocument);

            CadesSignatureValidation validationUtil = new CadesSignatureValidation();
            SignedDataValidationResult sdvr = validationUtil.validate(signedDocument, null);
            
            Assert.AreEqual(SignedData_Status.ALL_VALID, sdvr.getSDStatus());
        }
    }
}

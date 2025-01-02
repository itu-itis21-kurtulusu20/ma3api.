using System;
using System.IO;
using Org.BouncyCastle.Security;
using Org.BouncyCastle.Security.Certificates;
using tr.gov.tubitak.uekae.esya.api.xmlsignature;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.unit.help;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.unit.test;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.common;
using tr.gov.tubitak.uekae.esya.api.common.crypto;
using tr.gov.tubitak.uekae.esya.api.common.util;
using tr.gov.tubitak.uekae.esya.api.common.util.bag;
using tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11;
using tr.gov.tubitak.uekae.esya.api.smartcard.util;
using tr.gov.tubitak.uekae.esya.api.crypto.provider.core.baseTypes;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.resolver;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.unit.pfxsigner;

namespace tr.gov.tubitak.esya.api.xmlsignature.unit.test.signerHelpers
{
    public abstract class BaseParameterGetter
    {
        private static OfflineResolver policyResolver = new OfflineResolver();

        public BaseParameterGetter()
        {
            init();
        }

        public void init()
        {
            setPolicy();
        }

        //Burda ug'de pfx , internet makinasında akıllı kart ile test yapabilecek şekilde konfigurasyon yapılabilir
        public virtual ISignerManager getSignerManager()
        {
            return SignerManagerFactory.getSignerManager();
        }

        private Context context;
        //Burada farklı ortamlar için farklı xml doğrulama politikaları ile çalışılabilir.
        //Benzer şekilde farklı sertifika doğrulama politikaları yüklenebilir.
        public virtual Context getContext()
        {
            if (context == null)
            {
                context = new Context(getBaseDir());
            }

            return context;
        }



        private void setPolicy()
        {
            String policy_file_path_2 = getRootDir() + "\\docs\\config\\policy\\" + UnitTestParameters.POLICY_FILE_NAME2;
            String policy_file_path_3 = getRootDir() + "\\docs\\config\\policy\\" + UnitTestParameters.POLICY_FILE_NAME3;
            String policy_file_path_4 = getRootDir() + "\\docs\\config\\policy\\" + UnitTestParameters.POLICY_FILE_NAME4;
            policyResolver.register("urn:oid:2.16.792.1.61.0.1.5070.3.1.1", policy_file_path_2, "text/xml");
            policyResolver.register("urn:oid:2.16.792.1.61.0.1.5070.3.2.1", policy_file_path_3, "text/xml");
            policyResolver.register("urn:oid:2.16.792.1.61.0.1.5070.3.3.1", policy_file_path_4, "text/xml");
        }

        public BaseSigner _getSigner(int signSlotNo,int certSlotNo)
        {
            return getSignerManager().getSigner(signSlotNo,certSlotNo);
            
        }

        public ECertificate _getCertificateForSlot(int slotNo)
        {
            return getSignerManager().getSigningCertificate(slotNo);
        }
       
        private void _signWithSigner(XMLSignature signature, int signSlotNo,int certSlotNo)
        {
            try
            {
                BaseSigner signer = _getSigner(signSlotNo, certSlotNo);
                signature.sign(signer);

            }
            catch (Exception e)
            {
                throw e;
            }
            finally
            {
                getSignerManager().logout(signSlotNo);
            }
        }

        public void signWithBaseSigner(XMLSignature signature)
        {
            _signWithSigner(signature, UnitTestParameters.SMARTCARD_SLOTNO0, UnitTestParameters.SMARTCARD_SLOTNO0);
        }

        public void signWithBaseSigner2(XMLSignature signature) ////suleyman.uslu
        {
            _signWithSigner(signature, UnitTestParameters.SMARTCARD_SLOTNO1, UnitTestParameters.SMARTCARD_SLOTNO1);
        }

        public void signWithBaseSigner3(XMLSignature signature) //beytullah.yigit
        {
            _signWithSigner(signature, UnitTestParameters.SMARTCARD_SLOTNO2, UnitTestParameters.SMARTCARD_SLOTNO2);
        }

        public void signWithBaseSignerRevoked(XMLSignature signature) //revoked olacak!!!
        {
            _signWithSigner(signature, UnitTestParameters.SMARTCARD_SLOTNO_REVOKED, UnitTestParameters.SMARTCARD_SLOTNO_REVOKED);
        }

        public void signWithDifferentBaseSigner(XMLSignature signature)
        {
            _signWithSigner(signature, UnitTestParameters.SMARTCARD_SLOTNO1, UnitTestParameters.SMARTCARD_SLOTNO0);        
        }
     
        public ECertificate getCertificate()
        {
            return _getCertificateForSlot(UnitTestParameters.SMARTCARD_SLOTNO0);
        }

        public ECertificate getCertificate2()
        {
            return _getCertificateForSlot(UnitTestParameters.SMARTCARD_SLOTNO1);
        }

        public ECertificate getCertificate3()
        {
            return _getCertificateForSlot(UnitTestParameters.SMARTCARD_SLOTNO2);
        }

        public ECertificate getCertificateRevoked()
        {
            return _getCertificateForSlot(UnitTestParameters.SMARTCARD_SLOTNO_REVOKED);
        }

        public IResolver getPolicyResolver()
        {
            return policyResolver;
        }

        public String getRootDir()
        {
            return XmlSignatureTestHelper.getInstance().getRootDir();
        }

        public String getBaseDir()
        {
            return XmlSignatureTestHelper.getInstance().getBaseDir();
        }
    }
}

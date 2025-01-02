using System;
using System.Collections.Generic;
using System.IO;
using System.Threading;
using System.Windows.Forms;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.policy;
using tr.gov.tubitak.uekae.esya.api.cmssignature;
using tr.gov.tubitak.uekae.esya.api.cmssignature.attribute;
using tr.gov.tubitak.uekae.esya.api.cmssignature.signature;
using tr.gov.tubitak.uekae.esya.api.common.util;
using tr.gov.tubitak.uekae.esya.api.common;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.infra.mobile;
using tr.gov.tubitak.uekae.esya.api.infra.tsclient;

namespace MobileSignatureClient
{
    public partial class Form1 : Form
    {
        string[] fileNames;
        static int MOBILE_SIGNATURE_TIMEOUT = 120000;
        static int TIMEOUT_PER_SIGNATURE = 3000;
        private void loadLicense()
        {
            //write license path below
            FileStream fileStream = new FileStream(@"C:\ma3api-dotnet\lisans\lisans.xml", FileMode.Open, FileAccess.Read);
            LicenseUtil.setLicenseXml(fileStream);
        }

        public Form1()
        {
            InitializeComponent();
        }

        private void btnSelectFile_Click(object sender, EventArgs e)
        {
            openFileDialog1.Multiselect = true;
            openFileDialog1.ShowDialog(this);
            fileNames = openFileDialog1.FileNames;
            String fileNameString = "";
            foreach (String filename in fileNames)
            {
                fileNameString += filename + "; ";
            }
            txtFilePath.Text = fileNameString;
        }

        private MobileSigner initSigner()
        {
            String phoneNumber = txtPhoneNumber.Text;
            Operator mobileOperator = (Operator)cBoxOperator.SelectedIndex;
            PhoneNumberAndOperator phoneNumberAndOperator = new PhoneNumberAndOperator(phoneNumber, mobileOperator);

            EMSSPClientConnector emsspClientConnector = new EMSSPClientConnector();
            emsspClientConnector.setCertificateInitials(phoneNumberAndOperator);
            
            //GUI'den seçtir
            SignatureAlg signatureAlg = SignatureAlg.RSA_SHA256;
            ECertificate signerCert = null;
            string informativeText = getFileName(fileNames[0]);
            return new MobileSigner(emsspClientConnector, phoneNumberAndOperator, signerCert, informativeText, signatureAlg.getName(), null);
        }

        private void btnSign_Click(object sender, EventArgs e)
        {
            try
            {
                loadLicense();
                MobileSigner signer = initSigner();
                if (fileNames.Length == 1)
                {
                    createSingleSignature(signer, fileNames[0]);
                    String successMessage = "İmzalama işlemi başarılı. İmzalı dosya " + fileNames[0] + ".p7s ";
                    MessageBox.Show(successMessage, "Sonuç", MessageBoxButtons.OK, MessageBoxIcon.Information);
                }
                else if (fileNames.Length > 1)
                {
                    createMultipleSignature(signer, fileNames);
                }
                else
                {
                    throw new ESYAException("File not selected");
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show("İmzalama sırasında hata! " + ex.Message);
            }
        }

        private void createSingleSignature(MobileSigner mobileSigner, String filePath)
        {
            mobileSigner.getFingerPrintInfo().fingerPrintCalculatedEvent += Form_fingerPrintCalculatedEvent;
            signData(mobileSigner, filePath);
        }

        private void Form_fingerPrintCalculatedEvent(FingerPrintInfo fingerPrintInfo)
        {
            Console.WriteLine("Fingerprint value for " + fingerPrintInfo.getMobileSigner().getInformativeText() + ": " + fingerPrintInfo.getFingerPrint());
        }

        private void createMultipleSignature(MobileSigner mobileSigner, String[] filePaths)
        {
            try
            {
                int signatureCount = filePaths.Length;

                MultiMobileSigner multiMobileSigner = new MultiMobileSigner(mobileSigner, signatureCount);

                int TOTAL_TIMEOUT_MS = MOBILE_SIGNATURE_TIMEOUT + TIMEOUT_PER_SIGNATURE;

                List<SignatureResult> results = new List<SignatureResult>();
                Thread[] threads = new Thread[signatureCount];
                for (int i = 0; i < signatureCount; i++)
                {
                    int index = i;
                    MultiMobileSignerForOne mobileSignerForOne = new MultiMobileSignerForOne(multiMobileSigner, getFileName(filePaths[index]), index);

                    mobileSignerForOne.getFingerPrintInfo().fingerPrintCalculatedEvent += Form_fingerPrintCalculatedEvent;

                    threads[i] = new Thread(() => results.Add(signDataWithoutException(filePaths[index], mobileSignerForOne)));
                    threads[i].Start();
                    Console.WriteLine("Thread " + i + " started");
                }


                DateTime endTime = DateTime.Now.AddMilliseconds(TOTAL_TIMEOUT_MS);
                for (int i = 0; i < signatureCount; i++)
                {
                    TimeSpan timeSpan = endTime.Subtract(DateTime.Now);
                    if(timeSpan.TotalMilliseconds < 10)
                        timeSpan = TimeSpan.FromMilliseconds(10);

                    if (!threads[i].Join(timeSpan))
                    {
                        threads[i].Abort();
                        Console.WriteLine("Timeout Occured.");
                    }

                    Console.WriteLine("Joined thread: " + i);
                }

                bool allFailed = true;
                bool allSuccess = true;
                foreach (SignatureResult aResult in results)
                {
                    if (!aResult.isExceptionOccured())
                    {
                        allFailed = false;
                    }
                    else
                    {
                        allSuccess = false;
                    }
                }

                if (allFailed)
                {
                    MessageBox.Show("İmzalama sırasında hata oluştu. " + results[0].getException().Message, "Sonuç", MessageBoxButtons.OK, MessageBoxIcon.Information);
                }
                else if (allSuccess)
                {
                    MessageBox.Show("Çoklu imzalama başarılı.", "Sonuç", MessageBoxButtons.OK, MessageBoxIcon.Information);
                }
                else
                {
                    foreach (SignatureResult aResult in results)
                    {
                        if (aResult.isExceptionOccured())
                        {
                            Console.WriteLine("Exception occured for: " + aResult.getInformativeText());
                            Console.WriteLine(aResult.getException().ToString());
                            MessageBox.Show(
                                aResult.getInformativeText() + " için hata oluştu. " + aResult.getException().Message, "Sonuç", MessageBoxButtons.OK, MessageBoxIcon.Information);
                        }
                        else
                        {
                            MessageBox.Show(aResult.getInformativeText() + " için imza başarılı bir şekilde atıldı.", "Sonuç", MessageBoxButtons.OK, MessageBoxIcon.Information);
                        }
                    }
                }
            }
            catch (Exception ex)
            {
                String errorString = "Error in creating multiple signature";
                throw new ESYAException(errorString, ex);
            }
        }

        private String getFileName(String filePath)
        {
            return Path.GetFileName(filePath);
        }

        private void signData(MobileSigner mobileSigner, String filePath)
        {
            byte[] contentData = FileUtil.readBytes(filePath);

            BaseSignedData bs = new BaseSignedData();
            bs.addContent(new SignableByteArray(contentData));

            Dictionary<String, Object> params_ = new Dictionary<String, Object>();
            //write policy path below
            ValidationPolicy readValidationPolicy = PolicyReader.readValidationPolicy(new FileStream(@"C:\ma3api-dotnet\config\certval-policy.xml", FileMode.Open, FileAccess.Read));

            params_[EParameters.P_CERT_VALIDATION_POLICY] = readValidationPolicy;
            //In real system, validate certificate by giving parameter "true" instead of "false"
            params_[EParameters.P_VALIDATE_CERTIFICATE_BEFORE_SIGNING] = false;

            //Since SigningTime attribute is optional, add it to optional attributes list
            List<IAttribute> optionalAttributes = new List<IAttribute>();
            optionalAttributes.Add(new SigningTimeAttr(DateTime.Now));

            bs.addSigner(ESignatureType.TYPE_BES, null, mobileSigner, optionalAttributes, params_);
            FileUtil.writeBytes(filePath + ".p7s", bs.getEncoded());
        }

        private SignatureResult signDataWithoutException(String filePath, MobileSigner signer)
        {
            try
            {
                signData(signer, filePath);
            }
            catch (Exception ex)
            {
                return new SignatureResult(ex, true, signer.getInformativeText());
            }

            return new SignatureResult(null, false, signer.getInformativeText());
        }

        private SignatureResult signDataXLong(String filePath, MobileSigner signer)
        {
            try
            {
                BaseSignedData bs = new BaseSignedData();
                // bs.addContent(new SignableFile(new File(filePath)), true);
                bs.addContent(new SignableFile(new FileInfo(filePath)));

                ValidationPolicy validationPolicy =
                    PolicyReader.readValidationPolicy(new FileStream(@"T:\config\certval-policy.xml", FileMode.Open));
                Dictionary<string, object> _params = new Dictionary<string, object>
                {
                    {EParameters.P_VALIDATE_CERTIFICATE_BEFORE_SIGNING, false},
                    {EParameters.P_CERT_VALIDATION_POLICY, validationPolicy},
                    {
                        EParameters.P_TSS_INFO,
                        new TSSettings("********", 0, "********", DigestAlg.SHA256)
                    }
                };

                //In real system, validate certificate by giving parameter "true" instead of "false"

                //Since SigningTime attribute is optional, add it to optional attributes list
                List<IAttribute> optionalAttributes = new List<IAttribute> {new SigningTimeAttr(DateTime.Now)};

                bs.addSigner(ESignatureType.TYPE_ESXLong, null, signer, optionalAttributes, _params);

                FileUtil.writeBytes(filePath + ".p7s", bs.getEncoded());
            }
            catch (Exception ex)
            {

                return new SignatureResult(ex, true, signer.getInformativeText());
            }

            return new SignatureResult(null, false, signer.getInformativeText());
        }

        private void Form1_Load(object sender, EventArgs e)
        {

        }
    }
}

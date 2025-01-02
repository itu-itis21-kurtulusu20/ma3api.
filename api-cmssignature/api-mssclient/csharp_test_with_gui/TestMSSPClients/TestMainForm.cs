using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.IO;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.cmssignature;
using tr.gov.tubitak.uekae.esya.api.cmssignature.attribute;
using tr.gov.tubitak.uekae.esya.api.cmssignature.signature;
using tr.gov.tubitak.uekae.esya.api.common;
using tr.gov.tubitak.uekae.esya.api.common.crypto;
using tr.gov.tubitak.uekae.esya.api.common.util;
using tr.gov.tubitak.uekae.esya.api.infra.mobile;
using tr.gov.tubitak.uekae.esya.api.webservice.mssclient.wrapper;
using tr.gov.tubitak.uekae.esya.asn.util;

namespace TestMSSPClients
{
    public partial class TestMainForm : Form
    {
        private List<MSSParams> operatorParams = new List<MSSParams>();

        public TestMainForm()
        {
            InitializeComponent();
            operatorParams.Add(new MSSParams("http://MImzaTubitakBilgem", "zR4*9a2+78", "www.turkcelltech.com"));
            operatorParams.Add(new MSSParams("http://www.kamusm.gov.tr/prod", "907187", ""));

        }

        private void TestMainForm_Load(object sender, EventArgs e)
        {
            cBoxOperator.SelectedIndex = 0;
        }

        private void cBoxOperator_SelectedIndexChanged(object sender, EventArgs e)
        {
            int selectedIndex = cBoxOperator.SelectedIndex;
            MSSParams mssParams = operatorParams[selectedIndex];
            txtMsspUserName.Text = mssParams.AP_ID;
            txtMsspUserPwd.Text = mssParams.PWD;
            txtMsspDomain.Text = mssParams.DnsName;
        }

        private void txtMobileNumber_TextChanged(object sender, EventArgs e)
        {

        }

        private void btnTestProfileService_Click(object sender, EventArgs e)
        {
            MSSParams mssParams = new MSSParams(txtMsspUserName.Text, txtMsspUserPwd.Text, txtMsspDomain.Text);
            
            int msspTimeout = Int32.Parse(txtMSSPTimeout.Text);
            mssParams.QueryTimeOut = msspTimeout;

            int connectionTimeout = Int32.Parse(txtConnectionTimeout.Text);
            mssParams.ConnectionTimeOutMs = connectionTimeout;

            EMSSPRequestHandler msspRequestHandler = new EMSSPRequestHandler(mssParams);
            Operator mobileOperator = (Operator) cBoxOperator.SelectedIndex;
            string mobileNumber = txtMobileNumber.Text;
            PhoneNumberAndOperator phoneNumberAndOperator = new PhoneNumberAndOperator(mobileNumber, mobileOperator);
            ECertificate[] eCertificates = msspRequestHandler.GetCertificates(phoneNumberAndOperator);
            MessageBox.Show("İşlem başarılı...");
        }

        private void btnTestSignHash_Click(object sender, EventArgs e)
        {
            MSSParams mssParams = new MSSParams(txtMsspUserName.Text, txtMsspUserPwd.Text, txtMsspDomain.Text);

            int msspTimeout = Int32.Parse(txtMSSPTimeout.Text);
            mssParams.QueryTimeOut = msspTimeout;

            int connectionTimeout = Int32.Parse(txtConnectionTimeout.Text);
            mssParams.ConnectionTimeOutMs = connectionTimeout;
            
            EMSSPRequestHandler msspRequestHandler = new EMSSPRequestHandler(mssParams);
            Operator mobileOperator = (Operator) cBoxOperator.SelectedIndex;
            string mobileNumber = txtMobileNumber.Text;
            PhoneNumberAndOperator phoneNumberAndOperator = new PhoneNumberAndOperator(mobileNumber, mobileOperator);

            byte[] digestForSign = new byte[20];
            digestForSign[0] = 0x23;
            digestForSign[2] = 0x23;
            digestForSign[4] = 0x23;
            digestForSign[6] = 0x23;
            digestForSign[8] = 0x23;
            digestForSign[10] = 0x23;
            digestForSign[12] = 0x23;
            digestForSign[14] = 0x23;
            digestForSign[16] = 0x23;
            digestForSign[18] = 0x23;

            byte[] signature = msspRequestHandler.Sign(digestForSign, SigningMode.SIGNHASH, phoneNumberAndOperator,
                                                       "NET ile imzalama", Algorithms.SIGNATURE_RSA_SHA1);
            MessageBox.Show("İşlem Tamamlandı.");
        }

        private void btnBrowseFile_Click(object sender, EventArgs e)
        {
            OpenFileDialog fd = new OpenFileDialog();
            fd.ShowDialog();
            string fileName = fd.FileName;
            txtFileForSign.Text = fileName;
        }

        private void btnSignFile_Click(object sender, EventArgs e)
        {
            MSSParams mssParams = new MSSParams(txtMsspUserName.Text, txtMsspUserPwd.Text, txtMsspDomain.Text);

            int msspTimeout = Int32.Parse(txtMSSPTimeout.Text);
            mssParams.QueryTimeOut = msspTimeout;

            int connectionTimeout = Int32.Parse(txtConnectionTimeout.Text);
            mssParams.ConnectionTimeOutMs = connectionTimeout;

            EMSSPRequestHandler msspRequestHandler = new EMSSPRequestHandler(mssParams);
            Operator mobileOperator = (Operator)cBoxOperator.SelectedIndex;
            string mobileNumber = txtMobileNumber.Text;
            PhoneNumberAndOperator phoneNumberAndOperator = new PhoneNumberAndOperator(mobileNumber, mobileOperator);

            String sourceFilePath = txtFileForSign.Text;
            //--------------------------------------------------------------------------------//
            BaseSignedData bs = new BaseSignedData();
            bs.addContent(new SignableFile(new FileInfo(sourceFilePath)));

            //create parameters necessary for signature creation 
            Dictionary<String, Object> parameters = new Dictionary<String, Object>();
            parameters.Add(EParameters.P_VALIDATE_CERTIFICATE_BEFORE_SIGNING,false);


            List<IAttribute> optionalSignedAttributes = new List<IAttribute>();
            optionalSignedAttributes.Add(new SigningTimeAttr(DateTime.Now));

            ECertificate cert = null;
            ECertificate[] eCertificates = msspRequestHandler.GetCertificates(phoneNumberAndOperator);
            if(eCertificates!=null && eCertificates.Length!=0)
            {
                cert = eCertificates[0];
            }
            
            EMSSPClientConnector msspClientConnector = new EMSSPClientConnector(msspRequestHandler);
            MobileSigner mobileSigner = new MobileSigner(msspClientConnector,phoneNumberAndOperator,cert,"NET Test imza",Algorithms.SIGNATURE_RSA_SHA1);

            bs.addSigner(ESignatureType.TYPE_BES, cert, mobileSigner, optionalSignedAttributes, parameters);

            //write the contentinfo to file 
            String outputFilePath = sourceFilePath + ".p7s";
            AsnIO.dosyayaz(bs.getEncoded(),outputFilePath );
            MessageBox.Show(outputFilePath + " konumuna imzalı dosya kaydedildi.");

        }
    }
}

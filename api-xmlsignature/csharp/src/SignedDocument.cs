using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Xml;
using tr.gov.tubitak.uekae.esya.api.cmssignature.bundle;
using tr.gov.tubitak.uekae.esya.api.crypto;
using Logger = log4net.ILog;
using LE = tr.gov.tubitak.uekae.esya.api.common.license.LE;
using LV = tr.gov.tubitak.uekae.esya.api.common.license.LV;
using Constants = tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;
using Document = tr.gov.tubitak.uekae.esya.api.xmlsignature.document.Document;
using I18n = tr.gov.tubitak.uekae.esya.api.xmlsignature.util.I18n;
using IdGenerator = tr.gov.tubitak.uekae.esya.api.xmlsignature.util.IdGenerator;
using XmlCommonUtil = tr.gov.tubitak.uekae.esya.api.common.util.XmlUtil;
namespace tr.gov.tubitak.uekae.esya.api.xmlsignature
{
	/// <summary> Enveloped Document support with multiple signatures serial and/or parallel.
	/// 
	/// </summary>
	/// <author>  ayetgin
	/// date May 6, 2009
	/// </author>
	public class SignedDocument
	{
	    private static Logger logger = log4net.LogManager.GetLogger(typeof(SignedDocument));
		
		private System.Xml.XmlDocument mDocument;
		private Context mContext;
		
		private System.Xml.XmlElement mRootElement, mSignaturesElement, mDocumentsElement;

	    private readonly List<XMLSignature> mSignatures = new List<XMLSignature>();
		
		public SignedDocument(Document aDocument, Context aContext)
		{
		    mContext = aContext;

            try
            {

            /*
            try
			{
				LV.getInstance().checkLicenceDates(LV.Products.XMLIMZA);
			}
			catch (LE ex)
			{
				logger.Fatal("Lisans kontrolu basarisiz.");
				throw new System.SystemException("Lisans kontrolu basarisiz.");
			}
			try
			{*/
				
                mDocument = new XmlDocument();
			    mDocument.PreserveWhitespace = true;
                mDocument.Load(new MemoryStream(aDocument.Bytes));

                construct(mDocument, aContext);

                /*
                XmlNameTable nsTable = mDocument.NameTable;
                XmlNamespaceManager nsManager = new XmlNamespaceManager(nsTable);
                nsManager.AddNamespace("ds", Constants.NS_XMLDSIG);
                nsManager.AddNamespace("xades", Constants.NS_XADES_1_3_2);
                nsManager.AddNamespace(Constants.NS_MA3_PREFIX, Constants.NS_MA3);

                XmlNodeList signaturesList = mDocument.SelectNodes("//ds:Signature", nsManager);

                for (int i = 0; i < signaturesList.Count; i++)
				{
					XMLSignature s = new XMLSignature((System.Xml.XmlElement) signaturesList.Item(i), aContext);
				    s.IsRoot = false;
					mSignatures.Add(s);
				}
                 */
            }
			catch (System.Exception x)
			{
				logger.Error("Can't parse XML signature: " + aDocument.URI, x);
				throw new XMLSignatureException(x, "errors.cantConstructSignature", aDocument.URI);
			}
		}

        public SignedDocument(XmlDocument aDocument, Context aContext)
        {
            construct(aDocument, aContext);
        }
		
        public void construct(XmlDocument aDocument, Context aContext)
        {
            mContext = aContext;
            mDocument = aDocument;
            mContext.Document = mDocument;
            
            try
            {
                LV.getInstance().checkLicenceDates(LV.Products.XMLIMZA);
            }
            catch (LE ex)
            {
                logger.Fatal("Lisans kontrolu basarisiz.");
                throw new System.SystemException("Lisans kontrolu basarisiz.");
            }

            try
            {
                /*XmlNameTable nsTable = mDocument.NameTable;
                XmlNamespaceManager nsManager = new XmlNamespaceManager(nsTable);
                nsManager.AddNamespace("ds", Constants.NS_XMLDSIG);
                nsManager.AddNamespace("xades", Constants.NS_XADES_1_3_2);
                nsManager.AddNamespace(Constants.NS_MA3_PREFIX, Constants.NS_MA3);*/

                mRootElement = mDocument.DocumentElement;
                mDocumentsElement = XmlCommonUtil.selectFirstElementNoNS(mRootElement.FirstChild, "data");
                mSignaturesElement = XmlCommonUtil.selectFirstElementNoNS(mRootElement.FirstChild, "signatures");

                //XmlNodeList signaturesList = mDocument.SelectNodes("//ds:Signature", nsManager);

                //XmlNodeList signaturesList = mSignaturesElement.GetElementsByTagName("Signature", "*");

                XmlElement[] signatureElements = XmlCommonUtil.selectNodes(mSignaturesElement.FirstChild, Constants.NS_XMLDSIG, "Signature");

                /*for (int i = 0; i < signaturesList.Count; i++)
				{
					XMLSignature s = new XMLSignature((System.Xml.XmlElement) signaturesList.Item(i), mContext);
				    s.IsRoot = false;
					mSignatures.Add(s);
				}*/

                foreach (XmlElement element in signatureElements)
                {
                    XMLSignature s = new XMLSignature(element, mContext);
                    mSignatures.Add(s);
                }
            }
			catch (System.Exception x)
			{
				logger.Error("Can't parse XML signature: ", x);
				throw new XMLSignatureException(x, "errors.cantConstructSignature", aDocument);
			}
        }

		public SignedDocument(Context aContext)
		{
			mContext = aContext;
			mDocument = aContext.Document;
			
			//mRootElement = mDocument.CreateElement("ma3:envelope", Constants.NS_MA3);
            mRootElement = mDocument.CreateElement("envelope", null);
			mDocument.AppendChild(mRootElement);

            string xmlnsDsPrefix = mContext.Config.NsPrefixMap.getPrefix(Constants.NS_XMLDSIG);
            mRootElement.SetAttribute("xmlns:" + String.Intern(xmlnsDsPrefix), Constants.NS_XMLDSIG);

            //string xmlnsMa3Prefix = mContext.Config.NsPrefixMap.getPrefix(Constants.NS_MA3);
            //mRootElement.SetAttribute("xmlns:" + String.Intern(xmlnsMa3Prefix), Constants.NS_MA3);



		 //   mRootElement.SetAttribute(Constants.NS_MA3, Constants.NS_NAMESPACESPEC);
         //   mRootElement.SetAttribute(Constants.NS_XMLDSIG, Constants.NS_NAMESPACESPEC);
			
			
			//mSignaturesElement = mDocument.CreateElement("ma3:signatures", Constants.NS_MA3);
            mSignaturesElement = mDocument.CreateElement("signatures", null);
			//mDocumentsElement = mDocument.CreateElement("ma3:data", Constants.NS_MA3);
            mDocumentsElement = mDocument.CreateElement("data", null);

            XmlCommonUtil.addLineBreak(mSignaturesElement);
            XmlCommonUtil.addLineBreak(mDocumentsElement);

            XmlCommonUtil.addLineBreak(mRootElement);
			mRootElement.AppendChild(mDocumentsElement);
            XmlCommonUtil.addLineBreak(mRootElement);
			mRootElement.AppendChild(mSignaturesElement);
            XmlCommonUtil.addLineBreak(mRootElement);
		}
		
		/// <param name="aDocument">to be added
		/// </param>
		/// <returns> identifier to be used by references
		/// </returns>
		/// <throws>  XMLSignatureException if problem occurs while </throws>
		/// <summary>  dereferencing/accessing the document
		/// </summary>
		public virtual System.String addDocument(Document aDocument)
		{
			
			//System.Xml.XmlElement dataElement = mDocument.CreateElement("ma3:data-item", Constants.NS_MA3);
            System.Xml.XmlElement dataElement = mDocument.CreateElement("data-item", null);
			
			System.String id = mContext.IdGenerator.uret(IdGenerator.TYPE_DATA);
			dataElement.SetAttribute("Id",id);
			mContext.IdRegistry.put(id, dataElement);

		    byte[] documentBytes = aDocument.Bytes;
            string fromByteArray = new System.String(UTF8Encoding.UTF8.GetChars(documentBytes));

		   // string fromByteArray = StringUtil.FromByteArray(documentBytes);

		    //dataElement.AppendChild(dataElement.OwnerDocument.CreateTextNode(new System.String(SupportClass.ToCharArray(SupportClass.ToByteArray(aDocument.Bytes)))));
            dataElement.AppendChild(dataElement.OwnerDocument.CreateTextNode(fromByteArray));
			mDocumentsElement.AppendChild(dataElement);
            XmlCommonUtil.addLineBreak(mDocumentsElement);
			
			return id;
		}
		
		public virtual System.String addXMLDocument(Document aDocument)
		{
			try
			{
			    XmlDocument xmlDoc = new XmlDocument();
			    xmlDoc.PreserveWhitespace = true;
                xmlDoc.Load(new System.IO.MemoryStream((byte [])aDocument.Bytes.Clone()));
				return addXMLNode(xmlDoc.DocumentElement);
			}
			catch (System.Exception x)
			{
				throw new XMLSignatureException(x, "errors.cantAddDocument", aDocument.URI);
			}
		}
		
		public virtual System.String addXMLNode(System.Xml.XmlNode aNode)
		{
			//System.Xml.XmlElement dataElement = mDocument.CreateElement("ma3:data-item", Constants.NS_MA3);
            System.Xml.XmlElement dataElement = mDocument.CreateElement("data-item", null);
			
			System.String id = mContext.IdGenerator.uret(IdGenerator.TYPE_DATA);
			dataElement.SetAttribute("Id", id);
			mContext.IdRegistry.put(id, dataElement);
			
			System.Xml.XmlNode importing = mDocument.ImportNode(aNode, true);
			dataElement.AppendChild(importing);
			
			mDocumentsElement.AppendChild(dataElement);
            XmlCommonUtil.addLineBreak(mDocumentsElement);
			
			return id;
		}

        public Context getContext()
        {
            return mContext;
        }
		
		public virtual XMLSignature createSignature()
		{
			XMLSignature s = new XMLSignature(mContext, false);
			mSignaturesElement.AppendChild(s.Element);
            XmlCommonUtil.addLineBreak(mSignaturesElement);
			
			mSignatures.Add(s);
			return s;
		}
		
		public virtual /*void*/ XMLSignature addSignature(XMLSignature signature)
		{
            /*
			mSignatures.Add(signature);

            // xImportDoc is the XmlDocument to be imported.
            // xTargetNode is the XmlNode into which the import is to be done.
            XmlNode xChildNode = mSignaturesElement.OwnerDocument.ImportNode(signature.Element, true);
            signature.Element = (XmlElement)xChildNode;
            signature.Document = mDocument;

			mSignaturesElement.AppendChild(signature.Element);
			XmlUtil.addLineBreak(mSignaturesElement);
             */

		    XMLSignature cloned;
		    try
		    {
		        XmlNode importing = mDocument.ImportNode(signature.Element, true);
                cloned = new XMLSignature((XmlElement)importing, mContext);
                mSignatures.Add(cloned);
		        mSignaturesElement.AppendChild(cloned.Element);
                XmlCommonUtil.addLineBreak(mSignaturesElement);
		    }
		    catch (Exception x)
		    {
		        throw new XMLSignatureRuntimeException("Error cloning signature when attaching to SignedDoc.", x);
		    }
		    return cloned;
		}

        public void removeSignature(XMLSignature signature)
        {
            mSignatures.Remove(signature);
            mSignaturesElement.RemoveChild(signature.Element);
        }

	    public int RootSignatureCount
	    {
	        get { return mSignatures.Count; }
	    }

	    public virtual XMLSignature getSignature(int aIndex)
		{
		    return mSignatures[aIndex];
		}
		
		public List<XMLSignature> getRootSignatures()
		{
		    return new List<XMLSignature>(mSignatures);
		}
		
		public virtual ValidationResult verify()
		{
			ValidationResult vr = new ValidationResult(GetType());
			foreach(XMLSignature signature in mSignatures)
			{
				ValidationResult verified = signature.verify();
				vr.addItem(verified);
				if (verified.getType() !=ValidationResultType.VALID)
				{
					vr.setStatus(verified.getType(), I18n.translate("validation.check.signedDoc"), verified.getMessage(), null);
					return verified;
				}
			}
            vr.setStatus(ValidationResultType.VALID, I18n.translate("validation.check.signedDoc"), I18n.translate("core.signedDocumentVerified"), null);
			return vr;
		}
		
		// output methods
		public virtual byte[] write()
		{
			System.IO.MemoryStream baos = new System.IO.MemoryStream();
			write(baos);
			return baos.ToArray();
		}
		
		public virtual void  write(Stream aStream)
		{
            if (!mDocument.InnerXml.Contains(XmlCommonUtil.XML_PREAMBLE_STR))
            {
                byte[] utf8Definition = XmlCommonUtil.XML_PREAMBLE;
                aStream.Write(utf8Definition, 0, utf8Definition.Length);
            }
            mDocument.Save(aStream);
		}

        public void finishAddingSignature(byte[] signature){
            byte[] tempSignature = DTBSRetrieverSigner.getTempSignatureBytes();

            List<XMLSignature> unFinishedSignatures = new List<XMLSignature>();
            List<XMLSignature> allSignatures = new List<XMLSignature>();

            foreach (XMLSignature xmlSignature in getRootSignatures()){
                allSignatures.Add(xmlSignature);
                allSignatures.AddRange(xmlSignature.AllCounterSignatures);
            }

            foreach (XMLSignature xmlSignature in allSignatures) {
                byte[] signatureOfSigner = xmlSignature.SignatureValue;
                if (Enumerable.SequenceEqual(signatureOfSigner, tempSignature)) {
                    unFinishedSignatures.Add(xmlSignature);
                }
            }

            if (unFinishedSignatures.Count == 0)
                throw new XMLSignatureException(Msg.getMsg(Msg.NO_UNFINISHED_SIGNATURE));

            //Normalde bitmemiş durumda olması gereken 1 adet imza olması lazım. Aynı anda imzalamaya başlamış kişilerden dolayı veya işlemini bitirmemiş
            //kişilerden dolayı yarım kalmış imza olabilir.

            if (unFinishedSignatures.Count > 1)
                logger.Info("Unfinished Signer Count: " + unFinishedSignatures.Count);

            bool valid = false;
            foreach (XMLSignature xmlSignature in unFinishedSignatures)
            {
                xmlSignature.SignatureValue = signature;

                ValidationResult result = xmlSignature.validateSignatureValue();

                if (result.mResultType.Equals(ValidationResultType.VALID))
                {
                    valid = true;
                    break;
                }
                else
                {
                    xmlSignature.SignatureValue = tempSignature;
                }
            }

            if (!valid)
                throw new XMLSignatureException(Msg.getMsg(Msg.NOT_VALID_SIGNATURE_VALUE_FOR_UNFINISHED_SIGNATURE));
        }
    }
}
using System;
using System.Collections.Generic;
using System.Text;
using tr.gov.tubitak.uekae.esya.api.signature;
using tr.gov.tubitak.uekae.esya.api.signature.impl;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.provider;
using XmlCommonUtil = tr.gov.tubitak.uekae.esya.api.common.util.XmlUtil;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature
{

	//using DocumentBuilderFactory = javax.xml.parsers.DocumentBuilderFactory;
	//using DocumentBuilder = javax.xml.parsers.DocumentBuilder;

	//using Node = org.w3c.dom.Node;
	//using NodeList = org.w3c.dom.NodeList;
	//using Document = org.w3c.dom.Document;
	
	//using XmlUtil = tr.gov.tubitak.uekae.esya.api.xmlsignature.util.XmlUtil;

	/// <summary>
	/// @author ahmety
	/// date: Oct 1, 2009
	/// </summary>
	public class ValidationResult : ValidationResultDetail
	{
		protected internal ValidationResultType mResultType;
	    protected internal string mCheckMessage;
		protected internal string mMessage;
		protected internal string mInfo;
		protected internal Type mVerifierClass;

		protected internal List<ValidationResult> items = new List<ValidationResult>();

		public ValidationResult(Type aVerifierClass)
		{
		   mVerifierClass = aVerifierClass;
		}

		/*public ValidationResult(ValidationResultType aResultType, string aMessage) : this(aResultType, aMessage, null)
		{
		}*/

		/*public ValidationResult(ValidationResultType aResultType, string aMessage, string aInfo)
		{
			mResultType = aResultType;
			mMessage = aMessage;
			mInfo = aInfo;
		}*/

        public ValidationResult(ValidationResultType aResultType,
                            String aCheckMessage,
                            String aMessage,
                            String aInfo,
                            Type aValidatorClass)
        {
            mResultType = aResultType;
            mCheckMessage = aCheckMessage;
            mMessage = aMessage;
            mVerifierClass = aValidatorClass;
        }

		/*public virtual void setStatus(ValidationResultType aType, string aMessage)
		{
			setStatus(aType, aMessage, null);
		}*/

		/*public virtual void setStatus(ValidationResultType aType, string aMessage, string aInfo)
		{
			mResultType = aType;
			mMessage = aMessage;
			mInfo = aInfo;
		}*/

        public void setStatus(ValidationResultType aType, string aCheckMessage, string aMessage, string aInfo)
        {
            mResultType = aType;
            mCheckMessage = aCheckMessage;
            mMessage = aMessage;
            mInfo = aInfo;
        }

        public void setType(ValidationResultType aResultType)
        {
            mResultType = aResultType;
        }

        public ValidationResultType getType()
        {
            return mResultType;
        }

        public String getCheckMessage()
        {
            return mCheckMessage;
        }

        public void setCheckMessage(String aCheckMessage)
        {
            mCheckMessage = aCheckMessage;
        }

        public void setMessage(String aMessage)
        {
            mMessage = aMessage;
        }

        public String getMessage()
        {
            return mMessage;
        }

        public String getVerifierClass()
        {
            return mVerifierClass.Name;
        }

        public void setVerifierClass(Type aVerifierClass)
        {
            mVerifierClass = aVerifierClass;
        }

		public virtual void addItem(ValidationResult aItem)
		{
			if (aItem != null)
			{
				items.Add(aItem);
			}
		}

        public int getItemCount()
        {
            return items.Count;
        }

		public virtual ValidationResult getItem(int aIndex)
		{
			return items[aIndex];
		}

        public tr.gov.tubitak.uekae.esya.api.signature.ValidationResultType getResultType()
        {
            return XMLProviderUtil.convert(mResultType);
        }

        public Type getValidatorClass()
        {
            return mVerifierClass;
        }

        public string getCheckResult()
        {
            return mMessage + ((mInfo != null) ? "\n" + mInfo : "");
        }

        public List<T> getDetails<T>() where T : ValidationResultDetail
        {
            List<ValidationResultDetail> details = getDetails();
            return details as List<T>;
        }

        public List<ValidationResultDetail> getDetails()
        {
            List<ValidationResultDetail> result = new List<ValidationResultDetail>(items.Count);
            for (int i = 0; i < result.Capacity; i++)
                result.Add(items[i]);
            return result;
        }
    
	    public virtual string toXml()
		{
			return toXml(0);
		}
	    private readonly ResultFormatter formatter = new ResultFormatter();
        public override String ToString()
        {
            return formatter.prettyPrint(this,0);
        }
        
		internal virtual string toXml(int indent)
		{
			StringBuilder buffer = new StringBuilder();
			bool verified = mResultType == ValidationResultType.VALID;
			StringBuilder indentStr = new StringBuilder();
			for (int i = 0; i < indent;i++)
			{
				indentStr.Append('\t');
			}
			buffer.Append(indentStr);
			buffer.Append(verified ? "<Verified" : "<Failed");
			if (mVerifierClass != null)
			{
                buffer.Append(" by=\"").Append(mVerifierClass).Append('\"');
			}
			buffer.Append(">\n");
            buffer.Append(indentStr).Append('\t').Append("<ResultType>").Append(mResultType).Append("</ResultType>\n");
            buffer.Append(indentStr).Append('\t').Append("<Message>").Append(XmlCommonUtil.escapeXMLData(mMessage)).Append("</Message>\n");
			if (mInfo != null)
			{
                buffer.Append(indentStr).Append('\t').Append("<Info>").Append(XmlCommonUtil.escapeXMLData(mInfo)).Append(indentStr).Append('\t').Append("</Info>\n");
			}
			if (items != null && items.Count > 0)
			{
                buffer.Append(indentStr).Append('\t').Append("<Details>\n");
				foreach (ValidationResult item in items)
				{
					buffer.Append(item.toXml(indent + 2));
				}
                buffer.Append(indentStr).Append("\t").Append("</Details>\n");
			}
            buffer.Append(indentStr).Append(verified ? "</Verified>" : "</Failed>").Append('\n');

			return buffer.ToString();
		} 

        /* bu pretty print'ler kullanilmamis bir yerde
		private static void prettyPrint(StringBuilder result, Node aNode, int level)
		{
			switch (aNode.NodeType)
			{
				case Node.DOCUMENT_NODE :
					prettyPrint(result, ((Document)aNode).DocumentElement, level);
					break;
				case Node.TEXT_NODE :
					string text = aNode.TextContent;
					Console.WriteLine("text: '" + text + "'");
					result.Append(text);
					if (text.Equals("\n"))
					{
						for (int i = 0;i < level;i++)
						{
							result.Append('\t');
						}
					}
					break;

				case Node.ELEMENT_NODE :

					result.Append("<").Append(aNode.NodeName);
					result.Append(">");

					if (aNode.hasChildNodes())
					{
						NodeList children = aNode.ChildNodes;
						for (int j = 0; j < children.Length; j++)
						{
							Node child = children.item(j);
							prettyPrint(result, child, level + 1);
						}
					}

					result.Append("</").Append(aNode.NodeName);
					result.Append(">");
					break;
			}
		}
         //* */
        /*
		private static string prettyPrint(string str)
		{
			try
			{

				DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				dbf.NamespaceAware = true;
				DocumentBuilder db = dbf.newDocumentBuilder();
				Document doc = db.parse(new ByteArrayInputStream(str.Bytes));

				StringBuilder result = new StringBuilder();
				prettyPrint(result, doc, 0);

				return result.ToString();
			}
			catch (Exception x)
			{
				Console.WriteLine("Cant format output xml: " + x.Message);
			}
			return str;
        }
         */
        /*
		static void Main(string[] args)
		{
			Console.WriteLine(prettyPrint("<Failed>\n" + "<ResultType>INVALID</ResultType>\n<Message>Cant validate signer certificate: Sertifika Sorunlu</Message>\n" + "<Items>\n" + "<Verified>\n" + "<ResultType>VALID</ResultType>\n<Message>Signature value validated.</Message>\n" + "</Verified>\n" + "<Verified>\n" + "<ResultType>VALID</ResultType>\n<Message>References are valid.</Message>\n" + "</Verified>\n" + "<Failed>\n" + "<ResultType>INVALID</ResultType>\n<Message>Cant validate signer certificate: Sertifika Sorunlu</Message>\n" + "<Items>\n" + "</Items>\n" + "</Failed>\n" + "</Items>\n" + "</Failed>"));
		}*/

	}

}
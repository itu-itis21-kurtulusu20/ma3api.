using System;
using System.Security.Cryptography;
using System.Xml;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.model.keyinfo
{

	using OIDUtil = tr.gov.tubitak.uekae.esya.api.common.util.OIDUtil;
	using XMLSignatureException = tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
	using Context = tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
	using BaseElement = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.BaseElement;
	using XmlUtil = tr.gov.tubitak.uekae.esya.api.xmlsignature.util.XmlUtil;
    using EConstants = tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;


	using Logger  = log4net.ILog;
	using Element = XmlElement;
    /*
	using ECDSAPublicKey = gnu.crypto.key.ecdsa.ECDSAPublicKey;
	using ECGNUPoint = gnu.crypto.sig.ecdsa.ecmath.curve.ECGNUPoint;
	using ECDomainParameter = gnu.crypto.sig.ecdsa.ecmath.curve.ECDomainParameter;
	using ECPointFp = gnu.crypto.sig.ecdsa.ecmath.curve.ECPointFp;*/

	/// <summary>
	/// @author ahmety
	/// date: Aug 25, 2009
	/// </summary>
	public class ECKeyValue : tr.gov.tubitak.uekae.esya.api.xmlsignature.model.BaseElement, KeyValueElement
	{

		private string mOID;
		//private ECDSAPublicKey mPublicKey;
	    private readonly ECDsa mEcDsa;

	    private readonly string mXmlStr;
		private Element mPublicKeyElement, mNamedCurveElement;

        public ECKeyValue(Context aContext, ECDsa aEcDsa)
            : base(aContext)
		{

			addLineBreak();
            mEcDsa = aEcDsa;
			//mPublicKey = aPublicKey;
            //TODO burda as�l beklenen nedir ?
            mXmlStr = mEcDsa.ToXmlString(false);/*
            byte[] bytes = mPublicKey.MQ.toOctetString(ECGNUPoint.UNCOMPRESSED);
			int[] oid = mPublicKey.MParameters.getmParamOID();

			mPublicKeyElement = insertElement(NS_XMLDSIG_11, TAG_PUBLICKEY);
			XmlUtil.setBase64EncodedText(mPublicKeyElement, bytes);

			mNamedCurveElement = insertTextElement(NS_XMLDSIG_11, TAG_NAMEDCURVE, OIDUtil.toURN(oid));*/

		}

		/// <summary>
		/// Construct KeyValueElement from existing </summary>
		/// <param name="aElement"> xml element </param>
		/// <param name="aContext"> according to context </param>
		/// <exception cref="tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException">
		///          when structure is invalid or can not be
		///          resolved appropriately </exception>
		public ECKeyValue(Element aElement, Context aContext) : base(aElement, aContext)
		{
            mEcDsa.FromXmlString(aElement.Value);
            /*
			// todo
			mNamedCurveElement = selectChildElement(NS_XMLDSIG_11, TAG_NAMEDCURVE);

			if (mNamedCurveElement != null)
			{
				mOID = XmlUtil.getText(mNamedCurveElement).Trim();

			}

			mPublicKeyElement = selectChildElement(NS_XMLDSIG_11, TAG_PUBLICKEY);
			if (mPublicKeyElement != null)
			{
				byte[] publicKeyBytes = XmlUtil.getBase64DecodedText(mPublicKeyElement);
				try
				{
					int[] oid = OIDUtil.fromURN(mOID);
					ECDomainParameter ecdparam = ECDomainParameter.getInstance(oid);
					ECGNUPoint point = new ECPointFp(ecdparam.MCurve, publicKeyBytes);

					mPublicKey = new ECDSAPublicKey(ecdparam, point);

				}
				catch (Exception x)
				{
					Console.WriteLine(x.ToString());
					Console.Write(x.StackTrace);
					throw new XMLSignatureException(x, "core.cantGeneratePublicKey", "ECDSA");
				}
			}
             */
		}

		public virtual AsymmetricAlgorithm PublicKey
		{
			get
			{
				return mEcDsa;
			}
		}

		public virtual string OID
		{
			get
			{
				return mOID;
			}
		}

		public override string Namespace
		{
			get
			{
				return EConstants.NS_XMLDSIG_11;
			}
		}

		public override string LocalName
		{
			get
			{
                return EConstants.TAG_ECKEYVALUE;
			}
		}
	}

}
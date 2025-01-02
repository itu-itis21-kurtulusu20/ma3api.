using System;
using System.Collections.Generic;
using System.Reflection;
using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.api.asn.cms;
using tr.gov.tubitak.uekae.esya.api.cmssignature.attribute;


//todo Annotation!
//@ApiClass

namespace tr.gov.tubitak.uekae.esya.api.cmssignature.signature
{
    /**
 * Electronic Signature Formats defined in ETSI TS 101 733, and implemented
 * this API.
 */
    public class ESignatureType
    {
        //private static readonly Dictionary<String, ESignatureType> msSignatureTypeRegistry = new Dictionary<String, ESignatureType>();

        public static readonly ESignatureType TYPE_BES = new ESignatureType
    (
        new Asn1ObjectIdentifier[] 
			    {
					AttributeOIDs.id_contentType,
					AttributeOIDs.id_messageDigest
			    },
        new Asn1ObjectIdentifier[] 
				{
			 		AttributeOIDs.id_messageDigest
				},
        new Asn1ObjectIdentifier[] { }
    );

        public static readonly ESignatureType TYPE_EPES = new ESignatureType
        (
            new Asn1ObjectIdentifier[] 
				{
			       AttributeOIDs.id_contentType,
			       AttributeOIDs.id_messageDigest,
			       AttributeOIDs.id_aa_ets_sigPolicyId
			     },

            new Asn1ObjectIdentifier[] 
		        {
		        	AttributeOIDs.id_messageDigest,
		            AttributeOIDs.id_aa_ets_sigPolicyId
		        },
            new Asn1ObjectIdentifier[] { }
      );

        public static readonly ESignatureType TYPE_EST = new ESignatureType
        (
              TYPE_BES.mSignedAttributeList,
              TYPE_BES.mCounterSignatureSignedAttributeList,
              new Asn1ObjectIdentifier[]
			    {
						AttributeOIDs.id_aa_signatureTimeStampToken
			    }
       );
        public static readonly ESignatureType TYPE_ESC = new ESignatureType
        (
           TYPE_BES.mSignedAttributeList,
           TYPE_BES.mCounterSignatureSignedAttributeList,
           new Asn1ObjectIdentifier[]
			{
					AttributeOIDs.id_aa_signatureTimeStampToken,
					AttributeOIDs.id_aa_ets_certificateRefs,
					AttributeOIDs.id_aa_ets_revocationRefs
		    }
       );
        public static readonly ESignatureType TYPE_ESX_Type1 = new ESignatureType
        (
            TYPE_BES.mSignedAttributeList,
            TYPE_BES.mCounterSignatureSignedAttributeList,
            new Asn1ObjectIdentifier[]
			{
					AttributeOIDs.id_aa_signatureTimeStampToken,
					AttributeOIDs.id_aa_ets_certificateRefs,
					AttributeOIDs.id_aa_ets_revocationRefs,
					AttributeOIDs.id_aa_ets_escTimeStamp
		    }
        );
        public static readonly ESignatureType TYPE_ESX_Type2 = new ESignatureType
        (
            TYPE_BES.mSignedAttributeList,
            TYPE_BES.mCounterSignatureSignedAttributeList,
            new Asn1ObjectIdentifier[]
			{
					AttributeOIDs.id_aa_signatureTimeStampToken,
					AttributeOIDs.id_aa_ets_certificateRefs,
					AttributeOIDs.id_aa_ets_revocationRefs,
					AttributeOIDs.id_aa_ets_certCRLTimestamp
			}
        );
        public static readonly ESignatureType TYPE_ESXLong = new ESignatureType
        (
            TYPE_BES.mSignedAttributeList,
            TYPE_BES.mCounterSignatureSignedAttributeList,
            new Asn1ObjectIdentifier[]
			{
					AttributeOIDs.id_aa_signatureTimeStampToken,
					AttributeOIDs.id_aa_ets_certificateRefs,
					AttributeOIDs.id_aa_ets_revocationRefs,
					AttributeOIDs.id_aa_ets_certValues,
					AttributeOIDs.id_aa_ets_revocationValues
			}
        );


        public static readonly ESignatureType TYPE_ESXLong_Type1 = new ESignatureType
        (
                TYPE_BES.mSignedAttributeList,
                TYPE_BES.mCounterSignatureSignedAttributeList,
                new Asn1ObjectIdentifier[]
				{
					AttributeOIDs.id_aa_signatureTimeStampToken,
					AttributeOIDs.id_aa_ets_certificateRefs,
					AttributeOIDs.id_aa_ets_revocationRefs,
					AttributeOIDs.id_aa_ets_escTimeStamp,
					AttributeOIDs.id_aa_ets_certValues,
					AttributeOIDs.id_aa_ets_revocationValues
				}
        );

        public static readonly ESignatureType TYPE_ESXLong_Type2 = new ESignatureType
        (
                TYPE_BES.mSignedAttributeList,
                TYPE_BES.mCounterSignatureSignedAttributeList,
                new Asn1ObjectIdentifier[]
				{
					AttributeOIDs.id_aa_signatureTimeStampToken,
					AttributeOIDs.id_aa_ets_certificateRefs,
					AttributeOIDs.id_aa_ets_revocationRefs,
					AttributeOIDs.id_aa_ets_certCRLTimestamp,
					AttributeOIDs.id_aa_ets_revocationValues,
					AttributeOIDs.id_aa_ets_certValues
				}
        );

        public static readonly ESignatureType TYPE_ESAv2 = new ESignatureType
            (
            TYPE_BES.mSignedAttributeList,
            TYPE_BES.mCounterSignatureSignedAttributeList,
            new Asn1ObjectIdentifier[]
                {

                    AttributeOIDs.id_aa_signatureTimeStampToken,
                    AttributeOIDs.id_aa_ets_certificateRefs,
                    AttributeOIDs.id_aa_ets_revocationRefs,
                    AttributeOIDs.id_aa_ets_revocationValues,
                    AttributeOIDs.id_aa_ets_certValues,
                    AttributeOIDs.id_aa_ets_archiveTimestampV2
                }
            );

        public static readonly ESignatureType TYPE_ESA = new ESignatureType
        (
            TYPE_BES.mSignedAttributeList,
            TYPE_BES.mCounterSignatureSignedAttributeList,
            new Asn1ObjectIdentifier[]
			{
                /*
				AttributeOIDs.id_aa_signatureTimeStampToken,
				AttributeOIDs.id_aa_ets_certificateRefs,
				AttributeOIDs.id_aa_ets_revocationRefs,
				AttributeOIDs.id_aa_ets_revocationValues,
				AttributeOIDs.id_aa_ets_certValues,	
                 */
                //id_aa_ets_archiveTimestampV2 veya id_aa_ets_archiveTimestamp olabilir
				//look SignatureParser.java
				//AttributeOIDs.id_aa_ets_archiveTimestampV2

			}
          );

        public static readonly Dictionary<Asn1ObjectIdentifier, Type> ATTRIBUTE_TABLO = new Dictionary<Asn1ObjectIdentifier, Type>();

        private readonly Asn1ObjectIdentifier[] mSignedAttributeList;
        private readonly Asn1ObjectIdentifier[] mCounterSignatureSignedAttributeList;
        private readonly Asn1ObjectIdentifier[] mUnSignedAttributeList;
        /**
             * Create ESignatureType from signed attribute list,counter signature signed attribute list and unsigned attribute list
              * @param aSignedList signed attribute list
              * @param aCSSignedList counter signature signed attribute list
              * @param aUnSignedList unsigned attribute list
              */
        ESignatureType(Asn1ObjectIdentifier[] aSignedList, Asn1ObjectIdentifier[] aCSSignedList, Asn1ObjectIdentifier[] aUnSignedList)
        {
            mSignedAttributeList = aSignedList;
            mCounterSignatureSignedAttributeList = aCSSignedList;
            mUnSignedAttributeList = aUnSignedList;
        }
        /**
         * Returns mandatory signed attribute of signature
         * @param aIsCounterSignature
         * @return
         */
        public Asn1ObjectIdentifier[] getMandatorySignedAttributes(bool aIsCounterSignature)
        {
            if (aIsCounterSignature)
                return mCounterSignatureSignedAttributeList;

            return mSignedAttributeList;
        }
        /**
         * Returns mandatory unsigned attribute of signature
         * @return
         */
        public Asn1ObjectIdentifier[] getMandatoryUnSignedAttributes()
        {
            return mUnSignedAttributeList;
        }
        /**
         * Create signer from e-signature type and base signed data
          * @param aType ESignature Type
          * @param aSignedData BaseSignedData
          */
        public static Signer createSigner(ESignatureType aType, BaseSignedData aSignedData)
        {
            if (aType == TYPE_BES)
                return new BES(aSignedData);
            else if (aType == TYPE_EPES)
                return new EPES(aSignedData);
            else if (aType == TYPE_EST)
                return new EST(aSignedData);
            else if (aType == TYPE_ESC)
                return new ESC(aSignedData);
            else if (aType == TYPE_ESX_Type1)
                return new ESX1(aSignedData);
            else if (aType == TYPE_ESX_Type2)
                return new ESX2(aSignedData);
            else if (aType == TYPE_ESXLong)
                return new ESXLong(aSignedData);
            else if (aType == TYPE_ESXLong_Type1)
                return new ESXLong1(aSignedData);
            else if (aType == TYPE_ESXLong_Type2)
                return new ESXLong2(aSignedData);
            else if (aType == TYPE_ESA)
                return new ESA(aSignedData);
            else if (aType == TYPE_ESAv2)
                return new ESAv2(aSignedData);
            return null;
        }
        /**
         * Create signer from e-signature type, base signed data and signer info
          * @param aType ESignature Type
          * @param aSignedData BaseSignedData
          * @param aSignerInfo ESignerInfo
          */
        public static Signer createSigner(ESignatureType aType, BaseSignedData aSignedData, ESignerInfo aSignerInfo)
        {
            if (aType == TYPE_BES)
                return new BES(aSignedData, aSignerInfo);
            else if (aType == TYPE_EPES)
                return new EPES(aSignedData, aSignerInfo);
            else if (aType == TYPE_EST)
                return new EST(aSignedData, aSignerInfo);
            else if (aType == TYPE_ESC)
                return new ESC(aSignedData, aSignerInfo);
            else if (aType == TYPE_ESX_Type1)
                return new ESX1(aSignedData, aSignerInfo);
            else if (aType == TYPE_ESX_Type2)
                return new ESX2(aSignedData, aSignerInfo);
            else if (aType == TYPE_ESXLong)
                return new ESXLong(aSignedData, aSignerInfo);
            else if (aType == TYPE_ESXLong_Type1)
                return new ESXLong1(aSignedData, aSignerInfo);
            else if (aType == TYPE_ESXLong_Type2)
                return new ESXLong2(aSignedData, aSignerInfo);
            else if (aType == TYPE_ESA)
                return new ESA(aSignedData, aSignerInfo);
            else if (aType == TYPE_ESAv2)
                return new ESAv2(aSignedData, aSignerInfo);
            return null;
        }


        //public static Type getSignatureClass(ESignatureType aTip)
        //{
        //    if (aTip == TYPE_BES)
        //        return typeof(BES);
        //    else if (aTip == TYPE_EPES)
        //        return typeof(EPES);
        //    else if (aTip == TYPE_EST)
        //        return typeof(EST);
        //    else if (aTip == TYPE_ESC)
        //        return typeof(ESC);
        //    else if (aTip == TYPE_ESX_Type1)
        //        return typeof(ESX1);
        //    else if (aTip == TYPE_ESX_Type2)
        //        return typeof(ESX2);
        //    else if (aTip == TYPE_ESXLong)
        //        return typeof(ESXLong);
        //    else if (aTip == TYPE_ESXLong_Type1)
        //        return typeof(ESXLong1);
        //    else if (aTip == TYPE_ESXLong_Type2)
        //        return typeof(ESXLong2);
        //    else if (aTip == TYPE_ESA)
        //        return typeof(ESA);
        //    return null;

        //}


        //todo Buradaki Template yerine dogrudan Signer'lı metod tanimlamasi yapsak ne olur?
        //@SuppressWarnings("unchecked")
        //public static Signer createSigner(ESignatureType aType, BaseSignedData aSignedData)
        //{

        //    Type c = ESignatureType.getSignatureClass(aType);

        //    Type[] paramClasses = new Type[] { typeof(BaseSignedData) };

        //    Object[] paramValues = new Object[] { aSignedData };

        //    Signer signer;
        //    try
        //    {
        //        //t = (T) c.getDeclaredConstructor(paramClasses).newInstance(paramValues);
        //        signer = (Signer)Activator.CreateInstance(c, paramValues);
        //    }
        //    catch (Exception e)
        //    {
        //        throw new CMSSignatureException("", e);
        //    }

        //    return signer;

        //}


        //todo Buradaki Template yerine dogrudan Signer'lı metod tanimlamasi yapsak ne olur?
        //@SuppressWarnings("unchecked")
        //public static Signer createSigner(ESignatureType aType, BaseSignedData aSignedData, ESignerInfo aSignerInfo)
        //{

        //    Type c = ESignatureType.getSignatureClass(aType);

        //    Type[] paramClasses = new Type[] { typeof(BaseSignedData), typeof(ESignerInfo) };

        //    Object[] paramValues = new Object[] { aSignedData, aSignerInfo };

        //    Signer signer;
        //    try
        //    {
        //        //t = (T) c.getDeclaredConstructor(paramClasses).newInstance(paramValues);
        //        signer = (Signer)Activator.CreateInstance(c, paramValues);
        //    }
        //    catch (Exception e)
        //    {
        //        throw new CMSSignatureException("", e);
        //    }

        //    return signer;
        //}
        //todo values ve name medoları yazılacak!!
        public static ESignatureType[] values()
        {

            Type declaringType = MethodBase.GetCurrentMethod().DeclaringType;
            //String strs = MethodBase.GetCurrentMethod().DeclaringType.Name;
            List<ESignatureType> values = new List<ESignatureType>();
            FieldInfo[] fInfos = declaringType.GetFields();
            //Object obj = fInfos[0].GetValue(null);
            foreach (FieldInfo fInfo in fInfos)
            {
                if (fInfo.FieldType == declaringType)
                {
                    values.Add((ESignatureType)fInfo.GetValue(null));
                }
            }
            return values.ToArray();

            ////Object obj = pInfos[0].GetValue(new Object());
            //throw new NotImplementedException("values() HENüz implement edilmedi");
        }
        public String name()
        {
            Type declaringType = MethodBase.GetCurrentMethod().DeclaringType;          
            FieldInfo[] fInfos = declaringType.GetFields();
            foreach (FieldInfo fInfo in fInfos)
            {
                if (fInfo.FieldType == declaringType)
                {
                    if (((ESignatureType)fInfo.GetValue(null)).Equals(this))
                    {
                        return fInfo.Name;
                    }
                }
            }
            return null;
            //throw new NotImplementedException("name() HENÜZ İMPLEMENT EDİLMEDİ");
        }

    }
}

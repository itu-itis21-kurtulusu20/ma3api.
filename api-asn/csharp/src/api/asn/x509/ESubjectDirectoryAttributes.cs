using System;
using System.Reflection;
using System.Text;
using Com.Objsys.Asn1.Runtime;
using log4net;
using tr.gov.tubitak.uekae.esya.api.asn.cms;
using tr.gov.tubitak.uekae.esya.api.common.bundle;
using tr.gov.tubitak.uekae.esya.asn.util;
using tr.gov.tubitak.uekae.esya.asn.x509;
using Attribute = tr.gov.tubitak.uekae.esya.asn.x509.Attribute;
namespace tr.gov.tubitak.uekae.esya.api.asn.x509
{
    public class ESubjectDirectoryAttributes : BaseASNWrapper<SubjectDirectoryAttributes>
    {
        private static readonly ILog logger = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);

        public ESubjectDirectoryAttributes(SubjectDirectoryAttributes aObject)
            : base(aObject)
        {
        }

        public ESubjectDirectoryAttributes(EAttribute[] attributes)
            : base(new SubjectDirectoryAttributes())
        {
            mObject.elements = unwrapArray<Attribute, EAttribute>(attributes);
        }

        public override String ToString()
        {

            StringBuilder result = new StringBuilder();
            Attribute[] attributes = mObject.elements;
            if (attributes != null)
            {
                foreach (Attribute anAttrlar in attributes)
                {
                    result.Append(_attributeCikar(anAttrlar)).Append("\n");
                }
            }
            return result.ToString();
        }

        public Asn1OpenType[] getAttributeValue(Asn1ObjectIdentifier aOID)
        {
            Attribute[] attrAr = this.getObject().elements;
            for (int i = 0; i < attrAr.Length; i++)
                if (attrAr[i].type.Equals(aOID))
                    return attrAr[i].values.elements;
            return null;
        }

        public ERoleSyntax getRoleSyntax()
        {
            try
            {
                Asn1OpenType[] attributeValues = getAttributeValue(UtilSubjectDirectoryAttr.oid_at_role);
                if (attributeValues == null)
                    return null;
                return new ERoleSyntax(attributeValues[0].mValue);
            }
            catch (Exception ex)
            {
                logger.Error("Failed to Read RoleSyntax.", ex);
                return null;
            }
        }

        private String _attributeCikar(Attribute aAttribute)
        {
            Asn1ObjectIdentifier tip = aAttribute.type;
            Asn1OpenType[] degerler = aAttribute.values.elements;
            try
            {
                if (tip.Equals(UtilSubjectDirectoryAttr.oid_pda_countryOfCitizenship))
                {
                    return Resource.message(Resource.COUNTRYCITIZENSHIP) + " = " + _printableToString(degerler);
                }
                else if (tip.Equals(UtilSubjectDirectoryAttr.oid_pda_countryOfResidence))
                {
                    return Resource.message(Resource.COUNTRYRESIDENCE) + " = " + _printableToString(degerler);
                }
                else if (tip.Equals(UtilSubjectDirectoryAttr.oid_pda_dateOfBirth))
                {
                    return Resource.message(Resource.DATEOFBIRTH) + " = " + _dateToString(degerler);
                }
                else if (tip.Equals(UtilSubjectDirectoryAttr.oid_pda_gender))
                {
                    return Resource.message(Resource.GENDER) + " = " + _printableToString(degerler);
                }
                else if (tip.Equals(UtilSubjectDirectoryAttr.oid_pda_placeOfBirth))
                {
                    return Resource.message(Resource.PLACEOFBIRTH) + " = " + _directoryToString(degerler);
                }
                else if (tip.Equals(UtilSubjectDirectoryAttr.oid_at_role))
                {
                    return Resource.message(Resource.ROLE) + " = " + _roleToString(degerler);
                }/*else if(tip.equals(UtilSubjectDirectoryAttr.oid_win_upn))
       	 {
       		 
       	 }*/
            }
            catch (Exception aEx)
            {
                logger.Warn("Attribute degeri alınırken hata oluştu", aEx);
                return "";
            }

            return "";
        }

        private String _printableToString(Asn1OpenType[] aDegerler)
        {
            String attr = "";
            for (int i = 0; i < aDegerler.Length; i++)
            {
                Asn1OpenType value = aDegerler[i];
                Asn1DerEncodeBuffer encBuf = new Asn1DerEncodeBuffer();
                value.Encode(encBuf);
                Asn1PrintableString val = new Asn1PrintableString();
                Asn1DerDecodeBuffer decBuf = new Asn1DerDecodeBuffer(encBuf.MsgCopy);
                val.Decode(decBuf);
                attr += val.mValue;
            }
            return attr;
        }

        private String _dateToString(Asn1OpenType[] aDegerler)
        {
            String attr = "";
            for (int i = 0; i < aDegerler.Length; i++)
            {
                Asn1OpenType value = aDegerler[i];
                Asn1DerEncodeBuffer encBuf = new Asn1DerEncodeBuffer();
                value.Encode(encBuf);
                Asn1GeneralizedTime val = new Asn1GeneralizedTime();
                Asn1DerDecodeBuffer decBuf = new Asn1DerDecodeBuffer(encBuf.MsgCopy);
                val.Decode(decBuf);
                int year = val.Year;  //for Asn error
                attr += val.GetTime().ToLocalTime().ToString("d MMM yyyy");//val.ToString("d MMM yyyy");//new SimpleDateFormat("dd.MM.yyyy").format(val.getTime().getTime());
            }
            return attr;
        }

        private String _directoryToString(Asn1OpenType[] aDegerler)
        {
            String attr = "";
            for (int i = 0; i < aDegerler.Length; i++)
            {
                Asn1OpenType value = aDegerler[i];
                Asn1DerEncodeBuffer encBuf = new Asn1DerEncodeBuffer();
                value.Encode(encBuf);
                DirectoryString val = new DirectoryString();
                Asn1DerDecodeBuffer decBuf = new Asn1DerDecodeBuffer(encBuf.MsgCopy);
                val.Decode(decBuf);
                switch (val.ChoiceID)
                {
                    case DirectoryString._UTF8STRING:
                        {
                            attr += ((Asn1UTF8String)val.GetElement()).mValue;
                            break;
                        }
                    case DirectoryString._PRINTABLESTRING:
                        {
                            attr += ((Asn1PrintableString)val.GetElement()).mValue;
                            break;
                        }
                    case DirectoryString._TELETEXSTRING:
                        {
                            attr += ((Asn1T61String)val.GetElement()).mValue;
                            break;
                        }
                    case DirectoryString._UNIVERSALSTRING:
                        {
                            attr += ((Asn1UniversalString)val.GetElement()).mValue;
                            break;
                        }
                    case DirectoryString._BMPSTRING:
                        {
                            attr += ((Asn1BMPString)val.GetElement()).mValue;
                            break;
                        }
                    default:
                        break;
                }
            }
            return attr;
        }

        private String _roleToString(Asn1OpenType[] aDegerler)
        {
            String attr = "";
            for (int i = 0; i < aDegerler.Length; i++)
            {
                Asn1OpenType value = aDegerler[i];
                Asn1DerEncodeBuffer encBuf = new Asn1DerEncodeBuffer();
                value.Encode(encBuf);
                RoleSyntax val = new RoleSyntax();
                Asn1DerDecodeBuffer decBuf = new Asn1DerDecodeBuffer(encBuf.MsgCopy);
                val.Decode(decBuf);
                GeneralName rolName = val.roleName;
                if (rolName.ChoiceID == GeneralName._REGISTEREDID)
                {
                    int[] oid = ((Asn1ObjectIdentifier)rolName.GetElement()).mValue;
                    attr += "{";
                    for (int j = 0; j < oid.Length; j++)
                    {
                        attr += oid[j] + " ";
                    }
                    attr += "}";
                }

            }
            return attr;
        }

        public EExtension toExtension(bool aCritic)
        {
            return new EExtension(EExtensions.oid_ce_subjectDirectoryAttributes, aCritic, getBytes());
        }

    }
}

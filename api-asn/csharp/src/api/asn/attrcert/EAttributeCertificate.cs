using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.api.asn.cms;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.common.util;
using tr.gov.tubitak.uekae.esya.asn.attrcert;
using tr.gov.tubitak.uekae.esya.asn.x509;
using Attribute = tr.gov.tubitak.uekae.esya.asn.x509.Attribute;

namespace tr.gov.tubitak.uekae.esya.api.asn.attrcert
{
    public class EAttributeCertificate : BaseASNWrapper<AttributeCertificate>
    {

        public EAttributeCertificate(AttributeCertificate aObject)
            : base(aObject)
        {

        }

        public EAttributeCertificate()
            : base(new AttributeCertificate())
        {

            mObject.acinfo = new AttributeCertificateInfo();
            mObject.acinfo.version = new AttCertVersion(AttCertVersion.v2);
        }

        public EAttributeCertificate(byte[] aBytes)
            : base(aBytes, new AttributeCertificate())
        {

        }

        public void setSignature(byte[] signature)
        {
            this.mObject.signatureValue = new Asn1BitString(signature.Length << 3, signature);
        }

        public void setAlgorithm(EAlgorithmIdentifier algorithmIdentifier)
        {
            this.mObject.signatureAlgorithm = algorithmIdentifier.getObject();
            this.mObject.acinfo.signature = algorithmIdentifier.getObject();
        }

        public byte[] getSignature()
        {
            return this.mObject.signatureValue.mValue;
        }

        public EAlgorithmIdentifier getAlgorithmIdentifier()
        {
            return new EAlgorithmIdentifier(this.mObject.signatureAlgorithm);
        }

        public EHolder getHolder()
        {
            return new EHolder(mObject.acinfo.holder);
        }

        public void setHolder(EHolder holder)
        {
            this.mObject.acinfo.holder = holder.getObject();
        }

        public EGeneralNames getIssuer()
        {
            if (mObject.acinfo.issuer == null)
                return null;
            if (mObject.acinfo.issuer.ChoiceID != AttCertIssuer._V2FORM)
                throw new SystemException("AttCertIssuer must be V2 Form:" + mObject.acinfo.issuer.ChoiceID);
            V2Form v2Form = (V2Form)mObject.acinfo.issuer.GetElement();
            if (v2Form.issuerName == null)
                return null;
            return new EGeneralNames(v2Form.issuerName);
        }

        public void setIssuer(EGeneralNames issuer)
        {
            AttCertIssuer attCertIssuer = new AttCertIssuer();
            V2Form v2Form = new V2Form();
            v2Form.issuerName = issuer.getObject();
            attCertIssuer.Set_v2Form(v2Form);
            this.mObject.acinfo.issuer = attCertIssuer;
        }

        public BigInteger getSerialNumber()
        {
            return mObject.acinfo.serialNumber.mValue;
        }

        public void setSerialNumber(BigInteger serialNumber)
        {
            this.mObject.acinfo.serialNumber = new Asn1BigInteger(serialNumber);
        }

        /**
         @return Certificate serial number in hexadecimal String form
         */
        public String getSerialNumberHex()
        {
            //return StringUtil.ToString(getSerialNumber().GetData());
            return BitConverter.ToString(getSerialNumber().GetData()).Replace("-", String.Empty);
        }

        public DateTime? getAttrCertValidityNotBefore()
        {
            int year = this.mObject.acinfo.attrCertValidityPeriod.notBeforeTime.Year;   //for Asn error
            return this.mObject.acinfo.attrCertValidityPeriod.notBeforeTime.GetTime();
        }

        public DateTime? getAttrCertValidityNotAfter()
        {
            int year = this.mObject.acinfo.attrCertValidityPeriod.notBeforeTime.Year;  //for Asn error
            return this.mObject.acinfo.attrCertValidityPeriod.notAfterTime.GetTime();
        }

        public void setAttrCertValidityNotBefore(DateTime? notBefore)
        {
            Asn1GeneralizedTime asn1GeneralizedTime = new Asn1GeneralizedTime();
            asn1GeneralizedTime.SetTime(notBefore.Value);
            asn1GeneralizedTime.Fraction = "";
            if (this.mObject.acinfo.attrCertValidityPeriod == null)
                this.mObject.acinfo.attrCertValidityPeriod = new AttCertValidityPeriod();
            this.mObject.acinfo.attrCertValidityPeriod.notBeforeTime = asn1GeneralizedTime;
        }

        public void setAttrCertValidityNotAfter(DateTime? notAfter)
        {
            Asn1GeneralizedTime asn1GeneralizedTime = new Asn1GeneralizedTime();
            asn1GeneralizedTime.SetTime(notAfter.Value);
            asn1GeneralizedTime.Fraction = "";
            if (this.mObject.acinfo.attrCertValidityPeriod == null)
                this.mObject.acinfo.attrCertValidityPeriod = new AttCertValidityPeriod();
            this.mObject.acinfo.attrCertValidityPeriod.notAfterTime = asn1GeneralizedTime;
        }

        public EAttribute[] getAttributes()
        {
            return wrapArray<EAttribute, Attribute>(mObject.acinfo.attributes.elements, typeof(EAttribute));
        }

        public void setAttributes(EAttribute[] attributes)
        {
            this.mObject.acinfo.attributes = new _SeqOfAttribute(unwrapArray<Attribute, EAttribute>(attributes));
        }

        public EExtension[] getExtensions()
        {
            return wrapArray<EExtension, Extension>(mObject.acinfo.extensions.elements, typeof(EExtension));
        }

        public void setExtensions(EExtension[] extensions)
        {
            this.mObject.acinfo.extensions = new Extensions(unwrapArray<Extension, EExtension>(extensions));
        }

        public byte[] getAttributeCertificateInfoBytes()
        {
            return new BaseASNWrapper<AttributeCertificateInfo>(mObject.acinfo).getBytes();
            // A: isnt it wiser?
            // B: test it first Dude..
        }
    }
}

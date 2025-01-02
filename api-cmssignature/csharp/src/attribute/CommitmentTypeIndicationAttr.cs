using System.Reflection;
using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.api.asn.cms;
using tr.gov.tubitak.uekae.esya.api.cmssignature.bundle;
using tr.gov.tubitak.uekae.esya.api.common;
using tr.gov.tubitak.uekae.esya.asn.cms;


//todo Annotation!
//@ApiClass

namespace tr.gov.tubitak.uekae.esya.api.cmssignature.attribute
{
    /**
     * There may be situations where a signer wants to explicitly indicate to a verifier that by signing the data, it illustrates a
     * type of commitment on behalf of the signer. The commitment-type-indication attribute conveys such
     * information. The commitment-type-indication attribute shall be a signed attribute.(etsi 101733v010801 5.11.1)
     * @author aslihan.kubilay
     *
     */
    public class CommitmentTypeIndicationAttr : AttributeValue
    {
        public static readonly Asn1ObjectIdentifier OID = AttributeOIDs.id_aa_ets_commitmentType;

        private readonly CommitmentType mTip;

        /**
            * Create CommitmentTypeIndicationAttr with commitment type
             * @param aTip CommitmentType
             * @throws CMSSignatureException
             */
        public CommitmentTypeIndicationAttr(CommitmentType aTip)
        {
            //super();
            mTip = aTip;
            if (mTip == null)
                throw new NullParameterException("Commitment type can not be null");
        }
        /**
        * Set Commitment Type Indication value
        */
        public override void setValue()
        {
            Asn1ObjectIdentifier oid = mTip.getOid();
            CommitmentTypeIndication cti = new CommitmentTypeIndication(oid);
            _setValue(cti);
        }
        /**
         * Checks whether attribute is signed or not.
         * @return True 
         */ 
        public override bool isSigned()
        {
            return true;
        }
        /**
         * Returns AttributeOID of Commitment Type Indication attribute
         * @return
         */
        public override Asn1ObjectIdentifier getAttributeOID()
        {
            return OID;
        }
        /**
         * Returns  commitment type
         * @param aAttribute EAttribute
         * @return 
         * @throws ESYAException
         */
        public static CommitmentType toCommitmentType(EAttribute aAttribute)
        {
            ECommitmentTypeIndication indication = new ECommitmentTypeIndication(aAttribute.getValue(0));
            //CommitmentType [] values = CommitmentType.values();

            FieldInfo[] values = typeof (CommitmentType).GetFields(BindingFlags.Public | BindingFlags.Static);

            foreach (FieldInfo fieldInfo in values)
            {
                if (fieldInfo.FieldType == typeof (CommitmentType))
                {
                    CommitmentType value = (CommitmentType) fieldInfo.GetValue(null);
                    if (value.getOid().Equals(indication.getOid()))
                        return value;
                }
            }
            throw new ESYAException(Msg.getMsg(Msg.UNKNOWN_COMMITMENT_TYPE));
        }
    }
}
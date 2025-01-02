using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.asn.ocsp;
using tr.gov.tubitak.uekae.esya.asn.x509;

namespace tr.gov.tubitak.uekae.esya.api.asn.ocsp
{
    public class EResponderID : BaseASNWrapper<ResponderID>
    {
        public const  byte _BYNAME = 1;
        public const  byte _BYKEY = 2;

    public EResponderID(ResponderID aObject) 
    :base(aObject)
    {
    }

    public EResponderID(byte[] aBytes)  
         : base(aBytes, new ResponderID())
    {
    }

    public EResponderID(EName name)  
           : base(new ResponderID())
    {
        mObject.Set_byName(name.getObject());
    }

    public EResponderID(Asn1OctetString name) 
           : base(new ResponderID())
    {
        mObject.Set_byKey(name);
    }

    public int getType(){
        return mObject.ChoiceID;
    }

    public EName getResponderIdByName(){
        return new EName((Name)mObject.GetElement());
    }

    public byte[] getResponderIdByKey(){
        return ((Asn1OctetString)mObject.GetElement()).mValue;
    }

    }
}

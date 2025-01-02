namespace tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ops
{
    class SafeSignOps : PKCS11Ops
    {
        public SafeSignOps()
            : base(CardType.SAFESIGN)
        {
        }
    }
}

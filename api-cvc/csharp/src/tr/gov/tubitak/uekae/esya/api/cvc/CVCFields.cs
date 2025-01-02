using tr.gov.tubitak.uekae.esya.api.asn.cvc;

namespace tr.gov.tubitak.uekae.esya.api.cvc
{
    public class CVCFields
    {
        private ECpi _cpi;
        private ECar _car;
        private EChr _chr;
        private ECha _cha;
        private EAlgId _oid;
        private ERsaPuK _rsaPuK = new ERsaPuK();
        private ECxd _cxd;
        private ECed _ced;


        public ECpi get_cpi()
        {
            return _cpi;
        }

        public void set_cpi(ECpi _cpi)
        {
            this._cpi = _cpi;
        }

        public ECar get_car()
        {
            return _car;
        }

        public void set_car(ECar _car)
        {
            this._car = _car;
        }

        public EChr get_chr()
        {
            return _chr;
        }

        public void set_chr(EChr _chr)
        {
            this._chr = _chr;
        }

        public EAlgId get_oid()
        {
            return _oid;
        }

        public void set_oid(EAlgId _oid)
        {
            this._oid = _oid;
        }

        public ECha get_cha()
        {
            return _cha;
        }

        public void set_cha(ECha _cha)
        {
            this._cha = _cha;
        }

        public ERsaPuK get_rsaPuK()
        {
            return _rsaPuK;
        }

        public void set_rsaPuK(ERsaPuK _rsaPuK)
        {
            this._rsaPuK = _rsaPuK;
        }

        public ECxd get_cxd()
        {
            return _cxd;
        }

        public void set_cxd(ECxd _cxd)
        {
            this._cxd = _cxd;
        }

        public ECed get_ced()
        {
            return _ced;
        }

        public void set_ced(ECed _ced)
        {
            this._ced = _ced;
        }

        public void setModulus(byte[] modulus)
        {
            this._rsaPuK.setModulus(modulus);
        }

        public void setExponent(byte[] exponent)
        {
            this._rsaPuK.setExponent(exponent);
        }
    }
}

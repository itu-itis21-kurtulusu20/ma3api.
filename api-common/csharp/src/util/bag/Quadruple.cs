
//todo Annotation!
//@ApiClass
namespace tr.gov.tubitak.uekae.esya.api.common.util.bag
{
    public class Quadruple<T1, T2, T3, T4>
    {
        private readonly T1 _mObj1;
        private readonly T2 _mObj2;
        private readonly T3 _mObj3;
        private readonly T4 _mObj4;

        public Quadruple(T1 aNesne1, T2 aNesne2, T3 aNesne3, T4 aNesne4)
        {
            _mObj1 = aNesne1;
            _mObj2 = aNesne2;
            _mObj3 = aNesne3;
            _mObj4 = aNesne4;
        }

        public T1 first()
        {
            return _mObj1;
        }

        public T2 second()
        {
            return _mObj2;
        }

        public T3 third()
        {
            return _mObj3;
        }

        public T4 fourth()
        {
            return _mObj4;
        }

    }
}

//todo Annotation!
//@ApiClass
namespace tr.gov.tubitak.uekae.esya.api.common.util.bag
{
    public class Triple<T1, T2, T3>
    {
        private T1 mObj1;
        private T2 mObj2;
        private T3 mObj3;

        public Triple()
        {
        }

        public Triple(T1 aNesne1, T2 aNesne2, T3 aNesne3)
        {
            mObj1 = aNesne1;
            mObj2 = aNesne2;
            mObj3 = aNesne3;
        }

        public T1 first()
        {
            return mObj1;
        }

        public T2 second()
        {
            return mObj2;
        }

        public T3 third()
        {
            return mObj3;
        }

        public T1 getmObj1()
        {
            return mObj1;
        }

        public void setmObj1(T1 aNesne1)
        {
            mObj1 = aNesne1;
        }

        public T2 getmObj2()
        {
            return mObj2;
        }

        public void setmObj2(T2 aNesne2)
        {
            mObj2 = aNesne2;
        }

        public T3 getmObj3()
        {
            return mObj3;
        }

        public void setmObj3(T3 aNesne3)
        {
            mObj3 = aNesne3;
        }
    }
}

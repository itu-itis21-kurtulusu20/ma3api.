//todo Annotation!
//@ApiClass
namespace tr.gov.tubitak.uekae.esya.api.common.util.bag
{
    public class Pair<T1, T2>
    {
        private T1 mObj1;
        private T2 mObj2;

        public Pair()
        {
        }

        public Pair(T1 aNesne1, T2 aNesne2)
        {
            mObj1 = aNesne1;
            mObj2 = aNesne2;
        }

        public T1 first()
        {
            return mObj1;
        }

        public T2 second()
        {
            return mObj2;
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

        public override string ToString()
        {
            return "[" + mObj1 + "," + mObj2 + "]";
        }
    }
}

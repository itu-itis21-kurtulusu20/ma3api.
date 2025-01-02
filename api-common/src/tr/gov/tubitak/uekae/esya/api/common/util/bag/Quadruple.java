package tr.gov.tubitak.uekae.esya.api.common.util.bag;


import java.io.Serializable;

/**
 * @author ayetgin
 */

public class Quadruple<T1, T2, T3, T4>  implements Serializable
{

    private T1 mNesne1;
    private T2 mNesne2;
    private T3 mNesne3;
    private T4 mNesne4;

    public Quadruple(T1 aNesne1, T2 aNesne2, T3 aNesne3, T4 aNesne4)
    {
        mNesne1 = aNesne1;
        mNesne2 = aNesne2;
        mNesne3 = aNesne3;
        mNesne4 = aNesne4;
    }

    public T1 birinci()
    {
        return mNesne1;
    }

    public T2 ikinci()
    {
        return mNesne2;
    }

    public T3 ucuncu()
    {
        return mNesne3;
    }

    public T4 dorduncu()
    {
        return mNesne4;
    }

}

package tr.gov.tubitak.uekae.esya.api.common.util.bag;


import java.io.Serializable;

/**
 * @author ayetgin
 */

public class Triple<T1, T2, T3> implements Serializable
{
    private T1 mNesne1;
    private T2 mNesne2;
    private T3 mNesne3;

    public Triple()
    {
    }

    public Triple(T1 aNesne1, T2 aNesne2, T3 aNesne3)
    {
        mNesne1 = aNesne1;
        mNesne2 = aNesne2;
        mNesne3 = aNesne3;
    }

    public T1 birinci(){
        return mNesne1;
    }

    public T2 ikinci(){
        return mNesne2;
    }

    public T3 ucuncu(){
        return mNesne3;
    }

    public T1 getmNesne1()
    {
        return mNesne1;
    }

    public void setmNesne1(T1 aNesne1)
    {
        mNesne1 = aNesne1;
    }

    public T2 getmNesne2()
    {
        return mNesne2;
    }

    public void setmNesne2(T2 aNesne2)
    {
        mNesne2 = aNesne2;
    }

    public T3 getmNesne3()
    {
        return mNesne3;
    }

    public void setmNesne3(T3 aNesne3)
    {
        mNesne3 = aNesne3;
    }
}

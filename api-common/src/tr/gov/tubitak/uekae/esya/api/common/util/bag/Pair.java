package tr.gov.tubitak.uekae.esya.api.common.util.bag;

import java.io.Serializable;

/**
 * @author ayetgin
 */

public class Pair<T1, T2> implements Serializable
{

    private  T1 mObject1;
    private  T2 mObject2;

    public Pair()
    {
    }

    public Pair(T1 aObj1, T2 aObj2)
    {
        mObject1 = aObj1;
        mObject2 = aObj2;
    }

    public T1 first()
    {
        return mObject1;
    }

    public T2 second()
    {
        return mObject2;
    }

    public T1 getObject1()
    {
        return mObject1;
    }

    public void setObject1(T1 aObj1)
    {
        mObject1 = aObj1;
    }

    public T2 getObject2()
    {
        return mObject2;
    }

    public void setObject2(T2 aObj2)
    {
        mObject2 = aObj2;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Pair pair = (Pair) o;

        if (mObject1 != null ? !mObject1.equals(pair.mObject1) : pair.mObject1 != null) return false;
        if (mObject2 != null ? !mObject2.equals(pair.mObject2) : pair.mObject2 != null) return false;

        return true;
    }

    @Override
    public int hashCode()
    {
        int result = mObject1 != null ? mObject1.hashCode() : 0;
        result = 31 * result + (mObject2 != null ? mObject2.hashCode() : 0);
        return result;
    }

    public String toString()
    {
        return "["+ mObject1 +","+ mObject2 +"]";
    }
}

package tr.gov.tubitak.uekae.esya.api.common.util;

import tr.gov.tubitak.uekae.esya.api.common.ESYAException;

/**
 * @author ayetgin
 */
public interface ItemSource<T>
{
    boolean open();
    boolean close();
    boolean reset();

    T nextItem() throws ESYAException;
    boolean  atEnd();

}

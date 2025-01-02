package tr.gov.tubitak.uekae.esya.api.xmlsignature.util;

import java.util.UUID;

/**
 * @author ayetgin
 */
public class UniqueIdGenerator extends IdGenerator
{


    @Override
    public String uret(String aIdType)
    {
        return aIdType + "-Id-" +UUID.randomUUID();
    }

    @Override
    public void update(String id)
    {
    }

    public static void main(String[] args)
    {
        String id = new UniqueIdGenerator().uret("Signature");
        System.out.println(id);
    }
}

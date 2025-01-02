package tr.gov.tubitak.uekae.esya.api.smartcard.config;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ayetgin
 */
public class CardTypeConfig
{
    private String name;
    private String lib;
    private String lib32;
    private String lib64;
    private List<String> atrs = new ArrayList<String>();

    public CardTypeConfig()
    {
    }

    public CardTypeConfig(String aName, String aLib, String aLib32, String aLib64, List<String> aAtrs)
    {
        name = aName;
        lib = aLib;
        lib32 = aLib32;
        lib64 = aLib64;
        if (aAtrs!=null)
            atrs = aAtrs;
    }

    public String getName()
    {
        return name;
    }

    public String getLib()
    {
        return lib;
    }

    public String getLib32()
    {
        return lib32;
    }

    public String getLib64()
    {
        return lib64;
    }

    public List<String> getAtrs()
    {
        return atrs;
    }

    @Override
    public String toString()
    {
        StringBuilder buffer = new StringBuilder()
                .append("[ Card type: ").append(name).append("\n")
                .append("lib : ").append(lib).append(", lib32 : ").append(lib32).append(", lib64 : ").append(lib64).append("\n");
        for (String atr : atrs){
            buffer.append("  atr : ").append(atr).append("\n");
        }
        buffer.append("]\n");
        return buffer.toString();
    }
}

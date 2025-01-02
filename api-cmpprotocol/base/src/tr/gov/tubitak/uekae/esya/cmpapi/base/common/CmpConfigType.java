package tr.gov.tubitak.uekae.esya.cmpapi.base.common;


/**
 * <b>Author</b>    : zeldal.ozdemir <br>
 * <b>Project</b>   : MA3   <br>
 * <b>Copyright</b> : TUBITAK Copyright (c) 2006-2011 <br>
 * <b>Date</b>: Dec 21, 2010 - 1:49:01 PM <p>
 * <b>Description</b>: <br>
 * Available Configuration Types for CMP Api, it's just definition for naming
 */

public enum CmpConfigType implements IConfigType {
    ESYA(new ConfigTypeEsya()),
    TURKCELL(new ConfigTypeTurkcell()),
    EKK(new ConfigTypeEKK());

    private IConfigType configTypeDelegate;

    CmpConfigType(IConfigType configTypeDelegate) {
        this.configTypeDelegate = configTypeDelegate;
    }

    private IConfigType getConfigType() {
        return configTypeDelegate;
    }

    @Override
    public String toString() {
        return configTypeDelegate.getTypeName();
    }

    public static CmpConfigType getConfigType(String configTypeName) {
        for (CmpConfigType configType : CmpConfigType.values()) {
            if (configType.getConfigType().getTypeName().equalsIgnoreCase(configTypeName))
                return configType;
        }
        return null;
    }

    public String getTypeName() {
        return configTypeDelegate.getTypeName();
    }
}

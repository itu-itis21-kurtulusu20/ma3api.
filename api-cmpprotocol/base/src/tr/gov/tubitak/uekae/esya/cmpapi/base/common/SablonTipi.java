package tr.gov.tubitak.uekae.esya.cmpapi.base.common;


/**
 * <b>Author</b>    : zeldal.ozdemir <br>
 * <b>Project</b>   : MA3   <br>
 * <b>Copyright</b> : TUBITAK Copyright (c) 2006-2011 <br>
 * <b>Date</b>: Nov 5, 2010 - 9:25:18 AM <p>
 * <b>Description</b>: <br>
 * @deprecated dont use aynmore, will be removed in future releases
 */

@Deprecated()
public enum SablonTipi {
        ANAHTAR_SERVERDA_URETILECEK_IMZALAMA(1),
        ANAHTAR_SERVERDA_URETILECEK_SIFRELEME(2),
        ANAHTAR_CLIENTTA_URETILECEK_IMZALAMA(3),
        ANAHTAR_CLIENTTA_URETILECEK_SIFRELEME(4)
    ;
    private int sablonTipiInt;

    SablonTipi(int sablonTipiInt) {

        this.sablonTipiInt = sablonTipiInt;
    }

    public int getSablonTipiInt() {
        return sablonTipiInt;
    }

    /**
     * returns SablonTipi with corresponding sablonTipiInt.
     * @param sablonTipiInt
     * @return
     */
    public static SablonTipi getSablonTipi(int sablonTipiInt){
        for (SablonTipi protocolType : SablonTipi.values()) {
            if(protocolType.getSablonTipiInt() == sablonTipiInt )
                return protocolType;
        }
        return null;
    }

}

package util;

/**
 * <b>Author</b>    : zeldal.ozdemir <br/>
 * <b>Project</b>   : MA3   <br/>
 * <b>Copyright</b> : TUBITAK Copyright (c) 2006-2012 <br/>
 * <b>Date</b>: 9/5/12 - 12:58 AM <p/>
 * <b>Description</b>: <br/>
 */
public class EcAsHexHolder {
    private final static String delim = "_";
    public final static String Prime  = "P"; // prime curve
    public final static String Binary  = "B"; // binary curve
    private String curveType = Prime;
    private String p;
    private String a;
    private String b;
    private String gx;
    private String gy;
    private String n;
    private int h;

    public String merge(){
        return curveType + delim  +
                p + delim +
                a + delim +
                b + delim +
                gx + delim +
                gy + delim +
                n + delim +
                h ;
    }

    public void parse(String merged){
        if(merged == null )
            throw new IllegalArgumentException("Null Parameter to Parse");
        String[] split = merged.split(delim);
        if(split.length != 8)
            throw new IllegalArgumentException("Invalid Parameter to Parse:"+merged+" it must be like: \"curveType_p_a_b_gx_gy_n_h\"");
        curveType = split[0];
        p = split[1];
        a = split[2];
        b = split[3];
        gx = split[4];
        gy = split[5];
        n = split[6];
        h = Integer.parseInt(split[7]);
    }


    public String getP() {
        return p;
    }

    public void setP(String p) {
        this.p = p;
    }

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }

    public String getB() {
        return b;
    }

    public void setB(String b) {
        this.b = b;
    }

    public String getGx() {
        return gx;
    }

    public void setGx(String gx) {
        this.gx = gx;
    }

    public String getGy() {
        return gy;
    }

    public void setGy(String gy) {
        this.gy = gy;
    }

    public String getN() {
        return n;
    }

    public void setN(String n) {
        this.n = n;
    }

    public String getCurveType() {
        return curveType;
    }

    public void setCurveType(String curveType) {
        this.curveType = curveType;
    }

    public int getH() {
        return h;
    }

    public void setH(int h) {
        this.h = h;
    }
}

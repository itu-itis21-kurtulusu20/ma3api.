package tr.gov.tubitak.uekae.esya.api.common.lcns;

public class Bld{
    public String gD(){
        return "${timestamp}";
    }
    public String gV(){
        return "${pom.version}";
    }
}

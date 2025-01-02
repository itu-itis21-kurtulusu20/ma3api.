package tr.gov.tubitak.uekae.esya.api.infra.directory;

import javax.naming.NamingException;
import javax.naming.directory.DirContext;

/**
 * <b>Author</b>    : zeldal.ozdemir <br>
 * <b>Project</b>   : MA3   <br>
 * <b>Copyright</b> : TUBITAK Copyright (c) 2006-2012 <br>
 * <b>Date</b>: 9/20/12 - 6:13 PM <p>
 * <b>Description</b>: <br>
 */
public abstract class ExternalDirectoryContext extends StaticDirectoryInfo {

    public ExternalDirectoryContext(StaticDirectoryInfo directoryInfo) {
        super(directoryInfo.getDizinNo(),directoryInfo.getIP(),directoryInfo.getPort(),
                directoryInfo.getDizinMod(),directoryInfo.getType(), directoryInfo.getDizinCalismaNoktasi(),directoryInfo.isDizindeMailIleAramaYap(),
                directoryInfo.isDizindeRdnTekillestir(),directoryInfo.isVarsayilan(),directoryInfo.getUserName(),directoryInfo.getUserPassword());
    }

    public abstract DirContext openConnection() throws NamingException;


}

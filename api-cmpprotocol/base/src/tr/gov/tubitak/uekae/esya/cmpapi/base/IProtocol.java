package tr.gov.tubitak.uekae.esya.cmpapi.base;

import tr.gov.tubitak.uekae.esya.api.asn.esya.EESYAServiceConfigType;
import tr.gov.tubitak.uekae.esya.cmpapi.base.common.CmpConfigType;

/**
 * <b>Author</b>    : zeldal.ozdemir <br>
 * <b>Project</b>   : MA3   <br>
 * <b>Copyright</b> : TUBITAK Copyright (c) 2006-2011 <br>
 * <b>Date</b>: Nov 11, 2010 - 3:00:16 PM <p>
 * <b>Description</b>: <br>
 * Base Protocol implementations for Client Cmp Api
 */
public interface IProtocol {
    void runProtocol();
    void setServiceConfigType(CmpConfigType cmpConfigType);
}

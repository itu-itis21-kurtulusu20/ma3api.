package tr.gov.tubitak.uekae.esya.cmpapi.base20;

import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.cmpapi.base20.common.CMPConnectionException;
import tr.gov.tubitak.uekae.esya.cmpapi.base20.common.CMPProtocolException;

/**
 * <b>Author</b>    : zeldal.ozdemir <br>
 * <b>Project</b>   : MA3   <br>
 * <b>Copyright</b> : TUBITAK Copyright (c) 2006-2011 <br>
 * <b>Date</b>: Nov 11, 2010 - 3:00:16 PM <p>
 * <b>Description</b>: <br>
 * Base Protocol implementations for Client Cmp Api
 */
public interface IProtocol {
    void runProtocol() throws CMPConnectionException, CMPProtocolException, ESYAException;
	void setLocaleTag(String localeTag);
}

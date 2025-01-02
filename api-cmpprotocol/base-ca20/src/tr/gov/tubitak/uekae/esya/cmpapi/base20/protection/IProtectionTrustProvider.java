package tr.gov.tubitak.uekae.esya.cmpapi.base20.protection;

import tr.gov.tubitak.uekae.esya.cmpapi.base20.common.IConfigType;

import java.util.List;

/**
 * Author: ZELDAL  @ UEKAE - TUBITAK
 * Date: 15.Tem.2010
 * Time: 09:30:08
 * DESCRIPTION:
 */

public interface IProtectionTrustProvider<T extends IConfigType> {

    List<IProtectionController> getAcceptedProtectionContollers();

    IProtectionGenerator getProtectionGenerator();
}

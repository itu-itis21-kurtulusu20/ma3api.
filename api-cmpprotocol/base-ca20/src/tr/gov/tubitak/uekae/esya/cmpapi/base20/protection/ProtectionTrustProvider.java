package tr.gov.tubitak.uekae.esya.cmpapi.base20.protection;

import tr.gov.tubitak.uekae.esya.cmpapi.base20.common.IConfigType;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zeldal.ozdemir
 * Date: Nov 5, 2010
 * Time: 11:36:16 AM
 * To change this template use File | Settings | File Templates.
 */

public class ProtectionTrustProvider<T extends IConfigType> implements IProtectionTrustProvider<T> {
    private List<IProtectionController> protectionControllers;
    private IProtectionGenerator protectionGenerator;

    public ProtectionTrustProvider(List<IProtectionController> protectionControllers, IProtectionGenerator protectionGenerator) {
        this.protectionControllers = protectionControllers;
        this.protectionGenerator = protectionGenerator;
    }

    public List<IProtectionController> getAcceptedProtectionContollers() {
        return protectionControllers;
    }

    public IProtectionGenerator getProtectionGenerator() {
        return protectionGenerator;
    }
}

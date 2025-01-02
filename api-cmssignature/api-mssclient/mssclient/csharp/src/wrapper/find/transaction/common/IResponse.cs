using tr.gov.tubitak.uekae.esya.api.webservice.mssclient.wrapper;

namespace tr.gov.tubitak.uekae.esya.api.webservice.mssclient.transaction.common
{
    /**
 * Basic response methods
 */

    public interface IResponse : IMessageAbstract
    {
        Status getStatus();
    }
}
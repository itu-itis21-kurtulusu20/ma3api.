#include "CMSException.h"

#include "Logger.h"

namespace esya {

    CMSException::CMSException()
    {
    }

    CMSException::CMSException(QString errorMessage) :
        mErrorMessage(errorMessage)
    {
        Logger::log("CMSException is thrown: " + mErrorMessage);
    }

    QString CMSException::getErrorMessage()
    {
        return mErrorMessage;
    }
}

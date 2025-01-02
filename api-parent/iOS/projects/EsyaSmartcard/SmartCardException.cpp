#include "SmartCardException.h"

#include "Logger.h"

namespace esya {

    SmartCardException::SmartCardException()
    {
    }

    SmartCardException::SmartCardException(QString errorMessage) :
        mErrorMessage(errorMessage)
    {
        Logger::log("constructor of SmartCardException");
        Logger::log("message: " + mErrorMessage);
    }

    QString SmartCardException::getErrorMessage()
    {
        return mErrorMessage;
    }
}


#include "UIException.h"

#include "Logger.h"

namespace esya {

    UIException::UIException()
    {
    }

    UIException::UIException(QString errorMessage) :
        mErrorMessage(errorMessage)
    {
        Logger::log("uiexcp constructor");
    }

    QString UIException::getErrorMessage()
    {
        return mErrorMessage;
    }
}


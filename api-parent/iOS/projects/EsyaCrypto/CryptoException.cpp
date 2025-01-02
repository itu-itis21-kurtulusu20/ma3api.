#include "CryptoException.h"

namespace esya {

    CryptoException::CryptoException()
    {
    }

    CryptoException::CryptoException(QString errorMessage) :
        mErrorMessage(errorMessage)
    {
    }
}


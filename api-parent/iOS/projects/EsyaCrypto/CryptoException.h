#ifndef CRYPTOEXCEPTION_H
#define CRYPTOEXCEPTION_H

#include <QString>

namespace esya {

    class CryptoException
    {
    public:
        CryptoException();
        CryptoException(QString errorMessage);
    private:
        QString mErrorMessage;
    };
}

#endif // CRYPTOEXCEPTION_H

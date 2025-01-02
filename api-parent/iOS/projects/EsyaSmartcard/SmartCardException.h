#ifndef SMARTCARDEXCEPTION_H
#define SMARTCARDEXCEPTION_H

#include <QString>

namespace esya {

    class SmartCardException
    {
    public:
        SmartCardException();
        SmartCardException(QString errorMessage);
        QString getErrorMessage();
    private:
        QString mErrorMessage;
    };
}

#endif // SMARTCARDEXCEPTION_H

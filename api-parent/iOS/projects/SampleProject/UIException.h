#ifndef UIEXCEPTION_H
#define UIEXCEPTION_H

#include <QString>

namespace esya {

    class UIException
    {
    public:
        UIException();
        UIException(QString errorMessage);
        QString getErrorMessage();
    private:
        QString mErrorMessage;
    };

}

#endif // UIEXCEPTION_H

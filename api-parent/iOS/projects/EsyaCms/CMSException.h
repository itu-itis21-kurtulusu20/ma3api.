#ifndef CMSEXCEPTION_H
#define CMSEXCEPTION_H

#include <QString>

namespace esya {

    class CMSException
    {
    public:
        CMSException();
        CMSException(QString errorMessage);
        QString getErrorMessage();
    private:
        QString mErrorMessage;
    };
}

#endif // CMSEXCEPTION_H

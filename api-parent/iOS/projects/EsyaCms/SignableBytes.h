#ifndef SIGNABLEBYTES_H
#define SIGNABLEBYTES_H

#include <QByteArray>
#include <QString>

#include "Signable.h"

namespace esya
{
    class SignableBytes : public Signable
    {
    public:
        SignableBytes(const char *data, int length, const char *uri, const char *mime);
        SignableBytes(QByteArray &data);
        virtual QByteArray getContent();
    private:
        SignableBytes();
        QByteArray mData;
        QString mUri;
        QString mMime;
    };
}

#endif // SIGNABLEBYTES_H

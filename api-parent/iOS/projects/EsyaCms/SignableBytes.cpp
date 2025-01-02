#include "SignableBytes.h"

namespace esya
{
    SignableBytes::SignableBytes(const char *data, int length, const char *uri, const char *mime)
        :mData(data, length), mUri(uri), mMime(mime)
    {

    }

    SignableBytes::SignableBytes(QByteArray &data)
        :mData(data)
    {

    }

    QByteArray SignableBytes::getContent()
    {
        return mData;
    }
}

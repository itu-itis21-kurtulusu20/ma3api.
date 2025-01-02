#ifndef BASESIGNER_H
#define BASESIGNER_H

#include <QByteArray>

namespace esya
{
    class BaseSigner
    {
    public:
        BaseSigner(){}
        virtual QByteArray sign(const QByteArray data) = 0;
        virtual ~BaseSigner(){}
    };
}

#endif // BASESIGNER_H

#ifndef SIGNABLE_H
#define SIGNABLE_H

#include <QByteArray>

namespace esya
{
    class Signable
    {
    public:
        Signable(){}
        ~Signable(){}
        virtual QByteArray getContent() = 0;
    };
}

#endif // SIGNABLE_H

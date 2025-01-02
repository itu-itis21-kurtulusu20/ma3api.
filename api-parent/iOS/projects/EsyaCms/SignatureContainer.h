#ifndef SIGNATURECONTAINER_H
#define SIGNATURECONTAINER_H

#include "ECertificate.h"

#include "Signature.h"

namespace esya
{
    class SignatureContainer
    {
    public:
        SignatureContainer(){};
        virtual ~SignatureContainer(){};
        virtual Signature * createSignature(const ECertificate &certificate) = 0;
        virtual void write(const QString &fileName) = 0; // TO DO simdilik dosya ismi alabilir, JAVA'da stream almis
        virtual QByteArray write() = 0;
    };
}

#endif // SIGNATURECONTAINER_H

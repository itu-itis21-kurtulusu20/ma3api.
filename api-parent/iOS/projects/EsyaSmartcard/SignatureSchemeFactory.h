#ifndef SIGNATURESCHEMEFACTORY_H
#define SIGNATURESCHEMEFACTORY_H

#include "SignatureScheme.h"

namespace esya {

    class SignatureSchemeFactory
    {
    public:
        SignatureSchemeFactory();
        static SignatureScheme* getSignatureScheme(QString signingAlg);
    };

}



#endif // SIGNATURESCHEMEFACTORY_H

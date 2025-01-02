#include "SignatureSchemeFactory.h"
#include "RSAScheme.h"

namespace esya {

    SignatureSchemeFactory::SignatureSchemeFactory()
    {
    }

    SignatureScheme* SignatureSchemeFactory::getSignatureScheme(QString signingAlg)
    {
        if(signingAlg.contains("ISO9796")) {
            //throw new std::exception("Not implemented");
        }
        else if(signingAlg.contains("RSAPSS")) {
            //throw new std::exception("Not implemented");
        }
        else if(signingAlg.contains("RSA")) {
            return new RSAScheme(signingAlg);
        }
        else if(signingAlg.contains("ECDSA")) {
            //throw new std::exception("Not implemented");
        }
        //throw new std::exception("Not implemented");
    }
}



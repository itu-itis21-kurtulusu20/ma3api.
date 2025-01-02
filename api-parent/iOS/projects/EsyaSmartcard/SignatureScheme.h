#ifndef SIGNATURESCHEME_H
#define SIGNATURESCHEME_H

#include <QByteArray>

namespace esya {

    class SignatureScheme {
    public:
        SignatureScheme(){}
        virtual QByteArray getSignatureInput(QByteArray toBeSigned) = 0;
        virtual QByteArray getPrefixForDigestAlg(QString digestAlg) = 0;
    };

}

#endif // SIGNATURESCHEME_H

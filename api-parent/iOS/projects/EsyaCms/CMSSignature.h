#ifndef CMSSIGNATURE_H
#define CMSSIGNATURE_H

#include "Signature.h"
#include "CMSContainer.h"
#include "SignedData.h"
#include <QByteArray>

namespace esya
{

    class CMSSignature : public Signature
    {
    public:
        CMSSignature(SignatureContainer *container, const SignedData &signedData, const ECertificate & certificate);

        virtual Signature * createCounterSignature(const ECertificate &certificate);
        virtual QList<Signature*> getCounterSignatures();
        virtual void addContent(Signable * data, bool includeContent);
        virtual bool sign(BaseSigner *signer);
        virtual QByteArray getSignature();
    private:
        SignatureContainer *mpContainer;
        SignedData mSignedData;
        SignerInfo mSignerInfo;
        ECertificate mSignersCertificate;
    };
}

#endif // CMSSIGNATURE_H

#ifndef CMSCONTAINER_H
#define CMSCONTAINER_H

#include "SignedData.h"

#include "SignatureContainer.h"
#include "CMSSignature.h"

namespace esya
{
    class CMSContainer : public SignatureContainer
    {
    public:
        CMSContainer();
        virtual ~CMSContainer();
        virtual Signature * createSignature(const ECertificate &certificate);
        virtual void write(const QString &fileName);
        virtual QByteArray write();
    private:
        SignedData mSignedData;
        // signatures
        // context
    };
}

#endif // CMSCONTAINER_H

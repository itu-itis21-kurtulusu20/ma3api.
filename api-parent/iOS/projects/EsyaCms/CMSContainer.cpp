#include "CMSContainer.h"

namespace esya {

    CMSContainer::CMSContainer()
    {

    }

    CMSContainer::~CMSContainer()
    {

    }

    Signature * CMSContainer::createSignature(const ECertificate &certificate)
    {
        CMSSignature * signature = new CMSSignature(this, mSignedData, certificate);
        // signatures.add(signature);
        return signature;
    }

    void CMSContainer::write(const QString &fileName)
    {

    }
    QByteArray CMSContainer::write()
    {
        return mSignedData.getEncodedBytes();
    }

}


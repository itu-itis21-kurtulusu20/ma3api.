#ifndef SIGNATURE_H
#define SIGNATURE_H

#include <QByteArray>
#include <QList>

#include "ECertificate.h"
#include "Signable.h"
#include "BaseSigner.h"

namespace esya
{
    class Signature
    {
    public:
        virtual ~Signature();


        //virtual void setSigningTime(const &QDateTime) = 0; // later - TO DO: QDate uygun mu?
       // virtual QDateTime getSigningTime() = 0; // later - TO DO: QDate uygun mu?

        //virtual void setSignaturePolicy(SignaturePolicyIdentifier &policy); // later - TO DO: SignaturePolicyIdentifier to be implemented
        //virtual SignaturePolicyIdentifier getSignaturePolicy(); // later - TO DO: SignaturePolicyIdentifier to be implemented

        virtual Signature * createCounterSignature(const ECertificate &certificate) = 0; // later
        virtual QList<Signature*> getCounterSignatures() = 0; // later

        virtual void addContent(Signable * data, bool includeContent) = 0;
        //virtual QList<Signable> getContents(); // later - TO DO: Signable to be implemented

        //virtual Algorithm getSignatureAlgorithm(); // later - TO DO: Algorithm to be implemented

        virtual bool sign(BaseSigner *signer) = 0; // TO DO: BaseSigner to be implemented

        //virtual SignatureType getSignatureType(); // later - TO DO: SignatureType to be implemented

        //virtual SignatureFormat getSignatureFormat(); // later - TO DO: SignatureFormat to be implemented

        virtual QByteArray getSignature() = 0;
    };
}

#endif // SIGNATURE_H

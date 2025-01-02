#ifndef SMARTCARDMANAGER_H
#define SMARTCARDMANAGER_H

#include "ECertificate.h"
#include "APDUSmartCard.h"
#include "APDUSigner.h"

namespace esya
{
    class SmartCardManager
    {
    public:
        SmartCardManager();
        ~SmartCardManager(){}
        void openSession();
        ECertificate getSignatureCertificate();
        BaseSigner * getSigner(const std::string & pin, const ECertificate & cert);
    private:
        APDUSmartCard mSmartcard;
    };
}

#endif // SMARTCARDMANAGER_H

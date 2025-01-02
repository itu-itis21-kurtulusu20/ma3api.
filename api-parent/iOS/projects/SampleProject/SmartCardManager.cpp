#include "SmartCardManager.h"
#include "APDUSmartCard.h"
#include "AlgorithmList.h"
#include "Logger.h"
#include "SmartCardException.h"

namespace esya
{
    SmartCardManager::SmartCardManager()
    {
    }

    void SmartCardManager::openSession()
    {
        Logger::log("Session is being opened in SmartCardManager");
        mSmartcard.openSession();
        Logger::log("Session opening process is done in SmartCardManager");
    }

    ECertificate SmartCardManager::getSignatureCertificate()
    {
        //QByteArray certByte = mSmartcard.getCertificate();
        QList<QByteArray> *certsBytes = mSmartcard.getCertificate();

        for(int i=0; i<certsBytes->size(); i++) {
            ECertificate cert(certByte->at(i));
            if(cert.isQualified())
                return cert;
        }

        //ECertificate cert(certByte);
        //return cert;
    }

    BaseSigner * SmartCardManager::getSigner(const std::string & pin, const ECertificate & cert)
    {
        mSmartcard.login(QString::fromStdString(pin));
        BaseSigner* mpSigner = mSmartcard.getSigner(cert.getEncodedBytes(), AlgorithmList::SIGNATURE_RSA_SHA256); // TODO algo
        return mpSigner;
    }

}



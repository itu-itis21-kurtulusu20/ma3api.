#ifndef APDUSMARTCARD_H
#define APDUSMARTCARD_H

#include "BaseSigner.h"
#include <QByteArray>
//#include "CommandTransmitterPCSC.h"
#include "MilkoCppLib.h"
#include "AbstractAkisCommands.h"
#include <QString>
#include <QList>

namespace esya
{
    class APDUSmartCard
    {
    public:
        APDUSmartCard();
        ~APDUSmartCard();
        QList<QByteArray> getCertificate();
        void openSession();
        QByteArray sign(const QByteArray data, const QString signingAlg);
        QByteArray tryAllKeys(const QByteArray data);
        QByteArray signAndCheck(const QByteArray data, int keyID);
        void login(const QString & pin);
        BaseSigner * getSigner(const QByteArray& cert, const QString& algorithm);
    private:
        AbstractAkisCommands *mpCommands;
        //CommandTransmitterPCSC pcsc;
        MilkoCppLib transmitter;
    };
}

#endif // APDUSMARTCARD_H

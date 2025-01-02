#include "Logger.h"

#include <iostream>

#include <QDir>
#include <QFile>
#include <QDateTime>
#include <QStandardPaths>

namespace esya {

    Logger::Logger()
    {
    }

    void Logger::log(QString text)
    {
        //QFile file("../Documents/log.txt");

        QString path = QStandardPaths::standardLocations(QStandardPaths::DataLocation).value(0);
        QDir dir(path);
        if (!dir.exists())
            dir.mkpath(path);
        if (!path.isEmpty() && !path.endsWith("/"))
            path += "/";

        //std::cout << "MA3LOG" << path.toUtf8().constData() << "sig.der" << std::endl;
        QFile file(path+"log.txt");
        //file.open(QIODevice::Append);

        if(file.exists()) {
            file.open(QIODevice::Append);
            file.write("\n");
            file.write(QDateTime::currentDateTime().toString().toStdString().c_str());
            file.write(" - ");
            file.write(text.toStdString().c_str());
            file.close();
        }
        else {
            file.open(QIODevice::WriteOnly);
            file.write(QDateTime::currentDateTime().toString().toStdString().c_str());
            file.write(" - ");
            file.write(text.toStdString().c_str());
            file.close();
        }
        file.close();
    }

}


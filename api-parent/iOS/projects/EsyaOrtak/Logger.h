#ifndef LOGGER_H
#define LOGGER_H

#include <QString>

namespace esya {

    class Logger
    {
    public:
        Logger();
        static void log(QString);
    };

}

#endif // LOGGER_H

//
//  MilkoCppLib.h
//  MilkoLib
//
//  Created by Hakan Aydın on 30/08/14.
//  Copyright (c) 2014 Udea. All rights reserved.
//

#ifndef MilkoLib_MilkoCppLib_h
#define MilkoLib_MilkoCppLib_h



#include "ICommandTransmitter.h"

class MilkoCppLib : public ICommandTransmitter
{
    void* wrapped;
    commandAttribute atr;
public:
    MilkoCppLib();
    ~MilkoCppLib();
    void connectCard();
    void disconnectCard();
    APDUResponse transmit(unsigned char* buf, int len);
    commandAttribute getAttribute();
};


#endif

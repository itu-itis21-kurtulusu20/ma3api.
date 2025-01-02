//
//  ObjSmartCardManager.h
//  MA3API
//
//  Created by Suleyman Uslu on 1/2/14.
//  Copyright (c) 2014 TUBITAK. All rights reserved.
//

#import <Foundation/Foundation.h>

#include "SmartCardManager.h"

@interface ObjSmartCardManager : NSObject

@property SmartCardManager smartCardManager;

-(id)initWithSmartCardManager:(SmartCardManager)smartCardManager;

@end

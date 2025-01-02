//
//  ObjSmartCardManager.m
//  MA3API
//
//  Created by Suleyman Uslu on 1/2/14.
//  Copyright (c) 2014 TUBITAK. All rights reserved.
//

#import "ObjSmartCardManager.h"

@implementation ObjSmartCardManager

-(id)initWithSmartCardManager:(SmartCardManager)smartCardManager {
    
    self.smartCardManager = smartCardManager;
    //@throw [NSException exceptionWithName:@"Test exception" reason:@"Reasonable exception" userInfo:nil];
    return self;
}

@end

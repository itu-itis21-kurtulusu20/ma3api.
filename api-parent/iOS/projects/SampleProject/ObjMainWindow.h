//
//  ObjMainWindow.h
//  MA3API
//
//  Created by Suleyman Uslu on 1/2/14.
//  Copyright (c) 2014 TUBITAK. All rights reserved.
//

#import <Foundation/Foundation.h>

#include "mainwindow.h"

@interface ObjMainWindow : NSObject

@property MainWindow* window;

-(id)initWithMainWindow:(MainWindow*)window;

@end

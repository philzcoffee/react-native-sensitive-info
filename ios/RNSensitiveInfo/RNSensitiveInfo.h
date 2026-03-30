/**
 * Copyright (c) 2015-present, Facebook, Inc.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */

#ifdef RCT_NEW_ARCH_ENABLED
#import <RNSensitiveInfoSpec/RNSensitiveInfoSpec.h>
#else
#import <React/RCTBridgeModule.h>
#endif

@interface RNSensitiveInfo : NSObject <
#ifdef RCT_NEW_ARCH_ENABLED
NativeSensitiveInfoSpec
#else
RCTBridgeModule
#endif
>

@end

#
# Copyright 2009 Cedric Priscal
# 
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
# 
# http://www.apache.org/licenses/LICENSE-2.0
# 
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License. 
#

LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

TARGET_PLATFORM := android-21
LOCAL_MODULE    := anaalgorithm
LOCAL_SRC_FILES := AnaAlgorithm.cpp  PcrAnaAlgorithm.cpp PcrAnaDataAlgorithm.cpp
LOCAL_LDLIBS    := -llog
#LOCAL_CPPFLAGS := -fexceptions -frtti
LOCAL_CPPFLAGS += -std=c++11 -D__cplusplus=201103L -DANDROID_STL=c++_shared
include $(BUILD_SHARED_LIBRARY)
#
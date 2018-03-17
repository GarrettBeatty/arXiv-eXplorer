#!/bin/bash

echo no | android create avd --force -n test -t android-$ANDROID_EMU_API_LEVEL --abi google_apis/$ANDROID_ABI
emulator -avd test -no-window &
android-wait-for-emulator
adb shell input keyevent 82 &



## 如果模拟器旋转但是内容没有跟随旋转

https://stackoverflow.com/questions/25864385/changing-android-device-orientation-with-adb

### 关闭屏幕转向
adb shell content insert --uri content://settings/system --bind name:s:accelerometer_rotation --bind value:i:0


### 打开屏幕转向
adb shell content insert --uri content://settings/system --bind name:s:accelerometer_rotation --bind value:i:1

### 屏幕横向
adb shell content insert --uri content://settings/system --bind name:s:user_rotation --bind value:i:1

### 屏幕纵向
adb shell content insert --uri content://settings/system --bind name:s:user_rotation --bind value:i:0

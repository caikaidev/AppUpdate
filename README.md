# AppUpdate
[中文文档](https://github.com/fccaikai/AppUpdate/blob/master/README_zh.md)

android app update library

### Screenshots

### ![](https://ww2.sinaimg.cn/large/006tNbRwgy1fdhmyj8ttmj30qk0b4t9e.jpg)



### Usage

#### setup

+ Step 1. Add the JitPack repository to your build file   

   Add it in your root build.gradle at the end of repositories:

   ```groovy
   allprojects {
   	repositories {
   		...
   		maven { url 'https://jitpack.io' }
   	}
   }
   ```

+ Step 2. Add the dependency   

   ```groovy
   dependencies {
            compile 'com.github.fccaikai:AppUpdate:1.0.0'
    }
   ```

#### usage


```java
UpdateWrapper updateWrapper = new UpdateWrapper.Builder(getApplicationContext())
    					//set interval Time
    	                .setTime(time)
    	                //set notification icon
    	                .setNotificationIcon(R.mipmap.ic_launcher_round)
    	                //set update file url
    	                .setUrl("you update json file url").build();
updateWrapper.start();
```

  The update json format：

```json
{
  "versionCode":1,
  "versionName":"1.0.0",
  "content":"1.新增抢单功能#2.性能优化",//use # to wrap
  "mustUpdate":true,	//must update ,true or false
  "url":"apk download url"
}
```

### Library

+ v7-support   

   ```compile 'com.android.support:appcompat-v7:25.2.0'```   

+ okhttp

   ```compile 'com.squareup.okhttp3:okhttp:3.6.0'```

 If you don't want to use,you can ```exclude``` it.




# AppUpdate

Android 检查更新库

### 屏幕截图

### ![](https://ww2.sinaimg.cn/large/006tNbRwgy1fdhmyj8ttmj30qk0b4t9e.jpg)



### 如何使用

#### 配置

- Step 1.配置jitpack仓库

   在根目录的```gradle.build```中添加jitpack地址：

  ```groovy
  allprojects {
  	repositories {
  		...
  		maven { url 'https://jitpack.io' }
  	}
  }
  ```

- Step 2. 添加库依赖：

  ```groovy
  dependencies {
           compile 'com.github.fccaikai:AppUpdate:1.0.1'
   }
  ```

#### 使用

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

  升级json文件格式：

```json
{
  "versionCode":1,
  "versionName":"1.0.0",
  "content":"1.新增抢单功能#2.性能优化",//使用 # 来进行换行
  "mustUpdate":true,	//是否强制 ,true or false
  "url":"apk download url"
}
```

### 依赖的库

- v7-support   

   ```compile 'com.android.support:appcompat-v7:25.2.0'```   

- okhttp

  ```compile 'com.squareup.okhttp3:okhttp:3.6.0'```

  如果你不想使用这两个库，可以使用```exclude```排除




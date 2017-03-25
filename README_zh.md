# AppUpdate

Android 检查更新库

### 屏幕截图

![](https://ww3.sinaimg.cn/large/006tNbRwgy1fdhug16dnhj30km0b4glu.jpg)



![](https://ww4.sinaimg.cn/large/006tNbRwgy1fdhuhh2vzej30ea0b474b.jpg)



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
           compile 'com.github.fccaikai:AppUpdate:2.0.6'
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
    	                .setUrl("you update json file url")
						//add callback ,return new version info
						.setCallback(new CheckUpdateTask.Callback() {
                                    @Override
                                    public void callBack(VersionModel model) {
                                        Log.d(TAG,"new version :" + 																		model.getVersionName());
                                    }
                         })
						 .build();
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
#### 自定义UI
+ 自定义Activity
   创建一个Activity 继承自```UpdateActivity```,并重写```protected Fragment getUpdateDialogFragment()```方法，比如：

 ```
 public class CustomsUpdateActivity extends UpdateActivity {
     @Override
     protected Fragment getUpdateDialogFragment() {
         return CustomsUpdateFragment.newInstance(mModel);
     }
 }
 ```
+ 设置主题

  Activity需要设置为Dialog主题，在```Androidmanifest.xml```中注册Activity中设置主题

  ```xml
  <activity
            android:name=".CustomsUpdateActivity"
            android:theme="@style/UpdateDialog">
  </activity>
  ```

  ​

  ​

+ 自定义FragmentDialog

 创建一个FragmentDialog，继承自```UpdateDialog```,代码如下：

 ```
 public class CustomsUpdateFragment extends UpdateDialog {

   public static CustomsUpdateFragment newInstance(VersionModel model) {

       Bundle args = new Bundle();
       args.putSerializable(Constant.MODEL, model);
       CustomsUpdateFragment fragment = new CustomsUpdateFragment();
       fragment.setArguments(args);
       return fragment;
     }

     @Override
     protected int getLayout() {
         return R.layout.fragment_update_dialog;
     }

     @Override
     protected void setContent(View view, int contentId) {
         super.setContent(view, R.id.content);
     }

     @Override
     protected void bindUpdateListener(View view, int updateId) {
         super.bindUpdateListener(view, R.id.update);
     }

     @Override
     protected void bindCancelListener(View view, int cancelId) {
         super.bindCancelListener(view, R.id.cancel);
     }

     @Override
     protected void initIfMustUpdate(View view, int id) {
         super.initIfMustUpdate(view, R.id.cancel);
     }
 }
 ```

+ 配置

 ```
 UpdateWrapper.Builder builder = ...;
 builder.setCustomsActivity(CustomsUpdateActivity.class);
 ...
 builder.build().start();
 ```
 具体使用请查看[demo](https://github.com/fccaikai/AppUpdate/blob/master/app/src/main/java/com/kcode/appupdate/MainActivity.java)
### 依赖的库

- v7-support

   ```compile 'com.android.support:appcompat-v7:25.2.0'```

- okhttp

  ```compile 'com.squareup.okhttp3:okhttp:3.6.0'```

  如果你不想使用这两个库，可以使用```exclude```排除




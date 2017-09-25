# AppUpdate
[中文文档](https://github.com/fccaikai/AppUpdate/blob/master/README_zh.md)

android app update library

### Screenshots

### ![](https://ww3.sinaimg.cn/large/006tNbRwgy1fdhug16dnhj30km0b4glu.jpg)

![](https://ww4.sinaimg.cn/large/006tNbRwgy1fdhuhh2vzej30ea0b474b.jpg)



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
            compile 'com.github.fccaikai:AppUpdate:2.1.4'
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
    	                .setUrl("you update json file url")
  						//set customs activity
  						.setCustomsActivity(cls)
  						 //set showToast. default is true
    	                .setIsShowToast(false)
  						//add callback ,return new version info
						.setCallback(new CheckUpdateTask.Callback() {
                                    @Override
                                    public void callBack(VersionModel model,booleab hasNewVersion) {
                                        Log.d(TAG,"new version :" + 																	model.getVersionName());
                                    }
                                })
  						.build();

updateWrapper.start();
```

  The update json format：

```json
{
  "versionCode":1,
  "versionName":"1.0.0",
  "content":"1.add something#2.add something",//use # to wrap
  "minSupport":1,	//min support version. while your app versionCode less than  minSupport,You must update app
  "url":"apk download url"
}
```

#### Custom

+ create  custome Activity

  create an activity ```extents UpdateActivity   ``` ,and Override ```protected Fragment getUpdateDialogFragment()```.like :

  ```java
  public class CustomsUpdateActivity extends UpdateActivity {
      @Override
      protected Fragment getUpdateDialogFragment() {
          return CustomsUpdateFragment.newInstance(mModel);
      }
  }
  ```

+ set theme

  set Activity theme as Dialog to,in ```Androidmanifest.xml```

  ```xml
  android:theme="@style/UpdateDialog"
  ```

  ```xml
  <activity 
            android:name=".CustomsUpdateActivity"
            android:theme="@style/UpdateDialog">
  </activity>
  ```

  ​

+ create custom FragmentDialog

  create a FragmentDialog ``` extends UpdateDialog```.like:

  ```java

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

+ set Custom Activity

  ```java
  UpdateWrapper.Builder builder = ...;
  builder.setCustomsActivity(CustomsUpdateActivity.class);
  ...
  builder.build().start();
  ```

  see the [demo](https://github.com/fccaikai/AppUpdate/blob/master/app/src/main/java/com/kcode/appupdate/MainActivity.java) .

### Library

+ v7-support   

   ```compile 'com.android.support:appcompat-v7:25.2.0'```   

 If you don't want to use,you can ```exclude``` it.




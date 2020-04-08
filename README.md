IPicker
======================================
A material design style pictures selector.

## Screenshot
<img src="arts/1.png" width="30%"> <img src="arts/2.png" width="30%"> <img src="arts/clipping.png" width="30%">

## Usage
### Gradle

```
dependencies {
   	implementation 'com.liuguangqiang.ipicker:IPicker:1.1.0'
}
```

### Maven
```
<dependency>
  <groupId>com.liuguangqiang.ipicker</groupId>
  <artifactId>IPicker</artifactId>
  <version>1.1.0</version>
  <type>pom</type>
</dependency>
```

### Manifest

```
<activity
     android:name="com.liuguangqiang.ipicker.IPickerActivity"
     android:screenOrientation="portrait"
     android:theme="@style/IPickerTheme" />
```

### Theme
```
<style name="IPickerTheme" parent="Theme.AppCompat.Light.DarkActionBar">
     <item name="colorPrimary">@color/color_primary</item>
     <item name="colorPrimaryDark">@color/color_primary_dark</item>
     <item name="colorAccent">@color/color_primary</item>
 </style>
```

### Open the picker
```java
IPicker.setLimit(1);
IPicker.open(context);
```

Return the selected images by EventBus.

```
@Subscribe
public void onEvent(IPickerEvent event) {
}
```

Also support to get the selected images by a listener.

```
IPicker.setOnSelectedListener(new IPicker.OnSelectedListener() {

      @Override
      public void onSelected(List<String> paths) {}

});
```

## License

    Copyright 2016 Eric Liu

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
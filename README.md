Volley CREATE Lab Module for Android
====================================


Overview
--------
This Android module builds on top of the [Volley HTTP Library](https://github.com/google/volley) for networking on Android.


Add Module to Project
---------------------
To include the module in another Android project, clone the repository at the root of the Android project to create a ```volley-createlab-module-android``` directory. To include the module in the project, modify the ```settings.gradle``` file:

```
include ':app', ':volley-createlab-module-android'
```

Then, add the following to the ```app/build.gradle``` file:

```
repositories {
    flatDir {
        dirs 'libs'
        dirs project(':volley-createlab-module-android').file('libs')
    }
}
...
dependencies {
    compile project(':volley-createlab-module-android')
    compile 'com.android.volley:volley:1.0.0'
    ...
}
```

You will also need to add the `android.permission.INTERNET` permission to your app's `AndroidManifest.xml` file.

If you are using version control for the android project, you will also likely want to add the repository as a submodule to your Android project's repository. You can do this with ```git submodule add```, then add the following line to the ```.gitmodules``` file:

```
ignore = dirty
```

This document was last written for Android Studio version 2.2.0 using Gradle version 2.14.1.

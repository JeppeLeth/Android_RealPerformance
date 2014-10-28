Realm Android Performance Test
=========

An Android application for performance comparison on device.

  - Run queries using Realm or SQLite
  - Show a graph of values stored as they are retrieved
  - Focus on Android lifecycle for Activities and Fragments are being respected and taken care of
  - Using user interface theming and design guideline for alignment with the official [Realm] website
  - Possibility for future comparison test addition of other comparison types

> Please be aware that this performance comparation *is not* an exact scientific bechmark, 
> and the results could very because of the choosen implementation and enviroment conditions.

Version
----

1.0

#### Improvements for further development

* Settings menu
* Dynamically change the number of queries
* Add more tests for differnet CRUD usages - Also try using indexing and other data types
* Add graphs that shows measurements like TPS, duration and AVG


Tech
-----------

This project is developed using the follow tools:

* Android SDK
* Android Studio 

Also the following libraries where used:
* [Realm] v. 0.72.0
* [GraphView] v.3.1.3
* [SmoothProgressBar] library-circular v. 1.0.1

Installation
--------------

``
Install the release.apk on your device or import the project in Android Studio and build using Gradle v. 12.0.1
``


License & Development
----

Please ask me!
[Jeppe Leth Nielsen]

[GraphView]:https://github.com/jjoe64/GraphView
[SmoothProgressBar]:https://github.com/castorflex/SmoothProgressBar
[Realm]:http://realm.io
[Jeppe Leth Nielsen]:mailto:jeppe.leth@gmail.com

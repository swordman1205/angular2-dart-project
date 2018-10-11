# Gui tests.
Gui tests is implemented with [fluentenium](http://fluentlenium.org/).

## Launching gui test from IDEA on mock web app.
* In IDEA right click on _app/web/public/index.html_, and select _Open in browser_->_Dartium_. Ensure `BackendType.MOCK` is enabled in `app/web/public/main.dart`.
* Copy paste link from dartium to `com.qurasense.AbstractIt.getBaseUrl`.
* Create/Update configuration for required test. For example for `com.qurasense.MainIt`. Set env value `CHROME_DRIVER_PATH`, driver could be downloaded [here](https://sites.google.com/a/chromium.org/chromedriver/downloads).
* Launch Test.

## Launching gui test from IDEA on emulator web app.
* Launch _pubsub_ and _datastore_ emulator.
* Launch spring boot applications.
* Launch dartium. Ensure `BackendType.EMULATOR` is enabled in `app/web/public/main.dart`.
* Copy paste link from dartium to `com.qurasense.AbstractIt.getBaseUrl`.
* Create/Update configuration for required test. For example for `com.qurasense.MainIt`. Set env value `CHROME_DRIVER_PATH`, driver could be downloaded [here](https://sites.google.com/a/chromium.org/chromedriver/downloads).
* Launch Test.
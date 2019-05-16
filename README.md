# xbdd-maven-plugin
A Maven plugin to automatically send test results to XBDD.

For more information on XBDD, visit: https://github.com/orionhealth/XBDD

[![Build Status](https://travis-ci.com/steveswinsburg/xbdd-maven-plugin.svg?branch=master)](https://travis-ci.org/steveswinsburg/xbdd-maven-plugin)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/e67f4a74d6b845d0834f3b6594e76c9d)](https://www.codacy.com/app/steveswinsburg/xbdd-maven-plugin)
[![codecov](https://codecov.io/gh/steveswinsburg/xbdd-maven-plugin/branch/master/graph/badge.svg)](https://codecov.io/gh/steveswinsburg/xbdd-maven-plugin)


# Commands
Build and test: 
````
mvn clean install
````

Snapshot deploy: 
```
mvn clean deploy
```
check: https://oss.sonatype.org/content/repositories/snapshots/

Release deploy: 
```
mvn release:clean release:prepare
mvn release:perform
```

# Release docs
* https://central.sonatype.org/pages/working-with-pgp-signatures.html
* https://central.sonatype.org/pages/apache-maven.html


# xbdd-maven-plugin
A Maven plugin to automatically send test results to XBDD.

For more information on XBDD, visit: https://github.com/orionhealth/XBDD

[![Build Status](https://travis-ci.com/steveswinsburg/xbdd-maven-plugin.svg?branch=master)](https://travis-ci.org/steveswinsburg/xbdd-maven-plugin)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/e67f4a74d6b845d0834f3b6594e76c9d)](https://www.codacy.com/app/steveswinsburg/xbdd-maven-plugin)
[![codecov](https://codecov.io/gh/steveswinsburg/xbdd-maven-plugin/branch/master/graph/badge.svg)](https://codecov.io/gh/steveswinsburg/xbdd-maven-plugin)

# Usage

TODO

# For developers (building)
Build and test: 
````
mvn clean install
````

# For developers (releasing)

1. Read https://central.sonatype.org/pages/apache-maven.html and https://central.sonatype.org/pages/working-with-pgp-signatures.html
2. Install `gpg`
3. Add a release profile to your `~/.m2/settings.xml`:
```
    <profile>
      <id>release</id>
      <properties>
        <gpg.executable>gpg</gpg.executable>
        <gpg.passphrase>your passphrase</gpg.passphrase>
      </properties>
    </profile>
```
4. Add a server block to your `~/.m2/settings.xml`:
```
    <servers>
      <!-- sonatype-->
      <server>
        <id>ossrh</id>
        <username>your username</username>
        <password>your password</password>
      </server>
    </servers>
```
_Note that this can be a token generated from https://oss.sonatype.org/._

**Snapshot deploy:** 
```
mvn clean deploy -Prelease
```
It should build and upload. Go to https://oss.sonatype.org/ and search for `xbdd-maven-plugin`. 

Release deploy: 
```
mvn release:clean release:prepare
mvn release:perform
```
Go to https://oss.sonatype.org/#stagingRepositories and look for errors. If ok, release.

# Release docs
* https://central.sonatype.org/pages/working-with-pgp-signatures.html
* https://central.sonatype.org/pages/apache-maven.html


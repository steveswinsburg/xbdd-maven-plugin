# xbdd-maven-plugin
A Maven plugin to automatically send test results to XBDD.

For more information on XBDD, visit: https://github.com/orionhealth/XBDD

[![Build Status](https://travis-ci.com/steveswinsburg/xbdd-maven-plugin.svg?branch=master)](https://travis-ci.org/steveswinsburg/xbdd-maven-plugin)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/e67f4a74d6b845d0834f3b6594e76c9d)](https://www.codacy.com/app/steveswinsburg/xbdd-maven-plugin)
[![codecov](https://codecov.io/gh/steveswinsburg/xbdd-maven-plugin/branch/master/graph/badge.svg)](https://codecov.io/gh/steveswinsburg/xbdd-maven-plugin)

# Usage

```
<groupId>io.github.steveswinsburg</groupId>
<artifactId>xbdd-maven-plugin</artifactId>
<version>1.2</version>
<configuration>
	<host>https://xbdd</host>
	<username>${xbdd.username}</username>
	<password>${xbdd.password}</password>
	<projectKey>your-project-name<projectKey>
	<projectVersion>${project.version}</projectVersion>
	<buildNumber>${bamboo.build.number}</buildNumber>
	<reports>
		<report>${project.build.directory}/cucumber-report-manual.json</report>
		<report>${project.build.directory}/cucumber-report-auto.json</report>
	</reports>
</configuration>
```
Run via `mvn clean verify xbdd:upload`.

# Configuration

*host* - the hostname of your XBDD server.
*username* - username of the XBDD user to upload the reports as. Best to pass it in on the commandline. In the example above, `-Dxbdd.username=theusername`
*password* - password for the XBDD user. Best to pass it in on the commandline as well. In the example above, `-Dxbdd.password=thepassword`
*projectKey* - optional. The name of the project in XBDD. If left blank it will default to the artifactId of the project.
MORE HERE.

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
mvn clean deploy -P release
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


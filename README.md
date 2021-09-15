# xbdd-maven-plugin
A Maven plugin to automatically send integration test results to XBDD.

For more information on XBDD, visit: https://github.com/orionhealth/XBDD

[![Build Status](https://travis-ci.com/steveswinsburg/xbdd-maven-plugin.svg?branch=master)](https://travis-ci.org/steveswinsburg/xbdd-maven-plugin)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/e67f4a74d6b845d0834f3b6594e76c9d)](https://www.codacy.com/app/steveswinsburg/xbdd-maven-plugin)
[![codecov](https://codecov.io/gh/steveswinsburg/xbdd-maven-plugin/branch/master/graph/badge.svg)](https://codecov.io/gh/steveswinsburg/xbdd-maven-plugin)

# Usage

Add the following block to your pom.xml

```
<plugin>
  <groupId>io.github.steveswinsburg</groupId>
  <artifactId>xbdd-maven-plugin</artifactId>
  <version>1.4</version>
  <executions>
    <execution>
      <phase>post-integration-test</phase>
      <goals>
        <goal>upload</goal>
      </goals>
      <configuration>
        <host>https://xbdd</host>
        <username>${xbdd.username}</username>
        <password>${xbdd.password}</password>
        <projectKey>your-project-name<projectKey>
        <projectVersion>${project.version}</projectVersion>
        <buildNumber>${bamboo.build.number}</buildNumber>
        <filesets>
          <fileset>
            <directory>${project.build.directory}/failsafe-reports</directory>
            <includes>
              <include>**/*.json</include>
            </includes>
          </fileset>
        </filesets>
      </configuration>
    </execution>
  </executions>
</plugin>

```
Doing it this way means the plugin is *automatically* wired into the test lifecycle, and any integration test failures will be captured by the report and uploaded, before the build fails.

You can also manually run the plugin via `mvn xbdd:upload`.


# Examples

See the examples project for a comprehensive example of feature files, how they are wired up, tagging, and ways to run this plugin.

# Configuration

* `host` - the hostname of your XBDD server.
* `username` - username of the XBDD user to upload the reports as. It is best to pass it in on the commandline so that credentials are not visible. You can either have the XML config with a placeholder as per the example above and then build via `-Dxbdd.username=theusername`, or remove the line entirely and pass it in via -Dusername=theusername.
* `password` - password of the XBDD user to upload the reports as. It is best to pass it in on the commandline so that credentials are not visible. You can either have the XML config with a placeholder as per the example above and then build via `-Dxbdd.password=thepassword`, or remove the line entirely and pass it in via -Dpassword=thepassword.
* `projectKey` - optional. The name of the project in XBDD. If left blank it will default to the artifactId of the project from the POM.
* `projectVersion` - optional. The version of the project in XBDD. If left blank it will default to the version of the project from the POM.
* `buildNumber` - optional. If using a CI tool like Bamboo you might like to pass this in, otherwise it will default to epoch seconds.
* `filesets` - required. Standard fileset config for listing the json report files. Note that only `includes` are currently supported. Raise a ticket if you want excludes to be supported as well. Prior to 1.4, this property was `reports` and has a set of reports that you could upload, but this wasn't flexible enough and was dropped in the 1.6 release.
* `xbdd.skip` - optional. Set to true to skip plugin execution. Leave this out or set to false if it is to run normally.

# For developers (building)
Build and test: 
````
mvn clean install
````

# For developers (releasing)

Note that all release plugins and processes are contained in the `release` profile which must always be activated.

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

**Snapshot deploy** 
```
mvn clean deploy -P release
```
It should build and upload. Go to https://oss.sonatype.org/ and search for `xbdd-maven-plugin`. 

**Release deploy**
```
mvn release:clean release:prepare -P release
mvn release:perform -P release
```

**Troubleshooting**

If the `release:perform` step doesn't auto release, go to https://oss.sonatype.org/#stagingRepositories and look for errors and fix them.

# Release docs
* https://central.sonatype.org/pages/working-with-pgp-signatures.html
* https://central.sonatype.org/pages/apache-maven.html


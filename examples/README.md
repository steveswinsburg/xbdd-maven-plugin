# xbdd-maven-plugin examples

This is an example project that pushes results to an XBDD instance via the xbdd-maven-plugin

It has tests that always pass, tests that always fail, manual tests and skipped tests.

The idea is that in all cases, the report is generated and uploaded.

## Usage

```
mvn clean integration-test xbdd:upload \
-Dxbdd.host=YOUR_XBDD_HOST \
-Dxbdd.username=YOUR_XBDD_USERNAME \
-Dxbdd.password=YOUR_XBDD_PASSWORD
```


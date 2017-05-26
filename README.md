# Assert Collector

***Optimize bug-find rate, Regress Less and Execute Fast***


[![Build Status][travis-shield]][travis-link] [![Coverage Status][coveralls-shield]][coveralls-link]


### What is Assert Collector?

Do you often find more bugs after you fix one of the bugs and rerun the regression, late regressions? What if your regression finds more bugs from a single run?  Assert Collector does it. 

Traditional Assertions exit from your scenario when it finds the first bug, but Assert Collector does not. It finds a bug and continues to find more bugs. The best use cases are the verifications of UI & JSON Objects!

Assert Collector builds on top of [Hamcrest Matchers]([hamcrest-matchers) interface.
      
--------------------      

### How to use?

Please see [the detailed example test-case](https://github.com/gkushang/assert-collector/blob/master/src/test/java/assertcollector/AssertCollectorTest.java#L32-L86) for more info.


Step 1: `veirfyThat` in-place of `assertThat` of Hamcrest. The rest of the interface is Hamcrest.

```aidl
   
    verifyThat( "Firstname is invalid", something.getFirstname(), is( "validFirstname" ) );
   
```

Step 2: `throwAnyFailures` will throw all the failures at once, and whenever required

```aidl
   
    verifyThat( "Firstname is invalid", something.getFirstname(), is( "validFirstname" ) );
    verifyThat( "Lastname is invalid", something.getLastname(), is( "invalidLastname" ) );
    verifyThat( "Username is invalid", something.getusername(), is( "invalidUsername" ) );
    
    throwAnyFailures(); // will throw two failures. Lastname is invalid & Username is invalid
   
```

Step 3: Put `throwAnyFailures` in `@After` hook. All remaining failures will be thrown by After hook. 

```aidl

@After
public void tearDown()
{
    ...
    ...
    
    throwAnyFailures();
}

```

-------------------------


[travis-shield]: https://img.shields.io/travis/google/truth.png
[travis-link]: https://travis-ci.org/google/truth
[coveralls-shield]: https://coveralls.io/repos/github/gkushang/assert-collector/badge.svg?branch=master
[coveralls-link]: https://coveralls.io/github/gkushang/assert-collector?branch=master
[hamcrest-matchers]: http://hamcrest.org/JavaHamcrest/javadoc/1.3/org/hamcrest/Matchers.html
[example]: https://github.com/gkushang/assert-collector/blob/master/src/test/java/assertcollector/AssertCollectorTest.java#L32-L86

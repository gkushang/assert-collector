package assertcollector;

import static assertcollector.AssertCollector.throwAnyFailures;
import static assertcollector.AssertCollector.verifyThat;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import org.junit.After;
import org.junit.Test;

public class AssertCollectorTest
{
    @Test
    public void verifyThatWithMessageTest()
    {
        verifyThat( "error message", "Hello", is( "Hello" ) );
        throwAnyFailures();
    }

    @Test( expected = AssertionError.class )
    public void throwAnyFailuresTest()
    {
        verifyThat( "error message", 3, is( 2 ) );
        throwAnyFailures();
    }

    @Test
    public void throwAnyFailuresMessageTest()
    {
        verifyThat( "Greeting is incorrect", "Hi, there", is( "Hello, there" ) );
        try
        {
            throwAnyFailures();
        }
        catch ( AssertionError e )
        {
            assertThat( e.getMessage().trim(), is( "Number of failure(s) : 1\n" + "\n" + "Greeting is incorrect\n"
                            + "Expected: is \"Hello, there\"\n" + "     but: was \"Hi, there\"" ) );
        }
    }

    @Test
    public void verifyThatWithoutMessageTest()
    {
        verifyThat( "Hello", is( "Hello" ) );
        throwAnyFailures();
    }

    @Test
    public void getNumberOfErrors_noErrorsTest()
    {
        verifyThat( "Hello", is( "Hello" ) );
        assertThat( AssertCollector.getNumberOfErrors(), is( 0 ) );
    }

    @Test
    public void getNumberOfErrors_moreThanOneErrorsTest()
    {
        verifyThat( "Hello", is( "Hi" ) );
        verifyThat( "Awesome", is( "is not awesome" ) );
        assertThat( AssertCollector.getNumberOfErrors(), is( 2 ) );
    }

    @Test
    public void getNumberOfErrors_duplicateErrorsShouldCountTest()
    {
        verifyThat( "DRY", is( "Keep it DRY" ) );
        verifyThat( "DRY", is( "Keep it DRY" ) );
        assertThat( AssertCollector.getNumberOfErrors(), is( 2 ) );
    }

    @After
    public void tearDown()
    {
        try
        {
            throwAnyFailures();
        }
        catch ( AssertionError ignored )
        {
        }
    }

}

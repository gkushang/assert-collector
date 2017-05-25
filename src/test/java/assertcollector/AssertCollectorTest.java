package assertcollector;

import static assertcollector.AssertCollector.throwAnyFailures;
import static assertcollector.AssertCollector.verifyThat;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import utils.Account;

public class AssertCollectorTest
{
    private Account account;

    @Before
    public void setup()
                    throws IOException
    {
        ObjectMapper mapper = new ObjectMapper();
        final String accountJsonFileName = "account.json";
        account = mapper.readValue( getClass().getClassLoader().getResource( accountJsonFileName ), Account.class );
    }

    /*
        Below test verifies the JSON object presented in the /test/resources/account.json
        The Account object has 3 bugs
        The Goal is to capture all 3 bugs in single run & report all together
     */
    @Test
    public void regressTheAccount_jsonObject()
    {
        // ---- START the execution (your regression suite)

        // BUG with firstName. AssertCollector should capture the bug and should continue to capture more bugs
        verifyThat( "First name should be valid", account.firstName, is( "valid" ) );

        verifyThat( "Last name should be valid", account.lastName, is( "valid" ) );

        // BUG with username, and should be captured by AssertCollector
        verifyThat( "Username should be valid", account.username, is( "valid" ) );

        verifyThat( "Email should be provided", account.email, is( not( StringUtils.EMPTY ) ) );

        // BUG with mustBeTrue, and should be captured by AssertCollector
        verifyThat( "MustBeTrue should be what it is", account.mustBeTrue, is( true ) );

        // ---- ENDS the execution

        // Total bugs captured by AssertCollector should be 3 from above execution.
        // Traditional assertions, e.g. Assert, would have only captured & reported only ONE bug with firstName
        assertThat( "AssertCollector should capture 3 bugs", AssertCollector.getNumberOfErrors(), is( 3 ) );

        //throwAnyFailures should report the detailed error messages with all 3 bugs
        try
        {
            throwAnyFailures(); //throws all the errors captured during execution
        }
        catch ( Throwable e )
        {
            assertThat( e.getMessage().trim(),
                            is( "Number of failure(s) : 3\n\n" + "First name should be valid\nExpected: is \"valid\""
                                            + "\n     but: was \"invalid\"\n\n"
                                            + "Username should be valid\nExpected: is \"valid\""
                                            + "\n     but: was \"invalid\"\n\n"
                                            + "MustBeTrue should be what it is\nExpected: is <true>"
                                            + "\n     but: was <false>" ) );
        }
    }

    @Test
    public void verifyThatWithMessage_test()
    {
        verifyThat( "error message", "Hello", is( "Hello" ) );
        throwAnyFailures();
    }

    @Test( expected = AssertionError.class )
    public void throwAnyFailures_test()
    {
        verifyThat( "error message", 3, is( 2 ) );
        throwAnyFailures();
    }

    @Test
    public void throwAnyFailuresMessage_test()
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
    public void verifyThatWithoutMessage_test()
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

package assertcollector;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hamcrest.Matcher;

public final class AssertCollector
{
    static private List<Throwable> errors = new ArrayList<Throwable>();

    static private int numberOfErrors;

    static private final String NEW_LINES = "\n\n";

    static public <T> void verifyThat( final T value, final Matcher<T> matcher )
    {
        verifyThat( StringUtils.EMPTY, value, matcher );
    }

    static <T> void verifyThat( final String reason, final T value, final Matcher<T> matcher )
    {
        try
        {
            assertThat( reason, value, matcher );
        }
        catch ( Throwable e )
        {
            errors.add( e );
        }
    }

    static public void throwAnyFailures()
    {
        String failuresAsString = _getFailuresAndClear();

        if ( !StringUtils.isEmpty( failuresAsString ) )
            fail( "Number of failure(s) : " + numberOfErrors + NEW_LINES + failuresAsString );
    }

    static public int getNumberOfErrors()
    {
        return errors.size();
    }

    static private String _getFailuresAndClear()
    {
        StringBuilder failures = new StringBuilder();
        numberOfErrors = getNumberOfErrors();

        if ( numberOfErrors > 0 )
        {
            for ( Throwable t : errors )
                failures.append( t.getMessage() ).append( NEW_LINES );
            errors.clear();
        }

        return failures.toString();
    }
}

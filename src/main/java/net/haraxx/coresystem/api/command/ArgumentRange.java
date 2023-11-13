package net.haraxx.coresystem.api.command;

/**
 * @author Juyas
 * @version 12.11.2023
 * @since 12.11.2023
 */
public class ArgumentRange
{

    private final int min, max;

    private ArgumentRange( int min, int max )
    {
        this.min = Math.min( min, max );
        this.max = Math.max( min, max );
    }

    public int getMin()
    {
        return min;
    }

    public int getMax()
    {
        return max;
    }

    public boolean inRange( int args )
    {
        return args >= min && args <= max;
    }

    public ArgumentRange shiftUp()
    {
        return of( min + 1, max + 1 );
    }

    public static ArgumentRange of( int min, int max )
    {
        return new ArgumentRange( min, max );
    }

    public static ArgumentRange all()
    {
        return of( Integer.MIN_VALUE, Integer.MAX_VALUE - 100 );
    }

    public static ArgumentRange openMax( int min )
    {
        return of( min, Integer.MAX_VALUE - 100 );
    }

    public static ArgumentRange openMin( int max )
    {
        return of( Integer.MIN_VALUE, max );
    }

    public static ArgumentRange fix( int minMax )
    {
        return of( minMax, minMax );
    }

}

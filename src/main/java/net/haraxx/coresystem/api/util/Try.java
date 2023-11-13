package net.haraxx.coresystem.api.util;

/**
 * @author Juyas
 * @version 10.11.2023
 * @since 10.11.2023
 */
@SuppressWarnings("CallToPrintStackTrace")
public class Try
{

    public record Result<T>(T result, Exception fail) {}

    public interface SafeSupplier<T>
    {

        T get() throws Exception;
    }

    public interface SafeRunnable
    {

        void run() throws Exception;
    }

    public static void silent( SafeRunnable runnable )
    {
        try
        {
            runnable.run();
        }
        catch ( Exception ignored )
        {
        }
    }

    public static <T> T silent( SafeSupplier<T> supplier )
    {
        return silent( supplier, null );
    }

    public static <T> T silent( SafeSupplier<T> supplier, T defaultValue )
    {
        try
        {
            return supplier.get();
        }
        catch ( Exception ignored )
        {
        }
        return defaultValue;
    }

    public static void log( SafeRunnable runnable )
    {
        try
        {
            runnable.run();
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }

    public static <T> T log( SafeSupplier<T> supplier )
    {
        try
        {
            return supplier.get();
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
        return null;
    }

    public static Exception process( SafeRunnable runnable )
    {
        try
        {
            runnable.run();
            return null;
        }
        catch ( Exception e )
        {
            return e;
        }
    }

    public static <T> Result<T> process( SafeSupplier<T> supplier )
    {
        try
        {
            return new Result<>( supplier.get(), null );
        }
        catch ( Exception e )
        {
            return new Result<>( null, e );
        }
    }

}
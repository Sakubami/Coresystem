package net.haraxx.coresystem.api.util;

import net.haraxx.coresystem.CoreSystem;

import java.util.logging.Level;

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

    public static void trace( SafeRunnable runnable )
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

    public static <T> T trace( SafeSupplier<T> supplier )
    {
        return trace( supplier, null );
    }

    public static <T> T trace( SafeSupplier<T> supplier, T defaultValue )
    {
        try
        {
            return supplier.get();
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
        return defaultValue;
    }

    public static void log( SafeRunnable runnable, Level logLevel, String message )
    {
        try
        {
            runnable.run();
        }
        catch ( Exception e )
        {
            CoreSystem.getInstance().getLogger().log( logLevel, message, e );
        }
    }

    public static <T> T log( SafeSupplier<T> supplier, Level logLevel, String message )
    {
        return log( supplier, logLevel, message, null );
    }

    public static <T> T log( SafeSupplier<T> supplier, Level logLevel, String message, T defaultValue )
    {
        try
        {
            return supplier.get();
        }
        catch ( Exception e )
        {
            CoreSystem.getInstance().getLogger().log( logLevel, message, e );
        }
        return defaultValue;
    }

    public static void logRaw( SafeRunnable runnable )
    {
        try
        {
            runnable.run();
        }
        catch ( Exception e )
        {
            CoreSystem.getInstance().getLogger().log( Level.WARNING, e.getMessage(), e );
        }
    }

    public static <T> T logRaw( SafeSupplier<T> supplier )
    {
        return logRaw( supplier, null );
    }

    public static <T> T logRaw( SafeSupplier<T> supplier, T defaultValue )
    {
        try
        {
            return supplier.get();
        }
        catch ( Exception e )
        {
            CoreSystem.getInstance().getLogger().log( Level.WARNING, e.getMessage(), e );
        }
        return defaultValue;
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
        return process( supplier, null );
    }

    public static <T> Result<T> process( SafeSupplier<T> supplier, T defaultValue )
    {
        try
        {
            return new Result<>( supplier.get(), null );
        }
        catch ( Exception e )
        {
            return new Result<>( defaultValue, e );
        }
    }

}
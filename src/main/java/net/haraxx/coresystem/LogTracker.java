package net.haraxx.coresystem;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class LogTracker extends Handler
{

    private final List<LogRecord> trackedRecords;

    public LogTracker()
    {
        this.trackedRecords = new ArrayList<>();
    }

    public List<LogRecord> getTrackedRecords()
    {
        return trackedRecords;
    }

    @Override
    public void publish( LogRecord record )
    {
        trackedRecords.add( record );
    }

    @Override
    public void flush()
    {
        //nothing
    }

    @Override
    public void close() throws SecurityException
    {

    }

    public LogTracker onlyExceptions()
    {
        setFilter( record -> record.getThrown() != null );
        return this;
    }

    public LogTracker onlyInClass( String className )
    {
        setFilter( record -> className.equals( record.getSourceClassName() ) );
        return this;
    }

}
package net.haraxx.coresystem.api.data.query;

import net.haraxx.coresystem.api.data.base.ColumnSettings;
import net.haraxx.coresystem.api.data.base.ModelBase;
import net.haraxx.coresystem.api.data.model.*;
import org.junit.*;

import java.util.UUID;

/**
 * @author Juyas
 * @version 23.11.2023
 * @since 23.11.2023
 */
public class SQLStatementGeneratorTest
{

    @Model(schema = "demo", table = "model1")
    private static class DemoModel1 extends ModelBase
    {

        @DatabaseColumn(name = "id", sqlType = "INT")
        private PrimaryKey key;
        @DatabaseColumn(name = "score", sqlType = "INT")
        private Column<Integer> anyNumber;
        @DatabaseColumn(name = "desc", sqlType = "VARCHAR(255)", nonNull = false)
        private Column<String> description;
        @DatabaseColumn(name = "ratio", sqlType = "FLOAT")
        private Column<Float> someFloatyStuff;
        @DatabaseColumn(name = "uniqueId", sqlType = "CHAR(36)", unique = true)
        private Column<String> uniqueId;

    }

    private static UUID uuid = UUID.randomUUID();

    public static final String EXPECTED_SCHEMA = "IF NOT EXISTS (SELECT * FROM sys.schemas WHERE name = N'demo') CREATE SCHEMA demo;";
    public static final String EXPECTED_TABLE = "IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE " +
            "TABLE_SCHEMA = N'demo' AND TABLE_NAME = N'model1') CREATE TABLE demo.model1 (id INT PRIMARY KEY AUTO_INCREMENT," +
            "score INT NOT NULL,desc VARCHAR(255),ratio FLOAT NOT NULL,uniqueId CHAR(36) UNIQUE NOT NULL);";
    static final String EXPECTED_DELETE = "DELETE FROM demo.model1 WHERE id=3;";
    static final String EXPECTED_REQUEST_ALL = "SELECT score,desc,ratio,uniqueId FROM demo.model1 WHERE id=3;";
    static final String EXPECTED_REQUEST_SCORE = "SELECT score FROM demo.model1 WHERE id=3;";
    static final String EXPECTED_KEY_REQUEST = "SELECT id FROM demo.model1 WHERE uniqueId=N'" + uuid + "';";
    static final String EXPECTED_PRECISE_KEY_REQUEST = "SELECT id FROM demo.model1 WHERE score=55 AND desc=N'Some description text.' AND ratio=2.5 AND uniqueId=N'" + uuid + "';";
    static final String EXPECTED_UPDATE_SCORE = "UPDATE demo.model1 SET score=55 WHERE id=3;";
    static final String EXPECTED_INSERT_ALL = "INSERT INTO demo.model1 (score,desc,ratio,uniqueId) VALUES (55,N'Some description text.',2.5,N'" + uuid + "');";

    private DemoModel1 data;
    private SQLStatementGenerator generator;

    @Before
    public void setUp() throws Exception
    {
        this.generator = new SQLStatementGenerator();
        data = ModelBase.generate( DemoModel1.class );
        data.key.value().set( 3L );
        data.anyNumber.value().set( 55 );
        data.uniqueId.value().set( uuid.toString() );
        data.someFloatyStuff.value().set( 2.5f );
        data.description.value().set( "Some description text." );
    }

    @Test
    public void createSchema()
    {
        String statement = generator.createSchema( data.getModel().modelSchema() );
        Assert.assertEquals( EXPECTED_SCHEMA, statement );
    }

    @Test
    public void createTable()
    {
        ColumnSettings[] array = data.getModel().columns().stream().map( ModelProperty::settings ).toArray( ColumnSettings[]::new );
        String statement = generator.createTable( data.getModel().modelSchema(), data.getModel().modelName(), data.getModel().primaryKey(), array );
        Assert.assertEquals( EXPECTED_TABLE, statement );
    }

    @Test
    public void delete()
    {
        String statement = generator.delete( data.getModel().modelSchema(), data.getModel().modelName(), data.getModel().primaryKey() );
        Assert.assertEquals( EXPECTED_DELETE, statement );
    }

    @Test
    public void request()
    {
        String requestAll = generator.request( data.getModel().modelSchema(), data.getModel().modelName(), data.getModel().primaryKey(), data.getModel().columns().toArray( new Column[0] ) );
        String scoreRequest = generator.request( data.getModel().modelSchema(), data.getModel().modelName(), data.getModel().primaryKey(), "score" );
        Assert.assertEquals( EXPECTED_REQUEST_ALL, requestAll );
        Assert.assertEquals( EXPECTED_REQUEST_SCORE, scoreRequest );
    }

    @Test
    public void requestKey()
    {
        String statement = generator.requestKey( data.getModel().primaryKey(), data.getModel().modelSchema(), data.getModel().modelName(), data.getModel().getColumn( "uniqueId" ) );
        Assert.assertEquals( EXPECTED_KEY_REQUEST, statement );
    }

    @Test
    public void requestKeyPrecisely()
    {
        String statement = generator.requestKeyPrecisely( data.getModel().primaryKey(), data.getModel().modelSchema(), data.getModel().modelName(), data.getModel().columns().toArray( new Column[0] ) );
        Assert.assertEquals( EXPECTED_PRECISE_KEY_REQUEST, statement );
    }

    @Test
    public void update()
    {
        String statement = generator.update( "score", data.anyNumber.value(), data.getModel().modelSchema(), data.getModel().modelName(), data.getModel().primaryKey() );
        Assert.assertEquals( EXPECTED_UPDATE_SCORE, statement );
    }

    @Test
    public void put()
    {
        String statement = generator.put( data.getModel().modelSchema(), data.getModel().modelName(), data.getModel().columns().toArray( new Column[0] ) );
        Assert.assertEquals( EXPECTED_INSERT_ALL, statement );
    }

}
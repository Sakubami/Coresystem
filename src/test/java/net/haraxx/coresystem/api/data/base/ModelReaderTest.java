package net.haraxx.coresystem.api.data.base;

import net.haraxx.coresystem.api.data.model.*;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Juyas
 * @version 22.11.2023
 * @since 22.11.2023
 */
public class ModelReaderTest
{

    @Model(schema = "demo", table = "model1")
    public static class DemoModel1 extends ModelBase
    {

        @DatabaseColumn(name = "id", sqlType = "INT")
        private PrimaryKey key;
        @DatabaseColumn(name = "score", sqlType = "INT")
        private Column<Integer> anyNumber;
        @DatabaseColumn(name = "desc", sqlType = "VARCHAR(255)")
        private Column<String> description;

    }

    @Model(schema = "demo", table = "")
    public static class DemoModelFail1 extends ModelBase
    {

        @DatabaseColumn(name = "id", sqlType = "INT")
        private PrimaryKey key;
        @DatabaseColumn(name = "score", sqlType = "INT")
        private Column<Integer> anyNumber;
        @DatabaseColumn(name = "desc", sqlType = "VARCHAR(255)")
        private Column<String> description;

    }

    @Model(schema = "demo", table = "fail2")
    public static class DemoModelFail2 extends ModelBase
    {

        @DatabaseColumn(name = "id", sqlType = "INT")
        private long key;
        @DatabaseColumn(name = "score", sqlType = "INT")
        private Column<Integer> anyNumber;
        @DatabaseColumn(name = "desc", sqlType = "VARCHAR(255)")
        private Column<String> description;

    }

    @Model(schema = "demo", table = "fail3")
    public static class DemoModelFail3 extends ModelBase
    {

        private PrimaryKey key;
        @DatabaseColumn(name = "score", sqlType = "INT")
        private Column<Integer> anyNumber;
        @DatabaseColumn(name = "desc", sqlType = "VARCHAR(255)")
        private Column<String> description;

    }

    public static class DemoModelFail4 extends ModelBase
    {

        @DatabaseColumn(name = "id", sqlType = "INT")
        private PrimaryKey key;
        @DatabaseColumn(name = "score", sqlType = "INT")
        private Column<Integer> anyNumber;
        @DatabaseColumn(name = "desc", sqlType = "VARCHAR(255)")
        private Column<String> description;

    }

    @Model(schema = "demo", table = "fail5")
    public static class DemoModelFail5 extends ModelBase
    {

        @DatabaseColumn(name = "id", sqlType = "INT")
        private PrimaryKey key;
        @DatabaseColumn(name = "", sqlType = "INT")
        private Column<Integer> anyNumber;
        @DatabaseColumn(name = "desc", sqlType = "VARCHAR(255)")
        private Column<String> description;

    }

    @Model(schema = "demo", table = "fail6")
    public static class DemoModelFail6 extends ModelBase
    {

        private DemoModelFail6( String init )
        {
        }

        @DatabaseColumn(name = "id", sqlType = "INT")
        private PrimaryKey key;
        @DatabaseColumn(name = "", sqlType = "INT")
        private Column<Integer> anyNumber;
        @DatabaseColumn(name = "desc", sqlType = "VARCHAR(255)")
        private Column<String> description;

    }

    @Test
    public void buildModel1() throws Exception
    {

        DemoModel1 model1 = ModelReader.buildModel( DemoModel1.class );
        Assert.assertNotNull( model1 );
        DataModel model = model1.getModel();
        Assert.assertNotNull( model );
        Assert.assertNotNull( model.primaryKey() );
        Assert.assertNotNull( model.columns() );
        Assert.assertNotNull( model.primaryKey().settings() );

        Assert.assertEquals( "demo", model.modelSchema() );
        Assert.assertEquals( "model1", model.modelName() );

        Assert.assertEquals( "id", model.primaryKey().settings().columnName() );
        Assert.assertEquals( "INT", model.primaryKey().settings().sqlType() );

        Assert.assertTrue( model.columns().containsKey( "score" ) );
        Assert.assertEquals( "INT", model.columns().get( "score" ).settings().sqlType() );
        Assert.assertTrue( model.columns().containsKey( "desc" ) );
        Assert.assertEquals( "VARCHAR(255)", model.columns().get( "desc" ).settings().sqlType() );

        Assert.assertNotNull( model1.key );
        Assert.assertNotNull( model1.anyNumber );
        Assert.assertNotNull( model1.description );

        Assert.assertNotNull( model1.key.value() );
        Assert.assertNotNull( model1.anyNumber.value() );
        Assert.assertNotNull( model1.description.value() );

        Assert.assertFalse( model1.key.value().get().isPresent() );
        Assert.assertFalse( model1.anyNumber.value().isCached() );
        Assert.assertFalse( model1.description.value().isCached() );

    }

    @Test
    public void buildFails()
    {
        Assert.assertThrows( ModelConstraintsDissatisfiedException.class, () -> ModelReader.buildModel( DemoModelFail1.class ) );
        Assert.assertThrows( ModelConstraintsDissatisfiedException.class, () -> ModelReader.buildModel( DemoModelFail2.class ) );
        Assert.assertThrows( ModelConstraintsDissatisfiedException.class, () -> ModelReader.buildModel( DemoModelFail3.class ) );
        Assert.assertThrows( ModelConstraintsDissatisfiedException.class, () -> ModelReader.buildModel( DemoModelFail4.class ) );
        Assert.assertThrows( ModelConstraintsDissatisfiedException.class, () -> ModelReader.buildModel( DemoModelFail5.class ) );
        Assert.assertThrows( ModelConstraintsDissatisfiedException.class, () -> ModelReader.buildModel( DemoModelFail6.class ) );
    }

}
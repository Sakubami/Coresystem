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
    private static class DemoModel extends ModelBase
    {

        @DatabaseColumn(name = "id", sqlType = "INT")
        private PrimaryKey key;
        @DatabaseColumn(name = "score", sqlType = "INT")
        private Column<Integer> anyNumber;
        @DatabaseColumn(name = "desc", sqlType = "VARCHAR(255)")
        private Column<String> description;

    }

    @Model(schema = "demo", table = "")
    private static class DemoModelFail1 extends ModelBase
    {

        @DatabaseColumn(name = "id", sqlType = "INT")
        private PrimaryKey key;
        @DatabaseColumn(name = "score", sqlType = "INT")
        private Column<Integer> anyNumber;
        @DatabaseColumn(name = "desc", sqlType = "VARCHAR(255)")
        private Column<String> description;

    }

    @Model(schema = "demo", table = "fail2")
    private static class DemoModelFail2 extends ModelBase
    {

        @DatabaseColumn(name = "id", sqlType = "INT")
        private long key;
        @DatabaseColumn(name = "score", sqlType = "INT")
        private Column<Integer> anyNumber;
        @DatabaseColumn(name = "desc", sqlType = "VARCHAR(255)")
        private Column<String> description;

    }

    @Model(schema = "demo", table = "fail3")
    private static class DemoModelFail3 extends ModelBase
    {

        private PrimaryKey key;
        @DatabaseColumn(name = "score", sqlType = "INT")
        private Column<Integer> anyNumber;
        @DatabaseColumn(name = "desc", sqlType = "VARCHAR(255)")
        private Column<String> description;

    }

    private static class DemoModelFail4 extends ModelBase
    {

        @DatabaseColumn(name = "id", sqlType = "INT")
        private PrimaryKey key;
        @DatabaseColumn(name = "score", sqlType = "INT")
        private Column<Integer> anyNumber;
        @DatabaseColumn(name = "desc", sqlType = "VARCHAR(255)")
        private Column<String> description;

    }

    @Model(schema = "demo", table = "fail5")
    private static class DemoModelFail5 extends ModelBase
    {

        @DatabaseColumn(name = "id", sqlType = "INT")
        private PrimaryKey key;
        @DatabaseColumn(name = "", sqlType = "INT")
        private Column<Integer> anyNumber;
        @DatabaseColumn(name = "desc", sqlType = "VARCHAR(255)")
        private Column<String> description;

    }

    @Model(schema = "demo", table = "fail6")
    private static class DemoModelFail6 extends ModelBase
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

        DemoModel model1 = ModelReader.buildModel( DemoModel.class );
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

        Assert.assertNotNull( model.getColumn( "score" ) );
        Assert.assertEquals( "INT", model.getColumn( "score" ).settings().sqlType() );
        Assert.assertNotNull( model.getColumn( "desc" ) );
        Assert.assertEquals( "VARCHAR(255)", model.getColumn( "desc" ).settings().sqlType() );

        Assert.assertNotNull( model1.key );
        Assert.assertNotNull( model1.anyNumber );
        Assert.assertNotNull( model1.description );

        Assert.assertNotNull( model1.key.value() );
        Assert.assertNotNull( model1.anyNumber.value() );
        Assert.assertNotNull( model1.description.value() );

        Assert.assertFalse( model1.key.value().get().isPresent() );
        Assert.assertFalse( model1.anyNumber.value().isCached() );
        Assert.assertFalse( model1.description.value().isCached() );

        Assert.assertEquals( Long.class, model1.key.javaType() );
        Assert.assertEquals( Integer.class, model1.anyNumber.javaType() );
        Assert.assertEquals( String.class, model1.description.javaType() );

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
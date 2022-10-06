package org.example.JdbcDtoRM.tests.doubles;

import org.example.JdbcDtoRM.JDBCConnectionProvider;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class JDBCConnectionProviderMock implements JDBCConnectionProvider {

    private static volatile Connection connection;
    private static Connection getConnection()
    {
        if(connection==null)
        {
            synchronized (JDBCConnectionProviderMock.class)
            {
                if(connection==null)
                {
                    try {
                        var driver = org.h2.Driver.load();
                        connection = driver.connect("jdbc:h2:mem:test;DATABASE_TO_UPPER=FALSE",new Properties());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }

            }
        }

        return connection;
    }

    @Override
    public  Connection provide() {
        return getConnection();
    }

    public boolean doesIntegerColumnExistInTable(String table, String column) throws Exception{
        ResultSet set = getConnection().getMetaData().getColumns(null, null, table, column);
        set.next();
        String typeName = set.getString("TYPE_NAME");
        set.close();

        return (typeName!=null && typeName.equals("INTEGER"));
    }

    public boolean doesStringColumnExistInTable(String table, String column) throws Exception {
        ResultSet set = getConnection().getMetaData().getColumns(null, null, table, column);
        set.next();
        String typeName = set.getString("TYPE_NAME");
        set.close();

        return (typeName!=null && typeName.equals("CHARACTER VARYING"));
    }

    public boolean doesTableExist(String name) throws SQLException {
        ResultSet set = getConnection().getMetaData().getTables(null, null, name, null);
        var exists = set.next();
        set.close();
        return exists;
    }

    public void cleanup() {
        try {
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        connection = null;
    }
}

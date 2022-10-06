package org.example.JdbcDtoRM;

import java.sql.Connection;

public interface JDBCConnectionProvider {

    Connection provide();

}

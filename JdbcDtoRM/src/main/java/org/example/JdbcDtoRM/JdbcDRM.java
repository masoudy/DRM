package org.example.JdbcDtoRM;

import org.example.dtomapper.DTODefinition;
import org.example.dtomapper.DTOMapper;
import org.example.dtomapper.field.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class JdbcDRM {

    private final String packageFQN;
    private final boolean createTablesIfNotExist;
    private final JDBCConnectionProvider provider;
    private DTOMapper dtoMapper;

    private JdbcDRM(String packageFQN, boolean createTablesIfNotExist, JDBCConnectionProvider provider){
        this.packageFQN = packageFQN;
        this.createTablesIfNotExist=createTablesIfNotExist;
        this.provider = provider;
        tryInitializing();
    }

    private void tryInitializing(){
        try {
            initialize();
        } catch (Exception e) {
            if(e instanceof RuntimeException)
                throw (RuntimeException)e;

            throw new RuntimeException(e);
        }
    }

    private void initialize() throws Exception{
        this.dtoMapper= DTOMapper
                .builder()
                .scanPackageForObjects(packageFQN)
                .build();

        for(Class c :dtoMapper.allAnnotatedClasses())
            createTableIfNotExists(c);
    }

    private void createTableIfNotExists(Class c) throws Exception{
        var def = DTOMapper.extractDTODefinitionFor(c);
        createTableIfNotExistFor(def);
    }

    private void createTableIfNotExistFor(DTODefinition def) throws Exception {
        StringBuilder createSQL = new StringBuilder("create table if not exists " + def.getName() + " (");

        if(!def.hasIdField())
            throw new DTOShouldHaveSingleFieldAnnotatedAsIdentifier(def);

        for(FieldDefinition fd : def.allFieldDefinitions())
        {
            if(fd instanceof StringFieldDefinition)
            {
                if(createSQL.indexOf("\n")>0)
                    createSQL.append(",");

                createSQL.append("\n    ").append(fd.getName()).append("    varchar(255)");
            }
            else if(fd instanceof IntegerFieldDefinition)
            {
                if(createSQL.indexOf("\n")>0)
                    createSQL.append(",");

                createSQL.append("\n    ").append(fd.getName()).append("    int");
            }
            else if(fd instanceof DTOFieldDefinition dfd)
            {
                DTODefinition d = dfd.dtoDefinition();
                createTableIfNotExistFor(d);

                if(createSQL.indexOf("\n")>0)
                    createSQL.append(",");

                if(d.idField() instanceof IntegerFieldDefinition)
                    createSQL.append("\n    ").append(calculateForeignKeyTitle(dfd)).append("    int");
                else if(d.idField() instanceof StringFieldDefinition)
                    createSQL.append("\n    ").append(calculateForeignKeyTitle(dfd)).append("    varchar(255)");

            }else if(fd instanceof DTOListFieldDefinition dlfd)
            {
                DTODefinition d = ((DTOListFieldDefinition) fd).dtoDefinition();
                createTableIfNotExistFor(d);
                addForeignKey(def,d,dlfd);
            }
            else
                throw new RuntimeException("not supported "+fd);
        }

        createSQL.append("\n);");

        System.out.println("creating :\n"+createSQL+"\n\n");

        var connection = provider.provide();
        var statement = connection.createStatement();
        statement.execute(createSQL.toString());
        statement.close();
    }

    private String calculateForeignKeyTitle(DTOFieldDefinition fd) {
        return fd.getName() + "_" + fd.dtoDefinition().getName() + "_" + fd.dtoDefinition().idField().getName() + "_fk";
    }

    private String calculateForeignKeyTitle(DTODefinition parentDTO,DTOListFieldDefinition dlfd)
    {
        return "_"+
                parentDTO.getName() +
                "_"
                + parentDTO.idField().getName()+
                "_fk ";
    }
    private void addForeignKey(DTODefinition parentDTO,DTODefinition childDTO,DTOListFieldDefinition childFieldInsideParent) throws Exception {

        String sql =
                "Alter table " + childDTO.getName() +
                        "\n add " +
                        calculateForeignKeyTitle(parentDTO,childFieldInsideParent);

        if(parentDTO.idField() instanceof IntegerFieldDefinition)
            sql+=" int;";
        else
            sql+=" varchar(255);";

        System.out.println(sql+"\n\n");

        var connection = provider.provide();
        var statement = connection.createStatement();
        statement.execute(sql);
        statement.close();
    }


    public static JdbcDRM.Builder builder() {
        return new JdbcDRM.Builder();
    }

    public void save(Object dto) {

        try {
            save(dto,null,null);
        }catch (Exception e)
        {
            if(e instanceof RuntimeException)
                throw (RuntimeException)e;

            throw new RuntimeException(e);
        }
    }

    private void save(Object dto,Object foreignKeyValue,String foreignKeyTitle) throws Exception {
        var def = DTOMapper.extractDTODefinitionFor(dto.getClass());
        var dtoIdValue = def.idField().getValueInsideObject(dto);

        var sql = new StringBuilder("insert into " + def.getName() + " (");
        var argList = new StringBuilder();
        var valueList = new StringBuilder();

        for(FieldDefinition fd : def.allFieldDefinitions())
        {
            if(fd instanceof StringFieldDefinition)
            {
                argList.append(",").append(fd.getName());
                valueList.append(",").append("'").append(((StringFieldDefinition) fd).getValueInsideObject(dto)).append("'");
            }
            else if(fd instanceof IntegerFieldDefinition)
            {
                argList.append(",").append(fd.getName());
                valueList.append(",").append(((IntegerFieldDefinition) fd).getValueInsideObject(dto));
            }
            else if(fd instanceof DTOFieldDefinition dfd)
            {
                var innerDTO = dfd.getValueInsideObject(dto);
                var innerDTOId = dfd.dtoDefinition().idField().getValueInsideObject(innerDTO);

                argList.append(",").append(calculateForeignKeyTitle(dfd));

                if(innerDTOId instanceof Number)
                    valueList.append(",").append(innerDTOId);
                else if(innerDTOId instanceof String)
                    valueList.append(",").append("'").append(innerDTOId).append("'");

                save(innerDTO);

            }else if(fd instanceof DTOListFieldDefinition dlfd)
            {
                List list = dlfd.getValueInsideObject(dto);
                for(Object e : list)
                    save(e,dtoIdValue,calculateForeignKeyTitle(def,dlfd));
            }
            else
                throw new RuntimeException("not supported "+fd);
        }

        if(foreignKeyTitle!=null && !foreignKeyTitle.isBlank())
        {
            argList.append(",").append(foreignKeyTitle);

            if(foreignKeyValue instanceof Number)
                valueList.append(",").append(foreignKeyValue);
            else if(foreignKeyValue instanceof String)
                valueList.append(",").append("'").append(foreignKeyValue).append("'");
        }


        sql.append(argList.deleteCharAt(0)).append(")");
        sql.append(" values ").append("(").append(valueList.deleteCharAt(0)).append(")");

        System.out.println("inserting :\n"+sql+"\n\n");

        var connection = provider.provide();
        var statement = connection.createStatement();
        statement.execute(sql.toString());
        statement.close();
    }

    public <T> T findById(String id, Class<T> dtoType) {

        try {
            return findByIdd(id,dtoType);
        }catch (Exception e)
        {
            if(e instanceof RuntimeException)
                throw (RuntimeException)e;

            throw new RuntimeException(e);
        }
    }

    public <T> T findById(Number id, Class<T> dtoType) {

        try {
            return findByIdd(id,dtoType);
        }catch (Exception e)
        {
            if(e instanceof RuntimeException)
                throw (RuntimeException)e;

            throw new RuntimeException(e);
        }
    }

    private  <T> T findByIdd(Object id, Class<T> dtoType) throws Exception
    {
        var def = DTOMapper.extractDTODefinitionFor(dtoType);
        var json = selectFromTable(def,id);

        json = replaceOneToOneForeignKeysWithJsonObjects(json,def);
        json = retrieveAndAddOneToManyObjectsToJson(json,def);

        return (T)def.convertCandidateJsonToObject(json);
    }

    private JSONObject replaceOneToOneForeignKeysWithJsonObjects(JSONObject json, DTODefinition def) throws Exception {

        for(FieldDefinition fd : def.allFieldDefinitions())
        {
            if(fd instanceof DTOFieldDefinition dfd)
            {
                var innerDTODefinition = dfd.dtoDefinition();
                var foreignKeyTitle = calculateForeignKeyTitle(dfd);
                var foreignKeyValue = json.get(foreignKeyTitle);
                var innerJson = selectFromTable(innerDTODefinition,foreignKeyValue);
                json.remove(foreignKeyTitle);
                json.put(dfd.getName(),innerJson);
            }
        }

        return json;
    }

    private JSONObject retrieveAndAddOneToManyObjectsToJson(JSONObject json, DTODefinition def) throws Exception {
        for(FieldDefinition fd : def.allFieldDefinitions())
        {
            if(fd instanceof DTOListFieldDefinition dlfd)
            {
                var foreignKeyTitle = calculateForeignKeyTitle(def,dlfd);
                Object foreignKeyValue = json.get(def.idField().getName());
                var innerDTOTableName = dlfd.dtoDefinition().getName();

                var jsonArray = selectFromTable(innerDTOTableName,foreignKeyTitle,foreignKeyValue);

                json.put(fd.getName(),jsonArray);
            }
        }

        return json;
    }


    private JSONObject selectFromTable(DTODefinition def,Object id) throws Exception
    {
        String tableName = def.getName();
        String idColumnTitle = def.idField().getName();
        var jsonArray = selectFromTable(tableName,idColumnTitle,id);

        if(jsonArray.isEmpty())
            throw new RuntimeException("No Object Found with id "+id+" and type "+def.getName());

        return jsonArray.getJSONObject(0);
    }

    private JSONArray selectFromTable(String tableName,String whereColumnKey, Object whereColumnValue) throws Exception
    {
        var connection = provider.provide();
        var statement = connection.createStatement();
        String sql = "select * from " + tableName + " where "
                + whereColumnKey
                + "="
                + (whereColumnValue instanceof String ? "'" : "")
                + whereColumnValue
                + (whereColumnValue instanceof String ? "'" : "");

        System.out.println("\nSelect SQL:\n"+sql+"\n\n");

        var set = statement.executeQuery(sql);

        var jsonArray = new JSONArray();
        while(set.next())
        {
            var json = new JSONObject();
            for(int i = 1; i<=set.getMetaData().getColumnCount();i++)
            {
                String fieldName = set.getMetaData().getColumnName(i);
                json.put(fieldName,extractValue(set,fieldName));
            }
            jsonArray.put(json);
        }

        set.close();
        statement.close();

        return jsonArray;
    }

    private Object extractValue(ResultSet set,String columnName) throws Exception {
        try {
            return set.getBigDecimal(columnName).toBigIntegerExact();
        }catch (Exception e)
        {
            return set.getString(columnName);
        }
    }


    public static class Builder{

        private String packageFQN;
        private boolean create;
        private JDBCConnectionProvider provider;

        public JdbcDRM.Builder scanPackageForObjects(String packageFQN) {
            this.packageFQN = packageFQN;
            return this;
        }

        public JdbcDRM build() {
            return new JdbcDRM(packageFQN,create,provider);
        }

        public Builder createTablesIfNotExist(boolean create) {
            this.create = create;
            return this;
        }

        public Builder connectionProvider(JDBCConnectionProvider provider) {
            this.provider = provider;
            return this;
        }
    }


}
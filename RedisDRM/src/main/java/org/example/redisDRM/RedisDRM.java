package org.example.redisDRM;

import org.example.dtomapper.DTOMapper;
import org.json.JSONObject;
import redis.clients.jedis.Jedis;

public class RedisDRM {
    private final String packageFQN;
    private final String host;
    private final int port;
    private  Jedis jedis;
    private DTOMapper dtoMapper;

    private RedisDRM(String packageFQN,String host,int port){
        this.packageFQN = packageFQN;
        this.host = host;
        this.port  = port;
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

    private void initialize() throws Exception {

        jedis = new Jedis(host,port);

        this.dtoMapper = DTOMapper
                .builder()
                .scanPackageForObjects(packageFQN)
                .build();
    }


    public static RedisDRM.Builder builder() {
        return new RedisDRM.Builder();
    }

    public void save(Object dto) {

        try {
            saveToRedis(dto);
        }catch (Exception e)
        {
            if(e instanceof RuntimeException)
                throw (RuntimeException)e;

            throw new RuntimeException(e);
        }
    }

    private void saveToRedis(Object dto) throws Exception {
        var def = DTOMapper.extractDTODefinitionFor(dto.getClass());
        var dtoIdValue = def.idField().getValueInsideObject(dto);
        var json = dtoMapper.toJson(dto).toString();
        jedis.set(dtoIdValue+"",json);
    }

    public <T> T findById(String id, Class<T> dtoType) {

        try {
            return findByIdFromRedis(id,dtoType);
        }catch (Exception e)
        {
            if(e instanceof RuntimeException)
                throw (RuntimeException)e;

            throw new RuntimeException(e);
        }
    }

    public <T> T findById(Number id, Class<T> dtoType) {

        try {
            return findByIdFromRedis(id,dtoType);
        }catch (Exception e)
        {
            if(e instanceof RuntimeException)
                throw (RuntimeException)e;

            throw new RuntimeException(e);
        }
    }

    private  <T> T findByIdFromRedis(Object id, Class<T> dtoType) throws Exception
    {
        var def = DTOMapper.extractDTODefinitionFor(dtoType);
        JSONObject json = new JSONObject(jedis.get(id+""));
        return (T)def.convertCandidateJsonToObject(json);
    }



    public void cleanup() {
        jedis.close();
    }


    public static class Builder{

        private String packageFQN;
        private String host;
        private int port;


        public RedisDRM.Builder scanPackageForObjects(String packageFQN) {
            this.packageFQN = packageFQN;
            return this;
        }

        public RedisDRM.Builder host(String host) {
            this.host = host;
            return this;
        }

        public RedisDRM.Builder port(int port) {
            this.port = port;
            return this;
        }

        public RedisDRM build() {
            return new RedisDRM(packageFQN,host,port);
        }
    }


}

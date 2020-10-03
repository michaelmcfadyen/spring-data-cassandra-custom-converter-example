package com.example.demo;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.datastax.oss.simulacron.common.cluster.NodeSpec;
import com.datastax.oss.simulacron.common.stubbing.EmptyReturnMetadataHandler;
import com.datastax.oss.simulacron.common.stubbing.PrimeDsl;
import com.datastax.oss.simulacron.server.BoundNode;
import com.datastax.oss.simulacron.server.Server;
import com.datastax.oss.simulacron.server.StubStore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static com.datastax.oss.simulacron.driver.SimulacronDriverSupport.defaultBuilder;
import static org.assertj.core.api.Assertions.assertThat;

public class MyTest {

    @Test
    void primeKeyspace() throws JsonProcessingException {
        Server server = Server.builder().withStubStore(stubStore()).build();
        try (BoundNode boundNode = server.register(NodeSpec.builder().build())) {
            Cluster cluster = defaultBuilder(boundNode).build();
            Session session = cluster.connect();

            assertThat(session.getCluster().getMetadata().getKeyspace("test")).isNotNull();
        }
    }

    private StubStore stubStore() throws JsonProcessingException {
        StubStore stubStore = new StubStore();
        stubStore.register(new EmptyReturnMetadataHandler("SELECT * FROM system_schema.keyspaces"));
        stubStore.register(new EmptyReturnMetadataHandler("SELECT * FROM system_schema.views"));
        stubStore.register(new EmptyReturnMetadataHandler("SELECT * FROM system_schema.tables"));
        stubStore.register(new EmptyReturnMetadataHandler("SELECT * FROM system_schema.columns"));
        stubStore.register(new EmptyReturnMetadataHandler("SELECT * FROM system_schema.indexes"));
        stubStore.register(new EmptyReturnMetadataHandler("SELECT * FROM system_schema.triggers"));
        stubStore.register(new EmptyReturnMetadataHandler("SELECT * FROM system_schema.types"));
        stubStore.register(new EmptyReturnMetadataHandler("SELECT * FROM system_schema.functions"));
        stubStore.register(new EmptyReturnMetadataHandler("SELECT * FROM system_schema.aggregates"));
        stubStore.register(new EmptyReturnMetadataHandler("SELECT * FROM system_schema.views"));
//        stubStore.register(new EmptyReturnMetadataHandler("SELECT * FROM system.schema_keyspaces"));
        stubStore.register(new EmptyReturnMetadataHandler("SELECT * FROM system.schema_columnfamilies"));
        stubStore.register(new EmptyReturnMetadataHandler("SELECT * FROM system.schema_columns"));
        stubStore.register(new EmptyReturnMetadataHandler("SELECT * FROM system.schema_triggers"));
        stubStore.register(new EmptyReturnMetadataHandler("SELECT * FROM system.schema_usertypes"));
        stubStore.register(new EmptyReturnMetadataHandler("SELECT * FROM system.schema_functions"));
        stubStore.register(new EmptyReturnMetadataHandler("SELECT * FROM system.schema_aggregates"));
        stubStore.register(new EmptyReturnMetadataHandler("SELECT * FROM system_virtual_schema.keyspaces"));
        stubStore.register(new EmptyReturnMetadataHandler("SELECT * FROM system_virtual_schema.columns"));
        stubStore.register(new EmptyReturnMetadataHandler("SELECT * FROM system_virtual_schema.tables"));


        HashMap<String, String> map = new HashMap<String, String>() {{
            put("strategy_class", "org.apache.cassandra.locator.SimpleStrategy");
            put("replication_factor", "1");
        }};

        ObjectMapper objectMapper = new ObjectMapper();
        stubStore.register(PrimeDsl.when("SELECT * FROM system.schema_keyspaces")
                .then(PrimeDsl.rows()
                        .columnTypes("keyspace_name", "varchar", "durable_writes", "boolean",
                                "strategy_class", "varchar", "strategy_options", "varchar")
                        .row("keyspace_name", "test", "durable_writes", "true", "strategy_options", objectMapper.writeValueAsString(map),
                                "strategy_class", "org.apache.cassandra.locator.SimpleStrategy"))
                .build());
        return stubStore;
    }
}

package com.example.demo;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.datastax.oss.driver.shaded.guava.common.collect.ImmutableMap;
import com.datastax.oss.simulacron.common.cluster.NodeSpec;
import com.datastax.oss.simulacron.common.stubbing.PrimeDsl;
import com.datastax.oss.simulacron.server.BoundNode;
import com.datastax.oss.simulacron.server.Server;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.datastax.oss.simulacron.driver.SimulacronDriverSupport.defaultBuilder;
import static org.assertj.core.api.Assertions.assertThat;

public class MyTest {
    private static final Map<String, String> KEYSPACE_COLUMNS = ImmutableMap.of(
            "keyspace_name", "varchar",
            "durable_writes", "boolean",
            "replication", "map<varchar, varchar>");

    @Test
    void primeKeyspace() {
        Server server = Server.builder().build();
        try (BoundNode boundNode = server.register(NodeSpec.builder().build())) {
            primeKeyspace("test", boundNode);
            Cluster cluster = defaultBuilder(boundNode).build();
            Session session = cluster.connect();

            assertThat(session.getCluster().getMetadata().getKeyspace("test")).isNotNull();
        }
    }

    public void primeKeyspace(String keyspaceName, BoundNode boundNode) {
        Map<String, Object> keyspaceRow = new HashMap<>();
        keyspaceRow.put("keyspace_name", keyspaceName);
        keyspaceRow.put("durable_writes", true);
        keyspaceRow.put("replication", ImmutableMap.of(
                "class", "org.apache.cassandra.locator.SimpleStrategy",
                "replication_factor", "1"));

        boundNode.prime(PrimeDsl.when("SELECT * FROM system_schema.keyspaces")
                .then(PrimeDsl.rows(Collections.singletonList(keyspaceRow), KEYSPACE_COLUMNS))
                .build());
        boundNode.prime(PrimeDsl.when("SELECT * FROM system_schema.keyspaces WHERE keyspace_name = '" + keyspaceName + '\'')
                .then(PrimeDsl.rows(Collections.singletonList(keyspaceRow), KEYSPACE_COLUMNS))
                .build());
    }
}

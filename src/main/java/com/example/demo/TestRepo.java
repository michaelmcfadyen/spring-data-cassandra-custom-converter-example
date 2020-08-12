package com.example.demo;

import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestRepo extends ReactiveCassandraRepository<TestDto, String> {
}

# AtlasFS

A Java-based POSIX-compliant metadata and consistency service for distributed filesystems.

## Overview

AtlasFS is a high-performance, distributed metadata service designed to provide POSIX filesystem semantics for distributed storage systems. It handles namespace operations, consistency guarantees, and metadata management using a scalable, fault-tolerant architecture.

## Architecture

### Core Components

1. **Metadata Server**: Central metadata management service
   - Namespace operations (create, delete, rename, etc.)
   - Inode management and attributes
   - Directory hierarchy management
   - Lock management for consistency

2. **Client Library**: Thin client for metadata operations
   - gRPC-based communication
   - Caching layer for performance
   - Retry and failover logic

3. **Consistency Engine**: Ensures POSIX semantics
   - Distributed locking
   - Transaction support
   - Conflict resolution

### Technology Stack

- **Language**: Java 11+
- **RPC Framework**: gRPC
- **Build System**: Maven
- **Storage Backend**: Pluggable (RocksDB, PostgreSQL, etc.)

## Project Structure

```
atlasfs/
├── atlasfs-proto/          # Protocol buffer definitions
├── atlasfs-server/         # Metadata server implementation
├── atlasfs-client/         # Client library
├── atlasfs-common/         # Shared utilities and models
└── atlasfs-examples/       # Example applications
```

## API Overview

The AtlasFS API provides POSIX-compliant operations:

- **Namespace Operations**: `create`, `delete`, `rename`, `link`, `symlink`
- **Attribute Operations**: `getattr`, `setattr`, `chmod`, `chown`
- **Directory Operations**: `mkdir`, `rmdir`, `readdir`, `lookup`
- **Extended Operations**: `getxattr`, `setxattr`, `listxattr`, `removexattr`

See `atlasfs-proto/src/main/proto/metadata.proto` for detailed API definitions.

## Design Decisions

### 1. gRPC for Communication
- **Rationale**: High performance, bi-directional streaming, multi-language support
- **Trade-off**: Additional complexity vs REST, but better performance for metadata ops

### 2. Centralized Metadata Server (Initial Design)
- **Rationale**: Simplifies consistency model, easier to implement POSIX semantics
- **Future**: Can scale horizontally with sharding/partitioning

### 3. Pluggable Storage Backend
- **Rationale**: Different deployments have different needs (embedded vs distributed DB)
- **Options**: RocksDB for single-node, PostgreSQL for HA, Cassandra for scale

### 4. Optimistic Locking with Conflict Detection
- **Rationale**: Better performance than pessimistic locking for most workloads
- **Fallback**: Exclusive locks available for operations requiring strict ordering

### 5. Client-Side Caching
- **Rationale**: Reduces load on metadata server, improves latency
- **Consistency**: Cache invalidation via server-initiated callbacks

## Getting Started

### Prerequisites

- Java 11 or higher
- Maven 3.6+
- Protocol Buffers compiler (protoc)

### Building

```bash
# Build all modules
mvn clean install

# Build specific module
mvn clean install -pl atlasfs-server
```

### Running the Server

```bash
cd atlasfs-server
mvn exec:java -Dexec.mainClass="com.atlasfs.server.MetadataServer"
```

### Using the Client

```java
// Connect to metadata server
MetadataClient client = MetadataClient.builder()
    .endpoint("localhost:8080")
    .build();

// Create a directory
CreateDirResponse response = client.createDir(
    CreateDirRequest.newBuilder()
        .setPath("/my/directory")
        .setMode(0755)
        .build()
);
```

## Configuration

Server configuration is managed via `application.properties`:

```properties
# Server settings
server.port=8080
server.host=0.0.0.0

# Storage backend
storage.backend=rocksdb
storage.path=/var/lib/atlasfs/metadata

# Performance tuning
cache.size.mb=512
thread.pool.size=64
```

## Testing

```bash
# Run all tests
mvn test

# Run integration tests
mvn verify -Pintegration-tests
```

## Next Steps

### Phase 1: Core Implementation (Current)
- [x] Define gRPC protocol
- [ ] Implement basic metadata server
- [ ] Create client library
- [ ] Add RocksDB storage backend

### Phase 2: Consistency & Locking
- [ ] Implement distributed locking
- [ ] Add transaction support
- [ ] Cache invalidation mechanism

### Phase 3: High Availability
- [ ] Leader election (Raft/ZooKeeper)
- [ ] Metadata replication
- [ ] Failover support

### Phase 4: Performance & Scale
- [ ] Metadata sharding
- [ ] Read replicas
- [ ] Performance benchmarking

### Phase 5: Advanced Features
- [ ] Extended attributes
- [ ] Access control lists (ACLs)
- [ ] Quota management
- [ ] Audit logging

## Contributing

Contributions are welcome! Please see [CONTRIBUTING.md](CONTRIBUTING.md) for guidelines.

## License

[License information to be added]

## Contact

For questions or feedback, please open an issue on GitHub.

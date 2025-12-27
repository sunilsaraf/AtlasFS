# AtlasFS Design Document

## Overview

AtlasFS is a distributed metadata service designed to provide POSIX filesystem semantics for distributed storage systems. This document describes the architecture, design decisions, and implementation strategy.

## Architecture

### System Components

```
┌─────────────────────────────────────────────────────────────┐
│                        Clients                               │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐      │
│  │   FUSE       │  │   Native     │  │    API       │      │
│  │   Client     │  │   App        │  │   Consumer   │      │
│  └──────────────┘  └──────────────┘  └──────────────┘      │
└─────────────────────────────────────────────────────────────┘
                           │
                    gRPC Protocol
                           │
┌─────────────────────────────────────────────────────────────┐
│                   AtlasFS Server Cluster                     │
│  ┌──────────────────────────────────────────────────────┐   │
│  │                Metadata Service                       │   │
│  │  ┌──────────┐  ┌──────────┐  ┌──────────┐           │   │
│  │  │Namespace │  │  Inode   │  │  Lock    │           │   │
│  │  │ Manager  │  │ Manager  │  │ Manager  │           │   │
│  │  └──────────┘  └──────────┘  └──────────┘           │   │
│  └──────────────────────────────────────────────────────┘   │
│                           │                                  │
│  ┌──────────────────────────────────────────────────────┐   │
│  │              Storage Abstraction Layer               │   │
│  └──────────────────────────────────────────────────────┘   │
│                           │                                  │
│  ┌────────────┐  ┌────────────┐  ┌────────────┐           │
│  │  RocksDB   │  │ PostgreSQL │  │  Cassandra │           │
│  │  Backend   │  │  Backend   │  │  Backend   │           │
│  └────────────┘  └────────────┘  └────────────┘           │
└─────────────────────────────────────────────────────────────┘
```

### Data Model

#### Inode Structure

Each inode represents a filesystem object (file, directory, symlink, etc.) and contains:

- **Inode Number**: Unique 64-bit identifier
- **Type**: File type (regular, directory, symlink, etc.)
- **Mode**: Permission bits
- **Owner/Group**: UID/GID
- **Size**: File size in bytes
- **Timestamps**: Access, modification, and change times
- **Link Count**: Number of hard links
- **Extended Attributes**: Key-value pairs

#### Directory Structure

Directories are implemented as:
- Inode with type=DIRECTORY
- Mapping of name → inode number
- Special entries: "." (self) and ".." (parent)

#### Namespace Operations

The namespace is organized as a tree structure with:
- Root directory at inode 1
- Parent-child relationships via directory entries
- Path resolution through iterative lookup

## Design Decisions

### 1. gRPC for RPC Framework

**Decision**: Use gRPC with Protocol Buffers for client-server communication.

**Rationale**:
- High performance with HTTP/2 multiplexing
- Strong typing with Protocol Buffers
- Bi-directional streaming support
- Multi-language client generation
- Built-in service discovery and load balancing

**Alternatives Considered**:
- REST/HTTP: Simpler but lower performance, no streaming
- Thrift: Similar benefits but less active community
- Custom TCP protocol: Maximum performance but high development cost

### 2. Centralized Metadata Server

**Decision**: Initial implementation uses a single metadata server with potential for clustering.

**Rationale**:
- Simplifies consistency model
- Easier to implement POSIX semantics
- Sufficient for many use cases (millions of files)
- Foundation for future distributed implementation

**Future Evolution**:
- Horizontal scaling via namespace partitioning
- Consistent hashing for directory distribution
- Read replicas for high availability

### 3. Pluggable Storage Backend

**Decision**: Abstract storage layer with multiple backend implementations.

**Rationale**:
- Different deployments have different requirements
- Flexibility to choose between performance and durability
- Easier testing with in-memory backends

**Backend Options**:
- **RocksDB**: High performance, embedded, single-node
- **PostgreSQL**: ACID guarantees, HA, familiar
- **Cassandra**: Distributed, eventual consistency, massive scale

### 4. Optimistic Concurrency Control

**Decision**: Use optimistic locking with version numbers for most operations.

**Rationale**:
- Better performance than pessimistic locking
- Lower contention for read-heavy workloads
- Simpler implementation

**Conflict Resolution**:
- Version numbers on each inode
- Compare-and-swap for updates
- Fallback to exclusive locks when needed

### 5. Client-Side Caching

**Decision**: Implement caching layer in client library.

**Rationale**:
- Reduces load on metadata server
- Improves latency for repeated operations
- Essential for FUSE performance

**Cache Invalidation**:
- TTL-based expiration
- Server-initiated callbacks for updates
- Lease-based consistency

### 6. POSIX Semantics

**Decision**: Provide full POSIX filesystem semantics.

**Rationale**:
- Maximum compatibility with existing applications
- Well-understood semantics
- Required for FUSE integration

**Challenges**:
- Strong consistency requirements
- Rename atomicity
- Hard link support

## Implementation Strategy

### Phase 1: Core Infrastructure (Current)

**Goals**:
- Set up project structure
- Define gRPC API
- Implement basic server framework
- Create client library skeleton

**Deliverables**:
- Maven multi-module project
- Protocol buffer definitions
- Server entry point
- Client builder pattern

### Phase 2: Storage Layer

**Goals**:
- Implement storage abstraction
- Add RocksDB backend
- Basic CRUD operations

**Key Components**:
- StorageBackend interface
- RocksDB implementation
- Inode serialization
- Directory entry management

### Phase 3: Core Operations

**Goals**:
- Implement essential filesystem operations
- Path resolution
- Directory operations
- File operations

**Operations**:
- lookup, getattr, setattr
- mkdir, rmdir, readdir
- create, unlink, rename
- link, symlink, readlink

### Phase 4: Consistency & Locking

**Goals**:
- Add distributed locking
- Implement versioning
- Cache invalidation

**Components**:
- Lock manager
- Version control
- Lease management
- Cache coherence protocol

### Phase 5: High Availability

**Goals**:
- Leader election
- Metadata replication
- Failover support

**Technologies**:
- Raft consensus (via etcd/Consul)
- State machine replication
- Snapshot and replay

### Phase 6: Performance Optimization

**Goals**:
- Metadata sharding
- Read replicas
- Performance testing

**Optimizations**:
- Batch operations
- Pipelining
- Asynchronous operations
- Connection pooling

## Performance Considerations

### Target Metrics

- **Latency**: < 1ms for cached operations, < 10ms for server roundtrip
- **Throughput**: > 100K ops/sec per server
- **Capacity**: > 1 billion inodes per server
- **Availability**: 99.9% uptime

### Bottlenecks

1. **Network Latency**: Minimize roundtrips with batching
2. **Lock Contention**: Use fine-grained locking
3. **Storage I/O**: Use write-ahead logging, batching
4. **Serialization**: Optimize protobuf encoding

## Security Considerations

### Authentication

- mTLS for client-server communication
- Token-based authentication
- Integration with external auth systems

### Authorization

- POSIX permissions (owner, group, other)
- Access control lists (ACLs)
- Extended attributes for security labels

### Audit

- Operation logging
- Security event tracking
- Compliance reporting

## Testing Strategy

### Unit Tests

- Component isolation
- Mock dependencies
- Fast feedback

### Integration Tests

- End-to-end scenarios
- Multiple clients
- Failure injection

### Performance Tests

- Load testing
- Stress testing
- Capacity planning

### Conformance Tests

- POSIX test suite
- Filesystem stress tests
- Compatibility verification

## Future Enhancements

### Advanced Features

- **Quotas**: Per-user/group storage limits
- **Snapshots**: Point-in-time filesystem views
- **Deduplication**: Content-addressed storage
- **Compression**: Transparent metadata compression

### Scalability

- **Namespace Partitioning**: Shard by directory subtree
- **Tiered Storage**: Hot/cold metadata separation
- **Global Namespace**: Federation across clusters

### Observability

- **Metrics**: Prometheus integration
- **Tracing**: Distributed tracing with Jaeger
- **Monitoring**: Health checks and dashboards

## References

- [POSIX Standard](https://pubs.opengroup.org/onlinepubs/9699919799/)
- [gRPC Documentation](https://grpc.io/docs/)
- [Protocol Buffers](https://developers.google.com/protocol-buffers)
- [RocksDB](https://rocksdb.org/)
- [Raft Consensus](https://raft.github.io/)

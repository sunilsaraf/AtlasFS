# AtlasFS Quick Start Guide

This guide will help you get started with AtlasFS in just a few minutes.

## Installation

### Prerequisites

Ensure you have the following installed:
- Java 11 or higher
- Maven 3.6 or higher

### Build from Source

```bash
# Clone the repository
git clone https://github.com/sunilsaraf/AtlasFS.git
cd AtlasFS

# Build the project
mvn clean package
```

## Running the Server

### Start the Metadata Server

```bash
# From the project root directory
cd atlasfs-server
mvn exec:java -Dexec.mainClass="com.atlasfs.server.MetadataServer"
```

The server will start on port 8080 by default. You should see output like:

```
INFO  c.a.server.MetadataServer - AtlasFS Metadata Server started on port 8080
```

### Custom Port

To run on a different port:

```bash
mvn exec:java -Dexec.mainClass="com.atlasfs.server.MetadataServer" -Dexec.args="9090"
```

## Using the Client

### Java Client Example

Add the client dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>com.atlasfs</groupId>
    <artifactId>atlasfs-client</artifactId>
    <version>0.1.0-SNAPSHOT</version>
</dependency>
```

### Basic Operations

```java
import com.atlasfs.client.MetadataClient;
import com.atlasfs.proto.*;

public class Example {
    public static void main(String[] args) {
        // Connect to the metadata server
        try (MetadataClient client = MetadataClient.builder()
                .endpoint("localhost:8080")
                .build()) {
            
            // Create a directory
            MkDirResponse dirResponse = client.mkdir(
                1L,              // parent inode (root)
                "my-directory",  // name
                0755,           // mode (permissions)
                1000,           // uid
                1000            // gid
            );
            System.out.println("Created directory with inode: " + 
                dirResponse.getAttributes().getInode());
            
            // Create a file
            CreateResponse fileResponse = client.create(
                dirResponse.getAttributes().getInode(), // parent
                "my-file.txt",                          // name
                0644,                                   // mode
                1000,                                   // uid
                1000                                    // gid
            );
            System.out.println("Created file with inode: " + 
                fileResponse.getAttributes().getInode());
            
            // Get file attributes
            GetAttrResponse attrResponse = client.getAttr(
                fileResponse.getAttributes().getInode()
            );
            System.out.println("File size: " + 
                attrResponse.getAttributes().getSize());
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

## Testing with gRPC Tools

### Using grpcurl

[grpcurl](https://github.com/fullstorydev/grpcurl) is a command-line tool for interacting with gRPC servers.

```bash
# Install grpcurl
go install github.com/fullstorydev/grpcurl/cmd/grpcurl@latest

# List available services
grpcurl -plaintext localhost:8080 list

# Describe the MetadataService
grpcurl -plaintext localhost:8080 describe atlasfs.metadata.MetadataService

# Call GetAttr method
grpcurl -plaintext -d '{"inode": 1}' \
    localhost:8080 atlasfs.metadata.MetadataService/GetAttr
```

### Using BloomRPC

[BloomRPC](https://github.com/bloomrpc/bloomrpc) is a GUI client for gRPC.

1. Download and install BloomRPC
2. Load the proto file: `atlasfs-proto/src/main/proto/metadata.proto`
3. Connect to `localhost:8080`
4. Select a method and send requests

## Configuration

### Server Configuration

Edit `atlasfs-server/src/main/resources/application.properties`:

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

### Logging Configuration

Edit `atlasfs-server/src/main/resources/logback.xml` to adjust logging levels:

```xml
<logger name="com.atlasfs" level="DEBUG"/>
<logger name="io.grpc" level="INFO"/>
```

## Next Steps

### Development

1. **Implement Storage Backend**: The current implementation uses stubs. Implement the RocksDB backend in `atlasfs-server`.

2. **Add Tests**: Write unit and integration tests for your components.

3. **Build FUSE Client**: Create a FUSE filesystem client using the AtlasFS client library.

### Deployment

1. **Containerize**: Create Docker images for the server
2. **Orchestrate**: Deploy using Kubernetes
3. **Monitor**: Add metrics and monitoring

### Learning More

- Read [DESIGN.md](DESIGN.md) for architecture details
- Check [CONTRIBUTING.md](CONTRIBUTING.md) for contribution guidelines
- Review the [Protocol Buffer definitions](atlasfs-proto/src/main/proto/metadata.proto)

## Troubleshooting

### Server Won't Start

**Problem**: `java.net.BindException: Address already in use`

**Solution**: Another process is using port 8080. Either stop that process or use a different port:

```bash
mvn exec:java -Dexec.mainClass="com.atlasfs.server.MetadataServer" -Dexec.args="9090"
```

### Build Fails

**Problem**: `protoc` not found

**Solution**: The Maven plugin will download the appropriate protoc compiler for your platform. Ensure you have an internet connection.

**Problem**: Java version mismatch

**Solution**: Ensure you're using Java 11 or higher:

```bash
java -version
```

### Client Connection Issues

**Problem**: `UNAVAILABLE: io exception`

**Solution**: Ensure the server is running and accessible:

```bash
# Test connectivity
nc -zv localhost 8080
```

## Support

For help and support:
- Open an issue on GitHub
- Read the documentation in the repository
- Check existing issues for similar problems

## License

[License information to be added]

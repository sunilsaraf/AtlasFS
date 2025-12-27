# AtlasFS Examples

This module contains example applications demonstrating how to use the AtlasFS client library.

## Available Examples

### BasicExample

Demonstrates fundamental filesystem operations:
- Creating directories
- Creating files
- Looking up entries
- Getting file attributes
- Renaming files

## Running the Examples

### Prerequisites

1. Build the project:
   ```bash
   cd /path/to/AtlasFS
   mvn clean package
   ```

2. Start the AtlasFS server in a separate terminal:
   ```bash
   cd atlasfs-server
   mvn exec:java -Dexec.mainClass="com.atlasfs.server.MetadataServer"
   ```

### Running BasicExample

From the project root:

```bash
cd examples
mvn exec:java -Dexec.mainClass="com.atlasfs.examples.BasicExample"
```

Or specify a custom server endpoint:

```bash
mvn exec:java -Dexec.mainClass="com.atlasfs.examples.BasicExample" \
    -Dexec.args="my-server:9090"
```

### Expected Output

**Note**: The current server implementation returns "UNIMPLEMENTED" errors for most operations. This is expected for the initial skeleton. Future implementations will provide full functionality.

With a working server, you should see output like:

```
Connecting to AtlasFS server at localhost:8080

=== Basic AtlasFS Operations Example ===

1. Creating directory /home
   Created directory with inode: 2

2. Creating directory /home/user
   Created directory with inode: 3

3. Creating file /home/user/document.txt
   Created file with inode: 4

4. Looking up file 'document.txt' in /home/user
   Found file with inode: 4
   File type: REGULAR
   File mode: 0644

5. Getting attributes for file inode 4
   Size: 0 bytes
   UID: 1000
   GID: 1000
   Mode: 0644
   Links: 1

6. Renaming document.txt to notes.txt
   Rename success: true

7. Verifying renamed file 'notes.txt'
   Found file with inode: 4
   (Same inode as before: true)

=== Example completed successfully! ===
```

## Creating Your Own Examples

To create a new example:

1. Create a new Java class in `src/main/java/com/atlasfs/examples/`
2. Import the client library:
   ```java
   import com.atlasfs.client.MetadataClient;
   import com.atlasfs.proto.*;
   ```

3. Use the MetadataClient builder:
   ```java
   try (MetadataClient client = MetadataClient.builder()
           .endpoint("localhost:8080")
           .build()) {
       // Your code here
   }
   ```

4. Run your example:
   ```bash
   mvn exec:java -Dexec.mainClass="com.atlasfs.examples.YourExample"
   ```

## Example Use Cases

Future examples could demonstrate:
- Recursive directory traversal
- Batch operations
- File permission management
- Extended attributes
- Symbolic links and hard links
- Concurrent access patterns
- Error handling and retry logic

## Need Help?

- Check the [main README](../README.md) for project overview
- Read [QUICKSTART.md](../QUICKSTART.md) for getting started
- Review [DESIGN.md](../DESIGN.md) for architecture details
- See the [protocol definitions](../atlasfs-proto/src/main/proto/metadata.proto) for API details

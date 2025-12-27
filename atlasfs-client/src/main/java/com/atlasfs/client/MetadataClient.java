package com.atlasfs.client;

import com.atlasfs.proto.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * Client for AtlasFS metadata service.
 * Provides a simple API for interacting with the metadata server.
 */
public class MetadataClient implements AutoCloseable {
    private static final Logger logger = LoggerFactory.getLogger(MetadataClient.class);
    
    private final ManagedChannel channel;
    private final MetadataServiceGrpc.MetadataServiceBlockingStub blockingStub;

    private MetadataClient(String host, int port) {
        this.channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();
        this.blockingStub = MetadataServiceGrpc.newBlockingStub(channel);
    }

    /**
     * Lookup a file or directory by name in a parent directory.
     */
    public LookupResponse lookup(long parentInode, String name) {
        logger.debug("Lookup: parent={}, name={}", parentInode, name);
        
        LookupRequest request = LookupRequest.newBuilder()
                .setParentInode(parentInode)
                .setName(name)
                .build();
        
        return blockingStub.lookup(request);
    }

    /**
     * Get attributes for an inode.
     */
    public GetAttrResponse getAttr(long inode) {
        logger.debug("GetAttr: inode={}", inode);
        
        GetAttrRequest request = GetAttrRequest.newBuilder()
                .setInode(inode)
                .build();
        
        return blockingStub.getAttr(request);
    }

    /**
     * Create a directory.
     */
    public MkDirResponse mkdir(long parentInode, String name, int mode, int uid, int gid) {
        logger.debug("MkDir: parent={}, name={}, mode={}", parentInode, name, mode);
        
        MkDirRequest request = MkDirRequest.newBuilder()
                .setParentInode(parentInode)
                .setName(name)
                .setMode(mode)
                .setUid(uid)
                .setGid(gid)
                .build();
        
        return blockingStub.mkDir(request);
    }

    /**
     * Create a file.
     */
    public CreateResponse create(long parentInode, String name, int mode, int uid, int gid) {
        logger.debug("Create: parent={}, name={}, mode={}", parentInode, name, mode);
        
        CreateRequest request = CreateRequest.newBuilder()
                .setParentInode(parentInode)
                .setName(name)
                .setMode(mode)
                .setUid(uid)
                .setGid(gid)
                .build();
        
        return blockingStub.create(request);
    }

    /**
     * Rename a file or directory.
     */
    public RenameResponse rename(long oldParentInode, String oldName, 
                                  long newParentInode, String newName) {
        logger.debug("Rename: from {}/{} to {}/{}", 
                oldParentInode, oldName, newParentInode, newName);
        
        RenameRequest request = RenameRequest.newBuilder()
                .setOldParentInode(oldParentInode)
                .setOldName(oldName)
                .setNewParentInode(newParentInode)
                .setNewName(newName)
                .build();
        
        return blockingStub.rename(request);
    }

    @Override
    public void close() throws Exception {
        if (channel != null) {
            channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
        }
    }

    /**
     * Builder for creating MetadataClient instances.
     */
    public static class Builder {
        private String host = "localhost";
        private int port = 8080;

        public Builder host(String host) {
            this.host = host;
            return this;
        }

        public Builder port(int port) {
            this.port = port;
            return this;
        }

        public Builder endpoint(String endpoint) {
            String[] parts = endpoint.split(":");
            this.host = parts[0];
            if (parts.length > 1) {
                this.port = Integer.parseInt(parts[1]);
            }
            return this;
        }

        public MetadataClient build() {
            return new MetadataClient(host, port);
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}

package com.atlasfs.server;

import com.atlasfs.proto.*;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of the MetadataService gRPC service.
 * This is a skeleton implementation that will be extended with actual storage backend.
 */
public class MetadataServiceImpl extends MetadataServiceGrpc.MetadataServiceImplBase {
    private static final Logger logger = LoggerFactory.getLogger(MetadataServiceImpl.class);

    @Override
    public void lookup(LookupRequest request, StreamObserver<LookupResponse> responseObserver) {
        logger.info("Lookup request: parent={}, name={}", request.getParentInode(), request.getName());
        
        // TODO: Implement actual lookup logic
        responseObserver.onError(
            io.grpc.Status.UNIMPLEMENTED
                .withDescription("Lookup not yet implemented")
                .asRuntimeException()
        );
    }

    @Override
    public void getAttr(GetAttrRequest request, StreamObserver<GetAttrResponse> responseObserver) {
        logger.info("GetAttr request: inode={}", request.getInode());
        
        // TODO: Implement actual getattr logic
        responseObserver.onError(
            io.grpc.Status.UNIMPLEMENTED
                .withDescription("GetAttr not yet implemented")
                .asRuntimeException()
        );
    }

    @Override
    public void setAttr(SetAttrRequest request, StreamObserver<SetAttrResponse> responseObserver) {
        logger.info("SetAttr request: inode={}", request.getInode());
        
        // TODO: Implement actual setattr logic
        responseObserver.onError(
            io.grpc.Status.UNIMPLEMENTED
                .withDescription("SetAttr not yet implemented")
                .asRuntimeException()
        );
    }

    @Override
    public void readDir(ReadDirRequest request, StreamObserver<DirectoryEntry> responseObserver) {
        logger.info("ReadDir request: inode={}", request.getInode());
        
        // TODO: Implement actual readdir logic
        responseObserver.onError(
            io.grpc.Status.UNIMPLEMENTED
                .withDescription("ReadDir not yet implemented")
                .asRuntimeException()
        );
    }

    @Override
    public void mkDir(MkDirRequest request, StreamObserver<MkDirResponse> responseObserver) {
        logger.info("MkDir request: parent={}, name={}", request.getParentInode(), request.getName());
        
        // TODO: Implement actual mkdir logic
        responseObserver.onError(
            io.grpc.Status.UNIMPLEMENTED
                .withDescription("MkDir not yet implemented")
                .asRuntimeException()
        );
    }

    @Override
    public void create(CreateRequest request, StreamObserver<CreateResponse> responseObserver) {
        logger.info("Create request: parent={}, name={}", request.getParentInode(), request.getName());
        
        // TODO: Implement actual create logic
        responseObserver.onError(
            io.grpc.Status.UNIMPLEMENTED
                .withDescription("Create not yet implemented")
                .asRuntimeException()
        );
    }

    @Override
    public void rename(RenameRequest request, StreamObserver<RenameResponse> responseObserver) {
        logger.info("Rename request: from {}/{} to {}/{}", 
            request.getOldParentInode(), request.getOldName(),
            request.getNewParentInode(), request.getNewName());
        
        // TODO: Implement actual rename logic
        responseObserver.onError(
            io.grpc.Status.UNIMPLEMENTED
                .withDescription("Rename not yet implemented")
                .asRuntimeException()
        );
    }

    // Additional methods will be implemented as needed
}

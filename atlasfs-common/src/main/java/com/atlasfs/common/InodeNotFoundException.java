package com.atlasfs.common;

/**
 * Exception thrown when an inode is not found in the metadata store.
 */
public class InodeNotFoundException extends Exception {
    private final long inode;

    public InodeNotFoundException(long inode) {
        super("Inode not found: " + inode);
        this.inode = inode;
    }

    public long getInode() {
        return inode;
    }
}

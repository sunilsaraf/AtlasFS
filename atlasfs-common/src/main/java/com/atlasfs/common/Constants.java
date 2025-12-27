package com.atlasfs.common;

/**
 * Constants used across AtlasFS modules.
 */
public final class Constants {
    
    // Root inode number
    public static final long ROOT_INODE = 1L;
    
    // Default permissions
    public static final int DEFAULT_DIR_MODE = 0755;
    public static final int DEFAULT_FILE_MODE = 0644;
    
    // Extended attribute limits
    public static final int MAX_XATTR_NAME_LENGTH = 255;
    public static final int MAX_XATTR_VALUE_LENGTH = 65536;
    
    // Lock timeouts (milliseconds)
    public static final long DEFAULT_LOCK_TIMEOUT_MS = 30000;
    public static final long MAX_LOCK_TIMEOUT_MS = 300000;
    
    private Constants() {
        // Utility class
    }
}

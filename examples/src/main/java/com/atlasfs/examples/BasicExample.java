package com.atlasfs.examples;

import com.atlasfs.client.MetadataClient;
import com.atlasfs.proto.*;

/**
 * Simple example demonstrating basic AtlasFS operations.
 */
public class BasicExample {
    
    public static void main(String[] args) {
        String serverEndpoint = args.length > 0 ? args[0] : "localhost:8080";
        
        System.out.println("Connecting to AtlasFS server at " + serverEndpoint);
        
        try (MetadataClient client = MetadataClient.builder()
                .endpoint(serverEndpoint)
                .build()) {
            
            System.out.println("\n=== Basic AtlasFS Operations Example ===\n");
            
            // Example 1: Create a directory
            System.out.println("1. Creating directory /home");
            MkDirResponse dirResponse = client.mkdir(
                1L,              // parent inode (root = 1)
                "home",          // directory name
                0755,           // permissions (rwxr-xr-x)
                1000,           // user id
                1000            // group id
            );
            long homeInode = dirResponse.getAttributes().getInode();
            System.out.println("   Created directory with inode: " + homeInode);
            
            // Example 2: Create a subdirectory
            System.out.println("\n2. Creating directory /home/user");
            MkDirResponse userDirResponse = client.mkdir(
                homeInode,       // parent inode
                "user",          // directory name
                0755,           // permissions
                1000,           // user id
                1000            // group id
            );
            long userInode = userDirResponse.getAttributes().getInode();
            System.out.println("   Created directory with inode: " + userInode);
            
            // Example 3: Create a file
            System.out.println("\n3. Creating file /home/user/document.txt");
            CreateResponse fileResponse = client.create(
                userInode,       // parent inode
                "document.txt",  // file name
                0644,           // permissions (rw-r--r--)
                1000,           // user id
                1000            // group id
            );
            long fileInode = fileResponse.getAttributes().getInode();
            System.out.println("   Created file with inode: " + fileInode);
            
            // Example 4: Lookup a file
            System.out.println("\n4. Looking up file 'document.txt' in /home/user");
            LookupResponse lookupResponse = client.lookup(userInode, "document.txt");
            System.out.println("   Found file with inode: " + 
                lookupResponse.getAttributes().getInode());
            System.out.println("   File type: " + 
                lookupResponse.getAttributes().getType());
            System.out.println("   File mode: " + 
                String.format("0%o", lookupResponse.getAttributes().getMode()));
            
            // Example 5: Get file attributes
            System.out.println("\n5. Getting attributes for file inode " + fileInode);
            GetAttrResponse attrResponse = client.getAttr(fileInode);
            FileAttributes attrs = attrResponse.getAttributes();
            System.out.println("   Size: " + attrs.getSize() + " bytes");
            System.out.println("   UID: " + attrs.getUid());
            System.out.println("   GID: " + attrs.getGid());
            System.out.println("   Mode: " + String.format("0%o", attrs.getMode()));
            System.out.println("   Links: " + attrs.getNlink());
            
            // Example 6: Rename a file
            System.out.println("\n6. Renaming document.txt to notes.txt");
            RenameResponse renameResponse = client.rename(
                userInode,      // old parent
                "document.txt", // old name
                userInode,      // new parent
                "notes.txt"     // new name
            );
            System.out.println("   Rename success: " + renameResponse.getSuccess());
            
            // Example 7: Verify renamed file
            System.out.println("\n7. Verifying renamed file 'notes.txt'");
            LookupResponse renamedLookup = client.lookup(userInode, "notes.txt");
            System.out.println("   Found file with inode: " + 
                renamedLookup.getAttributes().getInode());
            System.out.println("   (Same inode as before: " + 
                (renamedLookup.getAttributes().getInode() == fileInode) + ")");
            
            System.out.println("\n=== Example completed successfully! ===\n");
            
        } catch (io.grpc.StatusRuntimeException e) {
            System.err.println("gRPC error: " + e.getStatus());
            System.err.println("\nNote: Most operations are not yet implemented in the server.");
            System.err.println("This is expected for the initial skeleton implementation.");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

package com.umangcraft.cloudshare.repository;

import com.umangcraft.cloudshare.documents.FileMetadataDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface FileMetadataRepository extends MongoRepository<FileMetadataDocument, String> {
    List<FileMetadataDocument> findByClerkId(String clerkId);

    Long countByClerkId(String clerkId);
}

package com.gws.crm.core.export.service;

public interface FileStorageService {
    String store(byte[] data, String filename, String contentType);
    byte[] load(String path)  ;

    void delete(String path) ;


}
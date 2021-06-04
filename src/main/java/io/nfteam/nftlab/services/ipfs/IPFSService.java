package io.nfteam.nftlab.services.ipfs;

import org.springframework.core.io.FileSystemResource;

import java.io.IOException;

public interface IPFSService {
  IPFSResponses.UploadImage uploadImage(FileSystemResource file) throws IOException;
}

package io.nfteam.nftlab.services.ipfs;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface IPFSService {
  IPFSResponses.UploadImage uploadImage(ByteArrayResource file) throws IOException;
}

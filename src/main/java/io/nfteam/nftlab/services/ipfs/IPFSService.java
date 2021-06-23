package io.nfteam.nftlab.services.ipfs;

import org.springframework.core.io.ByteArrayResource;

import java.io.IOException;

public interface IPFSService {
  IPFSResponses.Uploaded upload(ByteArrayResource file) throws IOException;
}

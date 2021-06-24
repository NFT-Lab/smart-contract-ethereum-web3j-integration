package io.nfteam.nftlab.services.ipfs;

import io.nfteam.nftlab.services.ipfs.pinataresponses.Uploaded;
import org.springframework.core.io.ByteArrayResource;

import java.io.IOException;

public interface IPFSService {
  Uploaded upload(ByteArrayResource file) throws IOException;
}

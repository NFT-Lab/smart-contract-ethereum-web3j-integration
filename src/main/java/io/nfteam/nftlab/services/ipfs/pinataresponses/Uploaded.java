package io.nfteam.nftlab.services.ipfs.pinataresponses;

public interface Uploaded {
  String getHash();
  int getSize();
  String getTimestamp();
}

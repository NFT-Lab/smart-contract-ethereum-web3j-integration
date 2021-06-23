package io.nfteam.nftlab.services.ipfs;

public interface IPFSResponses {
    interface Uploaded {
        String getHash();
        int getSize();
        String getTimestamp();
    }
}

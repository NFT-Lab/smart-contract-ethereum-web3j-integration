package io.nfteam.nftlab.services.ipfs;

public interface IPFSResponses {
    interface UploadImage {
        String getHash();
        int getSize();
        String getTimestamp();
    }
}

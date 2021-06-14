package io.nfteam.nftlab.services;

import io.nfteam.nftlab.contracts.NFTLabStore;
import io.nfteam.nftlab.services.smartcontract.*;
import org.springframework.core.io.ByteArrayResource;

import java.math.BigInteger;
import java.util.List;

public interface NFTContractService {
    NFTID mint(UserTuple artist, ByteArrayResource file) throws Exception;

    void transfer(
            BigInteger tokenId,
            UserTuple seller,
            UserTuple buyer,
            String price,
            String timestamp) throws Exception;

    List getHistory(BigInteger tokenId) throws Exception;

    NFTLabStore.NFTLab getNFTById(BigInteger tokenId) throws Exception;

    NFTLabStore.NFTLab getNFTByHash(String hash) throws Exception;
}

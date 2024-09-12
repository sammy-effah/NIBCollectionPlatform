package com.nib.gh.donations.esbservice;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nib.gh.donations.esb.T_24_FundsTransfer;
import com.nib.gh.donations.esbrepository.T24FundsTransferRepository;

@Service
public class T24FundsTransferService {
@Autowired
    private T24FundsTransferRepository t24FundsTransferRepository;

     public List<T_24_FundsTransfer> findAll() {
        return t24FundsTransferRepository.findAll();
    }

}

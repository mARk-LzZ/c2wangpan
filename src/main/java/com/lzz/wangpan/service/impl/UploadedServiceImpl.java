package com.lzz.wangpan.service.impl;

import com.lzz.wangpan.mapper.UploadedMapper;
import com.lzz.wangpan.model.UploadedRecord;
import com.lzz.wangpan.service.IUploadedService;
import com.lzz.wangpan.util.ServiceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UploadedServiceImpl implements IUploadedService {

    private final UploadedMapper uploadedMapper;

    @Autowired
    public UploadedServiceImpl(UploadedMapper uploadedMapper) {this.uploadedMapper=uploadedMapper;}

    @SuppressWarnings("unchecked")
    @Override
    public List<UploadedRecord> list(String user, String file, String category, int offset) {
        return (List<UploadedRecord>) ServiceUtils.invokeFileFilter(uploadedMapper, "listUploadedBy", user, file,
                category, offset);
    }
}

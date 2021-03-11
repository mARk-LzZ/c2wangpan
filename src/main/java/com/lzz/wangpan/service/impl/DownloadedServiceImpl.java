package com.lzz.wangpan.service.impl;

import com.lzz.wangpan.mapper.DownloadedMapper;
import com.lzz.wangpan.model.DownloadRecord;
import com.lzz.wangpan.service.IDownloadedService;
import com.lzz.wangpan.util.ServiceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;



@Service
public class DownloadedServiceImpl implements IDownloadedService {

    private final DownloadedMapper downloadDAO;

    @Autowired
    public DownloadedServiceImpl(DownloadedMapper downloadDAO) {
        this.downloadDAO = downloadDAO;
    }

    @Override
    public void insertDownload(int userId, long fileId) {
        downloadDAO.insertDownload(userId, fileId);
    }

    @Override
    public void removeByFileId(long fileId) {
        downloadDAO.removeByFileId(fileId);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<DownloadRecord> list(String user, String file, String category, int offset) {
        return (List<DownloadRecord>) ServiceUtils.invokeFileFilter(downloadDAO, "listDownloadedBy", user, file,
                category, offset);
    }
}

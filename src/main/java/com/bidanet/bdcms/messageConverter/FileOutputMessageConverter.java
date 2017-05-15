package com.bidanet.bdcms.messageConverter;


import com.bidanet.bdcms.bean.FileOutput;
import org.apache.commons.io.FileUtils;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import java.io.File;
import java.io.IOException;

/**
 * Created by avatek on 2015/12/16 0016.
 */
public class FileOutputMessageConverter extends AbstractHttpMessageConverter<FileOutput> {
    public FileOutputMessageConverter() {
        super(MediaType.ALL);
    }

    @Override
    protected boolean supports(Class<?> clazz) {
        boolean b = FileOutput.class == clazz;
        return b;
    }

    @Override
    protected FileOutput readInternal(Class<? extends FileOutput> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {

        return new FileOutput("--");
    }

    @Override
    protected void writeInternal(FileOutput fileOutput, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        File file = fileOutput.getFile();

        if (file.exists()){
            outputMessage.getHeaders().set("Content-Disposition","filename="+fileOutput.getFilename());
            outputMessage.getHeaders().set("Content-Type",fileOutput.getContentType());

            outputMessage.getBody().write(FileUtils.readFileToByteArray(file));
//            HttpHeaders headers = outputMessage.getHeaders();

        }
    }
}

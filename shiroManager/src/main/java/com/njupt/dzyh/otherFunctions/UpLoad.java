package com.njupt.dzyh.otherFunctions;

import com.njupt.dzyh.enums.CommonResultEm;
import com.njupt.dzyh.utils.CommonResult;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.IOException;

public class UpLoad {
    public static int upLoadFile(String resource,MultipartFile file) throws IOException {

        File newFile = new File(resource,"registUsers.xlsx");

        if(!newFile.exists()){
            try {
                newFile.createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        try{
            file.transferTo(newFile);
            System.out.println(newFile.getPath());
            return 0;
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
    }
}

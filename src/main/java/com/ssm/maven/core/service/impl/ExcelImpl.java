package com.ssm.maven.core.service.impl;

import com.ssm.maven.core.entity.Case;
import org.apache.poi.hssf.usermodel.*;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

/**
 * @author fstar
 */

@Service("ExcelImpl")
public class ExcelImpl {
        public void exportCase(List<Case> caseList, String [] titles, ServletOutputStream outStream) throws  Exception{
            //book对应excel文件
            HSSFWorkbook workbook = new HSSFWorkbook();
            //sheet对应文件的sheet
            HSSFSheet hssfSheet = workbook.createSheet("sheet1");
            HSSFRow row = hssfSheet.createRow(0);
            //创建单元格
            HSSFCell hssfCell;
            for (int i = 0; i < titles.length; i++) {
                //列索引从0开始
                hssfCell = row.createCell(i);
                //列名1
                hssfCell.setCellValue(titles[i]);
            }
            for(int j = 0; j < caseList.size(); j ++){
                row = hssfSheet.createRow(j + 1);
                Case myCase = caseList.get(j);
                row.createCell(0).setCellValue(myCase.getCaseCode());
                row.createCell(1).setCellValue(myCase.getClientName());
                row.createCell(2).setCellValue(myCase.getOpponentName());
                row.createCell(3).setCellValue(myCase.getDealer());
                row.createCell(4).setCellValue(myCase.getRemarks());
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                row.createCell(5).setCellValue(df.format(myCase.getCreatedAt() * 1000));
            }
            try{
                workbook.write(outStream);
                outStream.flush();
                outStream.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
}

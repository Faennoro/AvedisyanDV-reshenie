package View;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Faennor on 25.05.2020.
 */
public class DirScanner {
    ArrayList<File> files = new ArrayList<>();

    //Рекурсия для сканирования папок и подпапок на наличие PDF-файлов и добавление их в список
    public void ScanDirForPDF(File dir){
        for (File file:dir.listFiles()) {
            if (file.isDirectory()) ScanDirForPDF(file);
            else if (FilenameUtils.getExtension(file.getName()).equals("PDF")) { files.add(file);
            }
        }
    }

    //Проход по списку PDF-файлов и их конвертация в JPG
    public void ConvertPDFtoJPG(){

    }

    //Удаление использованных PDF-файлов
    public void DeleteConvertedPDF(){

    }
}

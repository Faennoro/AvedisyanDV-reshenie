package View;

import org.apache.commons.io.FilenameUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.tools.imageio.ImageIOUtil;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Faennor on 25.05.2020.
 */
//Класс для сканирования папки и подпапок, конвертации найденных PDF в JPG, удаления использованных PDF
public class Converter {
    ArrayList<File> files = new ArrayList<>();

    //Рекурсия для сканирования папок и подпапок на наличие PDF-файлов и добавление их в список
    public void scanDirForPDF(File dir){
        for (File file:dir.listFiles()) {
            if (file.isDirectory()) scanDirForPDF(file); //Рекурсия для подпапки
            else if (FilenameUtils.getExtension(file.getName()).equals("pdf")) //Проверка на расширение
            {
                files.add(file);
                System.out.println("Файл добавлен");
            }
        }
        System.out.println("Сканирование папки "+dir.getAbsolutePath()+" завершено.");
    }

    //Проход по списку PDF-файлов и их конвертация в JPG
    public void convertAllPDFtoJPG(){
        System.out.println("Начало конвертации");
        //Проверка на наличия файлов в списке
        if (files.size()==0) {
            System.out.println("Список пуст");
        }else
        {
            //Проход по списку
            for (File file: files) {
                try {
                    new File(file.getParent()+"/JPG").mkdir(); //Создание папки JPG
                    //Преобразование PDF с помощью библиотеки pdfbox
                    PDDocument document = PDDocument.load(file); //чтение документа
                    PDFRenderer pdfRenderer = new PDFRenderer(document);
                    //для одностраничных файлов
                    if (document.getNumberOfPages()==1)
                    {
                        BufferedImage bim = pdfRenderer.renderImageWithDPI(
                                0, 300, ImageType.RGB);
                        //запись изображения
                        ImageIOUtil.writeImage(
                                bim,
                                //форматирование вида "путь-к-папке/JPG/имя-файла-без-расширения.jpg
                                String.format(file.getParent()+"/JPG/"+file.getName().substring(0,file.getName().length()-4)+".%s", "jpg"),
                                300);
                    } else
                    {
                        //для многостраничных файлов
                        for (int page = 0; page < document.getNumberOfPages(); ++page) {
                            BufferedImage bim = pdfRenderer.renderImageWithDPI(
                                    page, 300, ImageType.RGB);
                            ImageIOUtil.writeImage(
                                    bim, String.format(file.getParent()+"/JPG/"+file.getName().substring(0,file.getName().length()-4)+"_%d.%s", page + 1, "jpg"), 300);
                        }
                    }
                    document.close(); //закрытие файла
                    System.out.println("Файл сконвертирован");
                    file.delete(); //удаление файла после совершения конвертации
                }
                catch (IOException e){
                    System.out.println("Ошибка чтения файла.");
                }
            }
        }
    }
}

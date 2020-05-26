package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Created by Faennor on 23.05.2020.
 */
//Класс для десктопного приложения
public class App{
    public static void main(String[] args) {

        //Главное окно
        JFrame mainFrame = new JFrame("Конвертирование PDF-файлов в JPG");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(400,200);

        //Упорядочивание элементов
        Container container = mainFrame.getContentPane();
        container.setLayout(new FlowLayout(FlowLayout.LEFT,10,10));

        //Объявление элементов
        final JButton getDirectoryButton = new JButton("Выбрать каталог"); //Кнопка для выбора каталога
        final JButton mainButton = new JButton("Старт"); //Кнопка для запуска и остановки процесса конвертирования
        final JLabel currentDirLabel = new JLabel(""); //Показывает выбранную папку
        final JLabel startTimeLabel = new JLabel(""); //Показывает время запуска процесса
        final JLabel stopTimeLabel = new JLabel(""); //Показывает время остановки процесса
        final JFileChooser dirChooser = new JFileChooser(); //Диалоговое окно для выбора папки

        //Исполнитель для выполнения процесса каждые 5 минут
        final ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();

        //Создание экземпляра класса-обертки для отмены текущей задачи исполнителя
        final ScheduleController controller = new ScheduleController();

        //Добавление элементов на панель
        mainFrame.add(getDirectoryButton);
        mainFrame.add(mainButton);
        mainFrame.add(currentDirLabel);
        mainFrame.add(startTimeLabel);
        mainFrame.add(stopTimeLabel);

        //Действие для getDirectoryButton
        getDirectoryButton.addActionListener((ActionEvent e)-> {
                dirChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY); //указываются только папки
                dirChooser.showOpenDialog(mainFrame.getContentPane());
                currentDirLabel.setText("Выбранный каталог - "+dirChooser.getSelectedFile().getAbsolutePath());
        });

        //Действие для кнопки mainButton
        mainButton.addActionListener((ActionEvent e)-> {
            //Проверка на состояние кнопки
            if (mainButton.getText().equals("Старт"))
            {
                //проверка на отсутствие выбранной папки
                if (dirChooser.getSelectedFile()==null) {
                    startTimeLabel.setText("Необходимо выбрать файл!");
                } else{
                    //очистка времени окончания работы при повторном запуске, изменение текста кнопки
                    stopTimeLabel.setText("");
                    mainButton.setText("Стоп");
                    //Запуск исполнителя с кодом каждые 5 минут, присвоение задачи классу ScheduledFuture для отмены процесса
                    controller.future=exec.scheduleAtFixedRate((()->{
                        System.out.println("Процесс запущен");
                        //Проверка, является ли выбранный файл папкой
                        if (dirChooser.getSelectedFile().isDirectory()) {
                            Converter converter = new Converter(); //Создание экземпляра класса-конвертера
                            converter.scanDirForPDF(dirChooser.getSelectedFile()); //сканирование папки и подпапок на наличие PDF
                            converter.convertAllPDFtoJPG(); //конвертация списка PDF в JPG
                        }

                    }),0,5,TimeUnit.MINUTES);
                    startTimeLabel.setText("Время запуска: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                }
            }else {
                //Изменения текста кнопки для возможного повторного запуска процесса
                mainButton.setText("Старт");
                //Отмена выполнения текущей задачи исполнителя
                controller.future.cancel(true);
            }
});

        //Отображение GUI
        mainFrame.setVisible(true);
    }
}

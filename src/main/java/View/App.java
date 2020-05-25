package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
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
        JButton getDirectoryButton = new JButton("Выбрать каталог"); //Кнопка для выбора каталога
        final JButton startButton = new JButton("Старт"); //Кнопка для запуска процесса конвертирования
        final JButton stopButton = new JButton("Стоп");   //Кнопка для остановки процесса конвертирования
        final JLabel currentDirLabel = new JLabel(""); //Показывает выбранную папку
        final JLabel startTimeLabel = new JLabel(""); //Показывает время запуска процесса
        final JLabel stopTimeLabel = new JLabel(""); //Показывает время остановки процесса
        final JFileChooser dirChooser = new JFileChooser(); //Диалоговое окно для выбора папки

        //Исполнитель для выполнения процесса каждые 5 минут
        ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();

        //Добавление элементов на панель
        mainFrame.add(getDirectoryButton);
        mainFrame.add(startButton);
        stopButton.setEnabled(false); //При запуске приложения нет запущенного процесса конвертации
        mainFrame.add(stopButton);
        mainFrame.add(currentDirLabel);
        mainFrame.add(startTimeLabel);
        mainFrame.add(stopTimeLabel);

        //Действие для getDirectoryButton
        getDirectoryButton.addActionListener((ActionEvent e)-> {
                dirChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY); //указываются только папки
                dirChooser.showOpenDialog(mainFrame.getContentPane());
                currentDirLabel.setText("Выбранный каталог - "+dirChooser.getSelectedFile().getAbsolutePath());
        });

        //Действие для кнопки startButton
        startButton.addActionListener((ActionEvent e)-> {
                startButton.setEnabled(false);
                stopButton.setEnabled(true);
                stopTimeLabel.setText("");
                //Запуск исполнителя с кодом каждые 5 минут
                exec.scheduleAtFixedRate((()->{
                    if (dirChooser.getSelectedFile()!=null && dirChooser.getSelectedFile().isDirectory()) {
                        DirScanner dirScanner = new DirScanner();
                        dirScanner.ScanDirForPDF(dirChooser.getSelectedFile());
                    }

                }),0,5,TimeUnit.MINUTES);
                //Проверка, включился ли исполнитель
                if (!exec.isShutdown()) {
                startTimeLabel.setText("Время запуска: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                } else startTimeLabel.setText("Ошибка запуска процесса.");
        });

        //Действие для кнопки stopButton
        stopButton.addActionListener((ActionEvent e)-> {
                startButton.setEnabled(true);
                stopButton.setEnabled(false);
                exec.shutdown();
                //Проверка, выключился ли исполнитель
                if (exec.isShutdown()) {
                    stopTimeLabel.setText("Время остановки: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                } else stopTimeLabel.setText("Ошибка остановки процесса.");
        });

        //Отображение GUI
        mainFrame.setVisible(true);
    }
}

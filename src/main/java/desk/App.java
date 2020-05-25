package desk;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

        //Добавление элементов на панель
        mainFrame.add(getDirectoryButton);
        mainFrame.add(startButton);
        stopButton.setEnabled(false); //При запуске приложения нет запущенного процесса конвертации
        mainFrame.add(stopButton);
        mainFrame.add(currentDirLabel);
        mainFrame.add(startTimeLabel);
        mainFrame.add(stopTimeLabel);

        //Действие для getDirectoryButton

        getDirectoryButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dirChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY); //указываются только папки
                dirChooser.showOpenDialog(mainFrame.getContentPane());
                currentDirLabel.setText("Выбранный каталог - "+dirChooser.getSelectedFile().getAbsolutePath());

            }
        });

        //Действие для кнопки startButton
        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                startButton.setEnabled(false);
                stopButton.setEnabled(true);
                startTimeLabel.setText("Время запуска: "+LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                stopTimeLabel.setText("");
            }
        });

        //Действие для кнопки stopButton
        stopButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                startButton.setEnabled(true);
                stopButton.setEnabled(false);
                stopTimeLabel.setText("Время остановки: "+LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            }
        });

        //Отображение GUI
        mainFrame.setVisible(true);
    }
}

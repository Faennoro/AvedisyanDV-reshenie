package View;

import java.util.concurrent.ScheduledFuture;

/**
 * Created by Faennor on 26.05.2020.
 */
//Класс-обертка для отмены текущей задачи исполнителя
public class ScheduleController {

    //Используется для отмены выполнения процесса по нажатию кнопки Стоп, инициализируется при нажатии кнопки Старт
    public ScheduledFuture future = null;
}

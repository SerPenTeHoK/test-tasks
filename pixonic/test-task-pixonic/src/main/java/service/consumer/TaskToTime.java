package service.consumer;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.concurrent.Callable;

@Data
@AllArgsConstructor
public class TaskToTime {
    private LocalDateTime localDateTime;
    private Callable callable;
    private LocalDateTime orderTime;
}

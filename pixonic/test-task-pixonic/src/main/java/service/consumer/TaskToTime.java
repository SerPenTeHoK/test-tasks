package service.consumer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.concurrent.Callable;

@Data
@AllArgsConstructor
@Builder
public class TaskToTime {
    private final LocalDateTime localDateTime;
    private final Callable callable;
    private final LocalDateTime orderTime;
    private long counterNum;
}

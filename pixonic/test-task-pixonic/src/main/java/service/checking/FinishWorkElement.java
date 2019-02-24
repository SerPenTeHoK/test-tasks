package service.checking;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class FinishWorkElement {
    private final String threadName;
    private final int orderInThread;

    private LocalDateTime needStartTime;
    private LocalDateTime addingToQueueTime;
    private LocalDateTime factSendToRunTime;

    private long counterNum;
}

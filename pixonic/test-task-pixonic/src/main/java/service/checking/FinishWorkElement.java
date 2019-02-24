package service.checking;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FinishWorkElement {
    private String threadName;
    private int order;
}

package model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Border {
    int x1, y1, x2, y2;
    int time;
}

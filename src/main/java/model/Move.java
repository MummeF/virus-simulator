package model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Move {
    private Person person;
    private int fromIndex, toIndex;
}

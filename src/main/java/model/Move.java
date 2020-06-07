package model;

import lombok.Builder;
import lombok.Data;

import java.awt.*;

@Data
@Builder
public class Move {
    private Person person;
    private int fromIndex, toIndex;
}

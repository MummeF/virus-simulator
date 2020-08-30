package model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Border {
    int x1, x2, y1, y2;
    int time;

    public boolean blocks(int fromX, int fromY, int toX, int toY) {
        return (checkX(fromX, toX) && checkY(fromY, toY));
    }

    private boolean checkX(int fromX, int toX) {
        return (this.getX1() == fromX && this.getX2() == toX) || (this.getX1() == toX && this.getX2() == fromX);
    }

    private boolean checkY(int fromY, int toY) {
        return (this.getY1() == fromY && this.getY2() == toY) || (this.getY1() == toY && this.getY2() == fromY);
    }
}

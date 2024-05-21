package Players;

import Grid.Grid;

import java.util.ArrayList;

public abstract class Player {

    protected COLOR_ENUM color;
    public Player(COLOR_ENUM _color){
        this.color = _color;
    }

    public int do_turn(Grid tableau){
        return 0;
    };

    public COLOR_ENUM getColor(){
        return this.color;
    }

    public int[] joueurMax(int depth, Grid tab) {

        if(tab.checkWin() || tab.gridFull() || depth == 0){
            int score = evaluateBoard(tab);
            return new int[]{score, -1};
        }

        int u = Integer.MIN_VALUE;
        int bestColumn = -1;

        for (int i = 0; i < tab.getWidth(); i++) {
            if (tab.canAddToken(i)) {
                Grid newGrid = new Grid(tab.getPlateau());
                newGrid.addToken(i, this.color); // Add token for current player
                int[] eval = joueurMin(depth - 1, newGrid); // Evaluate using joueurMin
                if (eval[0] > u) {
                    u = eval[0];
                    bestColumn = i;
                }
            }
        }

        return new int[]{u, bestColumn};
    }


    public int[] joueurMin(int depth, Grid tab) {
        COLOR_ENUM opponentColor = color == COLOR_ENUM.RED ? COLOR_ENUM.YELLOW : COLOR_ENUM.RED;

        if(tab.checkWin() || tab.gridFull() || depth == 0){
            int score = evaluateBoard(tab);
            return new int[]{score, -1};
        }

        int u = Integer.MAX_VALUE;
        Integer bestColumn = null;

        for (int i = 0; i < tab.getWidth(); i++) {
            if (tab.canAddToken(i)) {
                Grid newGrid = new Grid(tab.getPlateau());
                newGrid.addToken(i, opponentColor); // Add token for opponent
                int[] eval = joueurMax(depth - 1, newGrid); // Evaluate using joueurMax
                if (eval[0] < u) {
                    u = eval[0];
                    bestColumn = i;
                }
            }
        }

        return new int[]{u, bestColumn};
    }

    public int evaluateBoard(Grid tab) {
        int score = 0;
        for (int col = 0; col < tab.getWidth(); col++) {
            int lastRow = tab.firstEmptyCell(col) - 1;
            if (lastRow >= 0) {
                score += evaluateCell(tab, col);
            }
        }
        return score;
    }

    public int evaluateCell(Grid tableau, int column){
        if(column == -1){
            return 0;
        }
        COLOR_ENUM myColor = this.color;
        COLOR_ENUM opponentColor = this.getColor() == COLOR_ENUM.RED ? COLOR_ENUM.YELLOW : COLOR_ENUM.RED;
        int score = 0;
        score += evaluateCellForColor(tableau, column, myColor);
        score -= evaluateCellForColor(tableau, column, opponentColor);
        return score;
    }

    public int evaluateCellForColor(Grid tableau, int column, COLOR_ENUM color){
        int score = 0;
        score += tableau.calculateScoreOfColorOnLine(column, color);
        score += tableau.calculateScoreOfColorOnColumn(column, color);
        score += tableau.calculateScoreOfColorOnDiagonals(column, color);
        return score;
    }
}

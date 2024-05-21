package Grid;

import Players.COLOR_ENUM;

import java.util.ArrayList;
import java.util.HashMap;

public class Grid {
    private COLOR_ENUM[][] plateau;

    public Grid(){
        plateau = new COLOR_ENUM[6][7];
    }


    public Grid(COLOR_ENUM[][] plateau){
        this.plateau = new COLOR_ENUM[6][7];
        for (int i = 0; i < plateau.length; i++) {
            for (int j = 0; j < plateau[0].length; j++) {
                this.plateau[i][j] = plateau[i][j];
            }
        }
    }

    public void addToken(int column, COLOR_ENUM color) {
        for (int i = plateau.length - 1; i >= 0; i--) {
            if (plateau[i][column] == null) {
                setCell(i,column,color);
                return;
            }
        }
    }

    public COLOR_ENUM getCell(int i, int j){
        return plateau[i][j];
    }

    public void setCell(int i, int j, COLOR_ENUM color){
        plateau[i][j] = color;
    }

    public int getLength(){
        return plateau.length;
    }

    public int getWidth(){
        return plateau[0].length;
    }

    public COLOR_ENUM[][] getPlateau() {
        return plateau;
    }

    public void setPlateau(COLOR_ENUM[][] plateau) {
        this.plateau = plateau;
    }

    public Boolean canAddToken(int column){
        return plateau[0][column] == null;
    }

    public int firstEmptyCell(int column){
        for (int i = 0; i < plateau.length; i++) {
            if (plateau[i][column] != null) {
                return i - 1;
            }
        }
        return plateau.length - 1;
    }

    public int lastEmptyCell() {
        int lastEmptyColumn = -1;
        int width = getWidth();
        for (int col = 0; col < width; col++) {
            if (firstEmptyCell(col) >= 0) {
                if (lastEmptyColumn != -1) {
                    // Si on trouve une autre colonne avec une case vide, ce n'est pas la dernière
                    return -1;
                }
                lastEmptyColumn = col;
            }
        }
        return lastEmptyColumn;
    }


    private int calculateScore(int count, int emptyCells) {
        switch (count) {
            case 1:
                if (emptyCells >= 3) {
                    return 1;
                }
                break;
            case 2:
                if (emptyCells >= 2) {
                    return 5;
                }
                break;
            case 3:
                if (emptyCells >= 1) {
                    return 50;
                }
                break;
            case 4:
                return 1000;
            default:
                return 0;
        }
        return 0;
    }


    public int calculateScoreOfColorOnColumn(int column, COLOR_ENUM color){
        int plateauLength = getLength();
        int indexRow = firstEmptyCell(column);
        //si la colonne est remplie
        if(indexRow <= -1){
            return -1;
        }

        // On part du principe que l'on va jouer sur la case actuelle
        int count = 1;
        int i = indexRow + 1;
        while(i < plateauLength && plateau[i][column] == color){
            count++;
            i++;
        }
        // Regarder désormais si il y a le nombre de cases vides nécessaires.
        switch (count){
            case 1:
                if(indexRow >= 3) {
                    return 1;
                } else {
                    return 0;
                }
            case 2:
                if(indexRow >= 2){
                    return 5;
                } else {
                    return 0;
                }
            case 3:
                if(indexRow >= 1){
                    return 50;
                } else {
                    return 0;
                }
            case 4:
                return 1000;
            default:
                return 0;
        }
    }

    public int calculateScoreOfColorOnLine(int column, COLOR_ENUM color){
        int plateauWidth = getWidth();
        int indexRow = firstEmptyCell(column);
        //si la colonne est remplie
        if(indexRow <= -1){
            return -1;
        }

        int maxScore = 0;

        // On va analyser tous les groupes de 4 cases autour de la position jouée
        for (int startCol = Math.max(0, column - 3); startCol <= Math.min(plateauWidth - 4, column); startCol++) {
            int score = evaluateLine(indexRow, startCol, color);
            maxScore = Math.max(maxScore, score);
        }

        return maxScore;
    }


    public int evaluateLine(int line, int startCol, COLOR_ENUM color){
        //on commence count à 1 car on part du principe que l'on va positionner le piont ici
        COLOR_ENUM opponentColor = color == COLOR_ENUM.RED ? COLOR_ENUM.YELLOW : COLOR_ENUM.RED;
        int count = 1;
        int emptyCells = 0;

        for (int i = startCol; i < startCol + 4; i++) {
            if(plateau[line][i] == opponentColor){
                return 0;
            }
            if (plateau[line][i] == color) {
                count++;
            } else if (plateau[line][i] == null) {
                emptyCells++;
            }
        }

        return calculateScore(count, emptyCells);
    }

    public int calculateScoreOfColorOnDiagonals(int column, COLOR_ENUM color) {
        int plateauWidth = getWidth();
        int plateauLength = getLength();
        int indexRow = firstEmptyCell(column);

        // Si la colonne est remplie
        if (indexRow <= -1) {
            return -1;
        }

        int maxScore = 0;

        // Calculer les limites pour la diagonale décroissante
        int LimitDecreasingDiagonal = Math.min(3, Math.min(indexRow, column));

        // Itération pour les diagonales décroissantes
        for (int i = -LimitDecreasingDiagonal; i <= LimitDecreasingDiagonal; i++) {
            int row = indexRow - i;
            int col = column - i;
            if (row >= 0 && row <= plateauLength - 4 && col >= 0 && col <= plateauWidth - 4) {
                int score = evaluateDecreasingDiagonal(row, col, color);
                maxScore = Math.max(maxScore, score);
            }
        }

        // Calculer les limites pour la diagonale croissante
        int LimitIncreasingDiagonal = Math.min(3, Math.min(indexRow, plateauWidth - column - 1));

        // Itération pour les diagonales croissantes
        for (int i = -LimitIncreasingDiagonal; i <= LimitIncreasingDiagonal; i++) {
            int row = indexRow + i;
            int col = column - i;
            if (row >= 3 && row < plateauLength && col >= 0 && col <= plateauWidth - 4) {
                int score = evaluateIncreasingDiagonal(row, col, color);
                maxScore = Math.max(maxScore, score);
            }
        }

        return maxScore;
    }


    public int evaluateDecreasingDiagonal(int line, int column, COLOR_ENUM color) {
        COLOR_ENUM opponentColor = color == COLOR_ENUM.RED ? COLOR_ENUM.YELLOW : COLOR_ENUM.RED;
        int count = 1;
        int emptyCells = 0;

        for (int i = 0; i < 4; i++) {
            if (plateau[line + i][column + i] == opponentColor) {
                return 0;
            }
            if (plateau[line + i][column + i] == color) {
                count++;
            } else if (plateau[line + i][column + i] == null) {
                emptyCells++;
            }
        }

        return calculateScore(count, emptyCells);
    }

    public int evaluateIncreasingDiagonal(int line, int column, COLOR_ENUM color) {
        COLOR_ENUM opponentColor = color == COLOR_ENUM.RED ? COLOR_ENUM.YELLOW : COLOR_ENUM.RED;
        int count = 1;
        int emptyCells = 0;

        for (int i = 0; i < 4; i++) {
            if (plateau[line - i][column + i] == opponentColor) {
                return 0;
            }
            if (plateau[line - i][column + i] == color) {
                count++;
            } else if (plateau[line - i][column + i] == null) {
                emptyCells++;
            }
        }

        return calculateScore(count, emptyCells);
    }


    public ArrayList<HashMap<Integer, Integer>> possibleColumns() {
        ArrayList<HashMap<Integer, Integer>> possibleColumns = new ArrayList<>();
        int plateauLength = getLength();
        for (int i = 0; i < plateauLength; i++) {
            int index = firstEmptyCell(i);
            if (index != -1) {
                HashMap<Integer, Integer> pc = new HashMap<>();
                pc.put(i, index);
                possibleColumns.add(pc);
            }
        }
        return possibleColumns;
    }

    public Boolean gridFull(){
        for (int i = 0; i < plateau[0].length; i++) {
            if (plateau[0][i] == null) {
                return false;
            }
        }
        return true;
    }

    public Boolean checkWin(){
        int plateauLength = this.getLength();
        int plateauWidth = this.getWidth();
        for (int i = 0; i < plateauLength; i++) {
            for (int j = 0; j < plateauWidth - 3; j++) {
                if (this.getCell(i, j) != null && this.getCell(i, j) == this.getCell(i, j + 1) && this.getCell(i, j) == this.getCell(i,j + 2) && this.getCell(i, j) == this.getCell(i, j + 3)) {
                    return true;
                }
            }
        }
        for (int i = 0; i < plateauLength - 3; i++) {
            for (int j = 0; j < plateauWidth; j++) {
                if (this.getCell(i, j) != null && this.getCell(i, j) == this.getCell(i + 1, j ) && this.getCell(i, j) == this.getCell(i + 2,j) && this.getCell(i, j) == this.getCell(i + 3,j)) {
                    return true;
                }
            }
        }
        for (int i = 0; i < plateauLength - 3; i++) {
            for (int j = 0; j < plateauWidth - 3; j++) {
                if (this.getCell(i, j) != null && this.getCell(i, j) == this.getCell(i + 1, j + 1) && this.getCell(i, j) == this.getCell(i + 2, j + 2) && this.getCell(i, j) == this.getCell(i + 3, j + 3)) {
                    return true;
                }
            }
        }
        for (int i = 0; i < plateauLength - 3; i++) {
            for (int j = 3; j < plateauWidth; j++) {
                if (this.getCell(i, j) != null && this.getCell(i, j) == this.getCell(i + 1, j  - 1) && this.getCell(i, j) == this.getCell(i + 2, j - 2) && this.getCell(i, j) == this.getCell(i + 3, j - 3)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void display(){
        for (int i = 0; i < plateau.length; i++) {
            System.out.print(i + " ");
            for (int j = 0; j < plateau[0].length; j++) {
                System.out.print(plateau[i][j] + " ");
            }
            System.out.println();
        }
    }
}

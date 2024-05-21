package Players;
import Grid.Grid;


public class MinimaxIAPlayer extends Player{

    public MinimaxIAPlayer(COLOR_ENUM _color){
        super(_color);
    }

    @Override
    public int do_turn(Grid tableau){
        return minimax(tableau, true, 5);
    }

    public int minimax(Grid tableau, Boolean isMaximizingPlayer, int maxDepth){
        if(isMaximizingPlayer){
            return joueurMax(maxDepth, tableau)[1];
        } else {
            return joueurMin(maxDepth, tableau)[1];
        }
    }



}

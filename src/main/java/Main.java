public class Main {

    public static void main(String[] args){
        Game game = new Game();
        //game.startGamePVP();
        //2 - the best heuristic
        int heuristicNumber = 3;
        //game.startGamePVPWithMinMaxAndEvaluation(heuristicNumber);
        game.startGameAIvsAI(heuristicNumber, "minmax");
        //game.startGameWhiteVsAI(heuristicNumber, "minmax");
        //game.startGameBlackVsAI(heuristicNumber, "minmax");
    }
}

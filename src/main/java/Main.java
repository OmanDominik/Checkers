public class Main {

    public static void main(String[] args){
        Game game = new Game();
        //game.startGamePVP();
        //2 - the best heuristic
        int heuristicNumber = 1;
        //game.startGamePVPWithMinMaxAndEvaluation(heuristicNumber);
        game.startGameAIvsAI(heuristicNumber);
        //game.startGameWhiteVsAI(heuristicNumber);
        //game.startGameBlackVsAI(heuristicNumber);
    }
}

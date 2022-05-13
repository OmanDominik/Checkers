import lombok.Getter;
import lombok.Setter;

import java.util.*;

import java.util.stream.Collectors;

@Getter
@Setter
public class Game {
    private static final int BOARD_SIZE = 8;
    private static final int MIN_MAX_DEPTH = 6;
    private List<List<Character>> board;
    int queensMoves = 0;

    public Game() {
        reset();
    }

    public void displayBoard() {
        String fieldColor;
        System.out.print(" ");
        for (int k = 0; k < BOARD_SIZE; k++) {
            System.out.print("  " + k);
        }
        System.out.println();
        for (int i = 0; i < BOARD_SIZE; i++) {
            System.out.print(i + " ");
            for (int j = 0; j < BOARD_SIZE; j++) {
                Character field = board.get(i).get(j);
                if ((i + j) % 2 == 0) {
                    fieldColor = Colors.ANSI_WHITE_BACKGROUND;
                } else {
                    fieldColor = Colors.ANSI_BLACK_BACKGROUND;
                }
                if (field != 'e') {
                    if (field == 'w')
                        System.out.print(fieldColor + ' ' + Colors.ANSI_WHITE + '●' + ' ' + Colors.ANSI_RESET);
                    if (field == 'W')
                        System.out.print(fieldColor + ' ' + Colors.ANSI_WHITE + '◆' + ' ' + Colors.ANSI_RESET);
                    if (field == 'b')
                        System.out.print(fieldColor + ' ' + Colors.ANSI_RED + '●' + ' ' + Colors.ANSI_RESET);
                    if (field == 'B')
                        System.out.print(fieldColor + ' ' + Colors.ANSI_RED + '◆' + ' ' + Colors.ANSI_RESET);
                } else
                    System.out.print(fieldColor + "   " + Colors.ANSI_RESET);
            }
            System.out.println();
        }
    }

    public void startGamePVP() {
        queensMoves = 0;
        Scanner scan = new Scanner(System.in);
        int move;
        List<String> possibleMoves = new ArrayList<>();
        System.out.println(Colors.ANSI_PURPLE + "THE GAME HAS STARTED!!!" + Colors.ANSI_RESET);
        while (queensMoves < 15) {
            System.out.println(Colors.ANSI_GREEN + "The state of the board:" + Colors.ANSI_RESET);
            displayBoard();
            if (lookForWhites()) {
                System.out.println(Colors.ANSI_CYAN + "Blacks won!!!");
                break;
            }

            System.out.println(Colors.ANSI_RED + "White's turn");
            possibleMoves = getWhitePossibleMoves(board);
            if (possibleMoves.isEmpty()) {
                System.out.println(Colors.ANSI_CYAN + "Blacks won!!!");
                break;
            }
            System.out.println(Colors.ANSI_BLUE + "Moves to make:");

            displayMoves(possibleMoves);
            System.out.print(Colors.ANSI_GREEN + "Choose move(give a number), wisely 😉: ");
            move = scan.nextByte();
            makeMove(board, possibleMoves.get(move), true);

            System.out.println(Colors.ANSI_GREEN + "The state of the board:" + Colors.ANSI_RESET);
            displayBoard();
            if (lookForBlacks()) {
                System.out.println(Colors.ANSI_CYAN + "Whites won!!!");
                break;
            }

            System.out.println(Colors.ANSI_RED + "Black's turn");
            possibleMoves = getBlackPossibleMoves(board);
            if (possibleMoves.isEmpty()) {
                System.out.println(Colors.ANSI_CYAN + "Whites won!!!");
                break;
            }
            System.out.println(Colors.ANSI_BLUE + "Moves to make:");

            displayMoves(possibleMoves);
            System.out.print(Colors.ANSI_GREEN + "Choose move(give a number), wisely 😉: ");
            move = scan.nextByte();
            makeMove(board, possibleMoves.get(move), true);

        }
        if (queensMoves >= 15) {
            System.out.println(Colors.ANSI_CYAN + "It's a draw!!!");
        }
        System.out.println(Colors.ANSI_BLUE + "Thanks for game!");
        reset();
    }

    public void startGamePVPWithMinMaxAndEvaluation(int heuristicNumber) {
        queensMoves = 0;
        Scanner scan = new Scanner(System.in);
        int move;
        List<String> possibleMoves = new ArrayList<>();
        System.out.println(Colors.ANSI_PURPLE + "THE GAME HAS STARTED!!!" + Colors.ANSI_RESET);
        while (queensMoves < 15) {
            System.out.println(Colors.ANSI_GREEN + "The state of the board:" + Colors.ANSI_RESET);
            displayBoard();
            displayEvaluation(heuristicNumber);
            if (lookForWhites()) {
                System.out.println(Colors.ANSI_CYAN + "Blacks won!!!");
                break;
            }

            System.out.println(Colors.ANSI_RED + "White's turn");
            possibleMoves = getWhitePossibleMoves(board);
            if (possibleMoves.isEmpty()) {
                System.out.println(Colors.ANSI_CYAN + "Blacks won!!!");
                break;
            }
            System.out.println(Colors.ANSI_BLUE + "Moves to make:");

            displayMoves(possibleMoves);
            displaySuggestedMove("whites", deepCopy(board), heuristicNumber);
            System.out.print(Colors.ANSI_GREEN + "Choose move(give a number), wisely 😉: ");
            move = scan.nextByte();
            makeMove(board, possibleMoves.get(move), true);

            System.out.println(Colors.ANSI_GREEN + "The state of the board:" + Colors.ANSI_RESET);
            displayBoard();
            displayEvaluation(heuristicNumber);
            if (lookForBlacks()) {
                System.out.println(Colors.ANSI_CYAN + "Whites won!!!");
                break;
            }

            System.out.println(Colors.ANSI_RED + "Black's turn");
            possibleMoves = getBlackPossibleMoves(board);
            if (possibleMoves.isEmpty()) {
                System.out.println(Colors.ANSI_CYAN + "Whites won!!!");
                break;
            }
            System.out.println(Colors.ANSI_BLUE + "Moves to make:");

            displayMoves(possibleMoves);
            displaySuggestedMove("blacks", deepCopy(board), heuristicNumber);
            System.out.print(Colors.ANSI_GREEN + "Choose move(give a number), wisely 😉: ");
            move = scan.nextByte();
            makeMove(board, possibleMoves.get(move), true);

        }
        if (queensMoves >= 15) {
            System.out.println(Colors.ANSI_CYAN + "It's a draw!!!");
        }
        System.out.println(Colors.ANSI_BLUE + "Thanks for game!");
        reset();
    }

    public void startGameAIvsAI(int heuristicNumber) {
        queensMoves = 0;
        int whiteMoves=0;
        int blackMoves=0;
        List<Long> whiteTime = new ArrayList<>();
        List<Long> blackTime = new ArrayList<>();
        long beforeWhiteMove;
        long afterWhiteMove;
        long beforeBlackMove;
        long afterBlackMove;

        String move;
        List<String> possibleMoves = new ArrayList<>();
        System.out.println(Colors.ANSI_PURPLE + "THE GAME HAS STARTED!!!" + Colors.ANSI_RESET);
        while (queensMoves < 15) {
            System.out.println(Colors.ANSI_GREEN + "The state of the board:" + Colors.ANSI_RESET);
            displayBoard();
            displayEvaluation(heuristicNumber);
            if (lookForWhites()) {
                System.out.println(Colors.ANSI_CYAN + "Blacks won!!!");
                break;
            }

            System.out.println(Colors.ANSI_RED + "White's turn");
            beforeWhiteMove = System.currentTimeMillis();
            possibleMoves = getWhitePossibleMoves(board);
            if (possibleMoves.isEmpty()) {
                System.out.println(Colors.ANSI_CYAN + "Blacks won!!!");
                break;
            }

            move = returnSuggestedMove("whites", deepCopy(board), heuristicNumber);
            makeMove(board, move, true);
            whiteMoves++;
            afterWhiteMove = System.currentTimeMillis();
            whiteTime.add(afterWhiteMove-beforeWhiteMove);

            System.out.println(Colors.ANSI_GREEN + "The state of the board:" + Colors.ANSI_RESET);
            displayBoard();
            displayEvaluation(heuristicNumber);
            if (lookForBlacks()) {
                System.out.println(Colors.ANSI_CYAN + "Whites won!!!");
                break;
            }

            System.out.println(Colors.ANSI_RED + "Black's turn");
            beforeBlackMove = System.currentTimeMillis();
            possibleMoves = getBlackPossibleMoves(board);
            if (possibleMoves.isEmpty()) {
                System.out.println(Colors.ANSI_CYAN + "Whites won!!!");
                break;
            }

            move = returnSuggestedMove("blacks", deepCopy(board), heuristicNumber);
            makeMove(board, move, true);
            blackMoves++;
            afterBlackMove = System.currentTimeMillis();
            blackTime.add(afterBlackMove-beforeBlackMove);
        }
        if (queensMoves >= 15) {
            System.out.println(Colors.ANSI_CYAN + "It's a draw!!!");
            System.out.println(Colors.ANSI_YELLOW + "Time spent for white moves: " +
                    (whiteTime.stream().collect(Collectors.summingLong(Long::longValue)))/1000 + " seconds");
            System.out.println("White moves: " + whiteMoves);
            System.out.println(Colors.ANSI_YELLOW + "Time spent for black moves: " +
                    (blackTime.stream().collect(Collectors.summingLong(Long::longValue)))/1000 + " seconds");
            System.out.println("Black moves: " + blackMoves);
        }
        System.out.println(Colors.ANSI_BLUE + "Thanks for game!");
        reset();
    }

    public void startGameWhiteVsAI(int heuristicNumber) {
        queensMoves = 0;
        Scanner scan = new Scanner(System.in);
        int move;
        String AiMove;
        List<String> possibleMoves = new ArrayList<>();
        System.out.println(Colors.ANSI_PURPLE + "THE GAME HAS STARTED!!!" + Colors.ANSI_RESET);
        while (queensMoves < 15) {
            System.out.println(Colors.ANSI_GREEN + "The state of the board:" + Colors.ANSI_RESET);
            displayBoard();
            displayEvaluation(heuristicNumber);
            if (lookForWhites()) {
                System.out.println(Colors.ANSI_CYAN + "Blacks won!!!");
                break;
            }

            System.out.println(Colors.ANSI_RED + "White's turn");
            possibleMoves = getWhitePossibleMoves(board);
            if (possibleMoves.isEmpty()) {
                System.out.println(Colors.ANSI_CYAN + "Blacks won!!!");
                break;
            }
            System.out.println(Colors.ANSI_BLUE + "Moves to make:");

            displayMoves(possibleMoves);
            System.out.print(Colors.ANSI_GREEN + "Choose move(give a number), wisely 😉: ");
            move = scan.nextByte();
            makeMove(board, possibleMoves.get(move), true);

            System.out.println(Colors.ANSI_GREEN + "The state of the board:" + Colors.ANSI_RESET);
            displayBoard();
            displayEvaluation(heuristicNumber);
            if (lookForBlacks()) {
                System.out.println(Colors.ANSI_CYAN + "Whites won!!!");
                break;
            }

            System.out.println(Colors.ANSI_RED + "Black's turn");
            possibleMoves = getBlackPossibleMoves(board);
            if (possibleMoves.isEmpty()) {
                System.out.println(Colors.ANSI_CYAN + "Whites won!!!");
                break;
            }

            AiMove = returnSuggestedMove("blacks", deepCopy(board), heuristicNumber);
            makeMove(board, AiMove, true);

        }
        if (queensMoves >= 15) {
            System.out.println(Colors.ANSI_CYAN + "It's a draw!!!");
        }
        System.out.println(Colors.ANSI_BLUE + "Thanks for game!");
        reset();
    }

    public void startGameBlackVsAI(int heuristicNumber) {
        queensMoves = 0;
        Scanner scan = new Scanner(System.in);
        String AiMove;
        int move;
        List<String> possibleMoves = new ArrayList<>();
        System.out.println(Colors.ANSI_PURPLE + "THE GAME HAS STARTED!!!" + Colors.ANSI_RESET);
        while (queensMoves < 15) {
            System.out.println(Colors.ANSI_GREEN + "The state of the board:" + Colors.ANSI_RESET);
            displayBoard();
            displayEvaluation(heuristicNumber);
            if (lookForWhites()) {
                System.out.println(Colors.ANSI_CYAN + "Blacks won!!!");
                break;
            }

            System.out.println(Colors.ANSI_RED + "White's turn");
            possibleMoves = getWhitePossibleMoves(board);
            if (possibleMoves.isEmpty()) {
                System.out.println(Colors.ANSI_CYAN + "Blacks won!!!");
                break;
            }

            AiMove = returnSuggestedMove("whites", deepCopy(board), heuristicNumber);
            makeMove(board, AiMove, true);

            System.out.println(Colors.ANSI_GREEN + "The state of the board:" + Colors.ANSI_RESET);
            displayBoard();
            displayEvaluation(heuristicNumber);
            if (lookForBlacks()) {
                System.out.println(Colors.ANSI_CYAN + "Whites won!!!");
                break;
            }

            System.out.println(Colors.ANSI_RED + "Black's turn");
            possibleMoves = getBlackPossibleMoves(board);
            if (possibleMoves.isEmpty()) {
                System.out.println(Colors.ANSI_CYAN + "Whites won!!!");
                break;
            }
            System.out.println(Colors.ANSI_BLUE + "Moves to make:");

            displayMoves(possibleMoves);
            System.out.print(Colors.ANSI_GREEN + "Choose move(give a number), wisely 😉: ");
            move = scan.nextByte();
            makeMove(board, possibleMoves.get(move), true);

        }
        if (queensMoves >= 15) {
            System.out.println(Colors.ANSI_CYAN + "It's a draw!!!");
        }
        System.out.println(Colors.ANSI_BLUE + "Thanks for game!");
        reset();
    }

    private void displayMoves(List<String> possibleMoves) {
        for (int i = 0; i < possibleMoves.size(); i++) {
            System.out.println(i + ":  " + possibleMoves.get(i));
        }
    }

    private List<String> getWhitePossibleMoves(List<List<Character>> board) {
        List<String> moves = new ArrayList<>();
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                String move = "";
                if (board.get(i).get(j) == 'w') {
                    //moves
                    if (i != 0 && j != 0)
                        if (board.get(i - 1).get(j - 1) == 'e') {
                            moves.add('(' + String.valueOf(i) + ',' + String.valueOf(j) + ") ->" +
                                    " (" + String.valueOf(i - 1) + ',' + String.valueOf(j - 1) + ") move");
                        }
                    if (i != 0 && j != BOARD_SIZE - 1)
                        if (board.get(i - 1).get(j + 1) == 'e') {
                            moves.add('(' + String.valueOf(i) + ',' + String.valueOf(j) + ") ->" +
                                    " (" + String.valueOf(i - 1) + ',' + String.valueOf(j + 1) + ") move");
                        }
                    //beat
                    move = '(' + String.valueOf(i) + ',' + String.valueOf(j) + ") ->" + " (";
                    int row = i;
                    int col = j;
                    findBeatsForWhite(board, row, col, move, moves, -1, -1, new ArrayList<Integer>());
                } else if (board.get(i).get(j) == 'W') {
                    move = '(' + String.valueOf(i) + ',' + String.valueOf(j) + ") ->" + " (";
                    int row = i;
                    int col = j;
                    findQueenMoves(board, row, col, move, moves);
                    findBeatsForWhiteQueens(board, row, col, move, moves, -1, -1, new ArrayList<Integer>());
                }
            }
        }
        return optimizeMoves(moves);
    }

    private List<String> getBlackPossibleMoves(List<List<Character>> board) {
        List<String> moves = new ArrayList<>();
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                String move = "";
                if (board.get(i).get(j) == 'b') {
                    if (i != BOARD_SIZE - 1 && j != 0)
                        if (board.get(i + 1).get(j - 1) == 'e') {
                            moves.add('(' + String.valueOf(i) + ',' + String.valueOf(j) + ") ->" +
                                    " (" + String.valueOf(i + 1) + ',' + String.valueOf(j - 1) + ") move");
                        }
                    if (i != BOARD_SIZE - 1 && j != BOARD_SIZE - 1)
                        if (board.get(i + 1).get(j + 1) == 'e') {
                            moves.add('(' + String.valueOf(i) + ',' + String.valueOf(j) + ") ->" +
                                    " (" + String.valueOf(i + 1) + ',' + String.valueOf(j + 1) + ") move");
                        }
                    move = '(' + String.valueOf(i) + ',' + String.valueOf(j) + ") ->" + " (";
                    int row = i;
                    int col = j;
                    findBeatsForBlack(board, row, col, move, moves, -1, -1, new ArrayList<Integer>());
                } else if (board.get(i).get(j) == 'B') {
                    move = '(' + String.valueOf(i) + ',' + String.valueOf(j) + ") ->" + " (";
                    int row = i;
                    int col = j;
                    findQueenMoves(board, row, col, move, moves);
                    findBeatsForBlackQueens(board, row, col, move, moves, -1, -1, new ArrayList<Integer>());
                }
            }
        }
        return optimizeMoves(moves);
    }

    private List<String> optimizeMoves(List<String> moves) {
        int maxLength = 0;

        for (String move : moves) {
            if (move.length() > maxLength)
                maxLength = move.length();
        }

        for (int i = 0; i < moves.size(); i++) {
            if (moves.get(i).length() != maxLength) {
                moves.remove(i);
                i--;
            }
        }

        return moves;
    }

    private void makeMove(List<List<Character>> board, String move, boolean inGame) {
        if (inGame)
            System.out.println(move);
        if (move.endsWith("move")) {
            int startRow = Integer.parseInt(String.valueOf(move.charAt(1)));
            int startCol = Integer.parseInt(String.valueOf(move.charAt(3)));
            int finishRow = Integer.parseInt(String.valueOf(move.charAt(10)));
            int finishCol = Integer.parseInt(String.valueOf(move.charAt(12)));
            Character pawn = board.get(startRow).get(startCol);
            if ((pawn == 'W' || pawn == 'B') && inGame) {
                queensMoves++;
            } else if (inGame) {
                queensMoves = 0;
            }
            board.get(startRow).set(startCol, 'e');
            board.get(finishRow).set(finishCol, pawn);
            if (finishRow == 0 || finishRow == BOARD_SIZE - 1) {
                if (pawn == 'b')
                    board.get(finishRow).set(finishCol, 'B');
                if (pawn == 'w')
                    board.get(finishRow).set(finishCol, 'W');
            }
        }
        if (move.endsWith("beat!")) {
            if(inGame)
                queensMoves = 0;
            List<Integer> indexes = new ArrayList<>();
            for (int i = 0; i < move.length(); i++) {
                if ((int) move.charAt(i) >= 48 && (int) move.charAt(i) <= 57)
                    indexes.add(Integer.parseInt(String.valueOf(move.charAt(i))));
            }
            Character pawn = board.get(indexes.get(0)).get(indexes.get(1));
            board.get(indexes.get(0)).set(indexes.get(1), 'e');
            board.get(indexes.get(indexes.size() - 2)).set(indexes.get(indexes.size() - 1), pawn);
            if (indexes.get(indexes.size() - 2) == 0) {
                if (pawn == 'w')
                    board.get(indexes.get(indexes.size() - 2)).set(indexes.get(indexes.size() - 1), 'W');
            }
            if(indexes.get(indexes.size() - 2) == BOARD_SIZE - 1){
                if (pawn == 'b')
                    board.get(indexes.get(indexes.size() - 2)).set(indexes.get(indexes.size() - 1), 'B');
            }
            for (int i = 0; i < indexes.size() - 2; i += 2) {
                board.get((indexes.get(i) + indexes.get(i + 2)) / 2).set((indexes.get(i + 1) + indexes.get(i + 3)) / 2, 'e');
            }

        }
        if (move.endsWith("beatQ")) {
            if(inGame)
                queensMoves = 0;
            List<Integer> indexes = new ArrayList<>();
            for (int i = 0; i < move.length(); i++) {
                if ((int) move.charAt(i) >= 48 && (int) move.charAt(i) <= 57)
                    indexes.add(Integer.parseInt(String.valueOf(move.charAt(i))));
            }
            Character pawn = board.get(indexes.get(0)).get(indexes.get(1));
            board.get(indexes.get(0)).set(indexes.get(1), 'e');
            board.get(indexes.get(indexes.size() - 2)).set(indexes.get(indexes.size() - 1), pawn);
            int startRow = 0;
            int startCol = 0;
            int finishRow = 0;
            int finishCol = 0;
            for (int i = 0; i < indexes.size() - 2; i += 2) {
                startRow = indexes.get(i);
                startCol = indexes.get(i + 1);
                finishRow = indexes.get(i + 2);
                finishCol = indexes.get(i + 3);

                while (startRow != finishRow && startCol != finishCol) {
                    board.get(startRow).set(startCol, 'e');
                    if (finishRow > startRow) {
                        startRow++;
                    } else {
                        startRow--;
                    }
                    if (finishCol > startCol) {
                        startCol++;
                    } else {
                        startCol--;
                    }
                }
            }
        }
    }

    private List<List<Character>> deepCopy(List<List<Character>> board) {
        List<List<Character>> result = new ArrayList<>();
        for (int i = 0; i < BOARD_SIZE; i++) {
            result.add(new ArrayList<>());
            for (int j = 0; j < BOARD_SIZE; j++) {
                result.get(i).add(board.get(i).get(j));
            }
        }

        return result;
    }

    private void findBeatsForWhite(List<List<Character>> board, int row, int col, String move, List<String> moves, int previousRow, int previousCol, List<Integer> beaten) {

        if (!(previousRow == row - 2 && previousCol == col - 2))
            if (row > 1 && col > 1) {
                if (((board.get(row - 1).get(col - 1) == 'b' || board.get(row - 1).get(col - 1) == 'B') && !isBeaten(row - 1, col - 1, beaten))
                        && board.get(row - 2).get(col - 2) == 'e') {
                    beaten.add(row - 1);
                    beaten.add(col - 1);
                    String newMove1 = move.concat(String.valueOf(row - 2) + ',' + String.valueOf(col - 2) + ')');
                    moves.add(newMove1 + " beat!");
                    findBeatsForWhite(board, row - 2, col - 2, (newMove1 + " -> ("), moves, row, col, beaten);
                }
            }
        if (!(previousRow == row - 2 && previousCol == col + 2))
            if (row > 1 && col < BOARD_SIZE - 2) {
                if (((board.get(row - 1).get(col + 1) == 'b' || board.get(row - 1).get(col + 1) == 'B') && !isBeaten(row - 1, col + 1, beaten))
                        && board.get(row - 2).get(col + 2) == 'e') {
                    beaten.add(row - 1);
                    beaten.add(col + 1);
                    String newMove2 = move.concat(String.valueOf(row - 2) + ',' + String.valueOf(col + 2) + ')');
                    moves.add(newMove2 + " beat!");
                    findBeatsForWhite(board, row - 2, col + 2, (newMove2 + " -> ("), moves, row, col, beaten);
                }
            }
        if (!(previousRow == row + 2 && previousCol == col + 2))
            if (row < BOARD_SIZE - 2 && col < BOARD_SIZE - 2) {
                if (((board.get(row + 1).get(col + 1) == 'b' || board.get(row + 1).get(col + 1) == 'B') && !isBeaten(row + 1, col + 1, beaten))
                        && board.get(row + 2).get(col + 2) == 'e') {
                    beaten.add(row + 1);
                    beaten.add(col + 1);
                    String newMove3 = move.concat(String.valueOf(row + 2) + ',' + String.valueOf(col + 2) + ')');
                    moves.add(newMove3 + " beat!");
                    findBeatsForWhite(board, row + 2, col + 2, (newMove3 + " -> ("), moves, row, col, beaten);
                }
            }
        if (!(previousRow == row + 2 && previousCol == col - 2))
            if (row < BOARD_SIZE - 2 && col > 1) {
                if (((board.get(row + 1).get(col - 1) == 'b' || board.get(row + 1).get(col - 1) == 'B') && !isBeaten(row + 1, col - 1, beaten))
                        && board.get(row + 2).get(col - 2) == 'e') {
                    beaten.add(row + 1);
                    beaten.add(col - 1);
                    String newMove4 = move.concat(String.valueOf(row + 2) + ',' + String.valueOf(col - 2) + ')');
                    moves.add(newMove4 + " beat!");
                    findBeatsForWhite(board, row + 2, col - 2, (newMove4 + " -> ("), moves, row, col, beaten);
                }
            }
    }

    private void findBeatsForBlack(List<List<Character>> board, int row, int col, String move, List<String> moves, int previousRow, int previousCol, List<Integer> beaten) {

        if (!(previousRow == row - 2 && previousCol == col - 2))
            if (row > 1 && col > 1) {
                if (((board.get(row - 1).get(col - 1) == 'w' || board.get(row - 1).get(col - 1) == 'W') && !isBeaten(row - 1, col - 1, beaten))
                        && board.get(row - 2).get(col - 2) == 'e') {
                    beaten.add(row - 1);
                    beaten.add(col - 1);
                    String newMove1 = move.concat(String.valueOf(row - 2) + ',' + String.valueOf(col - 2) + ')');
                    moves.add(newMove1 + " beat!");
                    findBeatsForBlack(board, row - 2, col - 2, (newMove1 + " -> ("), moves, row, col, beaten);
                }
            }
        if (!(previousRow == row - 2 && previousCol == col + 2))
            if (row > 1 && col < BOARD_SIZE - 2) {
                if (((board.get(row - 1).get(col + 1) == 'w' || board.get(row - 1).get(col + 1) == 'W') && !isBeaten(row - 1, col + 1, beaten))
                        && board.get(row - 2).get(col + 2) == 'e') {
                    beaten.add(row - 1);
                    beaten.add(col + 1);
                    String newMove2 = move.concat(String.valueOf(row - 2) + ',' + String.valueOf(col + 2) + ')');
                    moves.add(newMove2 + " beat!");
                    findBeatsForBlack(board, row - 2, col + 2, (newMove2 + " -> ("), moves, row, col, beaten);
                }
            }
        if (!(previousRow == row + 2 && previousCol == col + 2))
            if (row < BOARD_SIZE - 2 && col < BOARD_SIZE - 2) {
                if (((board.get(row + 1).get(col + 1) == 'w' || board.get(row + 1).get(col + 1) == 'W') && !isBeaten(row + 1, col + 1, beaten))
                        && board.get(row + 2).get(col + 2) == 'e') {
                    beaten.add(row + 1);
                    beaten.add(col + 1);
                    String newMove3 = move.concat(String.valueOf(row + 2) + ',' + String.valueOf(col + 2) + ')');
                    moves.add(newMove3 + " beat!");
                    findBeatsForBlack(board, row + 2, col + 2, (newMove3 + " -> ("), moves, row, col, beaten);
                }
            }
        if (!(previousRow == row + 2 && previousCol == col - 2))
            if (row < BOARD_SIZE - 2 && col > 1) {
                if (((board.get(row + 1).get(col - 1) == 'w' || board.get(row + 1).get(col - 1) == 'W') && !isBeaten(row + 1, col - 1, beaten))
                        && board.get(row + 2).get(col - 2) == 'e') {
                    beaten.add(row + 1);
                    beaten.add(col - 1);
                    String newMove4 = move.concat(String.valueOf(row + 2) + ',' + String.valueOf(col - 2) + ')');
                    moves.add(newMove4 + " beat!");
                    findBeatsForBlack(board, row + 2, col - 2, (newMove4 + " -> ("), moves, row, col, beaten);
                }
            }
    }

    private void findQueenMoves(List<List<Character>> board, int row, int col, String move, List<String> moves) {
        int tempRow = row;
        int tempCol = col;
        while (tempRow > 0 && tempCol > 0) {
            if (board.get(tempRow - 1).get(tempCol - 1) == 'e') {
                String newMove1 = move.concat(String.valueOf(tempRow - 1) + ',' + String.valueOf(tempCol - 1) + ')');
                moves.add(newMove1 + " move");
                tempRow--;
                tempCol--;
            } else {
                break;
            }
        }
        tempRow = row;
        tempCol = col;
        while (tempRow > 0 && tempCol < BOARD_SIZE - 1) {
            if (board.get(tempRow - 1).get(tempCol + 1) == 'e') {
                String newMove2 = move.concat(String.valueOf(tempRow - 1) + ',' + String.valueOf(tempCol + 1) + ')');
                moves.add(newMove2 + " move");
                tempRow--;
                tempCol++;
            } else {
                break;
            }
        }
        tempRow = row;
        tempCol = col;
        while (tempRow < BOARD_SIZE - 1 && tempCol < BOARD_SIZE - 1) {
            if (board.get(tempRow + 1).get(tempCol + 1) == 'e') {
                String newMove3 = move.concat(String.valueOf(tempRow + 1) + ',' + String.valueOf(tempCol + 1) + ')');
                moves.add(newMove3 + " move");
                tempRow++;
                tempCol++;
            } else {
                break;
            }
        }
        tempRow = row;
        tempCol = col;
        while (tempRow < BOARD_SIZE - 1 && tempCol > 0) {
            if (board.get(tempRow + 1).get(tempCol - 1) == 'e') {
                String newMove4 = move.concat(String.valueOf(tempRow + 1) + ',' + String.valueOf(tempCol - 1) + ')');
                moves.add(newMove4 + " move");
                tempRow++;
                tempCol--;
            } else {
                break;
            }
        }
    }

    private void findBeatsForWhiteQueens(List<List<Character>> board, int row, int col, String move, List<String> moves, int previousRow, int previousCol, List<Integer> beaten) {
        int tempRow = row;
        int tempCol = col;
        boolean found = false;
        if (!((tempRow - 1) == previousRow && (tempCol - 1) == previousCol)) {
            while (tempRow > 1 && tempCol > 1) {
                if (board.get(tempRow - 1).get(tempCol - 1) == 'e') {
                    tempRow--;
                    tempCol--;
                } else if (((board.get(tempRow - 1).get(tempCol - 1) == 'b') || (board.get(tempRow - 1).get(tempCol - 1) == 'B')) && !isBeaten(tempRow - 1, tempCol - 1, beaten)) {
                    beaten.add(tempRow - 1);
                    beaten.add(tempCol - 1);
                    tempRow -= 2;
                    tempCol -= 2;
                    found = true;
                    break;
                } else
                    break;
            }
            while (tempCol >= 0 && tempRow >= 0 && tempCol < BOARD_SIZE && tempRow < BOARD_SIZE && found) {
                if ((board.get(tempRow).get(tempCol) == 'e')) {
                    String newMove = move.concat(String.valueOf(tempRow) + ',' + String.valueOf(tempCol) + ')');
                    moves.add(newMove + " beatQ");
                    findBeatsForWhiteQueens(board, tempRow, tempCol, (newMove + " -> ("), moves, tempRow + 1, tempCol + 1, beaten);
                    tempRow--;
                    tempCol--;
                } else {
                    break;
                }
            }
        }

        tempRow = row;
        tempCol = col;
        found = false;
        if (!(tempRow - 1 == previousRow && tempCol + 1 == previousCol)) {
            while (tempRow > 1 && tempCol < BOARD_SIZE - 2) {
                if (board.get(tempRow - 1).get(tempCol + 1) == 'e') {
                    tempRow--;
                    tempCol++;
                } else if (((board.get(tempRow - 1).get(tempCol + 1) == 'b') || (board.get(tempRow - 1).get(tempCol + 1) == 'B')) && !isBeaten(tempRow - 1, tempCol + 1, beaten)) {
                    beaten.add(tempRow - 1);
                    beaten.add(tempCol + 1);
                    tempRow -= 2;
                    tempCol += 2;
                    found = true;
                    break;
                } else
                    break;
            }
            while (tempCol >= 0 && tempRow >= 0 && tempCol < BOARD_SIZE && tempRow < BOARD_SIZE && found) {
                if ((board.get(tempRow).get(tempCol) == 'e')) {
                    String newMove = move.concat(String.valueOf(tempRow) + ',' + String.valueOf(tempCol) + ')');
                    moves.add(newMove + " beatQ");
                    findBeatsForWhiteQueens(board, tempRow, tempCol, (newMove + " -> ("), moves, tempRow + 1, tempCol - 1, beaten);
                    tempRow--;
                    tempCol++;
                } else {
                    break;
                }
            }
        }

        tempRow = row;
        tempCol = col;
        found = false;
        if (!((tempRow + 1) == previousRow && (tempCol + 1) == previousCol)) {
            while (tempRow < BOARD_SIZE - 2 && tempCol < BOARD_SIZE - 2) {
                if (board.get(tempRow + 1).get(tempCol + 1) == 'e') {
                    tempRow++;
                    tempCol++;
                } else if (((board.get(tempRow + 1).get(tempCol + 1) == 'b') || (board.get(tempRow + 1).get(tempCol + 1) == 'B')) && !isBeaten(tempRow + 1, tempCol + 1, beaten)) {
                    beaten.add(tempRow + 1);
                    beaten.add(tempCol + 1);
                    tempRow += 2;
                    tempCol += 2;
                    found = true;
                    break;
                } else
                    break;
            }
            while (tempCol >= 0 && tempRow >= 0 && tempCol < BOARD_SIZE && tempRow < BOARD_SIZE && found) {
                if ((board.get(tempRow).get(tempCol) == 'e')) {
                    String newMove = move.concat(String.valueOf(tempRow) + ',' + String.valueOf(tempCol) + ')');
                    moves.add(newMove + " beatQ");
                    findBeatsForWhiteQueens(board, tempRow, tempCol, (newMove + " -> ("), moves, tempRow - 1, tempCol - 1, beaten);
                    tempRow++;
                    tempCol++;
                } else {
                    break;
                }
            }
        }

        tempRow = row;
        tempCol = col;
        found = false;
        if (!((tempRow + 1) == previousRow && (tempCol - 1) == previousCol)) {
            while (tempRow < BOARD_SIZE - 2 && tempCol > 1) {
                if (board.get(tempRow + 1).get(tempCol - 1) == 'e') {
                    tempRow++;
                    tempCol--;
                } else if (((board.get(tempRow + 1).get(tempCol - 1) == 'b') || (board.get(tempRow + 1).get(tempCol - 1) == 'B')) && !isBeaten(tempRow + 1, tempCol - 1, beaten)) {
                    beaten.add(tempRow + 1);
                    beaten.add(tempCol - 1);
                    tempRow += 2;
                    tempCol -= 2;
                    found = true;
                    break;
                } else
                    break;
            }
            while (tempCol >= 0 && tempRow >= 0 && tempCol < BOARD_SIZE && tempRow < BOARD_SIZE && found) {
                if ((board.get(tempRow).get(tempCol) == 'e')) {
                    String newMove = move.concat(String.valueOf(tempRow) + ',' + String.valueOf(tempCol) + ')');
                    moves.add(newMove + " beatQ");
                    findBeatsForWhiteQueens(board, tempRow, tempCol, (newMove + " -> ("), moves, tempRow - 1, tempCol + 1, beaten);
                    tempRow++;
                    tempCol--;
                } else {
                    break;
                }
            }
        }
    }

    private void findBeatsForBlackQueens(List<List<Character>> board, int row, int col, String move, List<String> moves, int previousRow, int previousCol, List<Integer> beaten) {
        int tempRow = row;
        int tempCol = col;
        boolean found = false;
        if (!((tempRow - 1) == previousRow && (tempCol - 1) == previousCol)) {
            while (tempRow > 1 && tempCol > 1) {
                if (board.get(tempRow - 1).get(tempCol - 1) == 'e') {
                    tempRow--;
                    tempCol--;
                } else if (((board.get(tempRow - 1).get(tempCol - 1) == 'w') || (board.get(tempRow - 1).get(tempCol - 1) == 'W')) && !isBeaten(tempRow - 1, tempCol - 1, beaten)) {
                    beaten.add(tempRow - 1);
                    beaten.add(tempCol - 1);
                    tempRow -= 2;
                    tempCol -= 2;
                    found = true;
                    break;
                } else
                    break;
            }
            while (tempCol >= 0 && tempRow >= 0 && tempCol < BOARD_SIZE && tempRow < BOARD_SIZE && found) {
                if ((board.get(tempRow).get(tempCol) == 'e')) {
                    String newMove = move.concat(String.valueOf(tempRow) + ',' + String.valueOf(tempCol) + ')');
                    moves.add(newMove + " beatQ");
                    findBeatsForBlackQueens(board, tempRow, tempCol, (newMove + " -> ("), moves, tempRow + 1, tempCol + 1, beaten);
                    tempRow--;
                    tempCol--;
                } else {
                    break;
                }
            }
        }

        tempRow = row;
        tempCol = col;
        found = false;
        if (!((tempRow - 1) == previousRow && (tempCol + 1) == previousCol)) {
            while (tempRow > 1 && tempCol < BOARD_SIZE - 2) {
                if (board.get(tempRow - 1).get(tempCol + 1) == 'e') {
                    tempRow--;
                    tempCol++;
                } else if (((board.get(tempRow - 1).get(tempCol + 1) == 'w') || (board.get(tempRow - 1).get(tempCol + 1) == 'W')) && !isBeaten(tempRow - 1, tempCol + 1, beaten)) {
                    beaten.add(tempRow - 1);
                    beaten.add(tempCol + 1);
                    tempRow -= 2;
                    tempCol += 2;
                    found = true;
                    break;
                } else
                    break;
            }
            while (tempCol >= 0 && tempRow >= 0 && tempCol < BOARD_SIZE && tempRow < BOARD_SIZE && found) {
                if ((board.get(tempRow).get(tempCol) == 'e')) {
                    String newMove = move.concat(String.valueOf(tempRow) + ',' + String.valueOf(tempCol) + ')');
                    moves.add(newMove + " beatQ");
                    findBeatsForBlackQueens(board, tempRow, tempCol, (newMove + " -> ("), moves, tempRow + 1, tempCol - 1, beaten);
                    tempRow--;
                    tempCol++;
                } else {
                    break;
                }
            }
        }

        tempRow = row;
        tempCol = col;
        found = false;
        if (!((tempRow + 1) == previousRow && (tempCol + 1) == previousCol)) {
            while (tempRow < BOARD_SIZE - 2 && tempCol < BOARD_SIZE - 2) {
                if (board.get(tempRow + 1).get(tempCol + 1) == 'e') {
                    tempRow++;
                    tempCol++;
                } else if (((board.get(tempRow + 1).get(tempCol + 1) == 'w') || (board.get(tempRow + 1).get(tempCol + 1) == 'W')) && !isBeaten(tempRow + 1, tempCol + 1, beaten)) {
                    beaten.add(tempRow + 1);
                    beaten.add(tempCol + 1);
                    tempRow += 2;
                    tempCol += 2;
                    found = true;
                    break;
                } else
                    break;
            }
            while (tempCol >= 0 && tempRow >= 0 && tempCol < BOARD_SIZE && tempRow < BOARD_SIZE && found) {
                if ((board.get(tempRow).get(tempCol) == 'e')) {
                    String newMove = move.concat(String.valueOf(tempRow) + ',' + String.valueOf(tempCol) + ')');
                    moves.add(newMove + " beatQ");
                    findBeatsForBlackQueens(board, tempRow, tempCol, (newMove + " -> ("), moves, tempRow - 1, tempCol - 1, beaten);
                    tempRow++;
                    tempCol++;
                } else {
                    break;
                }
            }
        }

        tempRow = row;
        tempCol = col;
        found = false;
        if (!((tempRow + 1) == previousRow && (tempCol - 1) == previousCol)) {
            while (tempRow < BOARD_SIZE - 2 && tempCol > 1) {
                if (board.get(tempRow + 1).get(tempCol - 1) == 'e') {
                    tempRow++;
                    tempCol--;
                } else if (((board.get(tempRow + 1).get(tempCol - 1) == 'w') || (board.get(tempRow + 1).get(tempCol - 1) == 'W')) && !isBeaten(tempRow + 1, tempCol - 1, beaten)) {
                    beaten.add(tempRow + 1);
                    beaten.add(tempCol - 1);
                    tempRow += 2;
                    tempCol -= 2;
                    found = true;
                    break;
                } else
                    break;
            }
            while (tempCol >= 0 && tempRow >= 0 && tempCol < BOARD_SIZE && tempRow < BOARD_SIZE && found) {
                if ((board.get(tempRow).get(tempCol) == 'e')) {
                    String newMove = move.concat(String.valueOf(tempRow) + ',' + String.valueOf(tempCol) + ')');
                    moves.add(newMove + " beatQ");
                    findBeatsForBlackQueens(board, tempRow, tempCol, (newMove + " -> ("), moves, tempRow - 1, tempCol + 1, beaten);
                    tempRow++;
                    tempCol--;
                } else {
                    break;
                }
            }
        }
    }

    private boolean isBeaten(int row, int col, List<Integer> beaten) {
        boolean isAlreadyBeaten = false;
        for (int i = 0; i < beaten.size(); i += 2) {
            if (row == beaten.get(i) && col == beaten.get(i + 1)) {
                isAlreadyBeaten = true;
            }
        }
        return isAlreadyBeaten;
    }

    private boolean lookForWhites() {
        if (board.stream().collect(Collectors.toList()).contains('w') || board.stream().collect(Collectors.toList()).contains('W'))
            return true;
        return false;
    }

    private boolean lookForBlacks() {
        if (board.stream().collect(Collectors.toList()).contains('b') || board.stream().collect(Collectors.toList()).contains('B'))
            return true;
        return false;
    }

    //ocena stanu gry pion - 3 damka - 5 + wagi jako sfery 1-3
    private int boardEvaluationWithSpheres(List<List<Character>> board) {
        int rate = 0;
        int weight = 0;
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (i == 0 || j == 0 || i == BOARD_SIZE - 1 || j == BOARD_SIZE - 1) {
                    weight = 1;
                } else if (i == 1 || j == 1 || i == BOARD_SIZE - 2 || j == BOARD_SIZE - 2) {
                    weight = 2;
                } else {
                    weight = 3;
                }
                if (board.get(i).get(j) == 'w') {
                    rate += 3 * weight;
                }
                if (board.get(i).get(j) == 'W') {
                    rate += 5 * weight;
                }
                if (board.get(i).get(j) == 'b') {
                    rate -= 3 * weight;
                }
                if (board.get(i).get(j) == 'B') {
                    rate -= 5 * weight;
                }
            }
        }
        return rate;
    }

    //ocena stanu gry pion - 3 damka - 6 + wagi dla pionow jako wiersze im dalej tym lepiej
    private int boardEvaluation(List<List<Character>> board) {
        int rate = 0;
        int weightWhite;
        int weightBlack;
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                weightWhite = BOARD_SIZE - i;
                weightBlack = i + 1;
                if (board.get(i).get(j) == 'w') {
                    rate += weightWhite;
                }
                if (board.get(i).get(j) == 'W') {
                    rate += (3 + BOARD_SIZE);
                }
                if (board.get(i).get(j) == 'b') {
                    rate -= weightBlack;
                }
                if (board.get(i).get(j) == 'B') {
                    rate -= (3 + BOARD_SIZE);
                }
            }
        }
        return rate;
    }

    //ocena stanu gry pion - 3 damka - 5
    private int boardEvaluationCounting(List<List<Character>> board) {
        int rate = 0;
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (board.get(i).get(j) == 'w') {
                    rate += 3;
                }
                if (board.get(i).get(j) == 'W') {
                    rate += 5;
                }
                if (board.get(i).get(j) == 'b') {
                    rate -= 3;
                }
                if (board.get(i).get(j) == 'B') {
                    rate -= 5;
                }
            }
        }
        return rate;
    }

    private void displayEvaluation(int heuristicNumber) {
        int grade;
        switch (heuristicNumber) {
            case 1:
                grade = boardEvaluationWithSpheres(board);
                break;
            default:
                grade = boardEvaluation(board);
        }
        if (grade > 0) {
            System.out.println(Colors.ANSI_WHITE + "Rate: " + grade + " Whites are winning");
        } else if (grade < 0) {
            System.out.println(Colors.ANSI_BLACK + "Rate: " + grade + " Blacks are winning");
        } else {
            System.out.println(Colors.ANSI_GREEN + "rate: " + grade + " It's a draw");
        }
    }

    private void displaySuggestedMove(String color, List<List<Character>> gameBoard, int heuristicNumber) {
        String move;

        TreeNode root = new TreeNode(gameBoard, evaluation(gameBoard, heuristicNumber), "-");
        buildChoicesTree(root, 0, color, heuristicNumber);
        int minMax = findBestMove(root, 0, color);
        move = root.getMove();

        System.out.println(Colors.ANSI_RED + "Move suggested by MIN-MAX alghortm: " + move);
    }

    private String returnSuggestedMove(String color, List<List<Character>> gameBoard, int heuristicNumber) {
        String move;

        TreeNode root = new TreeNode(gameBoard, evaluation(gameBoard, heuristicNumber), "-");
        buildChoicesTree(root, 0, color, heuristicNumber);
        int minMax = findBestMove(root, 0, color);
        move = root.getMove();

        return move;
    }

    private void buildChoicesTree(TreeNode root, int depth, String color, int heuristic) {
        if (depth == MIN_MAX_DEPTH)
            return;
        int nodeNumber = 0;
        List<String> possibleMoves;
        if (color == "whites" && depth % 2 == 0) {
            possibleMoves = getWhitePossibleMoves(root.getBoard());
        } else if (color == "whites" && depth % 2 == 1) {
            possibleMoves = getBlackPossibleMoves(root.getBoard());
        } else if (color == "blacks" && depth % 2 == 0) {
            possibleMoves = getBlackPossibleMoves(root.getBoard());
        } else {
            possibleMoves = getWhitePossibleMoves(root.getBoard());
        }

        for (String move : possibleMoves) {
            List<List<Character>> newBoard = deepCopy(root.getBoard());
            makeMove(newBoard, move, false);
            root.children.add(new TreeNode(newBoard, evaluation(newBoard, heuristic), move));
            buildChoicesTree(root.children.get(nodeNumber), depth + 1, color, heuristic);
            nodeNumber++;
        }
    }

    private int findBestMove(TreeNode root, int depth, String actualColor) {

        if (depth == MIN_MAX_DEPTH - 1) {
            return root.getRate();
        }

        if (actualColor == "whites") {
            int maximum = Integer.MIN_VALUE;
            if (root.getChildren().size() == 0)
                return root.getRate();
            List<TreeNode> children = root.getChildren();
            for (int i = 0; i < children.size(); i++) {
                int value = findBestMove(children.get(i), depth + 1, "blacks");
                if (value > maximum) {
                    maximum = value;
                    if (depth == 0)
                        root.setMove(children.get(i).getMove());
                }
            }
            return maximum;
        } else if (actualColor == "blacks") {
            int minimum = Integer.MAX_VALUE;
            if (root.getChildren().size() == 0)
                return root.getRate();
            List<TreeNode> children = root.getChildren();
            for (int i = 0; i < children.size(); i++) {
                int value = findBestMove(children.get(i), depth + 1, "whites");
                if (value < minimum) {
                    minimum = value;
                    if (depth == 0)
                        root.setMove(children.get(i).getMove());
                }
            }
            return minimum;
        }
        return 0;
    }

    private int evaluation(List<List<Character>> board, int numberOfHeuristic) {
        int rate;
        switch (numberOfHeuristic) {
            case 1:
                rate = boardEvaluationWithSpheres(board);
                break;
            case 2:
                rate = boardEvaluation(board);
                break;
            default:
                rate = boardEvaluationCounting(board);
        }
        return rate;
    }

    private void reset() {
        board = new ArrayList<>();
        for (int i = 0; i < BOARD_SIZE; i++) {
            board.add(new ArrayList<>());
            for (int j = 0; j < BOARD_SIZE; j++) {
                board.get(i).add('e');
                if (i < 2) {
                    if ((i + j) % 2 == 1) {
                        board.get(i).set(j, 'b');
                    }
                }
                if (i > 5) {
                    if ((i + j) % 2 == 1) {
                        board.get(i).set(j, 'w');
                    }
                }
            }
        }
    }

}
package updated;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class ABGameBlack {
    private static Board board;
    private static int estimate = 0;

    public static String swapColours(char[] board) {
        StringBuilder newBoard = new StringBuilder(board.length);
        for (int i = 0; i < board.length; i++) {
            if (board[i] == 'W')
                newBoard.append('B');

            else if (board[i] == 'B')
                newBoard.append('W');

            else
                newBoard.append('x');

        }

        return newBoard.toString();
    }

    public static void main(String[] args) throws IOException {
        if (args.length != 3) {
            System.out.println("Input in the format: <input file> <output file> <depth> <player>");
            return;
        }

        String inputFile = args[0];
        String outputFile = args[1];
        int depth = Integer.parseInt(args[2]);

        String initialBoard = new String(Files.readAllBytes(Paths.get(inputFile))).trim();
        initialBoard = swapColours(initialBoard.toCharArray());
        board = new Board(initialBoard);

        Board bestBoard = alphaBetaMidgameEndgame(board, depth, 'W', Integer.MIN_VALUE, Integer.MAX_VALUE);

        String result = swapColours(bestBoard.getBoard());

        Files.write(Paths.get(outputFile), new String(result).getBytes());

        System.out.println("Board Position: " + new String(result));
        System.out.println("Positions evaluated by static estimation: " + estimate);
        System.out.println("MINIMAX estimate: " + bestBoard.staticEvaluationMidGameImproved());
    }

    private static Board alphaBetaMidgameEndgame(Board board, int depth, char player, int alpha,
            int beta) {
        estimate++;

        if (depth == 0) {
            return board;
        }

        List<Board> moves;
        if (player == 'W') {
            moves = board.generateMovesMidgameEndgame(player);
        } else {
            moves = board.generateMovesMidgameEndgame(player);
        }

        Board bestBoard = null;
        if (player == 'W') {
            int bestValue = Integer.MIN_VALUE;
            for (Board move : moves) {
                Board currBoard = alphaBetaMidgameEndgame(move, depth - 1, 'B', alpha, beta);
                int currValue = currBoard.staticEvaluationMidGameImproved();
                if (currValue > bestValue) {
                    bestValue = currValue;
                    bestBoard = move;
                }
                alpha = Math.max(alpha, bestValue);
                if (beta <= alpha) {
                    break;
                }
            }
        } else {
            int bestValue = Integer.MAX_VALUE;
            for (Board move : moves) {
                Board currBoard = alphaBetaMidgameEndgame(move, depth - 1, 'W', alpha, beta);
                int currValue = currBoard.staticEvaluationMidGameImproved();
                if (currValue < bestValue) {
                    bestValue = currValue;
                    bestBoard = move;
                }
                beta = Math.min(beta, bestValue);
                if (beta <= alpha) {
                    break;
                }
            }
        }

        return bestBoard;
    }

}

package updated;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class MiniMaxOpening {
    private static Board board;
    private static int estimate = 0;

    public static void main(String[] args) throws IOException {
        if (args.length != 3) {
            System.out.println("Input in the format: <input file> <output file> <depth> <player>");
            return;
        }

        String inputFile = args[0];
        String outputFile = args[1];
        int depth = Integer.parseInt(args[2]);

        String initialBoard = new String(Files.readAllBytes(Paths.get(inputFile))).trim();
        board = new Board(initialBoard);

        Board bestBoard = minimaxOpening(board, depth, 'W');

        Files.write(Paths.get(outputFile), new String(bestBoard.getBoard()).getBytes());

        System.out.println("Board Position: " + new String(bestBoard.getBoard()));
        System.out.println("Positions evaluated by static estimation: " + estimate);
        System.out.println("MINIMAX estimate: " + bestBoard.staticEvaluationOpening());
    }

    private static Board minimaxOpening(Board board, int depth, char player) {
        estimate++;
        if (depth == 0) {
            return board;

        }

        List<Board> moves;
        if (player == 'W') {
            moves = board.generateMovesOpening(player);
        } else {
            moves = board.generateMovesOpening(player);
        }

        Board bestBoard = null;
        if (player == 'W') {
            int bestValue = Integer.MIN_VALUE;
            for (Board move : moves) {
                Board currBoard = minimaxOpening(move, depth - 1, 'B');
                int currValue = currBoard.staticEvaluationOpening();
                if (currValue > bestValue) {
                    bestValue = currValue;
                    bestBoard = move;
                }

            }
        } else {
            int bestValue = Integer.MAX_VALUE;
            for (Board move : moves) {
                Board currBoard = minimaxOpening(move, depth - 1, 'W');
                int currValue = currBoard.staticEvaluationOpening();
                if (currValue < bestValue) {
                    bestValue = currValue;
                    bestBoard = move;
                }
            }

        }

        return bestBoard;
    }

}

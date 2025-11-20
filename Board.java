package updated;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Board {
    private char[] board;
    private static int[] mills_possible_1 = { 0, 1, 4 };
    private static int[] mills_possible_2 = { 3, 5, 6, 7, 8, 9, 10, 12, 13, 15, 16 };
    private static int[] mills_possible_3 = { 11, 14, 17 };

    int mills[][] = { { 0, 2, 4 }, { 1, 3, 5 }, { 9, 12, 15 }, { 11, 14, 17 }, { 6, 7, 8 }, { 9, 10, 11 },
            { 12, 13, 14 }, { 15, 16, 17 }, { 5, 6, 11 }, { 3, 7, 14 }, { 10, 13, 16 }, { 3, 7, 14 }, { 1, 8, 17 } };

    public Board(String boardStr) {
        this.board = boardStr.toCharArray();
    }

    public Board(char[] board) {
        this.board = Arrays.copyOf(board, board.length);
    }

    public char[] getBoard() {
        return board;
    }

    public void setBoard(int position, char value) {
        this.board[position] = value;
    }

    public int staticEvaluationOpening() {
        int numWhitePieces = 0;
        int numBlackPieces = 0;

        for (char c : board) {
            if (c == 'W')
                numWhitePieces++;
            if (c == 'B')
                numBlackPieces++;
        }
        return numWhitePieces - numBlackPieces;

    }

    private boolean mill_possible_1(int position) {
        for (int pos : mills_possible_1)
            if (pos == position)
                return true;

        return false;
    }

    private boolean mill_possible_2(int position) {
        for (int pos : mills_possible_2)
            if (pos == position)
                return true;

        return false;
    }

    private boolean mill_possible_3(int position) {
        for (int pos : mills_possible_3)
            if (pos == position)
                return true;

        return false;
    }

    private int position(char player) {
        int score = 0;
        for (int i = 0; i < board.length; i++) {
            if (board[i] == player) {
                if (mill_possible_1(i))
                    score += 1;
                if (mill_possible_2(i))
                    score += 2;
                if (mill_possible_3(i))
                    score += 3;
                if (closeMill(i, board))
                    score += 50;
            }
        }
        return score;
    }

    private int movementsPossible(char player) {
        return generateAdd(player).size() * 5;
    }

    private int countCompleteMills(char player) {
        int count = 0;
        for (int[] mill : mills) {
            if (board[mill[0]] == player && board[mill[1]] == player && board[mill[2]] == player)
                count++;
        }

        return count;
    }

    private int countPotentialMills(char player) {
        int count = 0;

        for (int[] mill : mills) {
            int playerCount = 0;
            int emptyCount = 0;
            for (int pos : mill) {
                if (board[pos] == player)
                    playerCount++;
                else if (board[pos] == 'x')
                    emptyCount++;
            }
            if (playerCount == 2 && emptyCount == 1)
                count++;
        }

        return count;
    }

    private int mills(char player) {
        int completeMills = countCompleteMills(player);
        int potentialMills = countPotentialMills(player);
        return completeMills * 50 + potentialMills * 10;
    }

    public int staticEvaluationOpeningImproved() {
        int whiteScore = position('W') + movementsPossible('W');
        int blackScore = position('B') + movementsPossible('B');
        return whiteScore - blackScore;

    }

    private int countBlockedOpponentPieces(char player) {
        char opponent = (player == 'W') ? 'B' : 'W';
        int count = 0;
        for (int pos = 0; pos < board.length; pos++) {
            if (board[pos] == opponent && isBlocked(pos)) {
                count++;
            }
        }
        return count;
    }

    private boolean isBlocked(int pos) {
        for (int neighbor : getNeighbors(pos)) {
            if (board[neighbor] == 'x') {
                return false;
            }
        }
        return true;
    }

    private int countPieces(char player) {
        int count = 0;
        for (int pos : board) {
            if (pos == player) {
                count++;
            }
        }
        return count;
    }

    public int staticEstimationImproved1() {
        int whiteScore = evaluateBoard('W');
        int blackScore = evaluateBoard('B');
        return whiteScore - blackScore;
    }

    private int countThreePieceConfigurations(char player) {
        int count = 0;
        for (int[] mill : mills) {
            int playerCount = 0;
            for (int pos : mill) {
                if (board[pos] == player) {
                    playerCount++;
                }
            }
            if (playerCount == 3) {
                count++;
            }
        }
        return count;
    }

    private int evaluateBoard(char player) {
        int score = 0;
        score += 18 * countCompleteMills(player);
        score += 26 * countPotentialMills(player); // change
        score += 1 * countBlockedOpponentPieces(player);
        score += 6 * countPieces(player);
        score += 12 * countPotentialMills(player);
        score += 7 * countThreePieceConfigurations(player);
        return score;
    }

    public int staticEvaluationMidgame() {
        int numWhitePieces = 0;
        int numBlackPieces = 0;
        int numBlackMoves = generateAdd('B').size();

        for (char c : board) {
            if (c == 'W')
                numWhitePieces++;
            if (c == 'B')
                numBlackPieces++;
        }

        if (numBlackPieces <= 2)
            return 10000;

        else if (numWhitePieces <= 2)
            return -10000;

        else if (numBlackMoves == 0)
            return 10000;
        else
            return (1000 * (numWhitePieces - numBlackPieces) - numBlackMoves);

    }

    public int staticEvaluationMidGameImproved() {
        int whiteScore = 0;
        int blackScore = 0;
        int numWhitePieces = 0;
        int numBlackPieces = 0;

        for (char c : board) {
            if (c == 'W')
                numWhitePieces++;
            if (c == 'B')
                numBlackPieces++;
        }

        if (numBlackPieces <= 2)
            return 10000;
        if (numWhitePieces <= 2)
            return -10000;

        whiteScore += numWhitePieces * 100;
        blackScore += numBlackPieces * 100;

        whiteScore += position('W');
        blackScore += position('B');

        whiteScore += mills('W');
        blackScore += mills('B');

        whiteScore += movementsPossible('W');
        blackScore += movementsPossible('B');

        return whiteScore - blackScore;
    }

    public List<Board> generateMovesOpening(char player) {
        return generateAdd(player);
    }

    public List<Board> generateMovesMidgameEndgame(char player) {
        int numWhitePieces = 0;
        for (char c : board) {
            if (c == 'W')
                numWhitePieces++;
        }
        if (numWhitePieces == 3) {
            return generateHopping();
        } else {
            return generateMove(player);
        }
    }

    public List<Board> generateAdd(char player) {
        List<Board> moves = new ArrayList<>();
        for (int i = 0; i < board.length; i++) {
            if (board[i] == 'x') {
                char[] newBoard = Arrays.copyOf(board, board.length);
                newBoard[i] = player;

                if (closeMill(i, newBoard)) {
                    generateRemove(newBoard, moves, 'W');
                } else {
                    moves.add(new Board(newBoard));
                }
            }
        }
        return moves;
    }

    private List<Board> generateHopping() {
        List<Board> moves = new ArrayList<>();
        for (int i = 0; i < board.length; i++) {
            if (board[i] == 'W') {
                for (int j = 0; j < board.length; j++) {
                    if (board[j] == 'x') {
                        char[] newBoard = Arrays.copyOf(board, board.length);
                        newBoard[i] = 'x';
                        newBoard[j] = 'W';
                        if (closeMill(j, newBoard)) {
                            generateRemove(newBoard, moves, 'W');
                        } else {
                            moves.add(new Board(newBoard));
                        }
                    }
                }
            }
        }
        return moves;
    }

    public List<Board> generateMove(char player) {
        List<Board> moves = new ArrayList<>();
        for (int i = 0; i < board.length; i++) {
            if (board[i] == player) {
                int[] neighbors = getNeighbors(i);
                for (int neighbor : neighbors) {
                    if (board[neighbor] == 'x') {
                        char[] newBoard = Arrays.copyOf(board, board.length);
                        newBoard[i] = 'x';
                        newBoard[neighbor] = player;
                        if (closeMill(neighbor, newBoard)) {
                            generateRemove(newBoard, moves, player);
                        } else {
                            moves.add(new Board(newBoard));
                        }
                    }
                }
            }
        }
        return moves;
    }

    public int[] getNeighbors(int location) {
        switch (location) {
            case 0:
                return new int[] { 1, 2, 15 };
            case 1:
                return new int[] { 0, 3, 8 };
            case 2:
                return new int[] { 0, 3, 4, 12 };
            case 3:
                return new int[] { 1, 2, 5, 7 };
            case 4:
                return new int[] { 2, 5, 9 };
            case 5:
                return new int[] { 3, 4, 6 };
            case 6:
                return new int[] { 5, 7, 11 };
            case 7:
                return new int[] { 3, 6, 8, 14 };
            case 8:
                return new int[] { 1, 7, 17 };
            case 9:
                return new int[] { 4, 10, 12 };
            case 10:
                return new int[] { 9, 11, 13 };
            case 11:
                return new int[] { 6, 10, 14 };
            case 12:
                return new int[] { 2, 9, 13, 15 };
            case 13:
                return new int[] { 10, 12, 14, 16 };
            case 14:
                return new int[] { 7, 11, 13, 17 };
            case 15:
                return new int[] { 0, 12, 16 };
            case 16:
                return new int[] { 13, 15, 17 };
            case 17:
                return new int[] { 8, 14, 16 };
            default:
                return new int[] {};
        }
    }

    public boolean closeMill(int position, char[] board) {
        char C = board[position];
        switch (position) {
            case 0:
                return (board[2] == C && board[4] == C);
            case 1:
                return (board[3] == C && board[5] == C) || (board[8] == C && board[17] == C);
            case 2:
                return (board[0] == C && board[4] == C);
            case 3:
                return (board[1] == C && board[5] == C) || (board[7] == C && board[14] == C);
            case 4:
                return (board[0] == C && board[2] == C);
            case 5:
                return (board[1] == C && board[3] == C) || (board[6] == C && board[11] == C);
            case 6:
                return ((board[7] == C && board[8] == C) || (board[5] == C && board[11] == C));
            case 7:
                return ((board[6] == C && board[8] == C) || (board[3] == C && board[14] == C));
            case 8:
                return ((board[6] == C && board[7] == C) || (board[1] == C && board[17] == C));
            case 9:
                return ((board[10] == C && board[11] == C) || (board[12] == C && board[15] == C));
            case 10:
                return ((board[9] == C && board[11] == C) || (board[13] == C && board[16] == C));
            case 11:
                return ((board[9] == C && board[10] == C) || (board[14] == C && board[17] == C))
                        || (board[5] == C && board[6] == C);
            case 12:
                return ((board[13] == C && board[14] == C) || (board[9] == C && board[15] == C));
            case 13:
                return ((board[12] == C && board[14] == C) || (board[10] == C && board[16] == C));
            case 14:
                return ((board[12] == C && board[13] == C) || (board[11] == C && board[17] == C))
                        || (board[3] == C && board[7] == C);
            case 15:
                return (board[9] == C && board[12] == C) || (board[16] == C && board[17] == C);
            case 16:
                return (board[10] == C && board[13] == C) || (board[15] == C && board[17] == C);
            case 17:
                return (board[11] == C && board[14] == C) || (board[15] == C && board[16] == C)
                        || (board[1] == C && board[8] == C);
        }
        return false;
    }

    public void generateRemove(char[] board, List<Board> moves, char player) {
        char opponent = (player == 'W') ? 'B' : 'W';
        boolean added = false;
        for (int i = 0; i < board.length; i++) {

            if (board[i] == opponent && !closeMill(i, board)) {
                char[] newBoard = Arrays.copyOf(board, board.length);
                newBoard[i] = 'x';
                moves.add(new Board(newBoard));
                added = true;
            }
        }
        if (!added) {
            for (int i = 0; i < board.length; i++) {
                if (board[i] == opponent) {
                    char[] newBoard = Arrays.copyOf(board, board.length);
                    newBoard[i] = 'x';
                    moves.add(new Board(newBoard));
                }
            }
        }

    }
}

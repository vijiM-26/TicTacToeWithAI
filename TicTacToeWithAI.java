import java.util.Random;
import java.util.Scanner;

public class TicTacToeWithAI {

    private static char[][] board = new char[3][3];
    private static char currentPlayer;
    private static char computerPlayer;
    private static char humanPlayer;
    private static boolean gameOver;

    public static void main(String[] args) {
        initializeGame();
        printBoard();

        while (!gameOver) {
            if (currentPlayer == humanPlayer) {
                playHuman();
            } else {
                playComputer();
            }
            printBoard();
            checkGameOver();
            togglePlayer();
        }

        announceResult();
    }

    private static void initializeGame() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = ' ';
            }
        }
        currentPlayer = 'X'; // 'X' starts the game
        computerPlayer = 'O'; // Computer is 'O'
        humanPlayer = 'X'; // Human is 'X'
        gameOver = false;
    }

    private static void printBoard() {
        System.out.println("-------------");
        for (int i = 0; i < 3; i++) {
            System.out.print("| ");
            for (int j = 0; j < 3; j++) {
                System.out.print(board[i][j] + " | ");
            }
            System.out.println("\n-------------");
        }
    }

    private static void playHuman() {
        Scanner scanner = new Scanner(System.in);
        int row, col;
        System.out.print("Enter row (0, 1, 2): ");
        row = scanner.nextInt();
        System.out.print("Enter column (0, 1, 2): ");
        col = scanner.nextInt();

        if (isValidMove(row, col)) {
            board[row][col] = humanPlayer;
        } else {
            System.out.println("Invalid move. Try again.");
            playHuman();
        }
    }

    private static void playComputer() {
        int[] bestMove = minimax(board, computerPlayer);
        board[bestMove[0]][bestMove[1]] = computerPlayer;
    }

    private static boolean isValidMove(int row, int col) {
        return row >= 0 && row < 3 && col >= 0 && col < 3 && board[row][col] == ' ';
    }

    private static void togglePlayer() {
        if (currentPlayer == 'X') {
            currentPlayer = 'O';
        } else {
            currentPlayer = 'X';
        }
    }

    private static void checkGameOver() {
        if (checkWin(humanPlayer)) {
            System.out.println("Human wins!");
            gameOver = true;
        } else if (checkWin(computerPlayer)) {
            System.out.println("Computer wins!");
            gameOver = true;
        } else if (isBoardFull()) {
            System.out.println("It's a draw!");
            gameOver = true;
        }
    }

    private static boolean checkWin(char player) {
        for (int i = 0; i < 3; i++) {
            // Check rows and columns
            if (board[i][0] == player && board[i][1] == player && board[i][2] == player ||
                board[0][i] == player && board[1][i] == player && board[2][i] == player) {
                return true;
            }
        }
        // Check diagonals
        return (board[0][0] == player && board[1][1] == player && board[2][2] == player) ||
               (board[0][2] == player && board[1][1] == player && board[2][0] == player);
    }

    private static boolean isBoardFull() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == ' ') {
                    return false;
                }
            }
        }
        return true;
    }

    private static void announceResult() {
        if (checkWin(humanPlayer)) {
            System.out.println("Human wins!");
        } else if (checkWin(computerPlayer)) {
            System.out.println("Computer wins!");
        } else {
            System.out.println("It's a draw!");
        }
    }

    // Minimax algorithm with alpha-beta pruning
    private static int[] minimax(char[][] board, char player) {
        int[] bestMove = new int[]{-1, -1};
        int bestScore = (player == computerPlayer) ? Integer.MIN_VALUE : Integer.MAX_VALUE;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == ' ') {
                    board[i][j] = player;
                    int score = minimaxHelper(board, 0, false);
                    board[i][j] = ' '; // Undo the move

                    if ((player == computerPlayer && score > bestScore) ||
                        (player == humanPlayer && score < bestScore)) {
                        bestScore = score;
                        bestMove[0] = i;
                        bestMove[1] = j;
                    }
                }
            }
        }

        return bestMove;
    }

    private static int minimaxHelper(char[][] board, int depth, boolean isMaximizing) {
        char opponent = (isMaximizing) ? computerPlayer : humanPlayer;

        if (checkWin(computerPlayer)) {
            return 1;
        } else if (checkWin(humanPlayer)) {
            return -1;
        } else if (isBoardFull()) {
            return 0;
        }

        int bestScore = (isMaximizing) ? Integer.MIN_VALUE : Integer.MAX_VALUE;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == ' ') {
                    board[i][j] = (isMaximizing) ? computerPlayer : humanPlayer;
                    int score = minimaxHelper(board, depth + 1, !isMaximizing);
                    board[i][j] = ' '; // Undo the move

                    if (isMaximizing) {
                        bestScore = Math.max(score, bestScore);
                    } else {
                        bestScore = Math.min(score, bestScore);
                    }
                }
            }
        }

        return bestScore;
    }
}

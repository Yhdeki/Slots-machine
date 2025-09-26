package other_stuff;

import java.util.*;

public class SlotMachine {
    static String[][] slotMachine = new String[5][200]; // Direct 2D array!
    static String[] symbolsForSlotMachine = { "⬛", "🍒", "🆓", "🃏", "💰", "🏆", "🍀", "🔔", "⭐", "🎰" };

    // Symbol distribution (how many of each symbol per reel)
    static int[] symbolCounts = { 45, 32, 8, 6, 5, 3, 17, 27, 27, 30 };
    // Corresponds to: ⬛ 🍒 🆓 🃏 💰 🏆 🍀 🔔 ⭐ 🎰

    static int amountOfChips = 100;
    static int betAmount = 0;
    static int freeSpins = 0;
    static Scanner scanner = new Scanner(System.in);
    static Random random = new Random();

    static String winningSymbol;
    static int countOfWinningSymbol;

    public static void main(String[] args) {
        // Initialize all 5 reels with ONE method call!
        initializeAllReels();

        System.out.println("Welcome to the slotmachine!🎰");
        System.out.println("You've got a 100 chips to start with!💵");
        while (true) {

            countOfWinningSymbol = 0;
            winningSymbol = "";
            betAmount = 0;

            if (freeSpins == 0) {
                takeBetAmount();
            } else {
                freeSpins--;
                System.out.println(
                        "You dont need to pay because you have " + freeSpins
                                + " free spins left! every spin will count as a bet of 30.");
                betAmount = 30;
            }
            System.out.println("Spinning...");
            try {
                Thread.sleep(750); // Simulate thinking time
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            spinSlotMachine();

            if (threeOrMore()) {
                calcwinnings();
                amountOfChips += betAmount;
                System.out.printf("You won %d chips!\n", betAmount);
            } else {
                System.out.println("No win this time, better luck next time!");
            }
            System.out.println("You've got " + amountOfChips + " chips left.");
            if (amountOfChips <= 0) {
                System.out.println("You've lost all your chips! Goodbye!");
                break;
            }

        }
        scanner.close();
    }

    // ONE method to rule them all! 🎰
    public static void initializeAllReels() {
        for (int reelIndex = 0; reelIndex < 5; reelIndex++) {
            slotMachine[reelIndex] = generateReel();
        }
    }

    // Generate a single reel with proper distribution
    public static String[] generateReel() {
        ArrayList<String> reel = new ArrayList<>();

        // Add symbols based on counts - this replaces ALL your if-else chains!
        for (int i = 0; i < symbolsForSlotMachine.length; i++) {
            String symbol = symbolsForSlotMachine[i];
            int count = symbolCounts[i];

            // Add 'count' number of 'symbol' to reel
            for (int j = 0; j < count; j++) {
                reel.add(symbol);
            }
        }

        // Shuffle for randomness (important for realistic gameplay!)
        Collections.shuffle(reel);

        return reel.toArray(new String[200]);
    }

    // Helper method to print a reel (for debugging)
    public static void printReel(int reelIndex) {
        System.out.println("Reel " + (reelIndex + 1) + ":");
        for (int i = 0; i < 20; i++) { // Print first 20 positions
            System.out.print(slotMachine[reelIndex][i] + " ");
        }
        System.out.println("...");

        // Count symbols to verify distribution
        Map<String, Integer> counts = new HashMap<>();
        for (String symbol : slotMachine[reelIndex]) {
            counts.put(symbol, counts.getOrDefault(symbol, 0) + 1);
        }
        System.out.println("Symbol distribution:");
        counts.forEach((symbol, count) -> System.out.println(symbol + ": " + count));
    }

    public static void takeBetAmount() {
        while (true) {
            System.out.print("Place your bet: ");
            try {
                betAmount = scanner.nextInt();
                if (betAmount < 0 || betAmount > amountOfChips) {
                    throw new NumberFormatException();
                }
                amountOfChips -= betAmount;
                scanner.nextLine();
                break;
            } catch (NumberFormatException e) {
                System.out.println("You need to input a number between zero and your total chips.");
            }
        }
    }

    public static void spinSlotMachine() {
        for (String[] reel : slotMachine) {
            List<String> reeList = Arrays.asList(reel);
            Collections.shuffle(reeList);
        }
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 5; j++) {=
                System.out.print(slotMachine[j][i] + ' ');
            }
            System.out.println();
        }

    }

    public static boolean threeOrMore() {
        String[] row = { slotMachine[0][1], slotMachine[1][1], slotMachine[2][1], slotMachine[3][1],
                slotMachine[4][1] };

        for (int i = 0; i < row.length; i++) {
            if (row[i].equals("🃏")) {
                while (true) {
                    System.out.println(
                            "What symbol would you like this 🃏 to be?\ncherry, bar, bell, star, lucky clover, free, money bag or trophie");
                    String symbolOfJoker = scanner.next();
                    switch (symbolOfJoker.toLowerCase()) {
                        case "cherry" -> row[i] = "🍒";
                        case "bar" -> row[i] = "🎰";
                        case "bell" -> row[i] = "🔔";
                        case "star" -> row[i] = "⭐";
                        case "lucky", "lucky clover" -> row[i] = "🍀";
                        case "free" -> row[i] = "🆓";
                        case "money", "money bag" -> row[i] = "💰";
                        case "trophie", "trophy" -> row[i] = "🏆";
                        default -> {
                            System.out.println("Invalid symbol choice, choose one of the options that were given");
                            continue;
                        }
                    }
                    break;
                }
            }
            // Count occurrences of each symbol in the row
            Map<String, Integer> symbolCountMap = new HashMap<>();
            for (String symbol : row) {
                if (!symbol.equals("⬛")) {
                    symbolCountMap.put(symbol, symbolCountMap.getOrDefault(symbol, 0) + 1);
                }
            }
            // Find the symbol with the highest count (at least 3)
            countOfWinningSymbol = 0;
            winningSymbol = "";
            for (Map.Entry<String, Integer> entry : symbolCountMap.entrySet()) {
                if (entry.getValue() >= 3 && entry.getValue() > countOfWinningSymbol && !entry.getKey().equals("⬛")) {
                    countOfWinningSymbol = entry.getValue();
                    winningSymbol = entry.getKey();
                }
            }
        }
        return countOfWinningSymbol >= 3 && winningSymbol != "⬛";
    }

    public static void calcwinnings() {
        switch (winningSymbol) {
            case "🍒":
                if (countOfWinningSymbol == 3)
                    betAmount *= 2;
                else if (countOfWinningSymbol == 4)
                    betAmount *= 4;
                else if (countOfWinningSymbol == 5)
                    betAmount *= 10;
                break;
            case "🎰":
                if (countOfWinningSymbol == 3)
                    betAmount *= 2;
                else if (countOfWinningSymbol == 4)
                    betAmount *= 6;
                else if (countOfWinningSymbol == 5)
                    betAmount *= 20;
                break;
            case "🔔":
                if (countOfWinningSymbol == 3) {
                    betAmount *= 4;
                } else if (countOfWinningSymbol == 4) {
                    betAmount *= 8;
                } else if (countOfWinningSymbol == 5) {
                    betAmount *= 15;
                }
                break;
            case "⭐":
                if (countOfWinningSymbol == 3) {
                    betAmount *= 4;
                } else if (countOfWinningSymbol == 4) {
                    betAmount *= 8;
                } else if (countOfWinningSymbol == 5) {
                    betAmount *= 15;
                }
                break;
            case "🍀":
                if (countOfWinningSymbol == 3) {
                    betAmount *= 5;
                } else if (countOfWinningSymbol == 4) {
                    betAmount *= 10;
                } else if (countOfWinningSymbol == 5) {
                    betAmount *= 20;
                }
                break;
            case "🆓": {
                apllyFreeSpins();
                break;
            }
            case "💰":
                if (countOfWinningSymbol == 3) {
                    betAmount *= 12;
                } else if (countOfWinningSymbol == 4) {
                    betAmount *= 30;
                } else if (countOfWinningSymbol == 5) {
                    betAmount *= 70;
                }
                break;
            case "🏆":
                if (countOfWinningSymbol == 3) {
                    betAmount *= 15;
                } else if (countOfWinningSymbol == 4) {
                    betAmount *= 40;
                } else if (countOfWinningSymbol == 5) {
                    betAmount *= 100;
                }
                break;
            default:
                System.out.println("Error in calculating winnings.");
                break;
        }
    }

    public static void apllyFreeSpins() {
        if (countOfWinningSymbol == 3) {
            freeSpins += 3;
        } else if (countOfWinningSymbol == 4) {
            freeSpins += 8;
        } else if (countOfWinningSymbol == 5) {
            freeSpins += 15;
        }
    }
}
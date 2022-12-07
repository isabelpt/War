import java.util.ArrayList;
import java.util.Scanner;

// Isabel Prado-Tucker
// Game class
public class Game {
    private Player p1;
    private Player p2;
    private Deck deck;

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public Game(Player p1, Player p2, Deck deck) {
        this.p1 = p1;
        this.p2 = p2;
        this.deck = deck;
        // Deal the deck and give to players hand
//        String[] r = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "King", "Queen", "Ace"};
//        String[] s = {"Hearts", "Clubs", "Spades", "Diamonds"};
//        int[] p = {2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14};
//        Deck d = new Deck(r, s, p);
        this.deck.shuffle();
        this.deck.splitDeck(p1, p2);
    }

    public void printInstructions() {
        System.out.println("Card game: War");

        // Instructions from bicycle-cards.com
        System.out.println("Each player turns up a card at the same time and the player with the higher card takes both"
                + "cards and puts them, face down, on the bottom of his stack.");
        System.out.println("If the cards are the same rank, it is War");
        System.out.println("Each player turns up one card face down and one card face up.");
        System.out.println("The player with the higher cards takes both piles (six cards).");
        System.out.println("If the turned-up cards are again the same rank, each player places another card face down"
                + " and turns another card face up.");
        System.out.println("The player with the higher card takes all 10 cards, and so on.");
        System.out.println("The game ends when one player has won all the cards.");
    }

    public void printNames() {
        String output = "————————————————————————————————————————————————————-\n";
        int spaces1 = 24 - p1.getName().length();
        int spaces2 = 24 - p2.getName().length();
        output += "| " + p1.getName();
        for (int i = 0; i < spaces1; i++) {
            output += " ";
        }
        output += "| " + p2.getName();
        for (int i = 0; i < spaces2; i++) {
            output += " ";
        }
        output += "|";
        System.out.println(output);
    }

    public void printTwoCards(Card c1, Card c2, int winner) {
        String[][] art = {c1.makeCard(), c2.makeCard()};
        String output = "————————————————————————————————————————————————————-\n";
        output += "| " + c1.toString();
        int spaces1 = 24 - c1.toString().length();
        for (int i = 0; i < spaces1; i++) {
            output += " ";
        }
        output += "| " + c2.toString();
        int spaces2 = 24 - c1.toString().length();
        for (int i = 0; i < spaces2; i++) {
            output += " ";
        }
        output += "|\n";
        if (winner == 1) {
            output += "|    " + ANSI_GREEN + art[0][0] + ANSI_RESET + "     |    " + art[1][0] + "     |\n";
            for (int i = 1; i < art[0].length; i++) {
                output += "|    " + ANSI_GREEN + art[0][i] + ANSI_RESET + "    |    " + art[1][i] + "    |\n";
            }
        } else if (winner == 2){
            output += "|    " + art[0][0] + "     |    "+ ANSI_GREEN + art[1][0] + ANSI_RESET + "     |\n";
            for (int i = 1; i < art[0].length; i++) {
                output += "|    " + art[0][i] + "    |    "+ ANSI_GREEN + art[1][i] + ANSI_RESET + "    |\n";
            }
        } else {
            output += "|    " + art[0][0] + "     |    " + art[1][0] + "     |\n";
            for (int i = 1; i < art[0].length; i++) {
                output += "|    " + art[0][i] + "    |    " + art[1][i] +  "    |\n";
            }
        }

        System.out.print(output);
    }

    public void printHandSizes() {
        String output = "————————————————————————————————————————————————————-\n" + "| " + p1.getName() + ": " + p1.handSize();
        int spaces = 22 - p1.getName().length() - String.valueOf(p1.handSize()).length();
        for (int i = 0; i < spaces; i++) {
            output += " ";
        }
        output += "| " + p2.getName() + ": " + p2.handSize();
        spaces = 22 - p2.getName().length() - String.valueOf(p2.handSize()).length();
        for (int i = 0; i < spaces; i++) {
            output += " ";
        }
        output += "|";
        System.out.println(output);
    }

    public void war(ArrayList<Card> warCards1, ArrayList<Card> warCards2) {
        Card blank = new Card("", "", 0);
        for (int i = 1; i < 5; i++) {
            if (p1.hasCards()) {
                warCards2.add(p1.getTopCard());
            }
            if (p2.hasCards()) {
                warCards2.add(p2.getTopCard());
            }
            if (i < 4) {
                printTwoCards(blank, blank, 0);
            }
        }

        if (warCards1.get(warCards1.size() - 1).getPoint() > warCards2.get(warCards2.size() - 1).getPoint()) {
            p1.addCards(warCards1);
            p1.addCards(warCards2);
            printTwoCards(warCards1.get(warCards1.size() - 1), warCards2.get(warCards2.size() - 1), 1);
        } else if (warCards1.get(warCards1.size() - 1).getPoint() < warCards2.get(warCards2.size() - 1).getPoint()) {
            p2.addCards(warCards1);
            p2.addCards(warCards2);
            printTwoCards(warCards1.get(warCards1.size() - 1), warCards2.get(warCards2.size() - 1), 2);
        } else {
            printTwoCards(warCards1.get(warCards1.size() - 1), warCards2.get(warCards2.size() - 1), 0);
            war(warCards1, warCards2);
        }
    }
    public Player playGame() {
        Scanner s = new Scanner(System.in);
        printInstructions();
        printNames();
        // Until a player has zero cards
        while (p1.hasCards() && p2.hasCards()) {
            // Each player deals top card from their hand
            System.out.println("————————————————————————————————————————————————————-");
            System.out.print("Press enter to get card or 'hand' to view hand sizes: ");
            String input = s.nextLine();
            if (input.equals("hand")) {
                printHandSizes();
            } else {
                // System.out.println();
                Card c1 = p1.getTopCard();
                Card c2 = p2.getTopCard();
                // System.out.print("1: " + c1.toString() + " | " + "2: " + c2.toString() + "\n");
//            c1.printCard();
//            c2.printCard();
                int winner;
                // If not same number they both go to one player
                // If same number enter WAR
                if (c1.getPoint() > c2.getPoint()) {
                    p1.addCard(c1);
                    p1.addCard(c2);
                    winner = 1;
                    printTwoCards(c1, c2, winner);
                    //System.out.println("1 greater");
                } else if (c1.getPoint() < c2.getPoint()) {
                    p2.addCard(c1);
                    p2.addCard(c2);
                    //System.out.println("2 greater");
                    winner = 2;
                    printTwoCards(c1, c2, winner);
                } else {
                    printTwoCards(c1, c2, 0);
                    System.out.println("————————————————————————————————————————————————————-");
                    System.out.println("| War!                                              |");
                    System.out.print("Press enter to proceed: ");
                    s.nextLine();
                    //System.out.println("War - equal");
                    ArrayList<Card> warCardsP1 = new ArrayList<Card>();
                    warCardsP1.add(c1);
                    ArrayList<Card> warCardsP2 = new ArrayList<Card>();
                    warCardsP2.add(c2);
                    war(warCardsP1, warCardsP2);
                }
                //System.out.println("1 size: " + p1.handSize() + " | " + "2 size: " + p2.handSize());
                // Each player deals four more cards
                // Values of top card compared and one player gets all 10 cards
            }
            }

        // Winner who still has cards is posted (need method to check for empty hand)
//
//        p1.printHand();
//        System.out.println("////////");
//        p2.printHand();
        if (p1.hasCards())
        {
            System.out.println("P1 wins");
            return p1;
        } else {
            System.out.println("P2 wins");
            return p2;
        }
    }

    public static void main(String[] args) {
        // To-Do: remove single letter variables
        Scanner s = new Scanner(System.in);
        System.out.print("Name: ");
        String name = s.nextLine();
        Player p1 = new Player(name);
        Player p2 = new Player("Bot");
        // Full Deck
        String[] r1 = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "K", "Q", "A"};
        String[] s1 = {"Hearts", "Clubs", "Spades", "Diamonds"};
        int[] points1 = {2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14};
        // Abridged deck
        String[] r2 = {"2", "3", "4", "5", "6", "7", "8", "9", "10"};
        String[] s2 = {"Hearts", "Clubs"};
        int[] points2 = {2, 3, 4, 5, 6, 7, 8, 9, 10};
        Deck d = new Deck(r2, s2, points2);
        Game g = new Game(p1, p2, d);
        g.playGame();
    }
}

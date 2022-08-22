package academy.mindswap.Connect4.Utilities;

/**
 * This class is responsible for the colors of the game.
 */
public enum Colors {


        BLUE("\u001B[34m"),
        RED("\u001B[31m"),
        YELLOW("\u001B[33m"),
        PURPLE("\u001B[35m"),
        GREEN("\u001B[32m"),
        CYAN("\u001B[36m");

        private String color;

    /**
     * Constructor method of enum Colors that accepts a String as parameter.
     * @param color
     */
        Colors(String color) {
            this.color = color;
        }

    /**
     * Method responsible for getting a specific color.
     * @return a String that represents a color.
     */
    public String getColor() {
            return color;
        }
    }


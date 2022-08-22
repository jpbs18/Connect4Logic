package academy.mindswap.Connect4.Utilities;

public enum Colors {


        BLUE("\u001B[34m"),
        RED("\u001B[31m"),
        YELLOW("\u001B[33m"),
        PURPLE("\u001B[35m"),
        GREEN("\u001B[32m"),
        CYAN("\u001B[36m");

        private String color;

        Colors(String color) {
            this.color = color;
        }

        public String getColor() {
            return color;
        }
    }


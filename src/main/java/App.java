public class App {
    public static void main(String[] args) {
        Simulation pv = new Simulation();
//        pv.generateRandomPersons(20);
//        pv.printAllPersons();
//        printMessageAsHeader("Volljaehrig");
//        pv.printAllPersonsOver18();

        pv.generatePersonsOnField();
        pv.printAllFieldsWithPersonOnIt();

    }

    private static void printMessageAsHeader(String message) {
        System.out.println("===================================================\n" +
                message +
                "\n===================================================");
    }
}

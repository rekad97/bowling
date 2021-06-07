import java.util.ArrayList;
import java.util.Objects;

public class Bowling {

    int totalPoints = 0;
    boolean afterStrike = false;

    //az alábbi függvény strike és spare esetén a következő egy vagy 2 dobást vizsgálja
    public void whatIsNext(String actual, String value) {
        //Ha egy spare után vagyunk, és ezután 2 hosszú a value, vagy egy olyan eset áll fenn, hogy strike után már volt egy strike,
        // és ezután jön egy 2 hosszú argumentum, akkor ezeknek mindig csak az első tagját kell hozzáadnunk az összpontszámhoz

        if(actual == "spare" && value.length() == 2 || (actual == "strike" && value.length()==2 && afterStrike)) {

            //ha szám-szám, akkor az inputFormat-ban kezelt eset miatt hozzáadódik mindkettő, de itt csak az első karakter értékére van szükség, ezért a második
            //karakter értékét levonjuk az összpontszámból
            try {
                Integer.parseInt(value);
                inputFormat(value);
                String nextValue = value;
                String secondChar = Character.toString(nextValue.charAt(1));
                int nextSecondThrow = Integer.parseInt(secondChar);
                totalPoints -= nextSecondThrow;

                //ha szám-/, akkor az inputFormat-ban kezelt eset miatt +10 adódik az összpontszámhoz, de itt csak az első karakter értékére van szükség,
                // ezért a már hozzáadott 10-et levonjuk, és helyette az első karakter értékét adjuk az összpontszámhoz
            } catch(Exception e) {
                inputFormat(value);
                totalPoints -= 10;
                String nextValue = value;
                String firstChar = Character.toString(nextValue.charAt(0));
                int nextThrow = Integer.parseInt(firstChar);
                totalPoints += nextThrow;
            }
            //ha egy spare után x következik 10-el növeljük a pontszámot
        } else if(actual == "spare" && value.equals("x")) {
            totalPoints += 10;

            //ha egy strike után x következik 10-el növeljük a pontszámot
        } else if(actual == "strike" && value.equals("x")) {
            totalPoints += 10;
        }
        //ha egy strike után úgy következik egy 2 karakterből álló argumentum, hogy előtte nem egy másik strike volt már bónuszként,
        // akkor mindkét karakterét hozzá kell adnunk (ez az inputFormat fgv-ben megtörténik)
        else if(actual == "strike" && value.length()==2 && !afterStrike) {
            try {
                Integer.parseInt(value);
                inputFormat(value);
            } catch (Exception e) {
                inputFormat(value);

            }
        }
    }

    //azt vizsgálja hogy az adott paramétert milyen formátumban kapjuk meg ezáltal ez hány pontot ér
    public String inputFormat(String value) {
        if (value.equals("x")) {
            totalPoints += 10;
            return "strike";
        } else if (value.equals("-")) {
            totalPoints += 0;
            return "miss";
        } else if (value.length() == 2) {
            if (value.charAt(1) == '/') {
                totalPoints += 10;
                return "spare";
            } else {
                String firstThrow = Character.toString(value.charAt(0));
                String secondThrow = Character.toString(value.charAt(1));

                int firstValue = Integer.parseInt(firstThrow);
                int secondValue = Integer.parseInt(secondThrow);

                totalPoints += firstValue;
                totalPoints += secondValue;
                return "score";
            }


        } else if(value.length() == 1 && !value.equals("x")) {
            int singleValue = Integer.parseInt(value);
            totalPoints += singleValue;
            return "score";
        }
        else {
            return "error";

        }

    }

    public void CalculateScore(String[] input) {
        if (input.length < 10 || input.length > 12) {
            System.out.println("Incorrect number of arguments!");
        } else if (input.length >= 10) {
            for (int i = 0; i < 9; i++) {
                int defaultIndex = i;
                String result = inputFormat(input[i]);

//minden kapott paraméteren végigmenve, egyesével vizsgáljuk az eseteket
                switch (result) {
                    //strike esetén a következő 2 dobás értékét is nézni kell
                    case "strike":
                        int next;
                        int nextTwo;
                        if(i + 1 < input.length && i + 2 < input.length) {
                            next = i + 1;
                            nextTwo = next + 1;

                            if (next < input.length) {
                                whatIsNext("strike", input[next]);
                            }
                            //ha az előző bemenet csak egy hosszú volt, akkor az egy strike volt, így a következő paramétert is vizsgálni kell,
                            // és annak az első karakterét az összpontszámhoz adni, vagy ha ez is egy strike, akkor +10pontot
                            if (input[next].length() == 1 && nextTwo < input.length) {
                                afterStrike = true;
                                whatIsNext("strike", input[nextTwo]);
                                afterStrike = false;
                            }

                            i = defaultIndex;
                        }
                        break;
                    case "spare":
                        //spare esetén a következő egy dobást kell bónusznak hozzáadnunk az összpontszámhoz
                        int nextInSpare = i+1;
                        if (nextInSpare < input.length) {
                           whatIsNext("spare", input[nextInSpare]);
                           i = defaultIndex;
                        }

                        break;
                    case "score":
                        break;
                    case "miss":
                        break;
                    case "error":
                        System.out.println("Wrong format!");
                    default:
                }
            }
            //mivel ha az utolsó dobásunk egy strike vagy spare, akkor kapunk egy vagy két bónusz dobást, ezért az utolsó esetet külön vizsgálom
            String lastFrame = inputFormat(input[9]);
            switch (lastFrame) {
                //ha az utolsó dobásunk egy strike, akkor 2 dobást kapunk még pluszban, ezek formátumát kell vizsgálni
                case "strike":
                    inputFormat(input[10]);
                    if(input.length == 12) {
                        inputFormat(input[11]);
                    }
                    break;
                //ha az utolsó dobásunk egy spare, akkor 1 dobást kapunk még pluszban, ezek formátumát kell vizsgálni
                case "spare":
                    inputFormat(input[10]);
                    break;
                //ha az utolsó dobásunk egy szám, akkor csak hozzáadjuk az inputFormat fgv-ben kezeltek szerint az összeget az összpontszámhoz,
                // ami már a lastFrame változó esetén megtörtént
                case "score":
                    break;
                case "miss":
                    break;
                case "error":
                    System.out.println("Wrong format!");
                default:
            }
        }

        System.out.println(totalPoints + " points");

    }

}

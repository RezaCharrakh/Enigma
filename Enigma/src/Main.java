import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        FileReader fileReader = new FileReader();
        StringBuilder date = new StringBuilder(input.nextLine());
        String code = input.nextLine();
        EnigmaData enigmaData = EnigmaData.findSetting(date);
//        System.out.println(enigmaData.date);
//        System.out.println(enigmaData.plugBoard);
//        System.out.println(enigmaData.rotor1);
//        System.out.println(enigmaData.rotor2);
//        System.out.println(enigmaData.rotor3);

        Enigma enigma = new Enigma();
        enigma.setRotor(String.valueOf(enigmaData.rotor1), enigma.rotor1);
        enigma.setRotor(String.valueOf(enigmaData.rotor2), enigma.rotor2);
        enigma.setRotor(String.valueOf(enigmaData.rotor3), enigma.rotor3);
        enigma.setPlugBoard(String.valueOf(enigmaData.plugBoard));
        enigma.setReflector();

        //to check the function
        StringBuilder decode;
        System.out.println(decode = enigma.process(code));
        Enigma enigma2 = new Enigma();
        enigma2.setRotor(String.valueOf(enigmaData.rotor1), enigma2.rotor1);
        enigma2.setRotor(String.valueOf(enigmaData.rotor2), enigma2.rotor2);
        enigma2.setRotor(String.valueOf(enigmaData.rotor3), enigma2.rotor3);
        enigma2.setPlugBoard(String.valueOf(enigmaData.plugBoard));
        enigma2.setReflector();
        System.out.println(enigma2.process(String.valueOf(decode)));

    }
}


class Enigma {
    HashMap<Character, Character> rotor1 = new HashMap<Character, Character>();
    HashMap<Character, Character> rotor2 = new HashMap<Character, Character>();
    HashMap<Character, Character> rotor3 = new HashMap<Character, Character>();
    HashMap<Character, Character> plugBoard = new HashMap<Character, Character>();
    HashMap<Character, Character> reflector = new HashMap<Character, Character>();

    void setRotor(String rotor, HashMap<Character, Character> map) {
        for (int i = 0; i < rotor.length(); i++) {
            char ch = (char) (97 + i);
            map.put(ch, rotor.charAt(i));
        }
    }

    void setPlugBoard(String plugBoard) {
        String[] letters = plugBoard.split(", ");
        for (int i = 0; i < letters.length; i++) {
            this.plugBoard.put(letters[i].charAt(0), letters[i].charAt(1));
            this.plugBoard.put(letters[i].charAt(1), letters[i].charAt(0));
        }
        for (int i = 0; i < 26; i++) {
            char ch = (char) (97 + i);
            this.plugBoard.putIfAbsent(ch, ch);
        }
    }

    void setReflector() {
        for (int i = 0; i < 26; i++) {
            char x = (char) (97 + i);
            char y = (char) (122 - i);
            reflector.put(x, y);
        }
    }
    String convertMapToString(HashMap<Character, Character> map) {
        String result = "";
        for (int i = 0; i < map.size(); i++) {
            char ch = (char) (97 + i);
            result += map.get(ch);
        }
        return result;
    }

    String shift(String input) {
        return input.charAt(input.length() - 1) + input.substring(0, input.length() - 1);
    }
    HashMap<Character, Character> reverseRotor(HashMap<Character, Character> map) {
        HashMap<Character, Character> newRotor = new HashMap<>();
        for (int i = 0; i < map.size(); i++) {
            char ch = (char) (97 + i);
            newRotor.put(map.get(ch), ch);
        }
        return newRotor;
    }

    StringBuilder process(String input) {
        int rotor3Counter = 0;
        int rotor2Counter = 0;
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
//            System.out.println(rotor3.keySet());
            char current;
            current = plugBoard.get(input.charAt(i));
            current = rotor3.get(current);
            current = rotor2.get(current);
            current = rotor1.get(current);
            current = reflector.get(current);
            current = reverseRotor(rotor1).get(current);
            current = reverseRotor(rotor2).get(current);
            current = reverseRotor(rotor3).get(current);
            current = plugBoard.get(current);
            result.append(current);
            setRotor(shift(convertMapToString(rotor3)), rotor3);
            rotor3Counter++;
            if (rotor3Counter % 26 == 0) {
                setRotor(shift(convertMapToString(rotor2)), rotor2);
                rotor2Counter++;
            }
            if (rotor2Counter % 26 == 0 && rotor2Counter != 0) {
                setRotor(shift(convertMapToString(rotor1)), rotor1);
            }
        }
        return result;
    }
}

class EnigmaData{
    StringBuilder date;
    StringBuilder plugBoard;
    StringBuilder rotor1;
    StringBuilder rotor2;
    StringBuilder rotor3;
    static ArrayList<EnigmaData> enigmaData = new ArrayList<>();

    public EnigmaData(StringBuilder date, StringBuilder plugBoard, StringBuilder rotor1, StringBuilder rotor2, StringBuilder rotor3) {
        this.date = date;
        this.plugBoard = plugBoard;
        this.rotor1 = rotor1;
        this.rotor2 = rotor2;
        this.rotor3 = rotor3;
    }
    static EnigmaData findSetting(StringBuilder date){
        for (int i = 0; i < enigmaData.size(); i++) {
            if (enigmaData.get(i).date.compareTo(date) == 0){
                return enigmaData.get(i);
            }
        }
        return null;
    }
}
class FileReader{
    File file = new File("EnigmaFile.txt");
    Scanner myReader;

    {
        try {
            myReader = new Scanner(file);
             while (myReader.hasNextLine()){
                 //date
                 myReader.next();
                 StringBuilder date = new StringBuilder(myReader.nextLine());
                 date.deleteCharAt(0);
                 //plugBoard
                 myReader.next();
                 StringBuilder plugBoard = new StringBuilder(myReader.nextLine());
                 plugBoard.deleteCharAt(0);
                 plugBoard.deleteCharAt(0);
                 plugBoard.deleteCharAt(plugBoard.length()-1);
                 //rotor1
                 myReader.next();
                 StringBuilder rotor1 = new StringBuilder(myReader.nextLine());
                 rotor1.deleteCharAt(0);
                 rotor1.deleteCharAt(0);
                 rotor1.deleteCharAt(rotor1.length()-1);
                 //rotor2
                 myReader.next();
                 StringBuilder rotor2 = new StringBuilder(myReader.nextLine());
                 rotor2.deleteCharAt(0);
                 rotor2.deleteCharAt(0);
                 rotor2.deleteCharAt(rotor2.length()-1);
                 //rotor3
                 myReader.next();
                 StringBuilder rotor3 = new StringBuilder(myReader.nextLine());
                 rotor3.deleteCharAt(0);
                 rotor3.deleteCharAt(0);
                 rotor3.deleteCharAt(rotor3.length()-1);

//                 System.out.println(plugBoard);
//                 System.out.println(rotor1);
//                 System.out.println(rotor2);
//                 System.out.println(rotor3);

                 EnigmaData.enigmaData.add(new EnigmaData(date, plugBoard, rotor1, rotor2, rotor3));
             }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
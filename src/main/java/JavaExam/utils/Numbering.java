package JavaExam.utils;

/**
 * Используется для номерации элементов на html-страницах.
 * */
public class Numbering {
    private int number = 1;

    public int getNumber() {
        return number++;
    }

    public void reset() {
        number = 1;
    }

    public void setStartNumber(int i) {
        number = i;
    }
}

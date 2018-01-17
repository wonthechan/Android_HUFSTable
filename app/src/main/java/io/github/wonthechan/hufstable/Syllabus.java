package io.github.wonthechan.hufstable;

/**
 * Created by YeChan on 2018-01-16.
 */

public class Syllabus {

    String syllabusKey;
    String syllabusValue;

    public Syllabus(String syllabusKey, String syllabusValue) {
        this.syllabusKey = syllabusKey;
        this.syllabusValue = syllabusValue;
    }

    public String getSyllabusKey() {
        return syllabusKey;
    }

    public void setSyllabusKey(String syllabusKey) {
        this.syllabusKey = syllabusKey;
    }

    public String getSyllabusValue() {
        return syllabusValue;
    }

    public void setSyllabusValue(String syllabusValue) {
        this.syllabusValue = syllabusValue;
    }
}

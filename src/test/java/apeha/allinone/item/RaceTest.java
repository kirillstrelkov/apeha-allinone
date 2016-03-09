package apeha.allinone.item;

import apeha.allinone.common.Race;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RaceTest {

    @Test
    public void elfTest() {
        String expected = "Эльф";
        assertEquals(expected, Race.ELF.toString());
    }

    @Test
    public void orcTest() {
        String expected = "Орк";
        assertEquals(expected, Race.ORC.toString());
    }

    @Test
    public void dwarfTest() {
        String expected = "Гном";
        assertEquals(expected, Race.DWARF.toString());
    }

    @Test
    public void hobbitTest() {
        String expected = "Хоббит";
        assertEquals(expected, Race.HOBBIT.toString());
    }

    @Test
    public void dragonTest() {
        String expected = "Дракон";
        assertEquals(expected, Race.DRAGON.toString());
    }

    @Test
    public void humanTest() {
        String expected = "Человек";
        assertEquals(expected, Race.HUMAN.toString());
    }

    @Test
    public void incorrectTest() {
        String text = "Неизвестная раса";
        assertEquals(null, Race.getRaceFrom(text));
    }
}

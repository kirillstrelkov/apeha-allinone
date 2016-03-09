package apeha.allinone.item;

import apeha.allinone.common.Category;
import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CategoryTest {
    List<String> expected = Lists.newArrayList("Шлемы", "Амулеты", "Латы",
            "Оружие", "Пояса", "Поножи", "Перчатки", "Щиты",
            "Стрелковое оружие", "Наручи", "Кольца", "Все");

    @Test
    public void allCategoriesTest() {
        assertEquals(expected.subList(0, 11).toString(), Category.getTypes()
                .toString());
    }

    @Test
    public void getCatFromString() {
        for (String e : expected) {
            Category category = Category.getCategoryByType(e);
            assertNotNull(category);
        }
    }
}

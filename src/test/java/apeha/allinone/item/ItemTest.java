package apeha.allinone.item;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ItemTest {
    public static String SHIELD = new String("[Щит Древних]\n"
            + " Щит Древних\n" + "прочность: 250 	Количество: много\n"
            + "Цена в магазине: 1650.00 ст.\n" + "Требуемый Уровень: 7\n"
            + "Сложение: +10\n" + "Оберег удачи: +80%\n"
            + "Оберег ответа: +60%\n" + "Оберег крита: +40%\n"
            + "Оберег уворота: +45%\n" + "Броня ног: +80\n"
            + "Броня левой руки: +80\n" + "Броня правой руки: +80\n"
            + "Оберег удачи противника: -35%\n"
            + "Оберег уворота противника: -20%\n" + "Урон 0-0");
    public static String SHOOTING = new String("[Лук ивовый]\n"
            + "	Лук ивовый\n" + "прочность: 150 	Количество: много\n"
            + "Цена в магазине: 148.23 ст.\n" + "Требуемая Раса: Эльф\n"
            + "Требуемый Уровень: 8\n" + "Сила: -7\n" + "Ловкость: +10\n"
            + "Злость: +5\n" + "Двуручное.\n" + "Урон 30-60\n" + "Зарядов 75\n"
            + "Влияние силы: 80%\n" + "Точность: 85%\n"
            + "Радиус поражения: 7\n");
    public static String RARE_SHIELD = " [Изумрудный Щит]\n"
            + "	Изумрудный Щит\n" + "прочность: 100 / 100 	Количество: 1\n"
            + "Цена: 119.07 ст. + 13.00\n" + "Требуемый Уровень: 8\n"
            + "Ловкость: +20\n" + "Реакция: -20\n" + "Оберег удачи: +10%\n"
            + "Оберег крита: -30%\n" + "Броня корпуса: +35\n"
            + "Броня ног: +35\n" + "Броня левой руки: +35\n"
            + "Броня правой руки: +35\n" + "Урон 0-0";

    @Test
    public void rareShieldTest() {
        Item createItem = ItemBuilder.createItem(RARE_SHIELD);
        assertEquals("Изумрудный Щит", createItem.getName());
        String itemString = " [Меч изумрудный]\n" + "	Меч изумрудный\n"
                + "прочность: 150 	Количество: 573\n"
                + "Цена в магазине: 8.62 ст.\n" + "Требуемая Раса: Эльф\n"
                + "Требуемый Уровень: 4\n" + "Сила: +1\n" + "Ловкость: +3\n"
                + "Сложение: +1\n" + "Оберег крита: +15%\n" + "Урон 2-12";
        assertEquals("Меч изумрудный", ItemBuilder.createItem(itemString)
                .getName());
    }

    @Test
    public void imennajaTest() {
        String text = "tehnocchiO (мод.) (закл.)\n" + "Требуемый Уровень: 9\n"
                + "Сложение: +28\n" + "Урон 10-710\n"
                + "Дней между передачами: 20";
        Item createItem = ItemBuilder.createItem(text);
        assertEquals("tehnocchiO (мод.) (закл.)", createItem.getName());
    }

    @Test
    public void valuePercentPlus() {
        String text = "Оберег удачи: +80%";
        String value = Property.getPropertyValueFrom(text);
        assertEquals("+80%", value);
    }

    @Test
    public void valuePercentMinus() {
        String text = "Оберег удачи противника: -35%";
        String value = Property.getPropertyValueFrom(text);
        assertEquals("-35%", value);
    }

    @Test
    public void valuePlus() {
        String text = "Броня правой руки: +80";
        String value = Property.getPropertyValueFrom(text);
        assertEquals("+80", value);
    }

    @Test
    public void valueMinus() {
        String text = "Злость: -1";
        String value = Property.getPropertyValueFrom(text);
        assertEquals("-1", value);
    }

    @Test
    public void itemShield() {
        Item item = ItemBuilder.createItem(SHIELD);
        assertEquals("Щит Древних\nпрочность: 250\n"
                        + "Цена в магазине: 1650.00\n" + "Требуемый Уровень: 7\n"
                        + "Сложение: +10\n" + "Оберег удачи: +80%\n"
                        + "Оберег ответа: +60%\n" + "Оберег крита: +40%\n"
                        + "Оберег уворота: +45%\n" + "Броня ног: +80\n"
                        + "Броня левой руки: +80\n" + "Броня правой руки: +80\n"
                        + "Оберег удачи противника: -35%\n"
                        + "Оберег уворота противника: -20%\n" + "Урон 0-0",
                item.toString());
        assertEquals("Щит Древних", item.getName());
        assertEquals(
                "-35%",
                item.getPropertiesAndValues().get(
                        Property.GUARD_OF_FORTUNE_ENEMY));
    }

    @Test
    public void simpleItemShieldTest() {
        SimpleItem item = ItemBuilder.createSimpleItem(SHIELD);
        assertEquals("Щит Древних\nпрочность: 250\n"
                        + "Цена в магазине: 1650.00\n" + "Требуемый Уровень: 7\n"
                        + "Сложение: +10\n" + "Оберег удачи: +80%\n"
                        + "Оберег ответа: +60%\n" + "Оберег крита: +40%\n"
                        + "Оберег уворота: +45%\n" + "Броня ног: +80\n"
                        + "Броня левой руки: +80\n" + "Броня правой руки: +80\n"
                        + "Оберег удачи противника: -35%\n"
                        + "Оберег уворота противника: -20%\n" + "Урон 0-0",
                item.toString());
        assertEquals("Щит Древних", item.getName());
    }

    @Test
    public void itemShootingArm() {
        assertEquals("Лук ивовый\nпрочность: 150\n"
                + "Цена в магазине: 148.23\n" + "Требуемая Раса: Эльф\n"
                + "Требуемый Уровень: 8\n" + "Сила: -7\n" + "Ловкость: +10\n"
                + "Злость: +5\n" + "Двуручное\n" + "Урон 30-60\n"
                + "Зарядов 75\n" + "Влияние силы: 80%\n" + "Точность: 85%\n"
                + "Радиус поражения: 7", ItemBuilder.createItem(SHOOTING)
                .toString());
    }

    @Test
    public void simpleItemShootingArm() {
        assertEquals("Лук ивовый\nпрочность: 150\n"
                + "Цена в магазине: 148.23\n" + "Требуемая Раса: Эльф\n"
                + "Требуемый Уровень: 8\n" + "Сила: -7\n" + "Ловкость: +10\n"
                + "Злость: +5\n" + "Двуручное\n" + "Урон 30-60\n"
                + "Зарядов 75\n" + "Влияние силы: 80%\n" + "Точность: 85%\n"
                + "Радиус поражения: 7", ItemBuilder.createSimpleItem(SHOOTING)
                .toString());
    }
}

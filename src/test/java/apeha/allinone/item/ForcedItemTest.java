package apeha.allinone.item;

import apeha.allinone.item.fortification.Fortification;
import apeha.allinone.item.fortification.Value;
import apeha.allinone.item.stone.Modification;
import org.junit.Test;

import static org.junit.Assert.*;

public class ForcedItemTest {

    @Test
    public void constTest() {
        String text = " [Амулет Мертвеца (слож. +3)]";
        Fortification fortification = Fortification.getFortification(text);
        assertEquals(fortification.getValue(), Value.CONSTITUTION);
        assertEquals(fortification.getValue().getProperty(),
                Property.CONSTITUTION);
        assertEquals(fortification.getTimes(), 3);
    }

    @Test
    public void armTest() {
        String text = " [Щит Измора (мод.) (закл.) (бронь +2)]";
        assertEquals(Fortification.getFortification(text).getValue(),
                Value.ARMOUR);
        assertEquals(Fortification.getFortification(text).getTimes(), 2);
    }

    @Test
    public void sharpTest() {
        String text = " [Меч Стихий (мод.) (заточ. +1)]";
        assertEquals(Fortification.getFortification(text).getValue(),
                Value.SHARPENING);
        assertEquals(Fortification.getFortification(text).getTimes(), 1);
    }

    @Test
    public void powerTest() {
        String text = " [Кольцо Самурая (мод.) (закл.) (сила +1)]";
        assertEquals(Fortification.getFortification(text).getValue(),
                Value.POWER);
        assertEquals(Fortification.getFortification(text).getTimes(), 1);
    }

    @Test
    public void agilityTest() {
        String text = " [Кольцо Мертвеца (мод.) (закл.) (ловк. +2)]";
        assertEquals(Fortification.getFortification(text).getValue(),
                Value.AGILITY);
        assertEquals(Fortification.getFortification(text).getTimes(), 2);
    }

    @Test
    public void reactionTest() {
        String text = " [Кольцо Поклонения (мод.) (закл.) (реак. +3)]";
        assertEquals(Fortification.getFortification(text).getValue(),
                Value.REACTION);
        assertEquals(Fortification.getFortification(text).getTimes(), 3);
    }

    @Test
    public void fortuneTest() {
        String text = " [Кольцо Самурая (мод.) (закл.) (удач. +4)]";
        assertEquals(Fortification.getFortification(text).getValue(),
                Value.FORTUNE);
        assertEquals(Fortification.getFortification(text).getTimes(), 4);
    }

    @Test
    public void spiteTest() {
        String text = " [Кольцо Самурая (мод.) (закл.) (злость +3)]";
        assertEquals(Fortification.getFortification(text).getValue(),
                Value.SPITE);
        assertEquals(Fortification.getFortification(text).getTimes(), 3);
    }

    @Test
    public void armourItemTest() {
        String text = " [Щит Измора (мод.) (закл.) (бронь +2)]\n"
                + "	Щит Измора (мод.) (закл.) (бронь +2)\n"
                + "Цена: 45.00 + 22.00\n" + "Количество: 1\n"
                + "Требуемый Уровень: 5\n" + "Броня головы: +30\n"
                + "Броня корпуса: +50\n" + "Броня ног: +30\n"
                + "Броня левой руки: +40\n" + "Броня правой руки: +40\n"
                + "Мастерство защиты: +90%\n" + "Урон 0-0";
        Item item = ItemBuilder.createItem(text);
        assertEquals(Fortification.getFortification(item).getValue(),
                Value.ARMOUR);
        assertEquals(Fortification.getFortification(item).getTimes(), 2);
    }

    @Test
    public void sharpeningItemTest() {
        String text = " [Меч Стихий (мод.) (заточ. +1)]\n"
                + "	Меч Стихий (мод.) (заточ. +1)\n"
                + "прочность: 200 / 200 	Количество: 1\n"
                + "Цена: 1300.00 ст. + 15.00\n" + "Требуемый Уровень: 13\n"
                + "Сила: +10\n" + "Ловкость: +40\n" + "Реакция: +25\n"
                + "Урон 120-130";
        Item item = ItemBuilder.createItem(text);
        assertEquals(Fortification.getFortification(item).getValue(),
                Value.SHARPENING);
        assertEquals(Fortification.getFortification(item).getTimes(), 1);
    }

    @Test
    public void powerItemTest() {
        String text = " [Кольцо Самурая (мод.) (закл.) (сила +1)]\n"
                + "	Кольцо Самурая (мод.) (закл.) (сила +1)\n"
                + "Цена: 180.00 + 15.00\n" + "Количество: 1\n"
                + "Требуемый Уровень: 7\n" + "Сила: +19\n" + "Злость: +12\n"
                + "Оберег ответа: +35%\n" + "Оберег уворота: +35%\n"
                + "Оберег крита противника: -90%";
        Item item = ItemBuilder.createItem(text);
        assertEquals(Fortification.getFortification(item).getValue(),
                Value.POWER);
        assertEquals(Fortification.getFortification(item).getTimes(), 1);
    }

    @Test
    public void agilityItemTest() {
        String text = " [Кольцо Мертвеца (мод.) (закл.) (ловк. +2)]\n"
                + "	Кольцо Мертвеца (мод.) (закл.) (ловк. +2)\n"
                + "Цена: 180.00 + 20.00\n" + "Количество: 1\n"
                + "Требуемый Уровень: 7\n" + "Ловкость: +22\n"
                + "Реакция: +14\n" + "Оберег удачи: +35%\n"
                + "Оберег крита: +35%\n" + "Оберег уворота противника: -80%";
        Item item = ItemBuilder.createItem(text);
        assertEquals(Fortification.getFortification(item).getValue(),
                Value.AGILITY);
        assertEquals(Fortification.getFortification(item).getTimes(), 2);
    }

    @Test
    public void reactionItemTest() {
        String text = " [Кольцо Поклонения (мод.) (закл.) (реак. +3)]\n"
                + "	Кольцо Поклонения (мод.) (закл.) (реак. +3)\n"
                + "прочность: 100/100 	\n" + "Требуемый Уровень: 6\n"
                + "Реакция: +27\n" + "Сложение: +8\n" + "Оберег удачи: +35%\n"
                + "Оберег крита: +35%\n" + "Оберег ответа противника: -90%\n"
                + "Цена в магазине: 105.00 ст.\n" + "количество: 1\n"
                + "Наложено заклятие еще: 8д 6ч 9мин ";
        Item item = ItemBuilder.createItem(text);
        assertEquals(Fortification.getFortification(item).getValue(),
                Value.REACTION);
        assertEquals(Fortification.getFortification(item).getTimes(), 3);
    }

    @Test
    public void fortuneItemTest() {
        String text = " [Кольцо Самурая (мод.) (закл.) (удач. +4)]\n"
                + "	Кольцо Самурая (мод.) (закл.) (удач. +4) \n"
                + "Цена: 180.00 + 20.00\n" + "Количество: 1\n"
                + "Требуемый Уровень: 7\n" + "Сила: +23\n" + "Злость: +12\n"
                + "Удача: +20\n" + "Оберег ответа: +35%\n"
                + "Оберег уворота: +80%";
        Item item = ItemBuilder.createItem(text);
        assertEquals(Fortification.getFortification(item).getValue(),
                Value.FORTUNE);
        assertEquals(Fortification.getFortification(item).getTimes(), 4);
    }

    @Test
    public void spiteItemTest() {
        String text = " [Кольцо Самурая (мод.) (закл.) (злость +3)]\n"
                + "	Кольцо Самурая (мод.) (закл.) (злость +3) 	\n"
                + "Цена: 180.00 + 10.00\n" + "Количество: 1\n"
                + "Требуемый Уровень: 7\n" + "Сила: +19\n" + "Злость: +27\n"
                + "Оберег ответа: +35%\n" + "Оберег уворота: +70%";
        Item item = ItemBuilder.createItem(text);
        assertEquals(Fortification.getFortification(item).getValue(),
                Value.SPITE);
        assertEquals(Fortification.getFortification(item).getTimes(), 3);
    }

    @Test
    public void constitutionTest() {
        String text = " [Амулет Мертвеца (слож. +3)]\n"
                + "Амулет Мертвеца (слож. +3)\n"
                + "прочность: 100 / 100 	Количество: 1\n"
                + "Цена: 27.00 ст. + 5.00\n" + "Требуемый Уровень: 4\n"
                + "Сила: +10\n" + "Злость: +10\n" + "Сложение: +15\n"
                + "Мастерство владения оружием: +10%";
        Item item = ItemBuilder.createItem(text);
        assertEquals(Fortification.getFortification(item).getValue(),
                Value.CONSTITUTION);
        assertEquals(Fortification.getFortification(item).getTimes(), 3);
    }

    @Test
    public void itemModTest() {
        String item = " [Амулет Мертвеца (слож. +3)]\n"
                + "Амулет Мертвеца (слож. +3)\n"
                + "прочность: 100 / 100 	Количество: 1\n"
                + "Цена: 27.00 ст. + 5.00\n" + "Требуемый Уровень: 4\n"
                + "Сила: +10\n" + "Злость: +10\n" + "Сложение: +15\n"
                + "Мастерство владения оружием: +10%";
        ModItem createModItem = ItemBuilder.createModItem(item);
        Modification mod1 = createModItem.getMod1();
        Modification mod2 = createModItem.getMod2();
        System.out.println(createModItem);
        System.out.println(mod1);
        System.out.println(mod2);
        assertNull(mod1);
        assertNull(mod2);
    }

    @Test
    public void spiteItemModTest() {
        String text = " [Кольцо Самурая (мод.) (закл.) (злость +3)]\n"
                + "	Кольцо Самурая (мод.) (закл.) (злость +3) 	\n"
                + "Цена: 180.00 + 10.00\n" + "Количество: 1\n"
                + "Требуемый Уровень: 7\n" + "Сила: +19\n" + "Злость: +27\n"
                + "Оберег ответа: +35%\n" + "Оберег уворота: +70%";
        ModItem createModItem = ItemBuilder.createModItem(text);
        Modification mod1 = createModItem.getMod1();
        Modification mod2 = createModItem.getMod2();
        assertEquals(mod1.toString(), "Сила: +5");
        assertEquals(mod2.toString(), "Оберег уворота: +35%");
    }

    @Test
    public void armourItemModTest() {
        String text = " [Щит Измора (мод.) (закл.) (бронь +2)]\n"
                + "	Щит Измора (мод.) (закл.) (бронь +2)\n"
                + "Цена: 45.00 + 22.00\n" + "Количество: 1\n"
                + "Требуемый Уровень: 5\n" + "Броня головы: +30\n"
                + "Броня корпуса: +50\n" + "Броня ног: +30\n"
                + "Броня левой руки: +40\n" + "Броня правой руки: +40\n"
                + "Мастерство защиты: +90%\n" + "Урон 0-0";
        ModItem createModItem = ItemBuilder.createModItem(text);
        Modification mod1 = createModItem.getMod1();
        Modification mod2 = createModItem.getMod2();
        assertEquals(mod1.toString(), "Мастерство защиты: +45%");
        assertEquals(mod2.toString(), "Мастерство защиты: +45%");
    }

    @Test
    public void sharpeningItemModTest() {
        String text = " [Меч Стихий (мод.) (заточ. +1)]\n"
                + "	Меч Стихий (мод.) (заточ. +1)\n"
                + "прочность: 200 / 200 	Количество: 1\n"
                + "Цена: 1300.00 ст. + 15.00\n" + "Требуемый Уровень: 13\n"
                + "Сила: +10\n" + "Ловкость: +40\n" + "Реакция: +25\n"
                + "Урон 120-130";
        ModItem createModItem = ItemBuilder.createModItem(text);
        Modification mod1 = createModItem.getMod1();
        Modification mod2 = createModItem.getMod2();
        // System.out.println(createModItem);
        assertEquals(mod1.toString(), "Ловкость: +15");
        assertNull(mod2);
    }

    @Test
    public void forceditems() {
        String item = "Кулон \"виктория\" (мод.) (закл.) (реак. +1)\n"
                + "прочность: 75/75\n" + "Цена: 30.94 ст. + 7.00\n"
                + "Требуемый Уровень: 6\n" + "Сила: +5\n" + "Реакция: +29\n"
                + "Мастерство владения оружием: +10%";
        ModItem modItem = ItemBuilder.createModItem(item);
        assertEquals(modItem.getMod1(), modItem.getMod2());
        assertEquals(modItem.getMod1().toString(), "Реакция: +7");

        item = "Пояс Террора (мод.) (закл.) (слож. +2)\n"
                + "прочность: 100/100\n" + "Цена: 360.00 ст. + 30.00\n"
                + "Требуемый Уровень: 8\n" + "Ловкость: +10\n"
                + "Реакция: +14\n" + "Сложение: +25\n" + "Оберег удачи: +55%\n"
                + "Оберег крита: +55%\n" + "Оберег ответа противника: -45%";
        modItem = ItemBuilder.createModItem(item);
        assertEquals(modItem.getMod1().toString(), "Сложение: +15");
        assertEquals(modItem.getMod2().toString(),
                "Оберег ответа противника: -45%");

        item = "Пояс Рассудка (мод.) (сила +1)\n" + "прочность: 100/100\n"
                + "Цена: 105.00 ст. + 9.00\n" + "Требуемый Уровень: 6\n"
                + "Сила: +25\n" + "Удача: +7\n" + "Оберег ответа: +40%\n"
                + "Оберег уворота: +40%";
        modItem = ItemBuilder.createModItem(item);
        assertEquals(modItem.getMod1().toString(), "Сила: +9");
        assertEquals(modItem.getMod2(), null);

        item = "Кольцо Мертвеца (мод.) (закл.) (ловк. +2)\n"
                + "прочность: 100/100\n" + "Цена: 180.00 ст. + 17.00\n"
                + "Требуемый Уровень: 7\n" + "Ловкость: +31\n"
                + "Реакция: +14\n" + "Оберег удачи: +35%\n"
                + "Оберег крита: +80%";
        modItem = ItemBuilder.createModItem(item);
        assertEquals(modItem.getMod1().toString(), "Ловкость: +9");
        assertEquals(modItem.getMod2().toString(), "Оберег крита: +45%");

        item = "Кольцо Мертвеца (мод.) (закл.) (ловк. +2)\n"
                + "прочность: 100/100\n" + "Цена: 180.00 ст. + 15.00\n"
                + "Требуемый Уровень: 7\n" + "Ловкость: +29\n"
                + "Реакция: +14\n" + "Оберег удачи: +35%\n"
                + "Оберег крита: +80%";
        modItem = ItemBuilder.createModItem(item);
        assertEquals(modItem.getMod1().toString(), "Ловкость: +7");
        assertEquals(modItem.getMod2().toString(), "Оберег крита: +45%");

        item = "Кольцо Мертвеца (мод.) (ловк. +1)\n" + "прочность: 100/100\n"
                + "Цена: 180.00 ст. + 7.00\n" + "Требуемый Уровень: 7\n"
                + "Ловкость: +26\n" + "Реакция: +14\n" + "Оберег удачи: +35%\n"
                + "Оберег крита: +35%";
        modItem = ItemBuilder.createModItem(item);
        assertEquals(modItem.getMod1().toString(), "Ловкость: +9");
        assertEquals(modItem.getMod2(), null);

        item = "Перстень Терпимости (мод.) (закл.) (сила +2)\n"
                + "прочность: 100/100\n" + "Цена: 180.00 ст. + 11.00\n"
                + "Требуемый Уровень: 7\n" + "Сила: +25\n" + "Удача: +14\n"
                + "Сложение: +4\n" + "Оберег ответа: +35%\n"
                + "Оберег крита: +80%";
        modItem = ItemBuilder.createModItem(item);
        assertEquals(modItem.getMod1().toString(), "Сила: +7");
        assertEquals(modItem.getMod2().toString(), "Оберег крита: +45%");

        item = "Кольцо Поклонения (мод.) (закл.) (реак. +2)\n"
                + "прочность: 100/100\n" + "Цена: 105.00 ст. + 19.00\n"
                + "Требуемый Уровень: 6\n" + "Реакция: +40\n"
                + "Сложение: +8\n" + "Оберег удачи: +35%\n"
                + "Оберег крита: +35%";
        modItem = ItemBuilder.createModItem(item);
        assertEquals(modItem.getMod1().toString(), "Реакция: +9");
        assertEquals(modItem.getMod2().toString(), "Реакция: +9");

        item = "Кольцо Поклонения (мод.) (реак. +2)\n" + "прочность: 100/100\n"
                + "Цена: 105.00 ст. + 10.00\n" + "Требуемый Уровень: 6\n"
                + "Реакция: +31\n" + "Сложение: +8\n" + "Оберег удачи: +35%\n"
                + "Оберег крита: +35%";
        modItem = ItemBuilder.createModItem(item);
        assertEquals(modItem.getMod1().toString(), "Реакция: +9");
        assertEquals(modItem.getMod2(), null);

        item = "Кольцо Ненависти (мод.) (реак. +1)\n" + "прочность: 100/100\n"
                + "Цена: 45.00 ст. + 5.00\n" + "Требуемый Уровень: 5\n"
                + "Реакция: +22\n" + "Сложение: +6\n" + "Оберег удачи: +30%\n"
                + "Оберег крита: +30%";
        modItem = ItemBuilder.createModItem(item);
        assertEquals(modItem.getMod1().toString(), "Реакция: +7");
        assertEquals(modItem.getMod2(), null);

        item = "Кольцо Возмездия (мод.) (удач. +1)\n" + "прочность: 100/100\n"
                + "Цена: 27.00 ст. + 3.50\n" + "Требуемый Уровень: 4\n"
                + "Сила: +4\n" + "Удача: +13\n" + "Оберег ответа: +25%\n"
                + "Оберег уворота: +25%\n" + "Мастерство кулачного боя: +35%";
        modItem = ItemBuilder.createModItem(item);
        assertEquals(modItem.getMod1().toString(),
                "Мастерство кулачного боя: +35%");
        assertEquals(modItem.getMod2(), null);
    }

    @Test
    public void notEqualForcedAndNorForced() {
        String forced = "Кольцо Возмездия (мод.) (удач. +1)\n"
                + "прочность: 100/100\n" + "Цена: 27.00 ст. + 3.50\n"
                + "Требуемый Уровень: 4\n" + "Сила: +4\n" + "Удача: +13\n"
                + "Оберег ответа: +25%\n" + "Оберег уворота: +25%\n"
                + "Мастерство кулачного боя: +35%";
        String unforced = "Кольцо Возмездия (мод.)\n" + "прочность: 100/100\n"
                + "Цена: 27.00 ст. + 3.50\n" + "Требуемый Уровень: 4\n"
                + "Сила: +4\n" + "Удача: +8\n" + "Оберег ответа: +25%\n"
                + "Оберег уворота: +25%\n" + "Мастерство кулачного боя: +35%";
        ModItem itemF = ItemBuilder.createModItem(forced);
        ModItem itemUnF = ItemBuilder.createModItem(unforced);
        assertFalse(itemF.equals(itemUnF));
    }
}

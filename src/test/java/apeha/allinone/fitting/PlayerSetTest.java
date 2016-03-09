package apeha.allinone.fitting;

import apeha.allinone.db.DbHandler;
import apeha.allinone.item.Item;
import apeha.allinone.item.ModItem;
import apeha.allinone.item.Property;
import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class PlayerSetTest {

    @Test
    public void addAmulet() {
        Item item = DbHandler.getItemByName("Амулет Мертвеца");
        ModItem modItem = new ModItem(item);
        PlayerSet player = new PlayerSet();
        assertEquals(0, (int) player.stats.get(Property.POWER));
        assertEquals(0, (int) player.stats.get(Property.SPITE));
        assertEquals(0, (int) player.stats.get(Property.MASTERY_OF_WEAPON));
        System.out.println(player.toString());
        player.putOn(modItem);
        assertEquals(10, (int) player.stats.get(Property.POWER));
        assertEquals(10, (int) player.stats.get(Property.SPITE));
        assertEquals(10, (int) player.stats.get(Property.MASTERY_OF_WEAPON));
        System.out.println("========");
        System.out.println(player.toString());
        player.putOn(new ModItem(DbHandler.getItemByName("Амулет Подавления")));
        assertEquals(18, (int) player.stats.get(Property.POWER));
        assertEquals(18, (int) player.stats.get(Property.SPITE));
        assertEquals(50, (int) player.stats.get(Property.MARKSMANSHIP));
        System.out.println("========");
        System.out.println(player.toString());
    }

    @Test
    public void addRings() {
        Item item = DbHandler.getItemByName("Кольцо Мертвеца");
        PlayerSet player = new PlayerSet();
        assertEquals(0, (int) player.stats.get(Property.AGILITY));
        assertEquals(0, (int) player.stats.get(Property.REACTION));
        assertEquals(0, (int) player.stats.get(Property.GUARD_OF_FORTUNE));
        assertEquals(0,
                (int) player.stats.get(Property.GUARD_OF_CRITICAL_STRIKE));
        System.out.println(player.toString());
        for (int i = 1; i < 11; i++) {
            ModItem modItem = new ModItem(item);
            int j = i;
            if (i >= 9)
                j = 8;
            player.putOn(modItem);
            System.out.println(player.toString());
            assertEquals(12 * j, (int) player.stats.get(Property.AGILITY));
            assertEquals(14 * j, (int) player.stats.get(Property.REACTION));
            assertEquals(35 * j,
                    (int) player.stats.get(Property.GUARD_OF_FORTUNE));
            assertEquals(35 * j,
                    (int) player.stats.get(Property.GUARD_OF_CRITICAL_STRIKE));
            System.out.println("========");
        }
        System.out.println(player.toString());
    }

    @Test
    public void addArms() {
        Item item = DbHandler.getItemByName("Палаш Доблести");
        PlayerSet player = new PlayerSet();
        assertEquals(0, (int) player.stats.get(Property.POWER));
        assertEquals(0, (int) player.stats.get(Property.SPITE));
        assertEquals(0, (int) player.stats.get(Property.FOTUNE));
        System.out.println(player.toString());
        for (int i = 1; i < 4; i++) {
            ModItem modItem = new ModItem(item);
            int j = i;
            if (i >= 3)
                j = 2;
            player.putOn(modItem);
            System.out.println(player.toString());
            assertEquals(20 * j, (int) player.stats.get(Property.POWER));
            assertEquals(7 * j, (int) player.stats.get(Property.SPITE));
            assertEquals(18 * j, (int) player.stats.get(Property.FOTUNE));
            System.out.println("========");
        }
        System.out.println(player.toString());
    }

    @Test
    public void addShields() {
        Item item = DbHandler.getItemByName("Изумрудный Щит");
        PlayerSet player = new PlayerSet();
        assertEquals(0, (int) player.stats.get(Property.AGILITY));
        assertEquals(0, (int) player.stats.get(Property.REACTION));
        assertEquals(0, (int) player.stats.get(Property.GUARD_OF_FORTUNE));
        assertEquals(0,
                (int) player.stats.get(Property.GUARD_OF_CRITICAL_STRIKE));
        System.out.println(player.toString());
        for (int i = 1; i < 4; i++) {
            ModItem modItem = new ModItem(item);
            int j = i;
            if (i >= 3)
                j = 2;
            player.putOn(modItem);
            System.out.println(player.toString());
            assertEquals(20 * j, (int) player.stats.get(Property.AGILITY));
            assertEquals(-20 * j, (int) player.stats.get(Property.REACTION));
            assertEquals(10 * j,
                    (int) player.stats.get(Property.GUARD_OF_FORTUNE));
            assertEquals(-30 * j,
                    (int) player.stats.get(Property.GUARD_OF_CRITICAL_STRIKE));
            System.out.println("========");
        }
        System.out.println(player.toString());
    }

    @Test
    public void addTwoHandled() {
        Item item = DbHandler.getItemByName("Пика двуручная");
        PlayerSet player = new PlayerSet();
        assertEquals(0, (int) player.stats.get(Property.POWER));
        for (int i = 1; i < 4; i++) {
            ModItem modItem = new ModItem(item);
            player.putOn(modItem);
            assertEquals(3, (int) player.stats.get(Property.POWER));
        }
    }

    @Test
    public void equalSetTest() {
        Item item1 = DbHandler.getItemByName("Кольцо Поклонения");
        Item item2 = DbHandler.getItemByName("Кольцо Подавления");
        Item item3 = DbHandler.getItemByName("Кольцо Подавления");
        Item item4 = DbHandler.getItemByName("Кольцо Мертвеца");
        Item item5 = DbHandler.getItemByName("Кольцо Мертвеца");
        Item item6 = DbHandler.getItemByName("Кольцо Мертвеца");
        Item item7 = DbHandler.getItemByName("Кольцо Юпитера");
        Item item8 = DbHandler.getItemByName("Кольцо Невиновности");
        List<Item> items = Lists.newArrayList(item1, item2, item3, item4,
                item5, item6, item7, item8);

        PlayerSet player = new PlayerSet();
        for (Item item : items) {
            ModItem modItem = new ModItem(item);
            player.putOn(modItem);
        }

        assertEquals(player, player);
        item1 = DbHandler.getItemByName("Кольцо Поклонения");
        item2 = DbHandler.getItemByName("Кольцо Подавления");
        item4 = DbHandler.getItemByName("Кольцо Мертвеца");
        item3 = DbHandler.getItemByName("Кольцо Подавления");
        item5 = DbHandler.getItemByName("Кольцо Мертвеца");
        item6 = DbHandler.getItemByName("Кольцо Мертвеца");
        item8 = DbHandler.getItemByName("Кольцо Невиновности");
        item7 = DbHandler.getItemByName("Кольцо Юпитера");
        items = Lists.newArrayList(item1, item2, item3, item4, item5, item6,
                item7, item8);

        PlayerSet player2 = new PlayerSet();
        for (Item item : items) {
            ModItem modItem = new ModItem(item);
            player2.putOn(modItem);
        }
        assertEquals(player, player2);

        item1 = DbHandler.getItemByName("Кольцо Поклонения");
        item2 = DbHandler.getItemByName("Кольцо Подавления");
        item4 = DbHandler.getItemByName("Кольцо Мертвеца");
        item3 = DbHandler.getItemByName("Кольцо Подавления");
        item5 = DbHandler.getItemByName("Кольцо Подавления");
        item6 = DbHandler.getItemByName("Кольцо Мертвеца");
        item8 = DbHandler.getItemByName("Кольцо Невиновности");
        item7 = DbHandler.getItemByName("Кольцо Юпитера");
        items = Lists.newArrayList(item1, item2, item3, item4, item5, item6,
                item7, item8);

        PlayerSet player3 = new PlayerSet();
        for (Item item : items) {
            ModItem modItem = new ModItem(item);
            player3.putOn(modItem);
        }
        assertFalse(player2.equals(player3));
        assertFalse(player.equals(player3));
    }

    @Test
    public void differentRingsToString() {
        Item item1 = DbHandler.getItemByName("Кольцо Поклонения");
        Item item2 = DbHandler.getItemByName("Кольцо Подавления");
        Item item3 = DbHandler.getItemByName("Кольцо Подавления");
        Item item4 = DbHandler.getItemByName("Кольцо Мертвеца");
        Item item5 = DbHandler.getItemByName("Кольцо Мертвеца");
        Item item6 = DbHandler.getItemByName("Кольцо Мертвеца");
        Item item7 = DbHandler.getItemByName("Кольцо Юпитера");
        Item item8 = DbHandler.getItemByName("Кольцо Невиновности");
        List<Item> items = Lists.newArrayList(item1, item2, item3, item4,
                item5, item6, item7, item8);

        PlayerSet player = new PlayerSet();
        System.out.println(player);
        System.out.println("============================================");
        // assertEquals(0, (int) player.stats.get(Property.POWER));
        for (Item item : items) {
            ModItem modItem = new ModItem(item);
            player.putOn(modItem);
            // System.out.println(player);
            // System.out.println("============================================");
            // assertEquals(3, (int) player.stats.get(Property.POWER));
        }
        System.out.println(player);
    }

    @Test
    public void scoreWith2Arms() {
        List<String> params = Lists.newArrayList("Сложение", "Ловкость",
                Property.GUARD_OF_DODGE_ENEMY.getName(),
                Property.GUARD_OF_FORTUNE.getName(),
                Property.GUARD_OF_CRITICAL_STRIKE.getName());

        ModItem arm = new ModItem(DbHandler.getItemByName("Тесак Очарования"));
        List<ModItem> items = Lists.newArrayList(arm, arm);

        SearchedSet player = new SearchedSet();
        for (ModItem item : items) {
            player.putOn(item);
        }
        player.updateScore(params);
        assertEquals(player.getScore(), 162);
    }

    @Test
    public void scoreWith2DifferentArms() {
        List<String> params = Lists.newArrayList("Сложение", "Ловкость",
                Property.GUARD_OF_DODGE_ENEMY.getName(),
                Property.GUARD_OF_FORTUNE.getName(),
                Property.GUARD_OF_CRITICAL_STRIKE.getName());

        ModItem arm1 = new ModItem(DbHandler.getItemByName("Тесак Очарования"));
        ModItem arm2 = new ModItem(DbHandler.getItemByName("Меч Стихий"));
        List<ModItem> items = Lists.newArrayList(arm1, arm2);

        SearchedSet player = new SearchedSet();
        for (ModItem item : items) {
            player.putOn(item);
        }
        player.updateScore(params);
        assertEquals(player.getScore(), 166);
    }

}

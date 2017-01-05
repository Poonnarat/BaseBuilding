package frickingnoobs.noobs;

/**
 * Created by Username on 01/01/2017.
 */
public abstract class Entity extends GameObject {
    public int level;
    public int health;
    public int str; //Strength stat affects damage and health (Labour jobs also affected)
    public int agi; //Agility stat affects fine movement (Craftsmanship and attack speed, dodging)
    public int in; //Intelligence stat affects learning of skills, (magic?)(Noble jobs)
}

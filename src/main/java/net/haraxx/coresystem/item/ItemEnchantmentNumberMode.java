package net.haraxx.coresystem.item;

/**
 * @author Juyas
 * @version 11.11.2023
 * @since 11.11.2023
 */
public enum ItemEnchantmentNumberMode
{

    RAW,
    ROMAN( "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X" ),
    ANIME( "F", "E", "D", "C", "B", "A", "AA", "S", "SS", "SSS" ),
    SIZE( "XXXS", "XXS", "XS", "S", "M", "L", "XL", "XXL", "XXXL", "XXXL+" );

    private final String[] numbers;

    ItemEnchantmentNumberMode( String... numbers )
    {
        this.numbers = numbers;
    }

    public String get( int number )
    {
        if ( number < 1 || number > numbers.length ) return String.valueOf( number );
        return numbers[number - 1];
    }

}

package issue10615

class Card {
    String title
    static hasMany = [admins: User]
}

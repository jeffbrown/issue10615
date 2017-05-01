package issue10615

class BootStrap {

    def init = { servletContext ->
        ['Jeff', 'Graeme', 'James'].each { name ->
            new User(name: name).save()
        }
        ['One', 'Two', 'Three'].each { name ->
            new Card(title: "Card $name").save()
        }
    }
    def destroy = {
    }
}

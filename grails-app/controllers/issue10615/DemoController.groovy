package issue10615

class DemoController {
    def index(Long id) {
        Card card = Card.get(id)
        User user = User.get(1)
        card.addToAdmins(user)
        card.save()

        redirect action: 'show', controller: 'card', params: [id: id]
    }
}

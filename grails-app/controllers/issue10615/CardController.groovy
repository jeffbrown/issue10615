package issue10615

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class CardController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Card.list(params), model:[cardCount: Card.count()]
    }

    def show(Card card) {
        respond card
    }

    def create() {
        respond new Card(params)
    }

    @Transactional
    def save(Card card) {
        if (card == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (card.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond card.errors, view:'create'
            return
        }

        card.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'card.label', default: 'Card'), card.id])
                redirect card
            }
            '*' { respond card, [status: CREATED] }
        }
    }

    def edit(Card card) {
        respond card
    }

    @Transactional
    def update(Card card) {
        if (card == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (card.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond card.errors, view:'edit'
            return
        }

        card.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'card.label', default: 'Card'), card.id])
                redirect card
            }
            '*'{ respond card, [status: OK] }
        }
    }

    @Transactional
    def delete(Card card) {

        if (card == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        card.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'card.label', default: 'Card'), card.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'card.label', default: 'Card'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
